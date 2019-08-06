import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentLose extends MenuComponentButton{
    private static String[] texts = new String[]{"You're Fired!", "AGAIN!?!? Get out!"};
    private boolean exit = false;
    public MenuComponentLose(){
        super(Display.getWidth()/2-400, -100, 800, 100, "", true, true, "/textures/gui/button");
        textInset+=10;
        if(Core.fired==texts.length-1){
            exit = true;
        }
        label = texts[Core.fired];
        addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(exit){
                    Core.helper.running = false;
                }else{
                    gui.open(new MenuTransition(gui, parent, new MenuMain(gui, null)));
                }
            }
        });
    }
    @Override
    public void render(){
        removeRenderBound();
        if(y<Display.getHeight()/2-50){
            y+=10;
        }
        super.render();
    }
}