import java.awt.event.ActionEvent;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentIceCreamDispenser extends MenuComponent{
    public MenuComponentCone cone;
    public MenuComponentIceCreamDispenser(double x, double y, double width, double height){
        super(x, y, width, height);
        cone = add(new MenuComponentCone(50, height-200, 100, 200));
        int flavors = IceCream.values().length;
        double buttonSize = width/flavors;
        for(int i = 0; i<flavors; i++){
            MenuComponentIceCreamButton button = new MenuComponentIceCreamButton(buttonSize*i, 0, buttonSize, buttonSize, "", true, true, "/textures/ice cream/"+IceCream.values()[i].texture, IceCream.values()[i]);
            add(button);
            IceCream flavor = IceCream.values()[i];
            button.addActionListener((ActionEvent e) -> {
                if(flavor.topping&&cone.iceCream.isEmpty())return;
                cone.addIceCream(flavor);
            });
        }
    }
    @Override
    public void tick(){
        cone.tick();
    }
    @Override
    public void render(){
        removeRenderBound();
        drawRect(x,y,x+width,y+height,ImageStash.instance.getTexture("/textures/ice cream dispenser.png"));
    }
    public void resetCone(){
        components.remove(cone);
        cone = add(new MenuComponentCone(50, height-200, 100, 200));
    }
    public static class MenuComponentIceCreamButton extends MenuComponentButton{
        final IceCream iceCream;
        public MenuComponentIceCreamButton(double x, double y, double width, double height, String label, boolean enabled, boolean useMouseover, String textureRoot, IceCream iceCream){
            super(x, y, width, height, label, enabled, useMouseover, textureRoot);
            this.iceCream = iceCream;
        }
    }
}