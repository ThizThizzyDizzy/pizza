import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentOpenClosedSign extends MenuComponent{
    double progress = -10;
    double thickness = 10;
    public MenuComponentOpenClosedSign(double x, double y, double width, double height){
        super(x, y, width, height);
    }
    @Override
    public void mouseEvent(int button, boolean pressed, float x, float y, float xChange, float yChange, int wheelChange) {
        if(Mouse.isButtonDown(0)){
            progress += xChange/8;
            progress = Math.max(-10, Math.min(10, progress));
        }
        if(Mouse.isButtonDown(1)){
            progress=0;
        }
    }
    @Override
    public void render(){
        removeRenderBound();
        double front = Math.abs(width*Math.cos(Math.toRadians((progress+10)*9)));
        double side = Math.abs(thickness*Math.sin(Math.toRadians((progress+10)*9)));
        double total = front+side;
        if(progress<0){
            GL11.glColor4d(0, 127/255D, 14/255D, 1);
            drawRect(x+width/2-total/2, y, x+width/2-total/2+side/2, y+height, 0);
            GL11.glColor4d(127/255D, 0, 0, 1);
            drawRect(x+width/2-total/2+side/2, y, x+width/2-total/2+side, y+height, 0);
            GL11.glColor4d(1, 1, 1, 1);
            drawRect(x+width/2-total/2+side, y, x+width/2+total/2, y+height, ImageStash.instance.getTexture("/textures/sign/-10.png"));
        }else if(progress>0){
            GL11.glColor4d(0, 127/255D, 14/255D, 1);
            drawRect(x+width/2+total/2-side, y, x+width/2+total/2-side/2, y+height, 0);
            GL11.glColor4d(127/255D, 0, 0, 1);
            drawRect(x+width/2+total/2-side/2, y, x+width/2+total/2, y+height, 0);
            GL11.glColor4d(1, 1, 1, 1);
            drawRect(x+width/2-total/2, y, x+width/2+total/2-side, y+height, ImageStash.instance.getTexture("/textures/sign/10.png"));
        }else{
            GL11.glColor4d(0, 127/255D, 14/255D, 1);
            drawRect(x+width/2-total/2, y, x+width/2, y+height, 0);
            GL11.glColor4d(127/255D, 0, 0, 1);
            drawRect(x+width/2, y, x+width/2+total/2, y+height, 0);
            GL11.glColor4d(1, 1, 1, 1);
        }
    }
}