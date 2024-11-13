package edu.upc.dsa.models;

public class StoreObject {

    public String name;
    public int price;

    public StoreObject(String Name, int Price){
        this.setName(Name);
        this.setPrice(Price);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString(){
        return ("Object [type: "+name+", price: "+price+"]");
    }

}
