import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class PurchaseDrugTest {
    private PurchaseDrug purchaseDrug;

    @BeforeEach
    public void setUp() {
        purchaseDrug = new PurchaseDrug();
        // Create a sample drugs.txt file for testing
        try (PrintWriter writer = new PrintWriter(new FileWriter("drugs.txt"))) {
            writer.println("Name;Price;Quantity");
            writer.println("Aspirin;10.0;100");
            writer.println("Ibuprofen;15.0;50");
        } catch (Exception e) {
            fail("Failed to set up test file");
        }
    }

    @Test
    public void testViewDrugs() {
        purchaseDrug.viewDrugs();
        // Add assertions to verify the output
        assertNotNull(purchaseDrug.drugsAvailableList);
        assertEquals(2, purchaseDrug.drugsAvailableList.size());
    }

    @Test
    public void testPlaceOrder() {
        boolean result = purchaseDrug.placeOrder("testUser");
        assertTrue(result);
    }

    @Test
    public void testAddToCart() {
        boolean result = purchaseDrug.addToCart("testUser", "Aspirin", 10.0, 2);
        assertTrue(result);
    }

    @Test
    public void testUpdateInventory() {
        ArrayList<Drug> drugs = new ArrayList<>();
        drugs.add(new Drug("Aspirin", 10.0, 98));
        drugs.add(new Drug("Ibuprofen", 15.0, 50));
        boolean result = purchaseDrug.updateInventory(drugs);
        assertTrue(result);
    }
}
