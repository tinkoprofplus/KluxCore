package tech.klux.kluxcore.eventbus;

public class Event {
    private boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
    }

    public enum EventPriority {

        LOWEST(3),
        LOW(2),
        NORMAL(0),
        HIGH(-1),
        HIGHEST(-2);

        private final int weight;

        EventPriority(int weight) {
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }
    }
    public interface Cancellable {
        boolean isCancelled();
        void setCancelled(boolean cancelled);
    }
}