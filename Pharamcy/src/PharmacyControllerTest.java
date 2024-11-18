import org.junit.jupiter.api.*;
import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PharmacyControllerTest {

    private static File userFile;
    private static File cartFile;

    @BeforeAll
    static void setUp() throws IOException {
        // Set up dummy files for testing
        userFile = new File("test_users.txt");
        File drugFile = new File("test_drugs.txt");
        cartFile = new File("test_carts.txt");

        // Create sample user data
        try (PrintWriter writer = new PrintWriter(new FileWriter(userFile))) {
            writer.println("tester;password");
        }

        // Create sample drug data
        try (PrintWriter writer = new PrintWriter(new FileWriter(drugFile))) {
            writer.println("Name;Price;Quantity");
            writer.println("Aspirin;5.99;10");
            writer.println("Paracetamol;4.99;20");
        }

        // Create empty cart file
        try (PrintWriter writer = new PrintWriter(new FileWriter(cartFile))) {
            writer.println("Username;Drug;Price;Quantity");
        }
    }

    @Test
    void testLoginValidUser() {
        Login login = new Login("tester", "password", userFile);
        assertTrue(login.validateUser("tester", "password"), "Valid login failed.");
    }

    @Test
    void testLoginInvalidUser() {
        Login login = new Login("tester", "wrong password", userFile);
        assertFalse(login.validateUser("tester", "wrong password"), "Invalid login passed.");
    }

    @Test
    void testRegistrationNewUser() throws IOException {
        Register register = new Register(new Scanner("new user\nnewpassword\n"), userFile);
        register.startRegistration();

        try (Scanner scanner = new Scanner(userFile)) {
            boolean userFound = false;
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equals("new user;newpassword")) {
                    userFound = true;
                    break;
                }
            }
            assertTrue(userFound, "New user was not registered correctly.");
        }
    }

    @Test
    void testViewDrugs() {
        PurchaseDrug purchaseDrug = new PurchaseDrug();
        purchaseDrug.viewDrugs();

        // Verify the drugs list is populated
        assertNotNull(purchaseDrug.drugsAvailableList);
        assertEquals(2, purchaseDrug.drugsAvailableList.size());
        assertEquals("Aspirin", purchaseDrug.drugsAvailableList.getFirst().getName());
        assertEquals(5.99, purchaseDrug.drugsAvailableList.getFirst().getPrice());
    }

    @Test
    void testPlaceOrder() {
        PurchaseDrug purchaseDrug = new PurchaseDrug();
        purchaseDrug.scan = new Scanner("1\n2\n");
        purchaseDrug.viewDrugs();

        assertTrue(purchaseDrug.placeOrder("testuser"), "Placing order failed.");

        // Verify cart file is updated
        try (Scanner scanner = new Scanner(cartFile)) {
            boolean orderFound = false;
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equals("testuser;Aspirin;5.99;2")) {
                    orderFound = true;
                    break;
                }
            }
            assertTrue(orderFound, "Order was not added to the cart correctly.");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCheckout() {
        Checkout checkout = new Checkout();
        assertTrue(checkout.checkout("tester"), "Checkout failed.");

        // Verify the checkout file is updated
        try (Scanner scanner = new Scanner(new File("checkouts.txt"))) {
            boolean checkoutFound = false;
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equals("tester;Aspirin;5.99;2")) {
                    checkoutFound = true;
                    break;
                }
            }
            assertFalse(checkoutFound, "Checkout was not recorded correctly.");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
