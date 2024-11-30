
import lombok.Getter;
import lombok.SneakyThrows;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL20.*;

@Getter
public abstract class ClientShader {

    // Goes to "resources/assets/pulsar/shaders/"
    private final String assetsDir = "../pulsar/shaders/";

    private final int program;

    public abstract void render(Object... objects);

    public ClientShader() {
        ShaderInfo info = getClass().getAnnotation(ShaderInfo.class);

        if(info == null || info.frag() == null) {
            // Use your own console util or replace with 'System.out.println' idk lol
            ConsoleUtil.error("Shader is missing info.");
        }

        int program = glCreateProgram();

        glAttachShader(program, getShader(info.frag(), GL_FRAGMENT_SHADER));
        glAttachShader(program, getShader(info.vertex(), GL_VERTEX_SHADER));

        glLinkProgram(program);

        this.program = program;

        if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
            ConsoleUtil.error("Failed to link GLSL Shaders for '" + info.frag() + "'!");
        }
    }

    public void useShader() {
        glUseProgram(program);
    }

    public void disuseShader() {
        glUseProgram(0);
    }

    private int createShader(final InputStream stream, final int type) {
        final int shader = glCreateShader(type);

        glShaderSource(shader, readInputStream(stream));
        glCompileShader(shader);

        return shader;
    }

    @SneakyThrows
    private String readInputStream(final InputStream stream) {
        final StringBuilder text = new StringBuilder();

        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;

        while((line = reader.readLine()) != null) {
            text.append(line).append('\n');
        }

        return text.toString();
    }

    @SneakyThrows
    private int getShader(final String location, final int type) {
        return createShader(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(assetsDir + location)).getInputStream(), type);
    }

    protected void setUniformF(final String name, final float... args) {
        final int location = glGetUniformLocation(program, name);

        switch(args.length) {
        case 1:
            glUniform1f(location, args[0]);
            break;
        case 2:
            glUniform2f(location, args[0], args[1]);
            break;
        case 3:
            glUniform3f(location, args[0], args[1], args[2]);
            break;
        case 4:
            glUniform4f(location, args[0], args[1], args[2], args[3]);
            break;
        }
    }

    protected void setUniformI(final String name, final int... args) {
        final int location = glGetUniformLocation(program, name);

        switch(args.length) {
        case 1:
            glUniform1i(location, args[0]);
            break;
        case 2:
            glUniform2i(location, args[0], args[1]);
            break;
        case 3:
            glUniform3i(location, args[0], args[1], args[2]);
            break;
        case 4:
            glUniform4i(location, args[0], args[1], args[2], args[3]);
            break;
        }
    }

    protected void drawQuad(final float x, final float y, final float width, final float height) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(x - 1, y - 1);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(x - 1, y + height + 1);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(x + width + 1, y + height + 1);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(x + width + 1, y - 1);
        GL11.glEnd();
    }

}
