package tech.klux.kluxcore.eventbus.listeners;

import tech.klux.kluxcore.eventbus.Event;

public interface IListener {
    void call(Object event);
    Class<?> getTarget();
    Event.EventPriority getPriority();
    boolean isStatic();
}
