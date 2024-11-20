import org.junit.jupiter.api.*;
import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PharmacyControllerTest {

    // Set up dummy file for testing
    private static File userFile = new File("test_users.txt");

    @BeforeAll
    static void setUp() throws IOException {
        // Create sample user data
        try (PrintWriter writer = new PrintWriter(new FileWriter(userFile))) {
            writer.println("testuser;testpassword");
        }
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
        assertEquals(10, purchaseDrug.drugsAvailableList.size());
        assertEquals("Aspirin", purchaseDrug.drugsAvailableList.get(0).getName());
        assertEquals(3.5, purchaseDrug.drugsAvailableList.get(0).getPrice());
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