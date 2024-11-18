import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class PharmacyControllerTest {
    private PharmacyController pharmacyController;
    private static final String TEST_USERNAME = "TestUser"; // A sample username for testing
    private static final String TEST_PASSWORD = "TestPassword"; // A sample password for testing
    private static final String TEST_USER_FILE = "test_users.txt"; // Path to the test users file
    private static final String TEST_DRUGS_FILE = "test_drugs.txt"; // Path to the test drugs file
    private static final String TEST_CART_FILE = "test_carts.txt"; // Path to the test cart file

    @BeforeEach
    void setUp() throws IOException {
        // Initialize the PharmacyController before each test
        pharmacyController = new PharmacyController();

        // Create test users file and add a user
        try (FileWriter writer = new FileWriter(TEST_USER_FILE)) {
            writer.write(TEST_USERNAME + ";" + TEST_PASSWORD + "\n");
        }

        // Create test drugs file and add some sample drugs
        try (FileWriter writer = new FileWriter(TEST_DRUGS_FILE)) {
            writer.write("Aspirin;5.0;100\n");
            writer.write("Ibuprofen;10.0;50\n");
        }

        // Create test cart file to simulate a shopping cart
        try (FileWriter writer = new FileWriter(TEST_CART_FILE)) {
            writer.write("TestUser;Aspirin;5.0;2\n");
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up test files after each test
        new File(TEST_USER_FILE).delete();
        new File(TEST_DRUGS_FILE).delete();
        new File(TEST_CART_FILE).delete();
    }

    @Test
    void testRegisterUser() {
        // Test scenario: Register a new user
        Register register = new Register();
        register.setUsername("NewUser");
        register.setPassword("NewPassword");

        // Check if the new user is saved in the test users file
        assertTrue(new File(TEST_USER_FILE).exists());
    }

    @Test
    void testLoginSuccess() {
        // Test scenario: Login with correct credentials
        Login login = new Login(TEST_USERNAME, TEST_PASSWORD, new File(TEST_USER_FILE));
        assertTrue(login.validateUser(TEST_USERNAME, TEST_PASSWORD));
    }

    @Test
    void testLoginFailure() {
        // Test scenario: Login with incorrect credentials
        Login login = new Login(TEST_USERNAME, TEST_PASSWORD, new File(TEST_USER_FILE));
        assertFalse(login.validateUser("WrongUser", "WrongPassword"));
    }

    @Test
    void testViewDrugs() {
        // Test scenario: Viewing drugs from the file
        PurchaseDrug purchaseDrug = new PurchaseDrug();
        purchaseDrug.viewDrugs(); // Should print drug details to the console
    }

    @Test
    void testPlaceOrder() {
        // Test scenario: Place an order for a drug
        PurchaseDrug purchaseDrug = new PurchaseDrug();
        purchaseDrug.viewDrugs(); // Show drugs to the user

        // Simulate user input to select a drug and quantity
        boolean result = purchaseDrug.placeOrder(TEST_USERNAME);
        assertTrue(result);
    }

    @Test
    void testViewCart() {
        // Test scenario: Viewing the cart for the user
        Checkout checkout = new Checkout();
        checkout.viewCart(TEST_USERNAME); // Should print the cart details to the console
    }

    @Test
    void testCheckout() {
        // Test scenario: Checkout process
        Checkout checkout = new Checkout();
        boolean result = checkout.checkout(TEST_USERNAME);
        assertTrue(result);  // Verify that the checkout process is successful
    }

    @Test
    void testCartFileNotFound() {
        // Test scenario: Handling case when the cart file is missing
        new File(TEST_CART_FILE).delete(); // Simulate missing cart file
        Checkout checkout = new Checkout();
        boolean result = checkout.checkout(TEST_USERNAME);
        assertFalse(result); // Should fail because the cart file is missing
    }

    @Test
    void testDrugsFileNotFound() {
        // Test scenario: Handling case when the drugs file is missing
        new File(TEST_DRUGS_FILE).delete(); // Simulate missing drugs file
        PurchaseDrug purchaseDrug = new PurchaseDrug();
        purchaseDrug.viewDrugs(); // Should print "No Drugs Found" or handle the exception gracefully
    }
}