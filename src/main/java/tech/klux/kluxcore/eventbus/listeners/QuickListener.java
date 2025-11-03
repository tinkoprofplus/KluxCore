package tech.klux.kluxcore.eventbus.listeners;

import tech.klux.kluxcore.eventbus.Event;

import java.util.function.Consumer;

public final class QuickListener<E> implements IListener {

    private final Class<E> type;
    private final Event.EventPriority weight;
    private final Consumer<E> action;

    public QuickListener(Class<E> type, Event.EventPriority weight, Consumer<E> action) {
        this.type = type;
        this.weight = weight;
        this.action = action;
    }

    public QuickListener(Class<E> type, Consumer<E> action) {
        this(type, Event.EventPriority.HIGH, action);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void call(Object event) {
        action.accept((E) event);
    }

    @Override
    public Class<?> getTarget() {
        return type;
    }

    @Override
    public Event.EventPriority getPriority() {
        return weight;
    }

    @Override
    public boolean isStatic() {
        return false;
    }
}