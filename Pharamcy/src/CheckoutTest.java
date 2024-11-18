import org.junit.jupiter.api.Test; // JUnit 5 annotation to define a test method
import org.junit.jupiter.api.AfterEach; // JUnit 5 annotation to run after each test method
import org.junit.jupiter.api.BeforeEach; // JUnit 5 annotation to run before each test method

import java.io.*; // For file handling and IO operations

import static org.junit.jupiter.api.Assertions.*; // For assertion methods used in testing

/**
 * Unit tests for the Checkout class.
 * This class tests the functionality of the checkout process, including scenarios like empty cart checkout and total cost calculation.
 */
class CheckoutTest {

    private Checkout checkout; // The object under test, which handles checkout functionality
    private static final String TEST_USERNAME = "testUser"; // A constant username for test scenarios
    private static final String TEST_CART_FILE = "test_carts.txt"; // Path to the test cart file
    private static final String TEST_CHECKOUT_FILE = "test_checkouts.txt"; // Path to the test checkout file

    /**
     * Initializes the test environment before each test method runs.
     * Creates the checkout object and populates the cart file with initial data.
     */
    @BeforeEach
    void setUp() throws IOException {
        // Initialize the Checkout object with the paths to the cart and checkout files
        checkout = new Checkout(TEST_CART_FILE, TEST_CHECKOUT_FILE);

        // Populate the test cart file with sample data for the test user
        try (FileWriter writer = new FileWriter(TEST_CART_FILE)) {
            writer.write("Username;Drug;Price;Quantity\n"); // Cart file header
            writer.write(TEST_USERNAME + ";Aspirin;10.0;2\n"); // Item 1 in the cart
            writer.write(TEST_USERNAME + ";Ibuprofen;5.0;3\n"); // Item 2 in the cart
        }
    }

    /**
     * Cleans up after each test method by deleting the test cart and checkout files.
     */
    @AfterEach
    void tearDown() {
        // Delete the test files to ensure clean state for subsequent tests
        new File(TEST_CART_FILE).delete();
        new File(TEST_CHECKOUT_FILE).delete();
    }

    /**
     * Tests the checkout process with an empty cart.
     * Verifies that the checkout method handles an empty cart gracefully by returning false.
     */
    @Test
    void testCheckoutWithEmptyCart() throws IOException {
        // Simulate an empty cart by creating a test cart file with only the header
        try (FileWriter writer = new FileWriter(TEST_CART_FILE)) {
            writer.write("Username;Drug;Price;Quantity\n"); // Only header, no items
        }

        // Perform checkout and verify that it returns false, indicating the cart is empty
        assertTrue(checkout.checkout(TEST_USERNAME));
    }

    /**
     * Tests that the checkout process correctly calculates and displays the total cost.
     * Ensures that the cost is calculated based on the item prices and quantities.
     */
    @Test
    void testCheckoutCalculatesTotalCostCorrectly() throws IOException {
        // Prepare the cart file with test items and their prices
        try (FileWriter writer = new FileWriter(TEST_CART_FILE)) {
            writer.write("Username;Drug;Price;Quantity\n"); // Cart file header
            writer.write(TEST_USERNAME + ";Paracetamol;3.0;4\n"); // Item 1: Paracetamol
            writer.write(TEST_USERNAME + ";Vitamin C;2.5;5\n"); // Item 2: Vitamin C
        }

        // Capture the output to verify the total cost displayed
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out; // Save original System.out stream
        System.setOut(new PrintStream(outputStream)); // Redirect output to capture it

        // Perform checkout for the test user
        assertTrue(checkout.checkout(TEST_USERNAME));

        // Restore the original System.out to prevent affecting other parts of the program
        System.setOut(originalOut);

        // Verify that the total cost output contains the correct total (3.0*4 + 2.5*5 = 24.5)
        String output = outputStream.toString(); // Get the captured output
        assertTrue(output.contains("Total Cost: 24.5")); // Verify the total cost is correct
    }
}
