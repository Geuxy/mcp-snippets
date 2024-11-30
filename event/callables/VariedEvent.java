
public class VariedEvent extends Event {

    private EventDirection direction;

    public void call(EventDirection direction) {
        this.direction = direction;
        this.call();
    }

    public boolean isPre() {
        return direction == EventDirection.PRE;
    }

    public boolean isMid() {
        return direction == EventDirection.MID;
    }

    public boolean isPost() {
        return direction == EventDirection.POST;
    }

}
