public class PoultryProduct extends MeatProduct {
    private String partName;
    private boolean isOrganic;

    public PoultryProduct(int productId, String meatName, double pricePerKg, double weightStock, String partName, boolean isOrganic) {
        super(productId, meatName, pricePerKg, weightStock);
        this.partName = partName;
        this.isOrganic = isOrganic;
    }

    @Override
    public String getMeatCategory() {
        return "Poultry"; 
    }
    public String getPartName() {
        return partName; 
    }
    public boolean isOrganic() {
        return isOrganic; 
    }
}