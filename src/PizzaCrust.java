public enum PizzaCrust{
    WHOLE_WHEAT("Whole Wheat Pizza Crust", "whole wheat", 5, 5);
    private final String name;
    final String texture;
    public final double height;
    public final double cost;
    private PizzaCrust(String name, String texture, double height, double cost){
        this.name = name;
        this.texture = texture;
        this.height = height;
        this.cost = cost;
    }
    public static PizzaCrust randomCrust(){
        return values()[Core.rand.nextInt(values().length)];
    }
    @Override
    public String toString(){
        return name;
    }
}