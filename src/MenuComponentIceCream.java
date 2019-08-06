import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentIceCream extends MenuComponent{
    private int falling = 0;
    public final IceCream iceCream;
    public double targetY;
    private final double scale;
    public MenuComponentIceCream(double x, double y, IceCream iceCream, double scale){
        super(x, -500, iceCream.size*scale, iceCream.size*scale);
        targetY = y;
        this.scale = scale;
        this.iceCream = iceCream;
    }
    @Override
    public void tick(){
        if(y<targetY){
            falling+=5;
            y+=falling;
        }else if(y>targetY){
            y = targetY;
        }
    }
    @Override
    public void render(){
        removeRenderBound();
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/ice cream/"+iceCream.texture+".png"));
    }
    public double height(){
        return iceCream.height*scale;
    }
}