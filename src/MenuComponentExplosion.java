import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentExplosion extends MenuComponent{
    private int rotation;
    private double opacity = 1;
    public MenuComponentExplosion(double x, double y){
        super(x, y, 0, 0);
    }
    @Override
    public void tick(){
        x-=50;
        y-=50;
        width+=100;
        height+=100;
        rotation+=10;
        opacity-=0.01;
    }
    @Override
    public void render(){
        removeRenderBound();
        if(opacity<=0){
            return;
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x+width/2, y+height/2, 0);
        GL11.glRotated(rotation, 0, 0, 1);
        GL11.glColor4d(1, 1, 1, opacity);
        drawRect(-width/2, -height/2, width/2, height/2, ImageStash.instance.getTexture("/textures/explosion.png"));
        GL11.glPopMatrix();
    }
}