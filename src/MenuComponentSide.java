import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentSide extends MenuComponent{
    private int falling = 0;
    public final Side side;
    public double targetY;
    public MenuComponentSide(double x, double y, Side side){
        super(x, -500, 100, 100);
        targetY = y;
        this.side = side;
    }
    @Override
    public void tick(){
        if(y<targetY){
            falling+=5;
            y+=falling;
        }
        if(y>targetY){
            y = targetY;
        }
    }
    @Override
    public void render(){
        removeRenderBound();
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/side/"+side.texture+".png"));
    }
}