
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.FontRenderer;

@Getter @Setter
public abstract class UIComponent {

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public abstract void render(int mouseX, int mouseY);

    public void onMouseClick(int mouseX, int mouseY, int button) {
    }

    public void onMouseRelease(int button) {
    }

    public void onKeyPress(int key, char text) {
    }

    public void onClose() {
    }

    public void setBounds(int x, int y, int w, int h) {
        this.setPos(x, y);
        this.setSize(w, h);
    }

    public void setPos(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public void setSize(int w, int h) {
        this.setWidth(w);
        this.setHeight(h);
    }

    public boolean isMouseAt(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public boolean isMouseAt(int mouseX, int mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

}
