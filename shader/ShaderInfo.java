
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ShaderInfo {

    String frag();
    String vertex() default "vertex_light.vsh";

}
