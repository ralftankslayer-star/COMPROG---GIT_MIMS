public abstract class MeatProduct {
    protected int productId;
    protected String meatName;
    protected double pricePerKg;
    protected double weightStock;

    public MeatProduct(int productId, String meatName, double pricePerKg, double weightStock) {
        this.productId = productId;
        this.meatName = meatName;
        this.pricePerKg = pricePerKg;
        this.weightStock = weightStock;
    }

    public int getProductId() {
        return productId; 
    }
    public String getMeatName() {
        return meatName; 
    }
    public double getPricePerKg() {
        return pricePerKg; 
    }
    public double getWeightStock() {
        return weightStock;
     }

    public void setPricePerKg(double pricePerKg) {
        this.pricePerKg=pricePerKg; 

    }
    public void setWeightStock(double weightStock) {
        this.weightStock=weightStock; 
    }

    public abstract String getMeatCategory();
}


