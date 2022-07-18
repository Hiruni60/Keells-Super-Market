package model;

public class Cart {

    private String proId;
    private String proName;
    private String proCategory;
    private Double unitPrice;
    private Integer sellQty;
    private Double total;

    @Override
    public String toString() {
        return "Order{" +

                ", proId='" + proId + '\'' +
                ", proName='" + proName + '\'' +
                ", proCategory='" + proCategory + '\'' +
                ", unitPrice=" + unitPrice +
                ", sellQty=" + sellQty +
                ", total=" + total +
                '}';
    }



    public Cart(String proId, String proName, String proCategory, Double unitPrice, Integer sellQty, Double total) {
        //this.setBillId(billId);
        this.setProId(proId);
        this.setProName(proName);
        this.setProCategory(proCategory);
        this.setUnitPrice(unitPrice);
        this.setSellQty(sellQty);
        this.setTotal(total);
    }



    /*public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }*/

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProCategory() {
        return proCategory;
    }

    public void setProCategory(String proCategory) {
        this.proCategory = proCategory;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getSellQty() {
        return sellQty;
    }

    public void setSellQty(Integer sellQty) {
        this.sellQty = sellQty;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
