package model;

// Déclaration de la classe Action.
public class Action {
    // Déclaration des propriétés de la classe Action.
    private String symbol; // Symbole de l'action, par exemple "AAPL" pour Apple.
    private Double price;  // Prix de l'action.

    // Constructeur de la classe Action.
    public Action(String symbol, Double price) {
        this.symbol = symbol; // Initialisation du symbole de l'action.
        this.price = price;   // Initialisation du prix de l'action.
    }

    // Méthodes getters et setters pour chaque propriété.

    // Getter pour le symbole de l'action.
    public String getSymbol() {
        return symbol;
    }

    // Setter pour le symbole de l'action.
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    // Getter pour le prix de l'action.
    public Double getPrice() {
        return price;
    }

    // Setter pour le prix de l'action.
    public void setPrice(Double price) {
        this.price = price;
    }
}