import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PharmacyController {
    private static String username;
    private static String password;
    private static boolean isLogged =false;
    public static void main(String[] args) {
      

        int option,drugSelector;
        Scanner scan = new Scanner(System.in);

        while(true) {
            try {
                do {
                    System.out.println("\nW E L C O M E  T O  P H A R M A C Y");
                    System.out.println("\nSELECT SERVICE");
                    System.out.println("1- Register");
                    System.out.println("2- Login");
                    System.out.println("0- to Quit");
                    System.out.print("\nEnter the number of the service: ");

                    option = scan.nextInt();
                    switch (option) {
                        case 0 -> System.exit(0);
                        case 1 -> new Register();

                        case 2 -> {
                            // this to prevent leading the newline after pressing on enter after reading the option
                            scan.nextLine();
                            System.out.print("Enter your username: ");
                            username = scan.nextLine().trim();
                            System.out.print("Enter your password: ");
                            password = scan.nextLine().trim();
                            Login loginTest = new Login(username, password);
                            // if the user logged in successfully he/she will be directed to the purchase page
                            if (loginTest.validateUser(username, password)) {
                                isLogged = true;
                                // this the view that will appear to the logged users
                                do {
                                    System.out.println("Welcome " + username + " to the pharmacy");
                                    System.out.println("1- View Drugs");
                                    System.out.println("2- Place Order");
                                    System.out.println("3- View Cart");
                                    System.out.println("4- Checkout");
                                    System.out.println("0- HOME");
                                    System.out.print("\nEnter the service number: ");
                                    drugSelector = scan.nextInt();
                                    if (drugSelector == 1) {
                                        new PurchaseDrug().viewDrugs();
                                    }else if(drugSelector ==2){
                                        PurchaseDrug order = new PurchaseDrug();
                                        if(order.placeOrder(username))
                                            System.out.println();
                                        else
                                            System.out.println("Failed to add the item to the cart");
//                                        new PurchaseDrug().viewDrugs();
                                    }
                                    if (drugSelector== 3){
                                        if(isLogged){
                                            Checkout checkout = new Checkout();
                                            checkout.viewCart(username);
                                        }
                                    }
                                    if(drugSelector ==4){
                                        Checkout checkout = new Checkout();
                                        checkout.checkout(username);
                                    }
                                } while (drugSelector != 0);

                            } else {
                                System.out.println("Wrong username or password!");
                            }
                        }

                        default -> {
                            System.out.println("Enter a valid option (1-4)");

                        }
                    }
                } while (option != 0);
            } catch (InputMismatchException e) {
                System.out.println("The option must be a number between (1-4)");
                scan.nextLine(); //  this to clear the invalid input
            }
        }
    }
}
