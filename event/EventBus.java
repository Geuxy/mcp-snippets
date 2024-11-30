
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {

    private final Map<Class<?>, CopyOnWriteArrayList<EventData>> registry = new ConcurrentHashMap<>();
    private final Comparator<EventData> comparator = Comparator.comparingInt(EventData::getPriority);

    public void subscribe(Object obj) {
        for(Method method : obj.getClass().getDeclaredMethods()) {
            if(!isListener(method)) {
                continue;
            }

            EventListener annotation = method.getAnnotation(EventListener.class);

            Class<?> eventType = method.getParameterTypes()[0];
            EventData eventData = new EventData(obj, method, annotation.priority());

            this.registry.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(eventData);
            this.registry.get(eventType).sort(comparator);
        }
    }

    public void unsubscribe(Object obj) {
        this.registry.values().forEach(v -> v.removeIf(d -> d.getSource() == obj));
    }

    public void callEvent(Event event) {
        List<EventData> listeners = registry.get(event.getClass());

        if(listeners != null && !listeners.isEmpty()) {
            listeners.forEach(l -> {
                try {
                    if(l != null) {
                        l.getTarget().invoke(l.getSource(), event);
                    }
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
            });
        }

    }

    public void clean() {
        this.registry.clear();
    }

    private boolean isListener(Method method) {
        return method.isAnnotationPresent(EventListener.class) && method.getParameterCount() == 1;
    }

}
