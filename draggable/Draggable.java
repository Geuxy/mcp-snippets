import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Draggable {

    private final String name;

    // Percentage-based position
    @Setter private float x;
    @Setter private float y;

    private float lastX;
    private float lastY;

    private final float width;
    private final float height;

    private float xOffset;
    private float yOffset;

    private ScreenSection sectionX = ScreenSection.START;
    private ScreenSection sectionY = ScreenSection.START;

    protected float renderX, renderY, renderWidth, renderHeight;

    public Draggable(String name, float width, float height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    protected abstract void render();
    protected abstract void renderDummy();

    public abstract boolean canRender();

    public void doRender(int screenWidth, int screenHeight) {
        this.renderX = x * screenWidth;
        this.renderY = y * screenHeight;
        this.renderWidth = width * screenWidth;
        this.renderHeight = height * screenHeight;
        this.render();
    }

    public void doRenderDummy(boolean dragged, int mouseX, int mouseY, int screenWidth, int screenHeight) {
        if(dragged) {
            this.animateDrag(mouseX, mouseY, screenWidth, screenHeight);
        }

        this.renderX = x * screenWidth;
        this.renderY = y * screenHeight;
        this.renderWidth = width;
        this.renderHeight = height;
        this.renderDummy();
    }

    public void animateDrag(int mouseX, int mouseY, int screenWidth, int screenHeight) {
        this.x = (mouseX - (lastX * screenWidth)) / screenWidth;
        this.y = (mouseY - (lastY * screenHeight)) / screenHeight;
    }

    public void drag(int mouseX, int mouseY, int screenWidth, int screenHeight) {
        this.lastX = (mouseX - (x * screenWidth)) / screenWidth;
        this.lastY = (mouseY - (y * screenHeight)) / screenHeight;
    }

    public void onResize(int screenWidth, int screenHeight) {
        this.renderX = sectionX == ScreenSection.END ? screenWidth - xOffset : sectionX == ScreenSection.MIDDLE ? ((float) screenWidth / 2) + xOffset : xOffset;
        this.renderY = sectionY == ScreenSection.END ? screenHeight - yOffset : sectionY == ScreenSection.MIDDLE ? ((float) screenHeight / 2) + yOffset : yOffset;
        this.x = renderX / screenWidth;
        this.y = renderY / screenHeight;
    }

    public void onStopDragging(int screenWidth, int screenHeight) {
        this.sectionX = x > 0.70 ? ScreenSection.END : x <= 0.30 ? ScreenSection.START : ScreenSection.MIDDLE;
        this.sectionY = y > 0.70 ? ScreenSection.END : y <= 0.30 ? ScreenSection.START : ScreenSection.MIDDLE;
        this.xOffset = sectionX == ScreenSection.START ? (x * screenWidth) : sectionX == ScreenSection.END ? (screenWidth - (x * screenWidth)) : (x * screenWidth) - ((float) screenWidth / 2);
        this.yOffset = sectionY == ScreenSection.START ? (y * screenHeight) : sectionY == ScreenSection.END ? (screenHeight - (y * screenHeight)) : (y * screenHeight) - ((float) screenHeight / 2);
    }

    public boolean isMouseAt(int mouseX, int mouseY) {
        return mouseX >= renderX && mouseX <= renderX + renderWidth && mouseY >= renderY && mouseY <= renderY + renderHeight;
    }

}
