package tech.klux.kluxcore.eventbus.listeners;

import tech.klux.kluxcore.eventbus.EventHandler;
import tech.klux.kluxcore.eventbus.Event;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.function.Consumer;

public final class LambdaListener implements IListener {

    private static final boolean LEGACY_JDK;
    private static Constructor<MethodHandles.Lookup> lookupCtor;
    private static Method privateLookup;

    static {
        LEGACY_JDK = System.getProperty("java.version", "").startsWith("1.8");
        try {
            if (LEGACY_JDK) {
                lookupCtor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
            } else {
                privateLookup = MethodHandles.class.getDeclaredMethod(
                        "privateLookupIn", Class.class, MethodHandles.Lookup.class);
            }
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final Class<?> eventType;
    private final Event.EventPriority weight;
    private final boolean staticFlag;
    private final Consumer<Object> invoker;

    public LambdaListener(LookupFactory factory,
                          Class<?> host,
                          Object instance,
                          Method m) {
        this.eventType = m.getParameterTypes()[0];
        this.staticFlag = Modifier.isStatic(m.getModifiers());
        this.weight = m.getAnnotation(EventHandler.class).priority();
        this.owner = owner;

        try {
            MethodHandles.Lookup lookup = obtainLookup(factory, host);
            MethodHandle raw = staticFlag
                    ? lookup.findStatic(host, m.getName(),
                    MethodType.methodType(void.class, eventType))
                    : lookup.findVirtual(host, m.getName(),
                    MethodType.methodType(void.class, eventType));

            MethodType lambdaSig = MethodType.methodType(void.class, Object.class);
            MethodType instantiated = MethodType.methodType(void.class, eventType);

            CallSite site = LambdaMetafactory.metafactory(
                    lookup,
                    "accept",
                    staticFlag
                            ? MethodType.methodType(Consumer.class)
                            : MethodType.methodType(Consumer.class, host),
                    lambdaSig,
                    raw,
                    instantiated);

            MethodHandle factoryHandle = site.getTarget();
            this.invoker = staticFlag
                    ? (Consumer<Object>) factoryHandle.invoke()
                    : (Consumer<Object>) factoryHandle.invoke(instance);
        } catch (Throwable t) {
            throw new RuntimeException("Cannot bind lambda listener", t);
        }
    }

    private static MethodHandles.Lookup obtainLookup(LookupFactory factory, Class<?> host)
            throws Throwable {
        if (LEGACY_JDK) {
            boolean old = lookupCtor.isAccessible();
            lookupCtor.setAccessible(true);
            try {
                return lookupCtor.newInstance(host);
            } finally {
                lookupCtor.setAccessible(old);
            }
        }
        return (MethodHandles.Lookup) privateLookup.invoke(null, host, MethodHandles.lookup());
    }

    @Override public void call(Object e) {
        invoker.accept(e);
    }

    @Override public Class<?> getTarget() {
        return eventType;
    }

    @Override public Event.EventPriority getPriority() {
        return weight;
    }

    @Override public boolean isStatic() {
        return staticFlag;
    }

    public interface LookupFactory {
        MethodHandles.Lookup create(Method m, Class<?> c) throws Throwable;
    }

    private Object owner = null;

    public Object getOwner() { return owner; }
}