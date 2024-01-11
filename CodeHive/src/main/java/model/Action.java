package model;

public class Action {
    private String symbol;
    private Double price;
    private Double volume;

    public Action(String symbol, Double price, Double volume) {
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
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

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }
}