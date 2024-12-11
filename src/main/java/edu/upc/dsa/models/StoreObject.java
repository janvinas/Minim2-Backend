package edu.upc.dsa.models;

import edu.upc.dsa.DB.SQLNotInsert;

public class StoreObject {

    @SQLNotInsert
    public String ID;
    public String name;
    public double price;
    public String URL;

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    public StoreObject() {

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public StoreObject(String Name, double Price, String URL){
        this.setName(Name);
        this.setPrice(Price);
        this.setURL(URL);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString(){
        return ("Object [type: "+name+", price: "+price+"]");
    }

}
