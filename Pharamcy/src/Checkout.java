import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Checkout {
    private String drugName;
    private double price;
    private int quantity;
    private double total=0;

    private Scanner scan;
    private ArrayList<Drug> checkoutList;
    private ArrayList<String> allCartContent;

    public void viewCart(String username){
        System.out.println("\nThe Cart");
        checkoutList = new ArrayList<Drug>();
        try {
            scan = new Scanner(new File("carts.txt"));
            if(!scan.hasNextLine()){
                System.out.println("No items in the cart");
                return;
            }
            scan.nextLine(); // to skip the header line of the file
            String[] drug;
            int lineNum = 1;

            while(scan.hasNextLine()){
                String line = scan.nextLine();
                drug = line.split(";"); // split line into an array of drug properties

                // Check if the username matches the one passed as argument
                if(drug[0].equalsIgnoreCase(username)){
                    drugName = drug[1];
                    price = Double.parseDouble(drug[2]);
                    quantity = Integer.parseInt(drug[3]);
                    total += price * quantity; // update total with cost of this drug

                    // Add the drug to the checkout list
                    checkoutList.add(new Drug(drugName, price, quantity));
                    System.out.println(lineNum + "- " + drugName + ":");
                    System.out.println("Quantity: " + quantity);
                    System.out.println("Cost: " + (price * quantity));
                    lineNum++;
                }
            }
            System.out.println("Total: "+total);
            if(lineNum == 1){
                System.out.println("No items in your cart"); // Display if the cart is empty
            }
            System.out.println();

            scan.close();

        }catch(Exception e){
            System.out.println("ERROR: Opening the cart. Try again.."); // Error message in case of issues reading the cart file
        }
    }

    // checkout method to process the items in the user's cart and write them to checkout file
    public boolean checkout(String username){
        try{
            FileWriter file = new FileWriter("checkouts.txt",true);
            PrintWriter writeCheckout = new PrintWriter(file);
            System.out.println("C H E C K  O U T");
            viewCart(username); // Display items in cart
            System.out.println("Total Cost: " + total);
           

            // Update the cart file after checkout if updateCart is successful
            if (updateCart(username)){
                for(int i = 0; i < checkoutList.size(); i++){
                    writeCheckout.println(username + ";" + checkoutList.get(i).getName() + ";" +
                            checkoutList.get(i).getPrice() + ";" +
                            checkoutList.get(i).getQuantity());
                }
                writeCheckout.close();
                System.out.println("Checkout Succeed");
                return true;
            } else {
                return false;
            }

        }catch(Exception e){
            System.out.println("ERROR: Checkout Failed try again ..."); // Error in checkout process
            return false;
        }
    }

    // updateCart method to remove items from the cart file after successful checkout
    private boolean updateCart(String username){
        allCartContent = new ArrayList<>();
        try{
            scan = new Scanner(new File("carts.txt"));
            PrintWriter writeCart = new PrintWriter("carts.txt");
            String[] drug;
            boolean toDelete = false;

            // Read all content from cart file and store in allCartContent list
            while(scan.hasNextLine()){
                drug = scan.nextLine().split(";");
                allCartContent.add(drug[0] + ";" + drug[1] + ";" + drug[2] + ";" + drug[3]);
            }
            //we start updating the cart
            writeCart.println("Username;Drug;Price;Quantity"); // Rewrite header in the cart file

            // Iterate over all items in cart to update it, excluding items in checkout list
            for(int i = 0; i < allCartContent.size(); i++){
                String[] cartItem = allCartContent.get(i).split(";");
                for(int j = 0; j < checkoutList.size(); j++){
                    // Check if current cart item belongs to user being checked out
                    if(cartItem[0].equalsIgnoreCase(username)){
                        toDelete = true;
                    }
                }
                if (toDelete){
                    continue; // Skip adding item if it belongs to the user
                }
                writeCart.println(allCartContent.get(i));
            }
            writeCart.close();

        }catch (Exception e){
            System.out.println("ERROR: In the checkout..."); // Error message in case of issues updating the cart
            return false;
        }
        return true;
    }
}
