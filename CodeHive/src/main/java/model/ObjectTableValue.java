package model;

public class ObjectTableValue {

    private String name;

    private Double price;

    private Double ownedValue;

    private Double quantity;

    public ObjectTableValue(String name, Double price, Double quantity){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.ownedValue = this.price*this.quantity;
    }

    public Double getOwnedValue() {
        return ownedValue;
    }

    public Double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public Double getQuantity() {
        return quantity;
    }
}
