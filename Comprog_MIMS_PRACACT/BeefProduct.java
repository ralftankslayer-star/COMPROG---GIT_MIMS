public class BeefProduct extends MeatProduct {
    private String beefGrade;
    private int marblingScore;

    public BeefProduct(int productId, String meatName, double pricePerKg, double weightStock, String beefGrade, int marblingScore) {
        super(productId, meatName, pricePerKg, weightStock);
        this.beefGrade = beefGrade;
        this.marblingScore = marblingScore;
    }

    @Override
    public String getMeatCategory() { return "Beef"; }
    public String getBeefGrade() { return beefGrade; }
    public int getMarblingScore() { return marblingScore; }
}
