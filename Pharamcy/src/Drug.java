public class Drug {
    // Instance variables to store the drug's name, price, and quantity
    private String name;
    private double price;
    private int quantity;

    // Constructor to initialize the drug's name, price, and quantity
    public Drug(String name, double price, int quantity){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Sets the name of the drug
    public void setName(String name) {
        this.name = name;
    }

    // Sets the price of the drug
    public void setPrice(double price) {
        this.price = price;
    }

    // Sets the quantity of the drug in the inventory
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Returns the name of the drug
    public String getName() {
        return name;
    }

    // Returns the price of the drug
    public double getPrice() {
        return price;
    }

    // Returns the quantity of the drug
    public int getQuantity() {
        return quantity;
    }
}
