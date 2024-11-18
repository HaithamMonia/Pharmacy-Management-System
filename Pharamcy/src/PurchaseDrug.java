import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class PurchaseDrug {
    private String drugName;
    private double price;
    private int availableStuck;
    public Scanner scan = new Scanner(System.in);
    public ArrayList<Drug> drugsAvailableList;

    // This method displays available drugs in the pharmacy by reading from "drugs.txt"
    public void viewDrugs() {
        drugsAvailableList = new ArrayList<>();
        try {
            scan = new Scanner(new File("drugs.txt"));
            // Check if file is empty
            if (!scan.hasNextLine()) {
                System.out.println("No Drugs Found");
                return;
            }
            scan.nextLine();  // Skip header line
            String[] drug;
            int lineNum = 1;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                drug = line.split(";");
                System.out.println(lineNum + "- " + line);

                // Create a new Drug object from line data and add it to the list
                Drug d = new Drug(drug[0], Double.parseDouble(drug[1]), Integer.parseInt(drug[2]));
                drugsAvailableList.add(d);
                lineNum++;
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("ERROR: Viewing the Drugs");
            return;
        }
    }

    // This method places an order for a drug and updates "drugs.txt"
    public boolean placeOrder(String username) {
        int option;
        Scanner scanKb = new Scanner(System.in);
        String DrugName;
        double price;
        int qty;
        int numElementToBuy;
        viewDrugs();  // Display available drugs
        System.out.print("\nEnter the number of the drug you want to purchase: ");
        option = scanKb.nextInt();

        try {
            // Loop until a valid option or cancellation is selected
            do {
                if (option != 0) {
                    // Retrieve the drug's details based on the selected option
                    DrugName = drugsAvailableList.get(option - 1).getName();
                    price = drugsAvailableList.get(option - 1).getPrice();
                    qty = drugsAvailableList.get(option - 1).getQuantity();

                    System.out.print("How many items you want to purchase: ");
                    numElementToBuy = scanKb.nextInt();

                    if (numElementToBuy <= 0) {  // Check if quantity is valid
                        System.out.println("Please Enter a valid number!");
                        return false;
                    } else if (qty == 0) {  // Check if drug is in stock
                        System.out.println("Sorry, " + DrugName + " is out of stock.");
                        return false;
                    } else if (numElementToBuy > qty) {  // Check if enough stock is available
                        System.out.println("The quantity requested is not available. Available stock of " +
                                DrugName + " is " + qty + "\nTry again..");
                        return false;
                    } else {
                        // Add drug to the cart if available
                        if (addToCart(username, DrugName, price, numElementToBuy)) {
                            // Update the available stock for the selected drug
                            drugsAvailableList.set(option - 1, new Drug(DrugName, price, qty - numElementToBuy));
                            // Update the inventory in "drugs.txt"
                            if (updateInventory(drugsAvailableList)) {
                                System.out.println(DrugName + " has been added successfully to your cart");
                                return true;
                            } else {
                                return false;  // Failed to update inventory
                            }
                        } else {
                            System.out.println("Failed to add the item. Try again.");
                            return false;
                        }
                    }
                }
            } while (option != 0);
            return true;

        } catch (Exception e) {
            System.out.println("ERROR: Failed to place the order!" + e.getMessage());
            return false;
        }
    }

    // Adds the selected drug to the cart file "carts.txt"
    public boolean addToCart(String username, String name, double price, int qty) {
        try {
            FileWriter file = new FileWriter("carts.txt", true);
            PrintWriter write = new PrintWriter(file);
            // Save the username, drug name, price, and quantity to the cart
            write.println(username + ";" + name + ";" + price + ";" + qty);
            write.close();
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: Adding the item to the cart. Please try again.");
            return false;
        }
    }

    // Updates the drug inventory by writing updated quantities to "drugs.txt"
    public boolean updateInventory(ArrayList<Drug> drugs) {
        try {
            PrintWriter write = new PrintWriter("drugs.txt");
            write.println("Name;Price;Quantity");  // Header line
            Drug drug;
            // Loop through each drug and write updated stock information
            for (int i = 0; i < drugs.size(); i++) {
                drug = drugs.get(i);
                write.println(drug.getName() + ";" + drug.getPrice() + ";" + drug.getQuantity());
            }
            write.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error: Failed to update Inventory");
            return false;
        }
    }
}
