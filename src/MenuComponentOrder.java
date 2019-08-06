import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentOrder extends MenuComponentButton{
    public int[] amounts = new int[Topping.values().length];
    public int[] targetSideAmounts = new int[Side.values().length];
    public int[] sideAmounts = new int[Side.values().length];
    public final PizzaCrust crust;
    private int sauces;
    private int toppings;
    private final MenuMakePizza menu;
    MenuComponentCrust pizza;
    MenuComponentCrust targetPizza;
    MenuComponentCone cone;
    MenuComponentCone targetCone;
    public boolean finished = false;
    public MenuComponentOrder(MenuMakePizza menu){
        super(0, 0, 350, Display.getHeight(), "", true);
        crust = PizzaCrust.randomCrust();
        if(Core.rand.nextDouble()<.7){
            targetCone = new MenuComponentCone(5*(width/8), 0, width/16, width/8);
            targetCone.addIceCream(IceCream.randomIceCream(false));
            while(Core.rand.nextDouble()<.6){
                targetCone.addIceCream(IceCream.randomIceCream());
            }
            if(targetCone.iceCream.isEmpty())targetCone = null;
        }
        targetPizza = new MenuComponentCrust(0, 0, 0, 0, crust);
        for(int i = 0; i<amounts.length; i++){
            Topping topping = Topping.values()[i];
            int amount = topping.regularAmount;
            if(i<Topping.sauces-1){
                if(Core.rand.nextDouble()<1D/(Topping.sauces)){
                    amounts[i] = Core.rand.nextInt(amount)+amount/2;
                    sauces++;
                    for(int j = 0; j<amounts[i]; j++){
                        MenuComponentTopping top = new MenuComponentTopping(0, 0, topping);
                        targetPizza.addTopping(top);
                        top.y = top.targetY;
                    }
                }else{
                    amounts[i] = 0;
                }
                continue;
            }
            if(i==Topping.sauces-1){
                if(sauces<1){
                    amounts[i] = Core.rand.nextInt(amount)+amount/2;
                    sauces++;
                    for(int j = 0; j<amounts[i]; j++){
                        MenuComponentTopping top = new MenuComponentTopping(0, 0, topping);
                        targetPizza.addTopping(top);
                        top.y = top.targetY;
                    }
                }else{
                    amounts[i] = 0;
                }
                continue;
            }
            if(Core.rand.nextDouble()<10D/Topping.values().length){
                amounts[i] = Core.rand.nextInt(amount)+amount/2;
                toppings++;
                for(int j = 0; j<amounts[i]; j++){
                    MenuComponentTopping top = new MenuComponentTopping(0, 0, topping);
                    targetPizza.addTopping(top);
                    top.y = top.targetY;
                }
            }else{
                amounts[i] = 0;
            }
            if(i==amounts.length-1&&toppings==0){
                amounts[i] = Core.rand.nextInt(amount)+amount/2;
            }
        }
        for(int i = 0; i<targetSideAmounts.length; i++){
            Side side = Side.values()[i];
            int amount = side.regularAmount;
            if(Core.rand.nextDouble()<1D/Side.values().length){
                targetSideAmounts[i] = Core.rand.nextInt(amount)+amount/2;
            }else{
                targetSideAmounts[i] = 0;
            }
        }
        targetPizza.findReadyComponents();
        toppings += sauces;
        this.menu = menu;
        addActionListener((ActionEvent e) -> {
            menu.selectedOrder = this;
        });
        targetPizza.cooked = 1;
    }
    public MenuComponentOrder(MenuMakePizza menu, int[] toppingAmounts){
        super(0, 0, 400, 2000, "", true);
        crust = PizzaCrust.randomCrust();
        targetPizza = new MenuComponentCrust(0, 0, 0, 0, crust);
        for(int i = 0; i<amounts.length; i++){
            Topping topping = Topping.values()[i];
            int amount = topping.regularAmount;
            amounts[i] = amount*toppingAmounts[i];
        }
        for(int i = 0; i<targetSideAmounts.length; i++){
            Side side = Side.values()[i];
            int amount = side.regularAmount;
            if(Core.rand.nextDouble()<.75/Side.values().length){
                targetSideAmounts[i] = Core.rand.nextInt(amount)+amount/2;
            }else{
                targetSideAmounts[i] = 0;
            }
        }
        targetPizza.findReadyComponents();
        toppings += sauces;
        this.menu = menu;
        addActionListener((ActionEvent e) -> {
            menu.selectedOrder = this;
        });
        targetPizza.cooked = 1;
    }
    @Override
    public void tick(){
        if(targetCone!=null){
            targetCone.tick();
        }
        if(pizza!=null&&((cone!=null&&targetCone!=null)||targetCone==null)){
            finished = true;
        }
    }
    @Override
    public void render(){
        removeRenderBound();
        int border = (int) Math.round(width/100);
        GL11.glColor4d(0.75, 0.75, 0.75, 1);
        drawRect(x, y, x+width, y+height, 0);
        GL11.glColor4d(0.95, 0.95, 0.95, 1);
        drawRect(x+border, y+border, x+width-border, y+height-border, 0);
//        GL11.glColor4d(0.95, 0.95, 0.95, 1);
//        drawRect(x, y, x+width, y+height, 0);
//        GL11.glColor4d(0.75, 0.75, 0.75, 1);
//        drawRect(x, y, x+border, y+height, 0);
//        drawRect(x, y, x+width, y+border, 0);
//        drawRect(x+width-border, y, x+width, y+height, 0);
//        drawRect(x, y+height-border, x+width, y+height, 0);
        GL11.glColor4d(1, 1, 1, 1);
        int pizzaIconSize = (int) Math.round((width-border*2)/2);
        int toppingIconSize = (int) Math.round((width-border*4)/4);
        int smileyIconSize = toppingIconSize/4;
        drawRect(x+border, y+border, x+border+pizzaIconSize, y+border+pizzaIconSize, ImageStash.instance.getTexture("/textures/crusts/"+crust.texture+".png"));
        GL11.glColor4d(1, 1, 1, 1);
        if(getPizza()!=null){
            if(getPizza().crust!=crust){
                GL11.glColor4d(1, 0, 0, 0.5);
            }else{
                GL11.glColor4d(0, 1, 0, 0.5);
            }
            drawRect(x+border, y+border, x+border+pizzaIconSize, y+border+pizzaIconSize, 0);
        }
        int offset = pizzaIconSize;
        Topping[] toppings = Topping.values();
        for(int i = 0; i<amounts.length; i++){
            if(amounts[i]>0){
                GL11.glColor4d(0.75, 0.75, 0.75, 1);
                drawRect(x+border,y+offset,x+width-border,y+border+offset,0);
                GL11.glColor4d(1, 1, 1, 1);
                drawRect(x+border, y+border+offset, x+border+toppingIconSize, y+border+toppingIconSize+offset, ImageStash.instance.getTexture("/textures/toppings/"+toppings[i].texture+".png"));
                GL11.glColor4d(0, 0, 0, 1);
                drawText(x+border+toppingIconSize, y+border+offset, x+width-border, y+border+offset+toppingIconSize*.25, toppings[i].regularAmount/2+"");
                drawCenteredText(x+border+toppingIconSize, y+border+offset, x+width-border, y+border+offset+toppingIconSize*.25, toppings[i].regularAmount+"");
                int length = (int) FontManager.getLengthForStringWithHeight(Math.round(toppings[i].regularAmount*1.5)+"", toppingIconSize*.25);
                drawText(x+width-border-length-1, y+border+offset, x+width-border, y+border+offset+toppingIconSize*.25, Math.round(toppings[i].regularAmount*1.5)+"");
                GL11.glColor4d(0.75, 0.75, 0.75, 1);
                drawRect(x+border+toppingIconSize, y+border+offset+toppingIconSize*.25, x+width-border, y+border+offset+toppingIconSize*.5, 0);
                GL11.glColor4d(0.95, 0.95, 0.95, 1);
                drawRect(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.25, x+width-border*2, y+offset+toppingIconSize*.5, 0);
                double percent = (amounts[i]-toppings[i].regularAmount*.5)/toppings[i].regularAmount;
                double actualPercent = Math.max(0,(getActualAmount(i)-toppings[i].regularAmount*.5)/toppings[i].regularAmount);
                double w = width-border*4-toppingIconSize;
                GL11.glColor4d(1, 0, 0, 1);
                drawRectWithBounds(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.25, x+border*2+toppingIconSize+(w*actualPercent), y+offset+toppingIconSize*.5,
                       x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.25, x+width-border*2, y+offset+toppingIconSize*.5, 0);
                GL11.glColor4d(0.85, 0.85, 0.85, 1);
                drawRect(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.25, x+border*2+toppingIconSize+(w*percent), y+offset+toppingIconSize*.5, 0);
                GL11.glColor4d(0, 1, 0, 1);
                drawRectWithBounds(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.25, x+border*2+toppingIconSize+(w*actualPercent), y+offset+toppingIconSize*.5,
                        x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.25, x+border*2+toppingIconSize+(w*percent), y+offset+toppingIconSize*.5, 0);
                GL11.glColor4d(0.75, 0.75, 0.75, 1);
                drawRect(x+border+toppingIconSize+smileyIconSize, y+border+offset+toppingIconSize*.75, x+width-border, y+border+offset+toppingIconSize, 0);
                GL11.glColor4d(0.95, 0.95, 0.95, 1);
                drawRect(x+border*2+toppingIconSize+smileyIconSize, y+border*2+offset+toppingIconSize*.75, x+width-border*2, y+offset+toppingIconSize, 0);
                percent = 1;
                actualPercent = getHappiness(i);
                w = width-border*4-toppingIconSize-smileyIconSize;
                drawRect(x+border+toppingIconSize, y+border+offset+toppingIconSize*.75, x+border+smileyIconSize+toppingIconSize, y+border+offset+toppingIconSize*.75+smileyIconSize, ImageStash.instance.getTexture("/textures/happy.png"));
                GL11.glColor4d(1, 0, 0, 1);
                drawRectWithBounds(x+border*2+toppingIconSize+smileyIconSize, y+border*2+offset+toppingIconSize*.75, x+border*2+smileyIconSize+toppingIconSize+(w*actualPercent), y+offset+toppingIconSize,
                       x+border*2+toppingIconSize+smileyIconSize, y+border*2+offset+toppingIconSize*.75, x+width-border*2+smileyIconSize, y+offset+toppingIconSize, 0);
                GL11.glColor4d(0.85, 0.85, 0.85, 1);
                drawRect(x+border*2+toppingIconSize+smileyIconSize, y+border*2+offset+toppingIconSize*.75, x+border*2+toppingIconSize+(w*percent)+smileyIconSize, y+offset+toppingIconSize, 0);
                GL11.glColor4d(0, 1, 0, 1);
                drawRectWithBounds(x+border*2+toppingIconSize+smileyIconSize, y+border*2+offset+toppingIconSize*.75, x+border*2+smileyIconSize+toppingIconSize+(w*actualPercent), y+offset+toppingIconSize,
                        x+border*2+toppingIconSize+smileyIconSize, y+border*2+offset+toppingIconSize*.75, x+border*2+toppingIconSize+(w*percent)+smileyIconSize, y+offset+toppingIconSize, 0);
                GL11.glColor4d(1, 1, 1, 1);
                offset += toppingIconSize;
            }
        }
        GL11.glColor4d(0.75, 0.75, 0.75, 1);
        drawRect(x+border+toppingIconSize, y+border+offset+toppingIconSize*.5, x+width-border, y+border+offset+toppingIconSize*.75, 0);
        GL11.glColor4d(0.95, 0.95, 0.95, 1);
        drawRect(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.5, x+width-border*2, y+offset+toppingIconSize*.75, 0);
        GL11.glColor4d(0.75, 0.75, 0.75, 1);
        drawRect(x+border,y+offset,x+width-border,y+border+offset,0);
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(x+border, y+border+offset, x+border+toppingIconSize, y+border+toppingIconSize+offset, ImageStash.instance.getTexture("/textures/oven/background.png"));
        drawRect(x+border, y+border+offset, x+border+toppingIconSize, y+border+toppingIconSize+offset, ImageStash.instance.getTexture("/textures/oven/0.png"));
        double percent = 1/3D;
        double actualPercent = getPizza()==null?0:(getPizza().cooked/3);
        double w = width-border*4-toppingIconSize;
        GL11.glColor4d(1, 0, 0, 1);
        drawRectWithBounds(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.5, x+border*2+toppingIconSize+(w*actualPercent), y+offset+toppingIconSize*.75,
               x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.5, x+width-border*2, y+offset+toppingIconSize, 0);
        GL11.glColor4d(0.85, 0.85, 0.85, 1);
        drawRect(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.5, x+border*2+toppingIconSize+(w*percent), y+offset+toppingIconSize*.75, 0);
        GL11.glColor4d(0, 1, 0, 1);
        drawRectWithBounds(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.5, x+border*2+toppingIconSize+(w*actualPercent), y+offset+toppingIconSize*.75,
                x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.5, x+border*2+toppingIconSize+(w*percent), y+offset+toppingIconSize*.75, 0);
        offset += toppingIconSize;
        Side[] sides = Side.values();
        for(int i = 0; i<targetSideAmounts.length; i++){
            if(targetSideAmounts[i]>0){
                GL11.glColor4d(0.75, 0.75, 0.75, 1);
                drawRect(x+border,y+offset,x+width-border,y+border+offset,0);
                GL11.glColor4d(1, 1, 1, 1);
                drawRect(x+border, y+border+offset, x+border+toppingIconSize, y+border+toppingIconSize+offset, ImageStash.instance.getTexture("/textures/sides/"+sides[i].texture+".png"));
                GL11.glColor4d(0, 0, 0, 1);
                drawText(x+border+toppingIconSize, y+border+offset, x+width-border, y+border+offset+toppingIconSize*.25, sides[i].regularAmount/2+"");
                drawCenteredText(x+border+toppingIconSize, y+border+offset, x+width-border, y+border+offset+toppingIconSize*.25, sides[i].regularAmount+"");
                int length = (int) FontManager.getLengthForStringWithHeight(Math.round(sides[i].regularAmount*1.5)+"", toppingIconSize*.25);
                drawText(x+width-border-length-1, y+border+offset, x+width-border, y+border+offset+toppingIconSize*.25, Math.round(sides[i].regularAmount*1.5)+"");
                GL11.glColor4d(0.75, 0.75, 0.75, 1);
                drawRect(x+border+toppingIconSize, y+border+offset+toppingIconSize*.25, x+width-border, y+border+offset+toppingIconSize*.5, 0);
                GL11.glColor4d(0.95, 0.95, 0.95, 1);
                drawRect(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.25, x+width-border*2, y+offset+toppingIconSize*.5, 0);
                percent = (targetSideAmounts[i]-sides[i].regularAmount*.5)/sides[i].regularAmount;
                actualPercent = Math.max(0,(sideAmounts[i]-sides[i].regularAmount*.5)/sides[i].regularAmount);
                w = width-border*4-toppingIconSize;
                GL11.glColor4d(1, 0, 0, 1);
                drawRectWithBounds(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.25, x+border*2+toppingIconSize+(w*actualPercent), y+offset+toppingIconSize*.5,
                       x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.25, x+width-border*2, y+offset+toppingIconSize*.5, 0);
                GL11.glColor4d(0.85, 0.85, 0.85, 1);
                drawRect(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.25, x+border*2+toppingIconSize+(w*percent), y+offset+toppingIconSize*.5, 0);
                GL11.glColor4d(0, 1, 0, 1);
                drawRectWithBounds(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.25, x+border*2+toppingIconSize+(w*actualPercent), y+offset+toppingIconSize*.5,
                        x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.25, x+border*2+toppingIconSize+(w*percent), y+offset+toppingIconSize*.5, 0);
                GL11.glColor4d(0.75, 0.75, 0.75, 1);
                drawRect(x+border+toppingIconSize+smileyIconSize, y+border+offset+toppingIconSize*.75, x+width-border, y+border+offset+toppingIconSize, 0);
                GL11.glColor4d(0.95, 0.95, 0.95, 1);
                drawRect(x+border*2+toppingIconSize+smileyIconSize, y+border*2+offset+toppingIconSize*.75, x+width-border*2, y+offset+toppingIconSize, 0);
                percent = 1;
                actualPercent = getSideHappiness(i);
                w = width-border*4-toppingIconSize-smileyIconSize;
                drawRect(x+border+toppingIconSize, y+border+offset+toppingIconSize*.75, x+border+smileyIconSize+toppingIconSize, y+border+offset+toppingIconSize*.75+smileyIconSize, ImageStash.instance.getTexture("/textures/happy.png"));
                GL11.glColor4d(1, 0, 0, 1);
                drawRectWithBounds(x+border*2+toppingIconSize+smileyIconSize, y+border*2+offset+toppingIconSize*.75, x+border*2+smileyIconSize+toppingIconSize+(w*actualPercent), y+offset+toppingIconSize,
                       x+border*2+toppingIconSize+smileyIconSize, y+border*2+offset+toppingIconSize*.75, x+width-border*2+smileyIconSize, y+offset+toppingIconSize, 0);
                GL11.glColor4d(0.85, 0.85, 0.85, 1);
                drawRect(x+border*2+toppingIconSize+smileyIconSize, y+border*2+offset+toppingIconSize*.75, x+border*2+toppingIconSize+(w*percent)+smileyIconSize, y+offset+toppingIconSize, 0);
                GL11.glColor4d(0, 1, 0, 1);
                drawRectWithBounds(x+border*2+toppingIconSize+smileyIconSize, y+border*2+offset+toppingIconSize*.75, x+border*2+smileyIconSize+toppingIconSize+(w*actualPercent), y+offset+toppingIconSize,
                        x+border*2+toppingIconSize+smileyIconSize, y+border*2+offset+toppingIconSize*.75, x+border*2+toppingIconSize+(w*percent)+smileyIconSize, y+offset+toppingIconSize, 0);
                GL11.glColor4d(1, 1, 1, 1);
                offset += toppingIconSize;
            }
        }
        GL11.glColor4d(0.75, 0.75, 0.75, 1);
        drawRect(x+border+toppingIconSize, y+border+offset+toppingIconSize*.5, x+width-border, y+border+offset+toppingIconSize*.75, 0);
        GL11.glColor4d(0.95, 0.95, 0.95, 1);
        drawRect(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.5, x+width-border*2, y+offset+toppingIconSize*.75, 0);
        GL11.glColor4d(0.75, 0.75, 0.75, 1);
        drawRect(x+border,y+offset,x+width-border,y+border+offset,0);
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(x+border, y+border+offset, x+border+toppingIconSize, y+border+toppingIconSize+offset, ImageStash.instance.getTexture("/textures/happy.png"));
        percent = Math.max(0,getPizza()==null?0:(getCost(getPizza())/getCost(targetPizza)));
        if(getPizza()!=null){
            double cooked = getPizza().cooked;
            getPizza().cooked = 1;
            percent = Math.max(0,getPizza()==null?0:(getCost(getPizza())/getCost(targetPizza)));
            getPizza().cooked = cooked;
        }
        actualPercent = Math.max(0,getPizza()==null?0:(getCost(getPizza())/getCost(targetPizza)));
        w = width-border*4-toppingIconSize;
        GL11.glColor4d(1, 0, 0, 1);
        drawRectWithBounds(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.5, x+border*2+toppingIconSize+(w*actualPercent), y+offset+toppingIconSize*.75,
               x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.5, x+width-border*2, y+offset+toppingIconSize, 0);
        GL11.glColor4d(0.85, 0.85, 0.85, 1);
        drawRect(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.5, x+border*2+toppingIconSize+(w*percent), y+offset+toppingIconSize*.75, 0);
        GL11.glColor4d(0, 1, 0, 1);
        drawRectWithBounds(x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.5, x+border*2+toppingIconSize+(w*actualPercent), y+offset+toppingIconSize*.75,
                x+border*2+toppingIconSize, y+border*2+offset+toppingIconSize*.5, x+border*2+toppingIconSize+(w*percent), y+offset+toppingIconSize*.75, 0);
        offset += toppingIconSize;
        GL11.glColor4d(1, 1, 1, 1);
        if(targetCone!=null){
            targetCone.width = width/16;
            targetCone.height = width/8;
            targetCone.x = x+border+pizzaIconSize+targetCone.iceCream.get(0).width/2-targetCone.width/2;
            targetCone.y = y+border+pizzaIconSize-targetCone.height;
            targetCone.render();
            double theY = targetCone.y;
            for(int i = 0; i<targetCone.iceCream.size(); i++){
                MenuComponentIceCream targetCream = targetCone.iceCream.get(i);
                if(theY==targetCone.y){
                    theY+=targetCream.height()*.9;
                }else{
                    theY+=targetCream.height()*.8;
                }
                targetCream.y = targetCone.y-theY;
                targetCream.x =targetCone.x+targetCone.width/2-targetCream.width/2;
                targetCream.y+=targetCone.y;
                targetCream.render();
                if(getCone().iceCream.size()>=i+1){
                    MenuComponentIceCream cream = getCone().iceCream.get(i);
                    if(cream.iceCream==targetCream.iceCream){
                        GL11.glColor4d(0, 1, 0, 0.5);
                    }else{
                        GL11.glColor4d(1, 0, 0, 0.5);
                    }
                    drawRect(targetCream.x, targetCream.y, targetCream.x+targetCream.width, targetCream.y+targetCream.height, 0);
                    GL11.glColor4d(1, 1, 1, 1);
                }
                targetCream.x-=targetCone.x;
                targetCream.y-=targetCone.y;
            }
            if(getCone()!=null&&getCone().iceCream.size()>targetCone.iceCream.size()){
                theY+=80*targetCone.scale*(getCone().iceCream.size()-targetCone.iceCream.size());
                GL11.glColor4d(1, 0, 0, 0.5);
                drawRect(targetCone.x,targetCone.y-theY+targetCone.y,targetCone.x+targetCone.width,targetCone.y-theY+targetCone.y+targetCone.width,0);
                GL11.glColor4d(1, 1, 1, 1);
            }
        }
        if(!getCone().iceCream.isEmpty()&&targetCone==null){
            GL11.glColor4d(1, 0, 0, 0.5);
            drawRect(width/2+x,y,width/2+x+width/8,y+width/2,0);
            GL11.glColor4d(1, 1, 1, 1);
        }
    }
    private double getActualAmount(int i){
        if(getPizza()==null){
            return 0;
        }
        return getPizza().getToppingAmount(Topping.values()[i]);
    }
    public double getHappiness(int i){
        if(amounts[i]>0&&getPizza()!=null){
            Topping topping = Topping.values()[i];
            double amount = getPizza().getToppingAmount(topping);
            double targetAmount = amounts[i];
            if(amount>2*targetAmount||amount<0.5*targetAmount){
                return 0;
            }
            if(amount<targetAmount){
                return ((amount-(targetAmount*.5))*2)/targetAmount;
            }else if(amount>targetAmount){
                return ((targetAmount*2)-amount)/targetAmount;
            }
            return 1;
        }
        return 0;
    }
    public double getSideHappiness(int i){
        if(amounts[i]>0){
            double amount = sideAmounts[i];
            double targetAmount = targetSideAmounts[i];
            if(amount>2*targetAmount||amount<0.5*targetAmount){
                return 0;
            }
            if(amount<targetAmount){
                return ((amount-(targetAmount*.5))*2)/targetAmount;
            }else if(amount>targetAmount){
                return ((targetAmount*2)-amount)/targetAmount;
            }
            return 1;
        }
        return 0;
    }
    public double getCost(MenuComponentCrust pizza){
        double cost = Math.min(pizza.getCost(1),targetPizza.getCost(1));
        double influence = 1D/(getToppingTypes(pizza, targetPizza)+1+getSideTypes());
        double pizzaValue = 0;
        double crustValue = cost*influence;
        if(pizza.crust!=crust){
            crustValue=0;
        }
        pizzaValue+=crustValue;
        for(int i = 0; i<amounts.length; i++){
            if(amounts[i]>0){
                double toppingValue = cost*influence;
                Topping topping = Topping.values()[i];
                double amount = pizza.getToppingAmount(topping);
                double targetAmount = amounts[i];
                if(amount>2*targetAmount||amount<0.5*targetAmount){
                    continue;
                }
                if(amount<targetAmount){
                    double percent = ((amount-(targetAmount*.5))*2)/targetAmount;
                    toppingValue *= percent;
                }else if(amount>targetAmount){
                    double percent = ((targetAmount*2)-amount)/targetAmount;
                    toppingValue *= percent;
                }
                pizzaValue+=toppingValue;
            }
        }
        for(int i = 0; i<sideAmounts.length; i++){
            if(sideAmounts[i]>0){
                double sideValue = cost*influence;
                if(sideAmounts[i]>2*targetSideAmounts[i]||sideAmounts[i]<0.5*targetSideAmounts[i]){
                    continue;
                }
                if(sideAmounts[i]<targetSideAmounts[i]){
                    double percent = ((sideAmounts[i]-(targetSideAmounts[i]*.5))*2)/targetSideAmounts[i];
                    sideValue *= percent;
                }else if(sideAmounts[i]>targetSideAmounts[i]){
                    double percent = ((targetSideAmounts[i]*2)-sideAmounts[i])/targetSideAmounts[i];
                    sideValue *= percent;
                }
                pizzaValue+=sideValue;
            }
        }
        return pizzaValue+getConeCost();
    }
    public double getConeCost(){
        if(targetCone==null||cone==null){
            return 0;
        }
        double cost = Math.min(cone.getCost(),targetCone.getCost());
        double influence = 1D/getIceCreamTypes(cone, targetCone);
        double coneValue = 0;
        for(IceCream cream : IceCream.values()){
            if(targetCone.getAmount(cream)>0){
                double creamValue = cost*influence;
                double amount = cone.getAmount(cream);
                double targetAmount = targetCone.getAmount(cream);
                if(amount>targetAmount+1||amount<targetAmount-1){
                    continue;
                }
                if(amount>targetAmount||amount<targetAmount){
                    creamValue *= 0.5;
                }
                coneValue += creamValue;
            }
        }
        return coneValue;
    }
    private MenuComponentCrust getPizza(){
        if(pizza!=null){
            return pizza;
        }
        if(menu.pizza!=null){
            return menu.pizza;
        }
        if(menu.oven.input!=null){
            return menu.oven.input;
        }
        if(menu.oven.pizza!=null){
            return menu.oven.pizza;
        }
        return null;
    }
    private MenuComponentCone getCone(){
        if(cone!=null){
            return cone;
        }
        if(menu.iceCream.cone!=null){
            return menu.iceCream.cone;
        }
        return null;
    }
    private int getToppingTypes(MenuComponentCrust pizza, MenuComponentCrust targetPizza){
        Set<Topping> types = new HashSet<>();
        for(MenuComponentTopping top : pizza.toppings){
            types.add(top.topping);
        }
        for(MenuComponentTopping top : targetPizza.toppings){
            types.add(top.topping);
        }
        return types.size();
    }
    private int getIceCreamTypes(MenuComponentCone cone, MenuComponentCone targetCone){
        Set<IceCream> types = new HashSet<>();
        for(MenuComponentIceCream cream : cone.iceCream){
            types.add(cream.iceCream);
        }
        for(MenuComponentIceCream cream : targetCone.iceCream){
            types.add(cream.iceCream);
        }
        return types.size();
    }
    private int getSideTypes(){
        Set<Side> types = new HashSet<>();
        for(int i = 0; i<targetSideAmounts.length; i++){
            if(targetSideAmounts[i]>0){
                types.add(Side.values()[i]);
            }
        }
        for(int i = 0; i<sideAmounts.length; i++){
            if(sideAmounts[i]>0){
                types.add(Side.values()[i]);
            }
        }
        return types.size();
    }
}