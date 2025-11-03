package tech.klux.kluxcore.eventbus;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}