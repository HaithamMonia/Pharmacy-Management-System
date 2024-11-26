import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a checkout system for an online pharmacy.
 * Handles cart management and checkout processes.
 */
public class Checkout {
    private String drugName;
    private double price;
    private int quantity;
    private double total = 0;
    private Scanner scan;
    private ArrayList<Drug> checkoutList;
    private ArrayList<String> allCartContent;

    private String cartFile;     // File path for the cart
    private String checkoutFile; // File path for the checkout records

    // Constructor to initialize the file paths
    public Checkout(String cartFile, String checkoutFile) {
        this.cartFile = cartFile;
        this.checkoutFile = checkoutFile;
    }
    // default constructor
    public Checkout(){
        this.cartFile = "cart.txt";
        this.checkoutFile = "checkout.txt";
    }

    /**
     * Displays the items in the user's cart.
     * @param username the username whose cart is to be viewed
     */
    public void viewCart(String username) {
        System.out.println("\nThe Cart");
        checkoutList = new ArrayList<>();
        try {
            scan = new Scanner(new File(cartFile)); // Open the cart file for reading
            if (!scan.hasNextLine()) {
                System.out.println("No items in the cart");
                return;
            }
            scan.nextLine(); // Skip the header line
            String[] drug;
            int lineNum = 1;

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                drug = line.split(";"); // Split line into an array of drug properties

                // Check if the username matches the one passed as argument
                if (drug[0].equalsIgnoreCase("Ali")) {
                    drugName = drug[1];
                    price = Double.parseDouble(drug[2]);
                    quantity = Integer.parseInt(drug[3]);
                    total += price * quantity; // Update total with the cost of this drug

                    // Add the drug to the checkout list
                    checkoutList.add(new Drug(drugName, price, quantity));
                    System.out.println(lineNum + "- " + drugName + ":");
                    System.out.println("Quantity: " + quantity);
                    System.out.println("Cost: " + (price + quantity));
                    lineNum++;
                }
            }
            System.out.println("Total: " + total);
            if (lineNum == 1) {
                System.out.println("No items in your cart"); // Display if the cart is empty
            }
            System.out.println();

            scan.close();

        } catch (Exception e) {
            System.out.println("ERROR: Opening the cart. Try again."); // Error handling
        }
    }

    /**
     * Processes the user's checkout and updates the checkout file.
     * @param username the username performing the checkout
     * @return true if checkout succeeds, false otherwise
     */
    public boolean checkout(String username) {
        try {
            FileWriter file = new FileWriter(checkoutFile, true);
            PrintWriter writeCheckout = new PrintWriter(file);
            System.out.println("C H E C K  O U T");
            viewCart(username); // Display items in cart
            System.out.println("Total Cost: " + total);

            // Update the cart file after checkout if updateCart is successful
            if (updateCart(username)) {
                for (Drug drug : checkoutList) {
                    writeCheckout.println(username + ";" + drug.getName() + ";" +
                            drug.getPrice() + ";" + drug.getQuantity());
                }
                writeCheckout.close();
                System.out.println("Checkout Succeeded");
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            System.out.println("ERROR: Checkout Failed. Try again."); // Error handling
            return false;
        }
    }

    /**
     * Updates the cart file to remove items after checkout.
     * @param username the username whose cart is to be updated
     * @return true if the cart is updated successfully, false otherwise
     */
    boolean updateCart(String username) {
        allCartContent = new ArrayList<>();
        try {
            scan = new Scanner(new File(cartFile));
            PrintWriter writeCart = new PrintWriter(cartFile);
            String[] drug;
            boolean toDelete = false;

            // Read all content from the cart file
            while (scan.hasNextLine()) {
                drug = scan.nextLine().split(";");
                allCartContent.add(drug[0] + ";" + drug[1] + ";" + drug[2] + ";" + drug[3]);
            }

            // Rewrite header in the cart file
            writeCart.println("Username;Drug;Price;Quantity");

            // Update cart content, excluding checked-out items
            for (String cartItem : allCartContent) {
                String[] cartFields = cartItem.split(";");
                toDelete = false;

                for (Drug d : checkoutList) {
                    // Check if current cart item belongs to the user being checked out
                    if (cartFields[0].equalsIgnoreCase(username) &&
                            cartFields[1].equalsIgnoreCase(d.getName())) {
                        toDelete = true;
                        break;
                    }
                }

                if (!toDelete) {
                    writeCart.println(cartItem); // Keep the item if not part of the checkout
                }
            }

            writeCart.close();

        } catch (Exception e) {
            System.out.println("ERROR: In the checkout process."); // Error handling
            return false;
        }
        return true;
    }
}
