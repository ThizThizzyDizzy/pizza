import java.util.ArrayList;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentCone extends MenuComponent{
    public ArrayList<MenuComponentIceCream> iceCream = new ArrayList<>();
    public boolean ready;
    public double coneHeight;
    final double scale;
    public MenuComponentCone(double x, double y, double width, double height){
        super(x, y, width, height);
        coneHeight = 180;
        scale = height/(coneHeight+20);
    }
    @Override
    public void tick(){
        for(MenuComponentIceCream cream : iceCream){
            cream.tick();
        }
    }
    @Override
    public void render(){
        removeRenderBound();
        ready = true;
        for(MenuComponentIceCream cream : iceCream){
            if(cream.y!=cream.targetY){
                ready = false;
            }
        }
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/cone.png"));
    }
    public double getCost(){
        double cost = 0;
        for(MenuComponentIceCream cream : iceCream){
            cost += cream.iceCream.cost;
        }
        return cost;
    }
    public void addIceCream(IceCream iceCream){
        double h = 0;
        for(MenuComponentIceCream c : this.iceCream){
            h+=c.iceCream.height*.8;
        }
        if(iceCream.topping&&this.iceCream.isEmpty()||h>300){
            return;
        }
        if(iceCream.topping){
            this.iceCream.add(add(new MenuComponentIceCream(width/2-(iceCream.size*scale)/2, this.iceCream.get(this.iceCream.size()-1).targetY-iceCream.height*scale, iceCream, scale)));
        }else{
            this.iceCream.add(add(new MenuComponentIceCream(width/2-(iceCream.size*scale)/2, height-coneHeight-(iceCream.height*scale), iceCream, scale)));
        }
        coneHeight+=(iceCream.height*scale)*.8;
    }
    public int getAmount(IceCream cream){
        int amount = 0;
        for(MenuComponentIceCream c : iceCream){
            if(c.iceCream==cream){
                amount++;
            }
        }
        return amount;
    }
}