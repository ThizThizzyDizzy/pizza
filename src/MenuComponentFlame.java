import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentFlame extends MenuComponent{
    double opacity;
    double rotation;
    double scale;
    public MenuComponentFlame(double x, double y, double opacity){
        super(x-50, y-50, 100, 100);
        this.opacity = opacity;
    }
    @Override
    public void tick(){
        rotation = Core.rand.nextInt(20)-10;
        scale = Core.rand.nextDouble()/5+.9;
        opacity-=0.01;
    }
    @Override
    public void render(){
        removeRenderBound();
        if(opacity<=0){
            return;
        }
        GL11.glColor4d(1, 1, 1, Math.min(1,opacity));
        GL11.glPushMatrix();
        GL11.glTranslated(x+width/2, y+height/2, 0);
        GL11.glScaled(scale, scale, 1);
        GL11.glRotated(rotation, 0, 0, 1);
        drawRect(-width/2, -height/2, width/2, height/2, ImageStash.instance.getTexture("/textures/flame.png"));
        GL11.glPopMatrix();
        GL11.glColor4d(1, 1, 1, 1);
    }
}