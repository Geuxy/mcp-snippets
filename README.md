# MCP Snippets
Java code snippets for Minecraft mod / client development

## Animation
A simple animation utility mainly for 2D rendering stuff
Example usage:
```
final Animation animation = new Animation();

@EventListener
public void onRender2D(Render2DEvent event) {
    this.animation.animate(100, 0.03F);

    Gui.drawRect(0, 0, animation.getValue(), 100, -1);
}
```

## Shader
A simple GL shader system
Example shader:
```
@ShaderInfo(frag = "wallpaper.fsh")
public class WallpaperShader extends ClientShader {

    @Override
    public void render(Object... objects) {
        float width = (float) objects[0];
        float height = (float) objects[1];
        float time = (float) objects[2];

        useShader();

        setUniformF("resolution", width, height);
        setUniformF("time", time);

        drawQuad(-1, -1, 1, 1);

        disuseShader();
    }

}
```
Example Shader Usage:
```
private WallpaperShader wallpaperShader;

private int time;

public void initGui() {
    this.time = System.currentTimeMillis();
}

@Override
public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    if(wallpaperShader == null) {
        wallpaperShader = new WallpaperShader();
    }

    drawDefaultBackground();
    
    GlStateManager.enableAlpha();
    GlStateManager.disableCull();

    wallpaperShader.render(width, height * 2, (System.currentTimeMillis() - time) / 1000F);
}
```

## Raycast
A utility to check what the player is aiming at depending on the given rotations.
Example Usage:
```
double range = 3;
MovingObjectPosition cast = RaycastUtil.raycast(range);

if(cast != null) {
    if(cast.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
        System.out.println("HIT ENTITY");

    } else if(cast.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
        System.out.println("HIT BLOCK");
    }
}

if(cast != null && cast.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
    System.out.println("HIT ENTITY");
}
```
