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
            writer.println("Aspirin;3.5;1");
            writer.println("Paracetamol;4.25;20");
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
        assertTrue(login.validateUser("testuser", "testpassword"), "Login Failed");
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
        assertEquals(10, purchaseDrug.drugsAvailableList.size());
        assertEquals("Paracetamol", purchaseDrug.drugsAvailableList.get(2).getName());
        assertEquals(4.25, purchaseDrug.drugsAvailableList.get(2).getPrice());
    }

    @Test
    void testPlaceOrder() throws FileNotFoundException {
        PurchaseDrug purchaseDrug = new PurchaseDrug();
        purchaseDrug.viewDrugs();

        assertTrue(purchaseDrug.placeOrder("testuser",3, 1), "Placing order failed.");

        // Verify cart file is updated
        try (Scanner scanner = new Scanner(cartFile)) {
            boolean orderFound = false;
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equals("testuser;Paracetamol;4.25;1")) {
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
        assertTrue(checkout.checkout("testuser"), "Checkout failed.");

        // Verify the checkout file is updated
        try (Scanner scanner = new Scanner(new File("checkouts.txt"))) {
            boolean checkoutFound = false;
        }
    }
}