import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentTopping extends MenuComponent{
    private int falling = 0;
    public int rotation;
    public final Topping topping;
    public double targetY;
    public int cooked;
    public MenuComponentTopping(double x, double y, Topping topping){
        super(x, y-Display.getHeight()-topping.size, topping.size, topping.size);
        targetY = y;
        this.topping = topping;
    }
    @Override
    public void render(){
        removeRenderBound();
        if(y<targetY){
            falling+=5;
            y+=falling;
        }
        if(y>targetY){
            y = targetY;
        }
        GL11.glPushMatrix();
        translate(x+width/2, y+height/2);
        GL11.glTranslated(x+width/2, y+height/2, 0);
        GL11.glRotated(rotation, 0, 0, 1);
        drawRect(-width/2, -height/2, width/2, height/2, ImageStash.instance.getTexture("/textures/toppings/"+topping.texture+(cooked==1?"Cooked":(cooked==2?"Burnt":""))+".png"));
        untranslate();
        GL11.glPopMatrix();
    }
    public Config save(){
        Config config = Config.newConfig();
        config.set("x", x);
        config.set("y", y);
        config.set("rot", rotation);
        config.set("name", topping.name());
        return config;
    }
    public static MenuComponentTopping load(Config config){
        MenuComponentTopping topping = new MenuComponentTopping(config.get("x"), config.get("y"), Topping.valueOf(config.get("name")));
        topping.y = topping.targetY = config.get("y");
        topping.rotation = config.get("rot");
        return topping;
    }
}