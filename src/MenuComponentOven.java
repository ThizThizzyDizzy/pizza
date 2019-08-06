import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentOven extends MenuComponent{
    MenuComponentCrust input;
    public MenuComponentCrust pizza;
    public int open = 0;
    public int tick = 0;
    public double cookingProgress = 0;
    public int cookTime = 0;
    private ArrayList<MenuComponentSmoke> smoke = new ArrayList<>();
    private ArrayList<MenuComponentFlame> flame = new ArrayList<>();
    private MenuComponentExplosion explosion;
    public boolean exploded;
    public boolean removePizza;
    private double cookDelay;
    private int ovenDoorY;
    public MenuComponentOven(){
        super(-250, 0, 250, 250);
        if(Core.fired>0){
            ovenDoorY = Display.getHeight();
        }
    }
    public boolean canHoldPizza(MenuComponentCrust pizza){
        return input==null&&this.pizza==null&&pizza.pizzaHeight<500;
    }
    public void addPizza(MenuComponentCrust pizza){
        input = pizza;
        input.x = 50;
        input.y = 250;
        input.width = 150;
        input.height = 100;
        input.rotation = 0;
    }
    @Override
    public void tick(){
        if(exploded){
            ovenDoorY+=50;
        }
        if(removePizza){
            components.remove(pizza);
            pizza = null;
            cookTime = 0;
            cookingProgress = 0;
            removePizza = false;
        }
        if(input!=null&&pizza==null){
            if(open<20&&tick!=15){
                open++;
                return;
            }
            if(tick<15){
                tick++;
                input.y-=10;
                return;
            }
            if(open>0){
                open--;
                return;
            }
            open = 0;
            tick = 0;
            pizza = input;
            startCooking(input);
            input = null;
        }
        if(pizza!=null&&open==0){
            if(cookDelay>0){
                cookDelay-=MenuMakePizza.simSpeed;
            }else{
                cookingProgress+=MenuMakePizza.simSpeed;
            }
        }
        if(pizza!=null&&pizza.smoke>0){
            for(int i = 0; i<pizza.smoke*10; i++){
                smoke.add(add(new MenuComponentSmoke(Core.rand.nextInt((int)pizza.width)+pizza.x, Core.rand.nextInt((int)pizza.height)+pizza.y, pizza.smoke)));
            }
        }
        if(pizza!=null&&pizza.flame>0){
            for(int i = 0; i<pizza.flame; i++){
                flame.add(add(new MenuComponentFlame(Core.rand.nextInt((int)width), Core.rand.nextInt((int)height), pizza.flame)));
            }
        }
        if(pizza!=null&&pizza.explosion&&x>=0&&!exploded){
            explosion = add(new MenuComponentExplosion(width/2, height/2));
        }
        for(Iterator<MenuComponentSmoke> it = smoke.iterator(); it.hasNext();){
            MenuComponentSmoke s = it.next();
            s.tick();
            if(s.opacity<=0){
                components.remove(s);
                it.remove();
            }
        }
        for(Iterator<MenuComponentFlame> it = flame.iterator(); it.hasNext();){
            MenuComponentFlame f = it.next();
            f.tick();
            if(f.opacity<=0){
                components.remove(f);
                it.remove();
            }
        }
        if(pizza!=null&&explosion!=null&&!exploded){
            exploded = true;
            parent.add(new MenuComponentLose());
            Core.fired++;
        }
        if(explosion!=null){
            explosion.tick();
            pizza = null;
        }
    }
    @Override
    public void render(){
        removeRenderBound();
        if(Core.fired>1){
            return;
        }
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/oven/background.png"));
        if(open>=18){
            drawRect(x, y+ovenDoorY, x+width, y+height+ovenDoorY, ImageStash.instance.getTexture("/textures/oven/"+open+".png"));
        }
        if(input!=null){
            input.render();
        }
        if(pizza!=null){
            pizza.x = x+50;
            pizza.cooked = cookingProgress/(cookTime+0D);
            pizza.render();
        }
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/oven/foreground.png"));
        if(open<18){            
            drawRect(x, y+ovenDoorY, x+width, y+height+ovenDoorY, ImageStash.instance.getTexture("/textures/oven/"+open+".png"));
        }
        if(pizza!=null){
            GL11.glColor4d(0, 0, 0, 1);
            drawRect(x, y, x+width, y+5, 0);
            if(pizza.cooked<=1){
                GL11.glColor4d(1-pizza.cooked, pizza.cooked, 0, 1);
            }else if(pizza.cooked<=1.5){
                GL11.glColor4d((pizza.cooked-1)*2, (1.5-pizza.cooked)*2, 0, 1);
            }else{
                GL11.glColor4d(1, 0, 0, 1);
            }
            drawRect(x+1, y+1, x+(width*Math.min(1,pizza.cooked))-1, y+4, 0);
            GL11.glColor4d(1, 1, 1, 1);
        }
    }
    private void startCooking(MenuComponentCrust input){
        double time = 12000;
        for(MenuComponentTopping topping : input.toppings){
            time += (topping.width/50D);
        }
        cookTime = (int) Math.round(time);
        if(input.isCooked){
            cookingProgress = (int) Math.round(input.cooked*cookTime);
            cookDelay=(int) Math.round(6000);
        }
    }
}