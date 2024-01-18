package model;

// Déclaration de la classe ObjectTableValue.
public class ObjectTableValue {

    // Propriétés privées de la classe.
    private String name;       // Nom de l'objet.
    private Double price;      // Prix unitaire de l'objet.
    private Double ownedValue; // Valeur totale détenue de l'objet (prix x quantité).
    private Double quantity;   // Quantité de l'objet possédée.

    // Constructeur de la classe.
    public ObjectTableValue(String name, Double price, Double quantity) {
        this.name = name; // Initialisation du nom.
        this.price = price; // Initialisation du prix.
        this.quantity = quantity; // Initialisation de la quantité.
        // Calcul de la valeur totale détenue.
        this.ownedValue = this.price * this.quantity;
    }

    // Méthodes getters pour accéder aux propriétés privées.

    // Renvoie la valeur totale détenue.
    public Double getOwnedValue() {
        return ownedValue;
    }

    // Renvoie le prix unitaire.
    public Double getPrice() {
        return price;
    }

    // Renvoie le nom.
    public String getName() {
        return name;
    }

    // Renvoie la quantité.
    public Double getQuantity() {
        return quantity;
    }
}
