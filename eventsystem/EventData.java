import java.lang.reflect.Method;

public record EventData(Method target, Object source) {
}
