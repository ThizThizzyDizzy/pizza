import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentSmoke extends MenuComponent{
    double opacity;
    public MenuComponentSmoke(double x, double y, double opacity){
        super(x-50, y-50, 100, 100);
        this.opacity = Core.rand.nextDouble()/2+opacity;
    }
    @Override
    public void tick(){
        y--;
        opacity-=0.005;
    }
    @Override
    public void render(){
        removeRenderBound();
        if(opacity<=0){
            return;
        }
        GL11.glColor4d(0.5+(opacity/3), 0.5+(opacity/3), 0.5+(opacity/3), Math.max(0, Math.min(1,opacity)));
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/smoke.png"));
        GL11.glColor4d(1, 1, 1, 1);
    }
}