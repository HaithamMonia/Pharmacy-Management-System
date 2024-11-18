import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class LoginTest {
    private Login login; // Instance of the Login class used in tests.
    private static final String TEST_USERNAME = "Haitham"; // A sample username for testing.
    private static final String TEST_PASSWORD = "Haitham"; // A sample password for testing.
    private static final String TEST_FILE = "test_users.txt"; // Path to the test file.

    @BeforeEach
    void setUp() throws IOException {
        // Set up the environment for each test case.
        // Initialize the Login object with a test file.
        login = new Login(TEST_USERNAME, TEST_PASSWORD, new File(TEST_FILE));

        // Populate the test file with a single valid user record.
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write(TEST_USERNAME + ";" + TEST_PASSWORD + "\n");
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up the test environment after each test.
        // Delete the test file to ensure no residual data affects other tests.
        new File(TEST_FILE).delete();
    }

    @Test
    void testValidateUser() {
        // Test scenario: Validating correct and incorrect login attempts.

        // Verify that a user with valid credentials can be authenticated successfully.
        assertTrue(login.validateUser(TEST_USERNAME, TEST_PASSWORD));

        // Verify that an invalid username and password combination is not authenticated.
        assertFalse(login.validateUser("wrongUser", "wrongPass"));
    }

    @Test
    void testValidateUserEmptyFile() throws IOException {
        // Test scenario: Handling login validation when the user file is empty.

        // Simulate an empty user file by deleting and recreating the file.
        new File(TEST_FILE).delete();
        new File(TEST_FILE).createNewFile();

        // Verify that no users can be validated since the file is empty.
        assertFalse(login.validateUser(TEST_USERNAME, TEST_PASSWORD));
    }

    @Test
    void testValidateUserFileNotFound() {
        // Test scenario: Handling login validation when the user file is missing.

        // Delete the test file to simulate a missing file.
        new File(TEST_FILE).delete();

        // Verify that validation fails because the file does not exist.
        assertFalse(login.validateUser(TEST_USERNAME, TEST_PASSWORD));
    }
}
