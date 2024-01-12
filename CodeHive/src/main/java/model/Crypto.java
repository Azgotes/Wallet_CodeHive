package model;

public class Crypto {
    private String name;
    private Double price;
    private Double marketCap;

    public Crypto(String name, Double price, Double marketCap) {
        this.name = name;
        this.price = price;
        this.marketCap = marketCap;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }
}
