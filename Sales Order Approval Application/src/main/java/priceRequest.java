public class priceRequest {

    //class priceRequest {
    private double pricePerUnit;
    private int custID;
    private String custName;
    private int itemID;
    private String itemName;
    private int itemQ;
    private int salesRepId;
    private boolean approval;
    private String comment;

    void setPricePerUnit(double p) {
        this.pricePerUnit = p;
    }

    void setCustID(int id) {
        this.custID = id;
    }

    void setCustName(String name) {
        this.custName = name;
    }

    void setItemID(int id) {
        this.itemID = id;
    }

    void setItemName(String s) {
        this.itemName = s;
    }

    void setItemQ(int q) {
        this.itemQ = q;
    }

    void setSalesRepId(int id) {this.salesRepId = id;}

    void setComment(String s) {
        this.comment = s;
    }

    double getPricePerUnit() {
        return pricePerUnit;
    }

    int getCustID() {
        return custID;
    }

    String getCustName() {
        return custName;
    }

    int getItemID() {
        return itemID;
    }

    String getItemName() {
        return itemName;
    }

    int getItemQ() {
        return itemQ;
    }

    int getSalesRepId() {return salesRepId; }

    String getComment() {
        return comment;
    }

    void setApproval(boolean b) {
        this.approval = b;
    }

    boolean getApproval() {
        return approval;
    }
}
