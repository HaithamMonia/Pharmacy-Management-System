import org.junit.jupiter.api.*;
import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PharmacyControllerTest {

    private static File userFile;
    private static File drugFile;
    private static File cartFile;

    @BeforeAll
    static void setUp() throws IOException {
        // Set up dummy files for testing
        userFile = new File("test_users.txt");
        drugFile = new File("test_drugs.txt");
        cartFile = new File("test_carts.txt");

        // Create sample user data
        try (PrintWriter writer = new PrintWriter(new FileWriter(userFile))) {
            writer.println("testuser;testpassword");
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

    @AfterAll
    static void tearDown() {
        // Clean up files
        userFile.delete();
        drugFile.delete();
        cartFile.delete();
    }

    @Test
    void testLoginValidUser() {
        Login login = new Login("testuser", "testpassword", userFile);
        assertTrue(login.validateUser("testuser", "testpassword"), "Valid login failed.");
    }

    @Test
    void testLoginInvalidUser() {
        Login login = new Login("testuser", "wrongpassword", userFile);
        assertFalse(login.validateUser("testuser", "wrongpassword"), "Invalid login passed.");
    }

    @Test
    void testRegistrationNewUser() throws IOException {
        Register register = new Register(new Scanner("newuser\nnewpassword\n"), userFile);
        register.startRegistration();

        try (Scanner scanner = new Scanner(userFile)) {
            boolean userFound = false;
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equals("newuser;newpassword")) {
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
        assertEquals("Aspirin", purchaseDrug.drugsAvailableList.get(0).getName());
        assertEquals(5.99, purchaseDrug.drugsAvailableList.get(0).getPrice());
    }

    @Test
    void testPlaceOrder() throws FileNotFoundException {
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
            assertFalse(orderFound, "Order was not added to the cart correctly.");
        }
    }

    @Test
    void testCheckout() throws FileNotFoundException {
        Checkout checkout = new Checkout();
        assertFalse(checkout.checkout("testuser"), "Checkout failed.");

        // Verify the checkout file is updated
        try (Scanner scanner = new Scanner(new File("checkouts.txt"))) {
            boolean checkoutFound = false;
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equals("testuser;Aspirin;5.99;2")) {
                    checkoutFound = true;
                    break;
                }
            }
            assertFalse(checkoutFound, "Checkout was not recorded correctly.");
        }
    }
}
