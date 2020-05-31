import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentCredits extends MenuComponent{
    public static ArrayList<String> credits = new ArrayList<>();
    private ArrayList<String> pendingCredits;
    static{
        credits.add("Made By");
        credits.add("");
        credits.add("ThizThizzyDizzy");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("Topping Selection");
        credits.add("");
        credits.add("ThizThizzyDizzy");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("Music");
        credits.add("");
        credits.add("\"Bushwick Tarentella Loop\"");
        credits.add("");
        credits.add("Kevin MacLeod (incompetech.com)");
        credits.add("");
        credits.add("Licensed under Creative Commons: By Attribution 3.0 License");
        credits.add("");
        credits.add("http://creativecommons.org/licenses/by/3.0/");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("Libraries Used");
        credits.add("");
        credits.add("SimpleLibrary");
        credits.add("by computerneek");
        credits.add("");
        credits.add("LWJGL");
        credits.add("by the LWJGL Team");
        credits.add("");
        credits.add("JLayer");
        credits.add("by javazoom");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("The people and events in "+Main.applicationName+" are entirely fictional.");
        credits.add("");
        credits.add("Any similarity to actual people or events is unintentional.");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("");
    }
    private double rot = 0;
    private int timer = 0;
    private int time = 15;
    private ArrayList<Line> text = new ArrayList<>();
    private static final double textHeight = 20;
    public MenuComponentCredits(double x, double y, double width, double height){
        super(x-width/2+textHeight, y-height/2, width, height);
        pendingCredits = new ArrayList<>(credits);
    }
    @Override
    public void render(){
        x = Display.getWidth()-width/2+textHeight;
        y = Display.getHeight()/2-height/2;
        removeRenderBound();
        GL11.glPushMatrix();
        GL11.glTranslated(x+width/2, y+height/2, 0);
        GL11.glRotated(rot, 0, 0, 1);
        drawRect(-width/2, -height/2, width/2, height/2, ImageStash.instance.getTexture("/textures/crusts/"+PizzaCrust.WHOLE_WHEAT.texture+".png"));
        GL11.glPopMatrix();
        GL11.glColor4d(0, 0, 0, 1);
        for(Line line : text){
            GL11.glPushMatrix();
            GL11.glTranslated(x+width/2, y+height/2, 0);
            GL11.glRotated(line.angle, 0, 0, 1);
            drawCenteredText(-width/2, -textHeight/2, 0, textHeight/2, line.text);
            GL11.glPopMatrix();
            GL11.glColor4d(0, 0, 0, 1);
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void tick(){
        if(pendingCredits.isEmpty()){
            pendingCredits = new ArrayList<>(credits);
        }
        timer++;
        if(timer>=time){
            timer-=time;
            text.add(new Line(pendingCredits.remove(0)));
        }
        rot+=.5;
        for(Iterator<Line> it = text.iterator(); it.hasNext();){
            Line line = it.next();
            line.angle+=.5;
            if(line.angle>90){
                it.remove();
            }
        }
    }
    private static class Line{
        private final String text;
        private double angle = -90;
        public Line(String text){
            this.text = text;
        }
    }
}