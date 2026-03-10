package Comprog_MIMS_PRACACT;

import java.sql.Timestamp;

public abstract class MeatProduct {
    protected int productId;
    protected String meatName;
    protected double pricePerKg;
    protected double weightStock;
  
    private boolean showAsNew = true;
    private Timestamp addedAt;
    private Timestamp updatedAt;

    public MeatProduct(int productId, String meatName, double pricePerKg, double weightStock) {
        this.productId = productId;
        this.meatName = meatName;
        this.pricePerKg = pricePerKg;
        this.weightStock = weightStock;
    }

    public int getProductId() { return productId; }
    public String getMeatName() { return meatName; }
    public double getPricePerKg() { return pricePerKg; }
    public double getWeightStock() { return weightStock; }

    public void setPricePerKg(double pricePerKg) { this.pricePerKg = pricePerKg; }
    public void setWeightStock(double weightStock) { this.weightStock = weightStock; }

    public boolean isShowAsNew() { return showAsNew; }
    public void setShowAsNew(boolean showAsNew) { this.showAsNew = showAsNew; }

    public Timestamp getAddedAt() { return addedAt; }
    public void setAddedAt(Timestamp addedAt) { this.addedAt = addedAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public abstract String getMeatCategory();
}