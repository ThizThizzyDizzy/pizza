import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.lwjgl.opengl.GL11;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentSimSpeedController extends MenuComponent{
    public double simSpeed = 1;
    private final MenuComponentButton slowest;
    private final MenuComponentButton slower;
    private final MenuComponentButton faster;
    private final MenuComponentButton fastest;
    public MenuComponentSimSpeedController(double x, double y, double width, double height) {
        super(x, y, width, height);
        slowest = add(new MenuComponentButton(0, 0, height, height, "", true, true, "/textures/gui/slowest"));
        slower = add(new MenuComponentButton(height, 0, height, height, "", true, true, "/textures/gui/slower"));
        faster = add(new MenuComponentButton(width-height*2, 0, height, height, "", true, true, "/textures/gui/faster"));
        fastest = add(new MenuComponentButton(width-height, 0, height, height, "", true, true, "/textures/gui/fastest"));
        slowest.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                simSpeed = 0.1;
            }
        });
        slower.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                simSpeed/=10;
            }
        });
        faster.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                simSpeed*=10;
            }
        });
        fastest.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                simSpeed=100;
            }
        });
    }
    @Override
    public void render(){
        removeRenderBound();
        slowest.enabled = slower.enabled = simSpeed!=0.1;
        fastest.enabled = faster.enabled = simSpeed!=100;
        GL11.glColor4d(0.75, 0.75, 0.75, 1);
        drawRect(x, y, x+width, y+height, 0);
        GL11.glColor4d(0.5, 0.5, 0.5, 1);
        drawRect(x, y, x+2, y+height, 0);
        drawRect(x, y, x+width, y+2, 0);
        drawRect(x, y+height-2, x+width, y+height, 0);
        drawRect(x+width-2, y, x+width, y+height, 0);
        GL11.glColor4d(0, 0, 0, 1);
        drawCenteredText(x+2, y+2, x+width-2, y+height-2, (simSpeed==0.1?simSpeed:Math.round(simSpeed))+"");
        GL11.glColor4d(1, 1, 1, 1);
    }
}