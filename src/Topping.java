public enum Topping{
    PIZZA_SAUCE      ("Pizza Sauce",       "pizza sauce",       50,  1000, 0.004,  0.00314),//  500 2mm $01.57
    WHITE_PIZZA_SAUCE("White Pizza Sauce", "white pizza sauce", 50,  1000, 0.004,  0.007),  //  500 2mm $03.50
    PEPPERONI        ("Pepperoni",         "pepperoni",         50,  250,  0.01,   0.0069), //  200 2mm $01.38
    CANADIAN_BACON   ("Canadian Bacon",    "canadian bacon",    100, 50,   0.05,   0.3095), //  100 2mm $30.95
    MUSHROOMS        ("Mushrooms",         "mushrooms",         75,  24,   0.1,    0.0836), //   50 5mm $04.18
    PINEAPPLE        ("Pineapple Tidbits", "pineapple",         50,  200,  0.02,   0.03786),//  500 1cm $18.93
    OLIVES           ("Olives",            "olives",            30,  100,  0.01,   0.04338),//  500 5mm $21.69
    SAUSAGE          ("Sausage",           "sausage",           25,  200,  0.005,  0.00765),//1,000 5mm $07.65
    BACON            ("Bacon",             "bacon",             50,  100,  0.008,  0.01956),//  250 2mm $04.89
    SPINACH          ("Spinach",           "spinach",           25,  250,  0.003,  0.00678),//1,000 3mm $06.78
    CHEESE           ("Cheese",            "cheese",            50,  500,  0.0025, 0.00390),//1,000 2mm $03.90
    PARMEZAN         ("Parmezan",          "parmezan",          25,  5000, 0.00025,0.00130),//5,000 1mm $01.30
    JALEPENOS        ("Jalapeno Peppers",  "jalapeno peppers",  100, 20,   0.05,   0.0332), //  200 1cm $06.64
    BANANA_PEPPERS   ("Banana Peppers",    "banana peppers",    100, 16,   0.05,   0.0449), //  200 1cm $08.98
    GREEN_PEPPER     ("Green Pepper",      "green pepper",      150, 10,   0.1,    0.179);  //  100 1cm $17.90
    private final String name;
    public final String texture;
    public final int size;
    public final int regularAmount;
    public final double height;
    public final double cost;
    public static final int sauces = 2;
    private Topping(String name, String texture, int size, int regularAmount, double height, double cost){
        this.name = name;
        this.texture = texture;
        this.size = size;
        this.regularAmount = regularAmount;
        this.height = height;
        this.cost = cost;
    }
    public static Topping randomTopping(){
        return values()[Core.rand.nextInt(values().length)];
    }
    @Override
    public String toString(){
        return name;
    }
}