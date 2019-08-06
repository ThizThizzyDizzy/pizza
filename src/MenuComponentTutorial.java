import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentTutorial extends MenuComponentButton{
    private int progress;
    private final MenuMakePizza menu;
    public MenuComponent highlight;
    private MenuComponent targetHighlight;
    private double opacity;
    private String text = "Welcome to the pizza simulator!";
    private boolean clickRequired = false;
    private int pizzaToppings = 2_000_000;
    private double tick;
    public MenuComponentTutorial(MenuMakePizza menu){
        super(0, 0, Display.getWidth(), Display.getHeight(), "", true);
        this.menu = menu;
    }
    @Override
    public void tick(){
        if(targetHighlight!=null){
            if(highlight==null){
                highlight = new MenuComponent(targetHighlight.x, targetHighlight.y, targetHighlight.width, targetHighlight.height){
                    @Override
                    public void render(){}
                };
            }
            double xChange = (targetHighlight.x-highlight.x);
            double yChange = (targetHighlight.y-highlight.y);
            double widthChange = (targetHighlight.width-highlight.width);
            double heightChange = (targetHighlight.height-highlight.height);
            highlight.x += xChange;
            highlight.y += yChange;
            highlight.width += widthChange;
            highlight.height += heightChange;
        }
        switch(progress){
            case 0:
                clickRequired = true;
                break;
            case 1:
                text = "First, select the pizza builder tool";
                targetHighlight = menu.pizzaButton;
                menu.ovenOverride = false;
                menu.storageOverride = false;
                if(menu.selectedMenu==menu.pizzaButton){
                    progress++;
                }
                break;
            case 2:
                text = "Next, select the crusts tab";
                targetHighlight = menu.crustsButton;
                if(menu.selectedList==menu.crusts){
                    progress++;
                }
                break;
            case 3:
                text = "Next, select a pizza crust";
                targetHighlight = menu.crusts;
                if(menu.pizza!=null){
                    progress++;
                }
                break;
            case 4:
                text = "Now, select the toppings tab";
                targetHighlight = menu.toppingsButton;
                if(menu.selectedList==menu.toppings){
                    progress++;
                }
                break;
            case 5:
                text = "Now, click a topping to add one to your pizza";
                targetHighlight = menu.toppings;
                menu.randomizeToppings.setIndex(menu.randomizeToppings.startingIndex);
                if(!menu.pizza.toppings.isEmpty()){
                    progress++;
                }
                break;
            case 6:
                text = "Now, switch off Random Topping placement";
                targetHighlight = menu.randomizeToppings;
                if(menu.randomizeToppings.startingIndex!=menu.randomizeToppings.getIndex()){
                    progress++;
                }
                break;
            case 7:
                text = "Then select a topping";
                targetHighlight = menu.toppings;
                if(menu.selectedTopping!=null){
                    progress++;
                    pizzaToppings = menu.pizza.toppings.size();
                }
                break;
            case 8:
                text = "Now click on the pizza to place the topping";
                targetHighlight = menu.pizza;
                if(menu.pizza.toppings.size()>pizzaToppings){
                    progress++;
                }
                break;
            case 9:
                text = "Decorate your pizza to your liking";
                if(Core.isMouseWithinComponent(menu.pizza)&&menu.selectedTopping!=null){
                    targetHighlight = menu.pizza;
                }
                if(Core.isMouseWithinComponent(menu.toppings)){
                    targetHighlight = menu.toppings;
                }
                if(Core.isMouseWithinComponent(menu.randomizeToppings)){
                    targetHighlight = menu.randomizeToppings;
                }
                clickRequired = true;
                break;
            case 10:
                text = "Now that your pizza is decorated, select the grab tool";
                targetHighlight = menu.grabButton;
                if(menu.selectedMenu==menu.grabButton){
                    progress++;
                }
                break;
            case 11:
                text = "Click and hold the pizza to pick it up";
                targetHighlight = menu.pizza;
                if(menu.pizzaGrabbed){
                    progress++;
                }
                break;
            case 12:
                text = "Place the pizza in the oven";
                menu.ovenOverride = true;
                targetHighlight = menu.oven;
                if(menu.oven.input!=null){
                    progress++;
                }else if(menu.pizza==null&&menu.oven.input==null&&menu.oven.pizza==null&&menu.storage.components.isEmpty()){
                    progress = 1;
                }else if(menu.storage.components.size()>0){
                    menu.pizza = menu.add((MenuComponentCrust)menu.storage.components.get(0));
                    menu.pizzaFromStorage = menu.pizza;
                    menu.pizzaVelocityX = 0;
                    menu.pizzaVelocityY = 0;
                    menu.pizzaGrabbed = false;
                    menu.pizza.resetLocation();
                    progress--;
                }
                break;
            case 13:
                text = "Wait until the pizza is in the oven";
                highlight = null;
                targetHighlight = null;
                if(menu.oven.pizza!=null){
                    progress++;
                }
                break;
            case 14:
                text = "Take out the pizza when it's done cooking. Do not wait too long";
                targetHighlight = menu.oven;
                if(menu.pizzaGrabbed){
                    progress++;
                }
                break;
            case 15:
                menu.ovenOverride = false;
                menu.storageOverride = true;
                text = "Place the pizza in storage";
                targetHighlight = menu.storage;
                if(menu.pizza==null&&menu.oven.input==null&&menu.oven.pizza==null&&menu.storage.components.isEmpty()){
                    progress = 1;
                }else if(menu.storage.components.size()>0){
                    progress++;
                }else if(menu.oven.input!=null){
                    progress-=2;
                }
                break;
            case 16:
                text = "Congratulations! You have finished the tutorial.";
                menu.storageOverride = false;
                clickRequired = true;
                highlight = targetHighlight = null;
                break;
            case 17:
                menu.tutoFinished = true;
                text = "";
                break;
        }
        tick++;
        opacity = Math.sin(tick/25)/4+.5;
    }
    @Override
    public void render(){
        removeRenderBound();
        if(highlight!=null){
            GL11.glColor4d(0, 0, 0, 0.25);
            drawRect(x, y, highlight.x, Display.getHeight(), 0);
            drawRect(highlight.x+highlight.width, y, Display.getWidth(), Display.getHeight(), 0);
            drawRect(highlight.x, y, highlight.x+highlight.width, highlight.y, 0);
            drawRect(highlight.x, highlight.y+highlight.height, highlight.x+highlight.width, Display.getHeight(), 0);
            GL11.glColor4d(0, 200/255D, 1, opacity);
            drawRect(highlight.x, highlight.y, highlight.x+highlight.width, highlight.y+highlight.height, 0);
        }else{
            GL11.glColor4d(0, 0, 0, 0.25);
            drawRect(x, y, x+width, y+height, 0);
        }
        GL11.glColor4d(0, 0, 0, 1);
        int textOffset = 0;
        String str = text;
        while(str!=null&&!str.isEmpty()){
            str = drawCenteredTextWithWrap(x, y+textOffset, x+width, y+50+textOffset, str);
            textOffset += 50;
        }
        if(clickRequired){
            drawCenteredText(x, y+textOffset, x+width, y+25+textOffset, "Click to continue");
            textOffset+=25;
        }
        if(progress==5){
            drawCenteredText(x, y+textOffset, x+width, y+25+textOffset, "Use Ctrl to add 10 toppings");
            textOffset+=25;
            drawCenteredText(x, y+textOffset, x+width, y+25+textOffset, "Use Shift to add 100 toppings");
            textOffset+=25;
            drawCenteredText(x, y+textOffset, x+width, y+25+textOffset, "Use Both to add 1,000 toppings");
            textOffset+=25;
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
    public void clicked(){
        if(clickRequired){
            progress++;
            clickRequired = false;
        }
    }
    public MenuComponent getHighlight() {
        return highlight;
    }
    public void setHighlight(MenuComponent highlight) {
        this.targetHighlight = highlight;
    }
}