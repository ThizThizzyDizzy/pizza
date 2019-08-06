import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentRollingButton extends MenuComponentButton{
    double rotation = 0;
    public double targetRotation = 0;
    private double rotationSpeed = 9;
    public MenuComponentRollingButton(double x, double y, double width, double height, String label, boolean enabled){
        super(x, y, width, height, label, enabled);
    }
    public MenuComponentRollingButton(double x, double y, double width, double height, String label, boolean enabled, boolean useMouseover){
        super(x, y, width, height, label, enabled, useMouseover);
    }
    public MenuComponentRollingButton(double x, double y, double width, double height, String label, boolean enabled, boolean useMouseover, String textureRoot){
        super(x, y, width, height, label, enabled, useMouseover, textureRoot);
    }
    @Override
    public void tick(){
        double oldRotation = rotation;
        if(rotation>targetRotation){
            rotation-=rotationSpeed;
            if(rotation<targetRotation){
                rotation=targetRotation;
            }
        }
        if(rotation<targetRotation){
            rotation+=rotationSpeed;
            if(rotation>targetRotation){
                rotation=targetRotation;
            }
        }
        double rotationChange = oldRotation-rotation;
        double circumference = Math.PI*width;
        double percent = rotationChange/360;
        double distance = circumference*percent;
        x+=distance;
        super.tick();
    }
    @Override
    public void render(){
        GL11.glPushMatrix();
        translate(x+width/2, y+height/2);
        GL11.glTranslated(x+width/2, y+height/2, 0);
        GL11.glRotated(rotation, 0, 0, -1);
        if(enabled){
            if(isPressed){
                drawRect(-width/2, -height/2, width/2, height/2, ImageStash.instance.getTexture(textureRoot+"Pressed.png"));
            }else{
                if(isMouseOver&&useMouseover){
                    drawRect(-width/2, -height/2, width/2, height/2, ImageStash.instance.getTexture(textureRoot+"Mouseover.png"));
                }else{
                    drawRect(-width/2, -height/2, width/2, height/2, ImageStash.instance.getTexture(textureRoot+".png"));
                }
            }
        }else{
            drawRect(-width/2, -height/2, width/2, height/2, ImageStash.instance.getTexture(textureRoot+"Disabled.png"));
        }
        GL11.glColor4d(0, 0, 0, 1);
        if(FontManager.getLengthForStringWithHeight(label, height/8)>width*.9){
            String str = label;
            int i = 0;
            while(str!=null&&!str.isEmpty()){
                str = drawCenteredTextWithWrap(-width/2-Display.getWidth()*2, -height/16, width/2-Display.getWidth()*2, height/16, str);
                i++;
            }
            str = label;
            int textHeight = (int) Math.round(height/8);
            int allTextHeight = textHeight*i;
            int yOffset = -allTextHeight/2;
            while(str!=null&&!str.isEmpty()){
                str = drawCenteredTextWithWrap(-width/2, -height/16+yOffset, width/2, height/16+yOffset, str);
                yOffset+=textHeight;
            }
        }else{
            drawCenteredText(-width/2, -height/16, width/2, height/16, label);
        }
        GL11.glColor4d(1, 1, 1, 1);
        untranslate();
        GL11.glPopMatrix();
    }
}