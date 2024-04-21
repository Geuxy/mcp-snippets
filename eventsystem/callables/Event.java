package callables;

import EventManager;

public class Event {

    public void call() {
        EventManager.callEvent(this);
    }

}
