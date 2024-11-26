import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class PurchaseDrug {
    private String drugsFileName;
    private String cartFileName;
    public ArrayList<Drug> drugsAvailableList;

    public PurchaseDrug() {
        this("drugs.txt", "carts.txt"); // Default filenames
    }

    public PurchaseDrug(String drugsFileName, String cartFileName) {
        this.drugsFileName = drugsFileName;
        this.cartFileName = cartFileName;
        drugsAvailableList = new ArrayList<>();
    }

    // This method displays available drugs in the pharmacy
    public void viewDrugs() {
        drugsAvailableList.clear(); // Clear previous data before loading new
        try (Scanner fileScanner = new Scanner(new File("durg.txt"))) {
            // Check if file is empty
            if (!fileScanner.hasNextLine()) {
                System.out.println("No Drugs Found");
                return;
            }
            fileScanner.nextLine(); // Skip header line
            int lineNum = 1;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] drug = line.split(";");
                System.out.println(lineNum + "- " + line);

                // Create a new Drug object from line data and add it to the list
                Drug d = new Drug(drug[1], Double.parseDouble(drug[2]), Integer.parseInt(drug[3]));
                drugsAvailableList.add(d);
                lineNum++;
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("ERROR: Viewing the Drugs - " + e.getMessage());
        }
    }

 

    // Adds the selected drug to the cart file
    public boolean addToCart(String username, String name, double price, int qty) {
        try (FileWriter fileWriter = new FileWriter(cartFileName, true);
             PrintWriter write = new PrintWriter(fileWriter)) {
            write.println(username + ";" + name + ";" + price + ";" + qty);
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: Adding the item to the cart - " + e.getMessage());
            return false;
        }
    }
   // This method places an order for a drug and updates inventory
   public boolean placeOrder(String username, int option, int numElementToBuy) {
    // Display available drugs
    try {
        if (option <= 0 || option > drugsAvailableList.size()) {
            System.out.println("Invalid option selected.");
            return false;
        }

        Drug selectedDrug = drugsAvailableList.get(option - 1);
        if (selectedDrug.getQuantity() == 0) {
            System.out.println("Sorry, " + selectedDrug.getName() + " is out of stock.");
            return false;
        }

        if (numElementToBuy <= 0 || numElementToBuy > selectedDrug.getQuantity()) {
            System.out.println("Invalid quantity. Available stock is " + selectedDrug.getQuantity());
            return false;
        }

        // Add drug to the cart
        if (addToCart(username, selectedDrug.getName(), selectedDrug.getPrice(), numElementToBuy)) {
            selectedDrug.setQuantity(selectedDrug.getQuantity() - numElementToBuy);
            // Update inventory
            if (updateInventory()) {
                System.out.println(selectedDrug.getName() + " has been added successfully to your cart");
                return true;
            } else {
                System.out.println("Failed to update inventory. Please try again.");
                return false;
            }
        } else {
            System.out.println("Failed to add the item to the cart.");
            return false;
        }
    } catch (Exception e) {
        System.out.println("ERROR: Failed to place the order - " + e.getMessage());
        return false;
    }
}
    // Updates the drug inventory by writing updated quantities to the file
    public boolean updateInventory() {
        try (PrintWriter write = new PrintWriter(drugsFileName)) {
            write.println("Name;Price;Quantity"); // Header line
            for (Drug drug : drugsAvailableList) {
                write.println(drug.getName() + ";" + drug.getPrice() + ";" + drug.getQuantity());
            }
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: Failed to update inventory - " + e.getMessage());
            return false;
        }
    }
}
