public class PorkProduct extends MeatProduct {
    private String cutType;
    private boolean hasBone;

    public PorkProduct(int productId, String meatName, double pricePerKg, double weightStock, String cutType, boolean hasBone) {
        super(productId, meatName, pricePerKg, weightStock);
        this.cutType = cutType;
        this.hasBone = hasBone;
    }

    @Override
    public String getMeatCategory() { return "Pork"; }
    public String getCutType() { return cutType; }
    public boolean isHasBone() { return hasBone; }
}