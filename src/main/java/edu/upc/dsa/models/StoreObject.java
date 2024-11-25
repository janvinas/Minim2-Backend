package edu.upc.dsa.models;

public class StoreObject {

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
