
import lombok.Getter;

@Getter
public class Event {

    private boolean cancelled;

    public void call() {
        // TODO: Create variable of "EventBus" to access "callEvent"
        Client.getInstance().getEventBus().callEvent(this);
    }

    public void cancel() {
        this.cancelled = true;
    }

}
