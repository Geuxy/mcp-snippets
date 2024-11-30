
public abstract class UIDraggableComponent extends UIComponent {

    private int lastX;
    private int lastY;

    public void setLastPos(int x, int y) {
        this.lastX = x;
        this.lastY = y;
    }

    public void animateDrag(int mouseX, int mouseY) {
        this.setPos(mouseX - lastX, mouseY - lastY);
    }

    public void drag(int mouseX, int mouseY) {
        this.setLastPos(mouseX - getX(), mouseY - getY());
    }

}
