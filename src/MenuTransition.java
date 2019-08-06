import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.game.Framebuffer;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuTransition extends Menu{
    private final Menu from;
    private final Menu to;
    private final MenuComponentRollingButton button;
    private final boolean direction = Core.rand.nextBoolean();
    private static Framebuffer buffer = null;
    public MenuTransition(GUI gui, Menu from, Menu to){
        super(gui, from);
        if(direction){
            button = add(new MenuComponentRollingButton(Display.getWidth(), 0, Display.getHeight(), Display.getHeight(), "", true, false, "/textures/crusts/"+PizzaCrust.WHOLE_WHEAT.texture));
            button.targetRotation = 360;
        }else{
            button = add(new MenuComponentRollingButton(-Display.getHeight(), 0, Display.getHeight(), Display.getHeight(), "", true, false, "/textures/crusts/"+PizzaCrust.WHOLE_WHEAT.texture));
            button.targetRotation = -360;
        }
        this.from = from;
        this.to = to;
    }
    @Override
    public void tick(){
        from.tick();
        to.tick();
        button.tick();
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat){
        if(key==Keyboard.KEY_M&&pressed&&!repeat){
            Sounds.toggleMusic();
        }
    }
    @Override
    public void render(int millisSinceLastTick){
        clearBoundStack();
        if(buffer==null){
            buffer = new Framebuffer(Core.helper, "FWAMEBUFFUH", Display.getWidth(), Display.getHeight());
        }
        buffer.bindRenderTarget2D();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        from.render(millisSinceLastTick);
        buffer.releaseRenderTarget();
        int buttonCenter = (int) Math.round(button.x+button.width/2);
        to.render(millisSinceLastTick);
        if(direction){
            drawRectWithBounds(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, buttonCenter, Display.getHeight(), buffer.getTexture());
        }else{
            drawRectWithBounds(0, 0, Display.getWidth(), Display.getHeight(), buttonCenter, 0, Display.getWidth(), Display.getHeight(), buffer.getTexture());
        }
        clearBoundStack();
        super.render(millisSinceLastTick);
    }
    @Override
    public void renderBackground(){
        if(direction){
            if(button.x<-button.width){
                gui.open(to);
            }
        }else{
            if(button.x>Display.getWidth()){
                gui.open(to);
            }
        }
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        button.label = "=)";
    }
}