package tech.klux.kluxcore.manager;

import tech.klux.kluxcore.eventbus.EventHandler;
import tech.klux.kluxcore.eventbus.listeners.*;
import tech.klux.kluxcore.eventbus.Cancellable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventManager {

    private static final EventManager INSTANCE = new EventManager();
    public static EventManager get() { return INSTANCE; }

    private final Map<Class<?>, List<IListener>> map = new ConcurrentHashMap<>();

    public EventManager() {}

    public void subscribe(Object subscriber) {
        Method[] methods = subscriber.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (!m.isAnnotationPresent(EventHandler.class)) continue;
            if (m.getParameterCount() != 1) continue;

            Class<?> eventType = m.getParameterTypes()[0];
            EventHandler ann = m.getAnnotation(EventHandler.class);

            IListener listener = new LambdaListener(
                    (lookupMethod, clazz) -> null,
                    subscriber.getClass(),
                    subscriber,
                    m
            );

            map.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
            sortList(map.get(eventType));
        }
    }

    public void unsubscribe(Object subscriber) {
        map.values().forEach(list -> list.removeIf(l -> {
            if (l instanceof LambdaListener) {
                LambdaListener ll = (LambdaListener) l;
                return ll.getOwner() == subscriber;
            }
            return false;
        }));
    }

    @SuppressWarnings("unchecked")
    public <E> E post(E event) {
        Class<?> clazz = event.getClass();
        while (clazz != null) {
            List<IListener> listeners = map.get(clazz);
            if (listeners != null) {
                for (IListener l : listeners) {
                    if (event instanceof Cancellable && ((Cancellable) event).isCancelled()) break;
                    l.call(event);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return event;
    }

    /*  Helpers  */
    private void sortList(List<IListener> list) {
        list.sort(Comparator.comparingInt(l -> l.getPriority().getWeight()));
    }
}