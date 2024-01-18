package model;

// Déclaration de la classe Crypto.
public class Crypto {
    // Propriétés privées de la classe Crypto.
    private String name; // Le nom de la cryptomonnaie.
    private Double price; // Le prix de la cryptomonnaie.

    // Constructeur de la classe Crypto.
    public Crypto(String name, Double price) {
        this.name = name; // Initialisation du nom.
        this.price = price; // Initialisation du prix.
    }

    // Méthodes getters et setters pour les propriétés.

    // Getter pour le nom.
    public String getName() {
        return name;
    }

    // Setter pour le nom.
    public void setName(String name) {
        this.name = name;
    }

    // Getter pour le prix.
    public Double getPrice() {
        return price;
    }

    // Setter pour le prix.
    public void setPrice(Double price) {
        this.price = price;
    }
}
