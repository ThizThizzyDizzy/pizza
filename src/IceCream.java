import java.util.ArrayList;
public enum IceCream{
    VANILLA  ("Vanilla",   "vanilla",   100, 100, 5   , false),// 5cm $05.00
    CHOCOLATE("Chocolate", "chocolate", 100, 100, 5   , false),// 5cm $05.00
    SPRINKLES("Sprinkles", "sprinkles", 100,  2,  .01,  true);// 1mm $00.10
    private final String name;
    public final String texture;
    public final int size;
    public final double height;
    public final double cost;
    public static final int sauces = 2;
    public final boolean topping;
    private IceCream(String name, String texture, int size, double height, double cost, boolean topping){
        this.name = name;
        this.texture = texture;
        this.size = size;
        this.height = height;
        this.cost = cost;
        this.topping = topping;
    }
    public static IceCream randomIceCream(){
        return values()[Core.rand.nextInt(values().length)];
    }
    public static IceCream randomIceCream(boolean topping){
        ArrayList<IceCream> creams = new ArrayList<>();
        ArrayList<IceCream> toppings = new ArrayList<>();
        for(IceCream c : values()){
            if(c.topping){
                toppings.add(c);
            }else{
                creams.add(c);
            }
        }
        if(topping){
            return toppings.get(Core.rand.nextInt(toppings.size()));
        }else{
            return creams.get(Core.rand.nextInt(creams.size()));
        }
    }
    @Override
    public String toString(){
        return name;
    }
}