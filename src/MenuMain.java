import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentOptionButton;
public class MenuMain extends Menu{
    private ArrayList<MenuComponentTopping> toppings = new ArrayList<>();
    private final MenuComponentButton play;
    private final MenuComponentButton exit;
    private final MenuComponentOptionButton tutorial;
    private static final int fallSpeed = 5;
    private final MenuComponentCredits credits;
    public MenuMain(GUI gui, Menu parent){
        super(gui, parent);
        play = add(new MenuComponentButton(Display.getWidth()/2-400, 200, 800, 100, "Make Pizza", true, true, "/textures/gui/button"));
        exit = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-200, 800, 100, "Exit", true, true, "/textures/gui/button"));
        tutorial = add(new MenuComponentOptionButton(Display.getWidth()/2-400, Math.round((play.y+exit.y)/2), 800, 100, "Tutorial", true, true, "/textures/gui/button", 0, "OFF", "ON"));
        credits = add(new MenuComponentCredits(Display.getWidth(), Display.getHeight()/2, 1200, 1200));
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==play){
            gui.open(new MenuTransition(gui, this, new MenuMakePizza(gui, this, tutorial.getIndex()==1)));
        }
        if(button==exit){
            Core.helper.running = false;
        }
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat){
        if(key==Keyboard.KEY_M&&pressed&&!repeat){
            Sounds.toggleMusic();
        }
    }
    private int tick = 0;
    public void render(int millisSinceLastTick){
        super.render(millisSinceLastTick);
        credits.render();
        play.render();
        exit.render();
        tutorial.render();
        GL11.glColor4d(0, 0, 0, 1);
        drawCenteredText(0, Display.getHeight()-25, Display.getWidth(), Display.getHeight(), "Press M to toggle music");
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void tick(){
        super.tick();
        tick++;
        if(tick%2==0){
            MenuComponentTopping top = new MenuComponentTopping(Core.rand.nextInt(Display.getWidth()), Display.getHeight(), Topping.randomTopping());
            top.rotation = Core.rand.nextInt(360);
            top.targetY = top.y;
            toppings.add(add(top));
        }
        for(MenuComponentTopping topping : toppings){
            topping.targetY+=fallSpeed;
            topping.y+=fallSpeed;
            topping.rotation+=fallSpeed;
        }
        for(Iterator<MenuComponentTopping> it = toppings.iterator(); it.hasNext();){
            MenuComponentTopping topping = it.next();
            if(topping.y>=Display.getHeight()){
                components.remove(topping);
                it.remove();
            }
        }
    }
    @Override
    public void renderBackground(){
        play.y = 200;
        exit.y = Display.getHeight()-200;
        tutorial.x = play.x = exit.x = Display.getWidth()/2-400;
        tutorial.y = Math.round((play.y+exit.y)/2);
    }
}