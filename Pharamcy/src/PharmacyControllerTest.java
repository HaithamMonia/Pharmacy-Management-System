import java.io.*;
import java.util.Scanner;

public class PharmacyControllerTest {
    public static void main(String[] args) throws Exception {
        // Simulate file setup for testing
        setupTestFiles();

        // Test cases for Register, Login, View Drugs, Place Order, View Cart, and Checkout
        testRegister();
        testLogin();
        testViewDrugs();
        testPlaceOrder();
        testViewCart();
        testCheckout();
    }

    private static void setupTestFiles() throws Exception {
        // Create or reset test files for "users.txt", "drugs.txt", "carts.txt", and "checkouts.txt"
        try (PrintWriter userWriter = new PrintWriter(new FileWriter("users.txt"))) {
            userWriter.println("admin;admin123");
        }

        try (PrintWriter drugWriter = new PrintWriter(new FileWriter("drugs.txt"))) {
            drugWriter.println("Name;Price;Quantity");
            drugWriter.println("Paracetamol;2.5;10");
            drugWriter.println("Aspirin;3.0;5");
        }

        try (PrintWriter cartWriter = new PrintWriter(new FileWriter("carts.txt"))) {
            cartWriter.println("Username;Drug;Price;Quantity");
        }

        try (PrintWriter checkoutWriter = new PrintWriter(new FileWriter("checkouts.txt"))) {
            checkoutWriter.println("Username;Drug;Price;Quantity");
        }
    }

    private static void testRegister() {
        System.out.println("\n=== Test Register ===");
        try {
            Register register = new Register(new Scanner(new ByteArrayInputStream("newUser\npassword123\n".getBytes())), new File("users.txt"));
            register.startRegistration();
            System.out.println("Registration successful!");
        } catch (Exception e) {
            System.err.println("Error in registration test: " + e.getMessage());
        }
    }

    private static void testLogin() {
        System.out.println("\n=== Test Login ===");
        Login login = new Login("admin", "admin123", new File("users.txt"));
        if (login.validateUser("admin", "admin123")) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed.");
        }
    }

    private static void testViewDrugs() {
        System.out.println("\n=== Test View Drugs ===");
        PurchaseDrug purchaseDrug = new PurchaseDrug();
        purchaseDrug.viewDrugs();
    }

    private static void testPlaceOrder() {
        System.out.println("\n=== Test Place Order ===");
        PurchaseDrug purchaseDrug = new PurchaseDrug();
        purchaseDrug.viewDrugs();
        if (purchaseDrug.placeOrder("admin")) {
            System.out.println("Order placed successfully!");
        } else {
            System.out.println("Failed to place order.");
        }
    }

    private static void testViewCart() {
        System.out.println("\n=== Test View Cart ===");
        Checkout checkout = new Checkout();
        checkout.viewCart("admin");
    }

    private static void testCheckout() {
        System.out.println("\n=== Test Checkout ===");
        Checkout checkout = new Checkout();
        if (checkout.checkout("admin")) {
            System.out.println("Checkout successful!");
        } else {
            System.out.println("Checkout failed.");
        }
    }
}
