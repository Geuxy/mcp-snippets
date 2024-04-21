import callables.Event;

import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EventManager {

    private static final Map<Class<?>, EventData> data = new HashMap<>();

    // I suggest replacing printStackTrace with something better like log4j
    private static final Consumer<Throwable> errorHandler = Throwable::printStackTrace;

    public static void register(Object o) {
        for(Method method : o.getClass().getMethods()) {
            if(!isBadMethod(method)) {
                data.put(method.getParameters()[0].getType(), new EventData(method, o));
            }
        }
    }

    public static void unregister(Object o) {
        data.entrySet().removeIf(e -> e.getValue().source() == o.getClass());
    }

    public static void callEvent(Event event) {
        for(Map.Entry<Class<?>, EventData> entry : data.entrySet()) {
            if(entry.getKey() == event.getClass()) {
                try {
                    entry.getValue().target().invoke(entry.getValue().source(), event);
                } catch (Throwable t) {
                    errorHandler.accept(t);
                }
            }
        }
    }

    private static boolean isBadMethod(Method method) {
        return !method.isAnnotationPresent(EventTarget.class) || method.getParameterCount() != 1;
    }

}
