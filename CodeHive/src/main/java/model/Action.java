package model;

public class Action {
    private String symbol;
    private Double price;

    public Action(String symbol, Double price) {
        this.symbol = symbol;
        this.price = price;
    }

    // Getters and setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}