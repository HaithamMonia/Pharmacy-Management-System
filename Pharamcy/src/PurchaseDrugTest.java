import org.junit.jupiter.api.*; // JUnit 5 testing framework
import java.io.*; // For file handling
import java.util.*; // For lists and other collections

import static org.junit.jupiter.api.Assertions.*; // Assertions for testing

/**
 * Unit test class for the PurchaseDrug class.
 * Tests the functionality of viewing drugs, placing orders, adding to cart,
 * and updating the inventory.
 */
class PurchaseDrugTest {
    private PurchaseDrug purchaseDrug; // Object under test
    private File testDrugsFile; // Temporary file for storing test drug data
    private File testCartsFile; // Temporary file for storing test cart data

    /**
     * Runs before each test.
     * Sets up temporary test-specific files and populates them with initial data.
     */
    @BeforeEach
    void setUp() throws IOException {
        // Create temporary files to isolate test data from production data
        testDrugsFile = new File("test_drugs.txt");
        testCartsFile = new File("test_carts.txt");

        // Initialize the PurchaseDrug object with test file paths
        purchaseDrug = new PurchaseDrug(testDrugsFile.getName(), testCartsFile.getName());

        // Write initial drug data to the test drugs file
        try (PrintWriter writer = new PrintWriter(testDrugsFile)) {
            writer.println("Name;Price;Quantity"); // Header for the drug inventory
            writer.println("Aspirin;10.0;20");     // Drug 1: Aspirin
            writer.println("Paracetamol;15.5;30"); // Drug 2: Paracetamol
            writer.println("Ibuprofen;20.0;0");   // Drug 3: Ibuprofen (out of stock)
        }

        // Write initial cart data to the test carts file
        try (PrintWriter writer = new PrintWriter(testCartsFile)) {
            writer.println("Username;Drug;Price;Quantity"); // Header for cart entries
        }
    }

    /**
     * Runs after each test.
     * Cleans up by deleting the temporary test files.
     */
    @AfterEach
    void tearDown() {
        // Delete temporary test files to ensure clean test execution
        testDrugsFile.delete();
        testCartsFile.delete();
    }

    /**
     * Tests the viewDrugs() method to ensure drugs are correctly loaded.
     */
    @Test
    void testViewDrugs() {
        // Call the viewDrugs method to load drugs from the test file
        purchaseDrug.viewDrugs();

        // Assert that the drugsAvailableList is not null and has 3 entries
        assertNotNull(purchaseDrug.drugsAvailableList);
        assertEquals(3, purchaseDrug.drugsAvailableList.size());

        // Assert that the first drug in the list is "Aspirin"
        assertEquals("Aspirin", purchaseDrug.drugsAvailableList.get(0).getName());
    }

    /**
     * Tests the placeOrder() method for a valid order scenario.
     */
    @Test
    void testPlaceOrder_ValidOrder() {
        // Call viewDrugs to initialize the drugs list
        purchaseDrug.viewDrugs();

        // Place an order for 2 units of Aspirin (drug ID 1)
        assertTrue(purchaseDrug.placeOrder("John", 1, 2));

        // Assert that the order was successful (returns true)
    }

    /**
     * Tests the placeOrder() method when the drug is out of stock.
     */
    @Test
    void testPlaceOrder_OutOfStock() {
        // Attempt to place an order for Ibuprofen (drug ID 3), which is out of stock
        assertFalse(purchaseDrug.placeOrder("John", 3, 1));

        // Assert that the order fails (returns false)
    }

    /**
     * Tests the addToCart() method to verify drugs can be added to the user's cart.
     */
    @Test
    void testAddToCart() {
        // Add 2 units of Aspirin to the cart for the user "John"
        assertTrue(purchaseDrug.addToCart("John", "Aspirin", 10.0, 2));

        // Assert that the operation succeeds (returns true)
    }

    /**
     * Tests the updateInventory() method to ensure the inventory file is updated.
     */
    @Test
    void testUpdateInventory() {
        // Call viewDrugs to load the drug inventory
        purchaseDrug.viewDrugs();

        // Simulate reducing the stock for Aspirin by updating its quantity
        purchaseDrug.drugsAvailableList.get(0).setQuantity(18);

        // Update the inventory file and assert that the operation succeeds
        assertTrue(purchaseDrug.updateInventory());
    }
}
