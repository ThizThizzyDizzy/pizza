import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.config2.Config;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentMulticolumnList;
import simplelibrary.opengl.gui.components.MenuComponentOptionButton;
public class MenuMakePizza extends Menu{
    public MenuComponentCrust pizza;
    public final MenuComponentMulticolumnList crusts;
    public final MenuComponentMulticolumnList toppings;
    public final MenuComponentMulticolumnList orders;
    public final MenuComponentMulticolumnList sides;
    public MenuComponentMulticolumnList selectedList;
    public final MenuComponentButton crustsButton;
    public final MenuComponentButton toppingsButton;
    public final MenuComponentButton ordersButton;
    public final MenuComponentButton sidesButton;
    public final MenuComponentButton pizzaButton;
    public final MenuComponentButton grabButton;
    public final MenuComponentOptionButton randomizeToppings;
    public final MenuComponentIceCreamDispenser iceCream;
    public Topping selectedTopping;
    public MenuComponentButton selectedMenu;
    public final MenuComponentOven oven;
    private final ArrayList<MenuComponentMulticolumnList> lists = new ArrayList<>();
    public final MenuComponentMulticolumnList storage;
    public boolean pizzaGrabbed;
    public boolean iceCreamGrabbed;
    public float pizzaVelocityX;
    public float pizzaVelocityY;
    public float iceCreamVelocity;
    private boolean menu = false;
    private final MenuComponentRollingButton back;
    private final MenuComponentRollingButton exit;
    private final ArrayList<MenuComponentFlame> flame = new ArrayList<>();
    private final ArrayList<MenuComponentSmoke> smoke = new ArrayList<>();
    public MenuComponentCrust pizzaFromStorage;
    private MenuComponentTutorial tuto;
    public boolean tutoFinished;
    public boolean ovenOverride = false;
    public boolean storageOverride = false;
    ArrayList<MenuComponentMoney> monies = new ArrayList<>();
    private final MenuComponentOpenClosedSign sign;
    public MenuComponentOrder selectedOrder;
    private final ArrayList<MenuComponentCrust> loadedPizzas = new ArrayList<>();
    public static double simSpeed = 1;
    private final MenuComponentSimSpeedController simSpeedController;
    public MenuMakePizza(GUI gui, Menu parent, boolean tutorial){
        super(gui, parent);
        crusts = add(new MenuComponentMulticolumnList(Display.getWidth(), 50, 220, Display.getHeight()-75, 200, 200, 20, true){
            @Override
            public void renderBackground(){
                super.renderBackground();
                GL11.glColor4d(0.75, 0.75, 0.75, 1);
                drawRect(x, y, x+width, y+height, 0);
                GL11.glColor4d(0.5, 0.5, 0.5, 1);
                drawRect(x, y, x+5, y+height, 0);
                drawRect(x, y, x+width, y+5, 0);
                drawRect(x, y+height-5, x+width, y+height, 0);
                drawRect(x+width-5, y, x+width, y+height, 0);
                GL11.glColor4d(1, 1, 1, 1);
            }
        });
        toppings = add(new MenuComponentMulticolumnList(Display.getWidth(), 50, 220, Display.getHeight()-75, 50, 50, 20, true){
            @Override
            public void renderBackground(){
                super.renderBackground();
                GL11.glColor4d(0.75, 0.75, 0.75, 1);
                drawRect(x, y, x+width, y+height, 0);
                GL11.glColor4d(0.5, 0.5, 0.5, 1);
                drawRect(x, y, x+5, y+height, 0);
                drawRect(x, y, x+width, y+5, 0);
                drawRect(x, y+height-5, x+width, y+height, 0);
                drawRect(x+width-5, y, x+width, y+height, 0);
                GL11.glColor4d(1, 1, 1, 1);
            }
        });
        orders = add(new MenuComponentMulticolumnList(Display.getWidth(), 150, 220, Display.getHeight()-175, 200, 1000, 20, true){
            @Override
            public void renderBackground(){
                super.renderBackground();
                GL11.glColor4d(0.75, 0.75, 0.75, 1);
                drawRect(x, y, x+width, y+height, 0);
                GL11.glColor4d(0.5, 0.5, 0.5, 1);
                drawRect(x, y, x+5, y+height, 0);
                drawRect(x, y, x+width, y+5, 0);
                drawRect(x, y+height-5, x+width, y+height, 0);
                drawRect(x+width-5, y, x+width, y+height, 0);
                GL11.glColor4d(1, 1, 1, 1);
            }
        });
        sides = add(new MenuComponentMulticolumnList(Display.getWidth(), 50, 220, Display.getHeight()-75, 100, 100, 20, true){
            @Override
            public void renderBackground(){
                super.renderBackground();
                GL11.glColor4d(0.75, 0.75, 0.75, 1);
                drawRect(x, y, x+width, y+height, 0);
                GL11.glColor4d(0.5, 0.5, 0.5, 1);
                drawRect(x, y, x+5, y+height, 0);
                drawRect(x, y, x+width, y+5, 0);
                drawRect(x, y+height-5, x+width, y+height, 0);
                drawRect(x+width-5, y, x+width, y+height, 0);
                GL11.glColor4d(1, 1, 1, 1);
            }
        });
        iceCream = add(new MenuComponentIceCreamDispenser(-200, 250, 200, 500));
        for(PizzaCrust crust : PizzaCrust.values()){
            MenuComponentCrustButton b = new MenuComponentCrustButton(0, 0, 200, 200, crust);
            b.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    if(pizza!=null){
                        if(pizza.toppings.isEmpty()&&pizza.cooked==0){
                            removePizza(0);
                        }else{
                            removePizza(-1);
                        }
                    }
                    pizza = add(new MenuComponentCrust(100,100,Display.getHeight()-200,Display.getHeight()-200, crust));
                    pizza.resetLocation();
                }
            });
            crusts.add(b);
        }
        for(Topping topping : Topping.values()){
            MenuComponentToppingButton b = new MenuComponentToppingButton(0, 0, 50, 50, topping);
            b.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    if(pizza!=null){
                        if(randomizeToppings.getIndex()==0){
                            int amount = 1;
                            amount *= Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)?10:1;
                            amount *= Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)?100:1;
                            int pizzaSize = (int) Math.round(pizza.width);
                            for(int i = 0; i<amount; i++){
                                while(true){
                                    int X = Core.rand.nextInt(pizzaSize);
                                    int Y = Core.rand.nextInt(pizzaSize);
                                    if(Core.distance(X, Y, pizzaSize/2, pizzaSize/2)<pizzaSize/2-topping.size/2-pizzaSize/10){
                                        MenuComponentTopping top = new MenuComponentTopping(X-topping.size/2, Y-topping.size/2, topping);
                                        pizza.addTopping(top);
                                        top.rotation = Core.rand.nextInt(360);
                                        break;
                                    }
                                }
                            }
                        }else{
                            selectedTopping = topping;
                        }
                    }
                }
            });
            toppings.add(b);
        }
        for(Side side : Side.values()){
            MenuComponentSideButton b = new MenuComponentSideButton(0, 0, 50, 50, side);
            b.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    if(selectedOrder!=null){
                        int index = 0;
                        for(int i = 0; i<Side.values().length; i++){
                            if(Side.values()[i]==side){
                                index = i;
                                break;
                            }
                        }
                        selectedOrder.sideAmounts[index]++;
                    }
                }
            });
            sides.add(b);
        }
        lists.add(crusts);
        lists.add(toppings);
        lists.add(orders);
        lists.add(sides);
        sign = add(new MenuComponentOpenClosedSign(Display.getWidth(), 50, 220, 100));
        crustsButton = add(new MenuComponentButton(Display.getWidth(), 0, 50, 50, "", true, true, "/textures/crusts/"+PizzaCrust.randomCrust().texture){
            @Override
            public void render(){
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/gui/crusts.png"));
                super.render();
            }
        });
        toppingsButton = add(new MenuComponentButton(Display.getWidth()+50, 0, 50, 50, "", true, true, "/textures/toppings/"+Topping.randomTopping().texture){
            @Override
            public void render(){
                if(enabled){
                    drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/gui/toppings.png"));
                    super.render();
                }else{
                    drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/gui/toppingsDisabled.png"));
                }
            }
        });
        MenuMakePizza menu = this;
        ordersButton = add(new MenuComponentButton(Display.getWidth()+100, 0, 50, 50, "", true, true, "/textures/gui/ordersButton"){
            MenuComponentOrder order;
            @Override
            public void render(){
                super.render();
                if(order==null){
                    int[] toppings = new int[Topping.values().length];
                    for(int i = 0; i<toppings.length; i++){
                        toppings[i] = 0;
                    }
                    toppings[0] = 1;
                    order = new MenuComponentOrder(menu, toppings);
                }
                order.x = x+width/2-width/4;
                order.y = y;
                order.width = width/2;
                order.height = height;
                order.render();
            }
            
        });
        sidesButton = add(new MenuComponentButton(Display.getWidth()+150, 0, 50, 50, "", true, true, "/textures/sides/"+Side.randomSide().texture){
            @Override
            public void render(){
                if(enabled){
                    drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/gui/sides.png"));
                    super.render();
                }else{
                    drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/gui/sidesDisabled.png"));
                }
            }
        });
        randomizeToppings = add(new MenuComponentOptionButton(Display.getWidth(), Display.getHeight()-25, 220, 25, "Topping Placement", true, true, "/textures/gui/button", 0, "Random", "Manual"));
        pizzaButton = add(new MenuComponentButton(Display.getWidth()-50, 0, 50, 50, "", true, true, "/textures/crusts/"+PizzaCrust.randomCrust().texture));
        grabButton = add(new MenuComponentButton(Display.getWidth()-50, 50, 50, 50, "", true, true, "/textures/gui/grabButton"));
        oven = add(new MenuComponentOven());
        double pizzaSize = Display.getHeight()*.5;
        back = add(new MenuComponentRollingButton(-pizzaSize, Display.getHeight()/2-pizzaSize/2, pizzaSize, pizzaSize, "Back", true, true, "/textures/crusts/"+PizzaCrust.WHOLE_WHEAT.texture));
        exit = add(new MenuComponentRollingButton(Display.getWidth(), Display.getHeight()/2-pizzaSize/2, pizzaSize, pizzaSize, "Exit", true, true, "/textures/crusts/"+PizzaCrust.WHOLE_WHEAT.texture));
        back.rotation = 180;
        exit.rotation = -180;
        storage = add(new MenuComponentMulticolumnList(0, Display.getHeight(), Display.getWidth()-orders.width-pizzaButton.width, 250, 250, 250, 20, false){
            @Override
            public void renderBackground(){
                super.renderBackground();
                GL11.glColor4d(0.75, 0.75, 0.75, 1);
                drawRect(x, y, x+width, y+height, 0);
                GL11.glColor4d(0.5, 0.5, 0.5, 1);
                drawRect(x, y, x+5, y+height, 0);
                drawRect(x, y, x+width, y+5, 0);
                drawRect(x, y+height-5, x+width, y+height, 0);
                drawRect(x+width-5, y, x+width, y+height, 0);
                GL11.glColor4d(1, 1, 1, 1);
            }
            @Override
            public void tick(){
                if(pizzaFromStorage!=null){
                    components.remove(pizzaFromStorage);
                    if(pizza!=null){
                        pizza.x = Math.max(250, pizza.x);
                    }
                    pizzaFromStorage = null;
                }
                super.tick();
            }
            
        });
        if(tutorial){
            tuto = add(new MenuComponentTutorial(this));
        }
        simSpeedController = add(new MenuComponentSimSpeedController(Display.getWidth()/2-250, -100, 500, 100));
        load();
    }
    @Override
    public void mouseEvent(int button, boolean pressed, float x, float y, float xChange, float yChange, int wheelChange){
        super.mouseEvent(button, pressed, x, y, xChange, yChange, wheelChange);
        if(menu){
            return;
        }
        if(button==0&&pressed&&selectedTopping!=null){
            int pizzaSize = (int) Math.round(pizza.width);
            if(Core.distance(x, y, pizzaSize/2+pizza.x, pizzaSize/2+pizza.y)<pizzaSize/2-selectedTopping.size){
                MenuComponentTopping top = new MenuComponentTopping(x-selectedTopping.size/2-pizza.x, y-selectedTopping.size/2-pizza.y, selectedTopping);
                pizza.addTopping(top);
                top.rotation = Core.rand.nextInt(360);
            }
        }else{
            if(button==0&&pressed&&Core.isMouseWithinComponent(iceCream.cone, iceCream)&&selectedMenu==grabButton){
                iceCreamGrabbed = true;
            }else if(button==0&&!pressed&&iceCreamGrabbed){
                iceCreamGrabbed = false;
            }else if(pizza!=null&&button==0&&pressed&&Core.distance(Mouse.getX(), Display.getHeight()-Mouse.getY(), pizza.x+pizza.width/2, pizza.y+pizza.height/2)<=pizza.width/2&&selectedMenu==grabButton){
                pizzaGrabbed = true;
            }else if(pizza!=null&&button==0&&!pressed&&pizzaGrabbed){
                pizzaGrabbed = false;
                if(isClickWithinBounds(x, y, storage.x, storage.y, storage.x+storage.width, storage.y+storage.height)){
                    storage.add(pizza);
                    removePizza(0);
                }
            }
        }
        if(pizzaGrabbed){
            pizzaVelocityX = xChange;
            pizzaVelocityY = yChange;
        }
        if(tuto!=null){
            if(tuto.getHighlight()==null){
                return;
            }else if(!isClickWithinBounds(x, y, tuto.highlight.x, tuto.highlight.y, tuto.highlight.x+tuto.highlight.width, tuto.highlight.y+tuto.highlight.height)){
                return;
            }
        }
        if(button==0&&pressed&&pizza==null&&oven.pizza!=null&&isClickWithinBounds(x, y, oven.x, oven.y, oven.x+oven.width, oven.y+oven.height)){
            oven.pizza.isCooked = true;
            pizza = oven.pizza;
            oven.removePizza = true;
            pizzaVelocityX = 0;
            pizzaVelocityY = 0;
            pizzaGrabbed = true;
        }
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat){
        if(key==Keyboard.KEY_ESCAPE&&pressed&&!repeat&&!oven.exploded){
            menu = !menu;
        }
        if(key==Keyboard.KEY_M&&pressed&&!repeat){
            Sounds.toggleMusic();
        }
    }
    @Override
    public void tick(){
        if(tutoFinished){
            components.remove(tuto);
            tuto = null;
            tutoFinished = false;
        }
        if(pizza!=null&&!components.contains(pizza)){
            add(pizza);
        }
        if(oven.exploded){
            selectedMenu = null;
        }
        back.tick();
        exit.tick();
        if(menu){
            back.targetRotation = 0;
            exit.targetRotation = 0;
            return;
        }
        if(isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), Display.getWidth()/2-250, 0, Display.getWidth()/2+250, 100)){
            if(simSpeedController.y<0){
                simSpeedController.y+=20;
            }
        }else{
            if(simSpeedController.y>-100){
                simSpeedController.y-=20;
            }
        }
        simSpeed = simSpeedController.simSpeed;
        if((Core.isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), 0, 250, 250, 750)||iceCream.cone.x!=50)&&storage.y==Display.getHeight()){
            if(iceCream.x<0){
                iceCream.x+=20;
            }
        }else if(iceCream.x>-200){
            iceCream.x-=20;
        }
        if(sign.progress==10&&Core.rand.nextDouble()<0.000125*simSpeed){
            orders.add(new MenuComponentOrder(this));
        }
        for(Iterator<MenuComponentMoney> it = monies.iterator(); it.hasNext();){
            MenuComponentMoney money = it.next();
            money.tick();
            if(money.opacity<=0){
                components.remove(money);
                it.remove();
            }
        }
        iceCream.tick();
        storage.tick();
        if(randomizeToppings.getIndex()==randomizeToppings.startingIndex){
            selectedTopping = null;
        }
        if(((Mouse.getY()<300||(pizzaVelocityY>0&&!pizzaGrabbed))&&iceCream.x==-200)||storageOverride){
            if(storage.y>Display.getHeight()-storage.height){
                storage.y-=25;
            }
        }else{
            if(storage.y<Display.getHeight()){
                storage.y+=25;
            }
        }
        if(pizza!=null){
            pizza.tick();
            if(pizza.smoke>0){
                for(int i = 0; i<pizza.smoke*10; i++){
                    smoke.add(add(new MenuComponentSmoke(Core.rand.nextInt((int)pizza.width)+pizza.x, Core.rand.nextInt((int)pizza.height)+pizza.y, pizza.smoke)));
                }
            }
            if(pizza.flame>0){
                for(int i = 0; i<pizza.flame*5; i++){
                    flame.add(add(new MenuComponentFlame(Core.rand.nextInt((int)pizza.width)+pizza.x, Core.rand.nextInt((int)pizza.height)+pizza.y, pizza.flame)));
                }
            }
            if(pizza.flame>0){
                pizza.cooked+=0.001;
                if(pizza.explosion){
                    removePizza(-1);
                }
            }else if(pizza.smoke>0){
                pizza.cooked-=0.0005;
            }
        }
        for(Iterator<MenuComponent> it = storage.components.iterator(); it.hasNext();){
            MenuComponent component = it.next();
            if(component instanceof MenuComponentCrust){
                MenuComponentCrust crust = (MenuComponentCrust) component;
                crust.tick();
                for(int i = 0; i<crust.smoke*10; i++){
                    smoke.add(add(new MenuComponentSmoke(Core.rand.nextInt((int)crust.width)+crust.x+storage.x, Core.rand.nextInt((int)crust.height)+crust.y+storage.y, crust.smoke)));
                }
                for(int i = 0; i<crust.flame*5; i++){
                    flame.add(add(new MenuComponentFlame(Core.rand.nextInt((int)crust.width)+crust.x+storage.x, Core.rand.nextInt((int)crust.height)+crust.y+storage.y, crust.flame)));
                }
                if(crust.flame>0){
                    crust.cooked+=0.001;
                    if(crust.explosion){
                        it.remove();
                    }
                }else if(crust.smoke>0){
                    crust.cooked-=0.0005;
                }
            }
        }
        back.targetRotation = 180;
        exit.targetRotation = -180;
        if(grabButton!=selectedMenu){
            pizzaGrabbed = false;
            iceCreamGrabbed = false;
        }
        if(ovenOverride||(oven.pizza!=null&&oven.pizza.explosion)||oven.exploded||(Core.distance(Mouse.getX(), Display.getHeight()-Mouse.getY(), 0, 0)<=375||(!pizzaGrabbed&&pizzaVelocityX<0&&pizzaVelocityY<0&&oven.canHoldPizza(pizza))||oven.tick>0||oven.open>0)){
            if(oven.x<0){
                oven.x+=25;
            }
        }else{
            if(oven.x>-250){
                oven.x-=25;
            }
        }
        if(selectedOrder!=null&&Mouse.isButtonDown(1)&&selectedOrder.finished){
            monies.add(add(new MenuComponentMoney(selectedOrder, selectedOrder.pizza, 2)));
            orders.components.remove(selectedOrder);
            selectedOrder = null;
        }
        if(iceCreamGrabbed){
            iceCream.cone.x = Mouse.getX()-iceCream.x-iceCream.cone.width/2;
            iceCream.cone.y = Display.getHeight()-Mouse.getY()-iceCream.y-iceCream.cone.height/2;
        }else{
            if(selectedOrder!=null&&isClickWithinBounds(iceCream.x+iceCream.cone.x+iceCream.cone.width/2, iceCream.y+iceCream.cone.y+iceCream.cone.height/2, pizzaButton.x-200, 0, pizzaButton.x, Display.getHeight())){
                selectedOrder.cone = iceCream.cone;
                iceCream.resetCone();
                iceCreamVelocity = 0;
                iceCreamGrabbed = false;
            }else if(Core.isPointWithinComponent(iceCream.x+iceCream.cone.x-iceCream.cone.width/2, iceCream.y+iceCream.cone.y-iceCream.cone.height/2, iceCream)){
                iceCream.cone.x = 50;
                iceCream.cone.y = 300;
                iceCreamVelocity = 0;
                iceCreamGrabbed = false;
            }else if(iceCream.cone.y+iceCream.cone.y>Display.getHeight()){
                iceCream.resetCone();
                iceCreamVelocity = 0;
                iceCreamGrabbed = false;
            }else{
                iceCream.cone.y+=iceCreamVelocity;
                iceCreamVelocity+=5;
            }
        }
        if(selectedOrder!=null){
            selectedOrder.tick();
        }
        if(pizza!=null){
            if(pizzaGrabbed){
                pizza.width = pizza.height = 250;
                pizza.x = Mouse.getX()-125;
                pizza.y = Display.getHeight()-Mouse.getY()-125;
            }else{
                if(Core.isPointWithinComponent(pizza.x+pizza.width/2, pizza.y+pizza.width/2, oven)&&oven.canHoldPizza(pizza)){
                    oven.addPizza(pizza);
                    removePizza(0);
                }else if(Core.isPointWithinComponent(pizza.x+pizza.width/2, pizza.y+pizza.width/2, storage)){
                    storage.add(pizza);
                    removePizza(0);
                }else if(selectedOrder!=null&&isClickWithinBounds(pizza.x+pizza.width/2, pizza.y+pizza.height/2, pizzaButton.x-200, 0, pizzaButton.x, Display.getHeight())){
                    selectedOrder.pizza = pizza;
                    removePizza(0);
                }else{
                    if(pizzaVelocityX!=0||pizzaVelocityY!=0){
                        pizza.x+=pizzaVelocityX;
                        pizza.y+=pizzaVelocityY;
                        pizza.rotation+=10;
                        if(pizza.x>Display.getWidth()||pizza.y>Display.getHeight()||pizza.x<-pizza.width||pizza.y<-pizza.height){
                            removePizza(1);
                        }
                    }else{
                        pizza.resetLocation();
                    }
                }
            }
        }
        for(MenuComponentMulticolumnList list : lists){
            if(list==selectedList&&selectedMenu==pizzaButton){
                if(list.x>Display.getWidth()-220){
                    list.x-=20;
                }
            }else{
                if(list.x<Display.getWidth()){
                    list.x+=20;
                }
            }
        }
        if(selectedMenu==pizzaButton){
            if(pizzaButton.x>Display.getWidth()-270){
                pizzaButton.x-=20;
            }
        }else{
            selectedTopping = null;
            selectedList = null;
            if(pizzaButton.x<Display.getWidth()-50){
                pizzaButton.x+=20;
            }
        }
        grabButton.x = pizzaButton.x;
        crustsButton.x = pizzaButton.x+50;
        toppingsButton.x = crustsButton.x+50;
        ordersButton.x = toppingsButton.x+50;
        sidesButton.x = ordersButton.x+50;
        randomizeToppings.x = crustsButton.x;
        sign.x = orders.x;
        oven.tick();
        for(Iterator<MenuComponentSmoke> it = smoke.iterator(); it.hasNext();){
            MenuComponentSmoke s = it.next();
            s.tick();
            if(s.opacity<=0){
                components.remove(s);
                it.remove();
            }
        }
        for(Iterator<MenuComponentFlame> it = flame.iterator(); it.hasNext();){
            MenuComponentFlame f = it.next();
            f.tick();
            if(f.opacity<=0){
                components.remove(f);
                it.remove();
            }
        }
        if(tuto!=null){
            tuto.tick();
        }
    }
    @Override
    public void renderBackground(){
        if((pizza!=null&&pizza.isCooked)||pizza==null){
            toppingsButton.enabled = false;
            selectedTopping = null;
        }else{
            toppingsButton.enabled = true;
        }
        sidesButton.enabled = selectedOrder!=null;
    }
    @Override
    public void render(int millisSinceLastTick){
        super.render(millisSinceLastTick);
        for (Iterator<MenuComponentCrust> it = loadedPizzas.iterator(); it.hasNext();) {
            MenuComponentCrust crust = it.next();
            crust.render();
            storage.add(crust);
            it.remove();
        }
        for(MenuComponent component : toppings.components){
            if(component instanceof MenuComponentToppingButton){
                MenuComponentToppingButton button = (MenuComponentToppingButton) component;
                if(button.isMouseOver){
                    double strLength = FontManager.getLengthForStringWithHeight(button.topping.toString(), 25);
                    GL11.glColor4d(0, 0, 0, 1);
                    drawText(Mouse.getX()-strLength, Display.getHeight()-Mouse.getY(), Mouse.getX(), Display.getHeight()-Mouse.getY()+25, button.topping.toString());
                    GL11.glColor4d(1, 1, 1, 1);
                }
            }
        }
        for(MenuComponent component : sides.components){
            if(component instanceof MenuComponentSideButton){
                MenuComponentSideButton button = (MenuComponentSideButton) component;
                if(button.isMouseOver){
                    double strLength = FontManager.getLengthForStringWithHeight(button.side.toString(), 25);
                    GL11.glColor4d(0, 0, 0, 1);
                    drawText(Mouse.getX()-strLength, Display.getHeight()-Mouse.getY(), Mouse.getX(), Display.getHeight()-Mouse.getY()+25, button.side.toString());
                    GL11.glColor4d(1, 1, 1, 1);
                }
            }
        }
        for(MenuComponent component : iceCream.components){
            if(component instanceof MenuComponentIceCreamDispenser.MenuComponentIceCreamButton){
                MenuComponentIceCreamDispenser.MenuComponentIceCreamButton button = (MenuComponentIceCreamDispenser.MenuComponentIceCreamButton) component;
                if(button.isMouseOver){
                    double strLength = FontManager.getLengthForStringWithHeight(button.iceCream.toString(), 25);
                    GL11.glColor4d(0, 0, 0, 1);
                    drawText(Math.max(0,Mouse.getX()-strLength), Display.getHeight()-Mouse.getY(), 32453, Display.getHeight()-Mouse.getY()+25, button.iceCream.toString());
                    GL11.glColor4d(1, 1, 1, 1);
                }
            }
        }
        for(MenuComponent component : crusts.components){
            if(component instanceof MenuComponentCrustButton){
                MenuComponentCrustButton button = (MenuComponentCrustButton) component;
                if(button.isMouseOver){
                    double strLength = FontManager.getLengthForStringWithHeight(button.crust.toString(), 25);
                    GL11.glColor4d(0, 0, 0, 1);
                    drawText(Mouse.getX()-strLength, Display.getHeight()-Mouse.getY(), Mouse.getX(), Display.getHeight()-Mouse.getY()+25, button.crust.toString());
                    GL11.glColor4d(1, 1, 1, 1);
                }
            }
        }
        if(oven.pizza!=null&&Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            GL11.glColor4d(0, 0, 0, 1);
            drawText(oven.x, 0, Display.getWidth(), 25, "Thickness: "+size(oven.pizza.pizzaHeight));
            drawText(oven.x, 25, Display.getWidth(), 50, "Cost: $"+Math.round(oven.pizza.pizzaCost*100)/100D);
            drawText(oven.x, 50, Display.getWidth(), 75, "Worth: $"+Math.round(oven.pizza.getCost(1)*100)/100D);
            GL11.glColor4d(1, 1, 1, 1);
        }
        if(pizza!=null&&Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            GL11.glColor4d(0, 0, 0, 1);
            drawText(oven.x+oven.width, 0, Display.getWidth(), 25, "Thickness: "+size(pizza.pizzaHeight));
            drawText(oven.x+oven.width, 25, Display.getWidth(), 50, "Cost: $"+Math.round(pizza.pizzaCost*100)/100D);
            drawText(oven.x+oven.width, 50, Display.getWidth(), 75, "Worth: $"+Math.round(pizza.getCost(1)*100)/100D);
            GL11.glColor4d(1, 1, 1, 1);
        }
        if(crustsButton.isMouseOver){
            double strLength = FontManager.getLengthForStringWithHeight("Pizza Crusts", 25);
            GL11.glColor4d(0, 0, 0, 1);
            drawText(Mouse.getX()-strLength, Display.getHeight()-Mouse.getY(), Mouse.getX(), Display.getHeight()-Mouse.getY()+25, "Pizza Crusts");
            GL11.glColor4d(1, 1, 1, 1);
        }
        if(toppingsButton.isMouseOver&&toppingsButton.enabled){
            double strLength = FontManager.getLengthForStringWithHeight("Toppings", 25);
            GL11.glColor4d(0, 0, 0, 1);
            drawText(Mouse.getX()-strLength, Display.getHeight()-Mouse.getY(), Mouse.getX(), Display.getHeight()-Mouse.getY()+25, "Toppings");
            GL11.glColor4d(1, 1, 1, 1);
        }
        if(sidesButton.isMouseOver&&sidesButton.enabled){
            double strLength = FontManager.getLengthForStringWithHeight("Sides", 25);
            GL11.glColor4d(0, 0, 0, 1);
            drawText(Mouse.getX()-strLength, Display.getHeight()-Mouse.getY(), Mouse.getX(), Display.getHeight()-Mouse.getY()+25, "Sides");
            GL11.glColor4d(1, 1, 1, 1);
        }
        if(ordersButton.isMouseOver){
            double strLength = FontManager.getLengthForStringWithHeight("Orders", 25);
            GL11.glColor4d(0, 0, 0, 1);
            drawText(Mouse.getX()-strLength, Display.getHeight()-Mouse.getY(), Mouse.getX(), Display.getHeight()-Mouse.getY()+25, "Orders");
            GL11.glColor4d(1, 1, 1, 1);
        }
        if(pizzaButton.isMouseOver){
            double strLength = FontManager.getLengthForStringWithHeight("Build Pizza", 25);
            GL11.glColor4d(0, 0, 0, 1);
            drawText(Mouse.getX()-strLength, Display.getHeight()-Mouse.getY(), Mouse.getX(), Display.getHeight()-Mouse.getY()+25, "Build Pizza");
            GL11.glColor4d(1, 1, 1, 1);
        }
        if(grabButton.isMouseOver){
            double strLength = FontManager.getLengthForStringWithHeight("Grab Tool", 25);
            GL11.glColor4d(0, 0, 0, 1);
            drawText(Mouse.getX()-strLength, Display.getHeight()-Mouse.getY(), Mouse.getX(), Display.getHeight()-Mouse.getY()+25, "Grab Tool");
            GL11.glColor4d(1, 1, 1, 1);
        }
        for(MenuComponent component : storage.components){
            if(component instanceof MenuComponentCrust){
                MenuComponentCrust crust = (MenuComponentCrust) component;
                if(crust.isMouseOver){
                    GL11.glColor4d(0, 0, 0, 1);
                    String str = "Thickness: "+size(crust.pizzaHeight);
                    double strLength = FontManager.getLengthForStringWithHeight(str, 25);
                    drawText(Math.max(0, Mouse.getX()-strLength), Display.getHeight()-Mouse.getY(), Display.getWidth(), Display.getHeight()-Mouse.getY()+25, str);
                    str = "Cost: $"+Math.round(crust.pizzaCost*100)/100D;
                    strLength = FontManager.getLengthForStringWithHeight(str, 25);
                    drawText(Math.max(0, Mouse.getX()-strLength), Display.getHeight()-Mouse.getY()+25, Display.getWidth(), Display.getHeight()-Mouse.getY()+50, str);
                    str = "Worth: $"+Math.round(crust.getCost(1)*100)/100D;
                    strLength = FontManager.getLengthForStringWithHeight(str, 25);
                    drawText(Math.max(0, Mouse.getX()-strLength), Display.getHeight()-Mouse.getY()+50, Display.getWidth(), Display.getHeight()-Mouse.getY()+75, str);
                    GL11.glColor4d(1, 1, 1, 1);
                }
            }
        }
        if(selectedOrder!=null){
            selectedOrder.x = pizzaButton.x-200;
            selectedOrder.y = 0;
            selectedOrder.width = 180;
            selectedOrder.height = Display.getHeight();
            selectedOrder.render();
        }
        if(tuto!=null){
            tuto.render();
        }
        exit.render();
        back.render();
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(menu){
            if(button==back){
                menu = false;
            }
            if(button==exit){
                save();
                gui.open(new MenuTransition(gui, this, parent));
            }
            return;
        }
        if(tuto!=null){
            if(tuto.getHighlight()==null){
                if(button==tuto){
                    tuto.clicked();
                }
                return;
            }else if(!isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), tuto.highlight.x, tuto.highlight.y, tuto.highlight.x+tuto.highlight.width, tuto.highlight.y+tuto.highlight.height)){
                if(button==tuto){
                    tuto.clicked();
                }
                return;
            }
        }
        if(button==crustsButton){
            selectedList = crusts;
        }
        if(button==toppingsButton){
            selectedList = toppings;
        }
        if(button==ordersButton){
            selectedList = orders;
        }
        if(button==sidesButton){
            selectedList = sides;
        }
        if(button==pizzaButton){
            selectedMenu = pizzaButton;
        }
        if(button==grabButton){
            selectedMenu = grabButton;
        }
    }
    private String size(double height){
        String[] metric = new String[]{"millimeter","centimeter", null, "meter", null, null, "kilometer", null, null, "megameter", null, null, "gigameter", null, null, "terameter", null, null, "petameter", null, null, "exameter", null, null, "zettameter", null, null, "yottameter"};
        int id = 0;
        while(height>=10){
            if(metric[id+1]==null){
                break;
            }
            height/=10;
            id++;
        }
        while(height>=100){
            if(metric[id+2]==null){
                break;
            }
            height/=100;
            id+=2;
        }
        while(height>=100){
            if(metric[id+3]==null){
                break;
            }
            height/=100;
            id+=3;
        }
        height = Math.round(height*1000D)/1000D;
        return height+" "+metric[id]+"s";
    }
    public void removePizza(int earned){
        if(pizza!=null){
            if(earned==1){
                int[] toppingAmounts = new int[Topping.values().length];
                for(int i = 0; i<toppingAmounts.length; i++){
                    if(pizza.getToppingAmount(Topping.values()[i])>0){
                        toppingAmounts[i] = Topping.values()[i].regularAmount;
                    }else{
                        toppingAmounts[i] = 0;
                    }
                }
                MenuComponentOrder order = new MenuComponentOrder(this, toppingAmounts);
                removePizza(order);
                return;
            }
            components.remove(pizza);
            if(earned!=0){
                if(earned==2){
                    monies.add(add(new MenuComponentMoney(selectedOrder, pizza, earned)));
                }else{
                    monies.add(add(new MenuComponentMoney(pizza, earned)));
                }
            }
            pizza = null;
        }
        pizzaVelocityX = pizzaVelocityY = 0;
    }
    private void removePizza(MenuComponentOrder order){
        if(pizza!=null){
            components.remove(pizza);
            monies.add(add(new MenuComponentMoney(order, pizza, 2)));
            pizza = null;
        }
        pizzaVelocityX = pizzaVelocityY = 0;
    }
    private void save(){
        File file = new File(Main.getAppdataRoot()+"\\save.dat");
        Config config = Config.newConfig(file);
        config.set("pizzas", storage.components.size());
        for(int i = 0; i<storage.components.size(); i++){
            MenuComponentCrust pizza = (MenuComponentCrust) storage.components.get(i);
            config.set("pizza "+i, pizza.save());
        }
        config.save();
    }
    private void load(){
        File file = new File(Main.getAppdataRoot()+"\\save.dat");
        if(!file.exists()){
            return;
        }
        Config config = Config.newConfig();
        config.load(file);
        for(int i = 0; i<config.get("pizzas", 0); i++){
            loadedPizzas.add(MenuComponentCrust.load(config.get("pizza "+i)));
        }
    }
    public static class MenuComponentMoney extends MenuComponent{
        double opacity = 1;
        private MenuComponentOrder order;
        private final MenuComponentCrust pizza;
        private final int earned;
        public MenuComponentMoney(MenuComponentCrust pizza, int earned){
            this(null, pizza, earned);
        }
        public MenuComponentMoney(MenuComponentOrder order, MenuComponentCrust pizza, int earned){
            super(500, 300, Display.getWidth(), 25);
            this.order = order;
            this.pizza = pizza;
            this.earned = earned;
            Sounds.playSound("SFX", "Sell");
        }
        @Override
        public void tick(){
            y--;
            opacity -= 0.01;
        }
        @Override
        public void render(){
            if(opacity<=0){
                return;
            }
            double money = Math.round(pizza.getCost(earned)*100)/100D;
            if(earned==2){
                money = Math.round((order.getCost(pizza)+order.getConeCost())*100)/100D;
            }
            if(money<0){
                GL11.glColor4d(1, 0, 0, opacity);
            }else{
                GL11.glColor4d(0, 1, 0, opacity);
            }
            drawText(x, y, Display.getWidth(), y+height,(money<1?"-":"+")+"$"+Math.abs(money));
            GL11.glColor4d(1, 1, 1, 1);
        }
    }
    public class MenuComponentCrustButton extends MenuComponentButton{
        private final PizzaCrust crust;
        public MenuComponentCrustButton(double x, double y, double width, double height, PizzaCrust crust){
            super(x, y, width, height, "", true, true, "/textures/crusts/"+crust.texture);
            this.crust = crust;
        }
    }
    public class MenuComponentToppingButton extends MenuComponentButton{
        private final Topping topping;
        public MenuComponentToppingButton(double x, double y, double width, double height, Topping topping){
            super(x, y, width, height, "", true, true, "/textures/toppings/"+topping.texture);
            this.topping = topping;
        }
    }
    public class MenuComponentSideButton extends MenuComponentButton{
        private final Side side;
        public MenuComponentSideButton(double x, double y, double width, double height, Side side){
            super(x, y, width, height, "", true, true, "/textures/sides/"+side.texture);
            this.side = side;
        }
    }
}