public enum Side{
    CHOCOLATE_CHIP_COOKIE("Chocolate Chip Cookie", "chocolate chip cookie", 4, 2);
    private final String name;
    public final String texture;
    public final int regularAmount;
    public final double cost;
    private Side(String name, String texture, int regularAmount, double cost){
        this.name = name;
        this.texture = texture;
        this.regularAmount = regularAmount;
        this.cost = cost;
    }
    public static Side randomSide(){
        return values()[Core.rand.nextInt(values().length)];
    }
    @Override
    public String toString(){
        return name;
    }
}