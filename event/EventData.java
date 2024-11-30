
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@Getter @RequiredArgsConstructor
public class EventData {

    private final Object source;
    private final Method target;
    private final int priority;

}
