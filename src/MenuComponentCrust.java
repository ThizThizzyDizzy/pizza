import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.Queue;
import simplelibrary.config2.Config;
import simplelibrary.game.Framebuffer;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentMulticolumnList;
public class MenuComponentCrust extends MenuComponent{
    public final PizzaCrust crust;
    public Framebuffer framebuffer;
    public Framebuffer cookedFramebuffer;
    public Framebuffer burntFramebuffer;
    private boolean newPizza = true;
    public double pizzaHeight = 0;
    public double pizzaCost = 0;
    public double rotation = 0;
    public double cooked;
    public boolean isCooked;
    public final ArrayList<MenuComponentTopping> toppings = new ArrayList<>();
    public boolean explosion;
    public double flame;
    public double smoke;
    public static final double pizzaSize = 750;
    public MenuComponentCrust(double x, double y, double width, double height, PizzaCrust crust){
        super(x, y, width, height);
        this.crust = crust;
        resetLocation();
        pizzaHeight = crust.height;
        pizzaCost = crust.cost;
    }
    @Override
    public void render(){
        removeRenderBound();
        if(newPizza){
            resetLocation();
            newPizza = false;
            framebuffer = new Framebuffer(Core.helper, null, (int)this.width, (int)this.height);
            cookedFramebuffer = new Framebuffer(Core.helper, null, (int)this.width, (int)this.height);
            burntFramebuffer = new Framebuffer(Core.helper, null, (int)this.width, (int)this.height);
            framebuffer.bindRenderTarget2D();
            GL11.glClearColor(1, 1, 1, 0);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            drawRect(0, 0, width, height, ImageStash.instance.getTexture("/textures/crusts/"+crust.texture+"Uncooked.png"));
            framebuffer.releaseRenderTarget();
            cookedFramebuffer.bindRenderTarget2D();
            GL11.glClearColor(1, 1, 1, 0);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            drawRect(0, 0, width, height, ImageStash.instance.getTexture("/textures/crusts/"+crust.texture+".png"));
            cookedFramebuffer.releaseRenderTarget();
            burntFramebuffer.bindRenderTarget2D();
            GL11.glClearColor(1, 1, 1, 0);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            drawRect(0, 0, width, height, ImageStash.instance.getTexture("/textures/crusts/"+crust.texture+"Burnt.png"));
            burntFramebuffer.releaseRenderTarget();
        }
        if(!components.isEmpty()){
            resetLocation();
            Queue<MenuComponentTopping> readyComponents = findReadyComponents();
            if(readyComponents!=null){
                drawComponents(readyComponents, framebuffer);
                for(MenuComponentTopping topping : readyComponents){
                    topping.cooked++;
                }
                drawComponents(readyComponents, cookedFramebuffer);
                for(MenuComponentTopping topping : readyComponents){
                    topping.cooked++;
                }
                drawComponents(readyComponents, burntFramebuffer);
            }
        }
        GL11.glPushMatrix();
        translate(x+width/2, y+height/2);
        GL11.glTranslated(x+width/2, y+height/2, 0);
        GL11.glRotated(rotation, 0, 0, 1);
        drawRect(-width/2, -height/2, width/2, height/2, framebuffer.getTexture());
        if(cooked<=1){
            GL11.glColor4d(1, 1, 1, cooked);
            drawRect(-width/2, -height/2, width/2, height/2, cookedFramebuffer.getTexture());
        }else if(cooked<=1.25){
            drawRect(-width/2, -height/2, width/2, height/2, cookedFramebuffer.getTexture());
            GL11.glColor4d(1, 1, 1, (cooked-1)*4);
            drawRect(-width/2, -height/2, width/2, height/2, burntFramebuffer.getTexture());
        }else if(cooked<=1.5){
            GL11.glColor4d(1-((cooked-1.25)*2), 1-((cooked-1.25)*2), 1-((cooked-1.25)*2), 1);
            drawRect(-width/2, -height/2, width/2, height/2, burntFramebuffer.getTexture());
            smoke = 0;
        }else{
            GL11.glColor4d(0.5, 0.5, 0.5, 1);
            drawRect(-width/2, -height/2, width/2, height/2, burntFramebuffer.getTexture());
            smoke = cooked-1.5;
            if(cooked<=3){
                flame = cooked-2;
            }else{
                explosion = true;
            }
        }
        GL11.glColor4d(1, 1, 1, 1);
        untranslate();
        GL11.glPopMatrix();
    }
    Queue<MenuComponentTopping> findReadyComponents(){
        Queue<MenuComponentTopping> value = null;
        for(Iterator<MenuComponent> it = components.iterator(); it.hasNext();){
            MenuComponent component = it.next();
            if(component instanceof MenuComponentTopping){
                MenuComponentTopping topping = (MenuComponentTopping) component;
                if(topping.y==topping.targetY){
                    if(value==null){
                        value = new Queue<>();
                    }
                    value.enqueue(topping);
                    pizzaHeight+=topping.topping.height;
                    pizzaCost += topping.topping.cost;
                    it.remove();
                }
            }
        }
        return value;
    }
    private void drawComponents(Queue<MenuComponentTopping> readyComponents, Framebuffer buffer){
        buffer.bindRenderTarget2D();
        for(MenuComponentTopping comp : readyComponents){
            comp.render();
        }
        buffer.releaseRenderTarget();
    }
    public void addTopping(MenuComponentTopping topping){
        toppings.add(add(topping));
    }
    @Override
    public void mouseEvent(double x, double y, int button, boolean isDown){
        if(button==0&&isDown){
            if(parent instanceof MenuComponentMulticolumnList){
                MenuComponentMulticolumnList list = (MenuComponentMulticolumnList) parent;
                MenuMakePizza menu = (MenuMakePizza) list.parent;
                if(menu.pizza==null){
                    menu.pizza = this;
                    menu.pizzaFromStorage = this;
                    menu.pizzaVelocityX = 0;
                    menu.pizzaVelocityY = 0;
                    menu.pizzaGrabbed = true;
                }
            }
        }
    }
    public void resetLocation(){
        width = height = pizzaSize;
        x = y = Display.getHeight()/2-height/2;
        rotation = 0;
    }
    public double getCost(int earned){
        if(earned>0){
            double cost = pizzaCost;
            if(cooked<.5){
                return -cost*(1-(cooked*2));
            }
            if(cooked>1.25){
                return -cost;
                
            }
            if(cooked<=1){
                return cost*(cooked-.5)*2;
            }else{
                return cost*(((1-cooked)*8)+1);
            }
        }else if(earned<0){
            return pizzaCost;
        }else{
            return 0;
        }
    }
    public int getToppingAmount(Topping topping){
        int amount = 0;
        for(MenuComponentTopping top : toppings){
            if(top.topping==topping){
                amount++;
            }
        }
        return amount;
    }
    public int getToppingTypes(){
        Set<Topping> types = new HashSet<>();
        for(MenuComponentTopping top : toppings){
            types.add(top.topping);
        }
        return types.size();
    }
    public Config save(){
        Config config = Config.newConfig();
        config.set("crust", crust.name());
        config.set("toppings", toppings.size());
        config.set("cooked", cooked);
        for(int i = 0; i<toppings.size(); i++){
            MenuComponentTopping topping = toppings.get(i);
            config.set("topping "+i, topping.save());
        }
        return config;
    }
    public static MenuComponentCrust load(Config config){
        MenuComponentCrust crust = new MenuComponentCrust(0, 0, 0, 0, PizzaCrust.valueOf(config.get("crust")));
        for(int i = 0; i<config.get("toppings", 0); i++){
            crust.addTopping(MenuComponentTopping.load(config.get("topping "+i)));
        }
        crust.cooked = config.get("cooked", 0D);
        if(crust.cooked>0){
            crust.isCooked = true;
        }
        return crust;
    }
}