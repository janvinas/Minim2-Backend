package edu.upc.dsa.models;

public class Inventory {
    String userID;
    String objectID;
    int quantity;

    public Inventory(String userID, String objectID, int quantity) {
        this.userID = userID;
        this.objectID = objectID;
        this.quantity = quantity;
    }

    public Inventory() {}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
