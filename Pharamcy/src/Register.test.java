import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class RegisterTest {

    @TempDir
    Path tempDir; // A temporary directory provided by JUnit to store test files during execution.

    private File userFile; // File object to represent the test users file.

    @BeforeEach
    void setUp() throws IOException {
        // Set up a new, empty users.txt file in the temporary directory before each test.
        // This ensures a clean slate for every test case, avoiding interference between tests.
        userFile = new File(tempDir.toFile(), "users.txt");
        userFile.createNewFile(); // Create an empty test file.
    }

    @Test
    void testPreventShortPassword() {
        // Test scenario: Ensuring that passwords shorter than the minimum required length (5 characters)
        // are rejected and a valid password is accepted afterwards.

        // Simulated user input with an initial short password followed by a valid password.
        String input = "testuser\nshor\nvalidpassword\n";
        Scanner inputScanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        // Using the parameterized constructor to inject the test input and user file.
        Register register = new Register(inputScanner, userFile);

        // Start the registration process with the simulated inputs.
        register.startRegistration();

        // Assertions to verify the expected username and valid password after registration.
        assertEquals("testuser", register.getUsername()); // Username should match the input.
        assertEquals("validpassword", register.getPassword()); // Password should match the corrected input.
    }

    @Test
    void testSetUsernameWithLessThanThreeCharacters() {
        // Test scenario: Ensuring that usernames shorter than the minimum required length (3 characters)
        // are rejected and a valid username is accepted afterwards.

        // Simulated user input with an initial invalid username followed by a valid username.
        String input = "ab\nvaliduser\nvalidpassword\n";
        Scanner inputScanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        // Using the parameterized constructor with the test input and user file.
        Register register = new Register(inputScanner, userFile);

        // Start the registration process with the simulated inputs.
        register.startRegistration();

        // Assertions to verify the expected valid username and password after registration.
        assertEquals("validuser", register.getUsername()); // Username should be updated to the valid input.
        assertEquals("validpassword", register.getPassword()); // Password should match the input.
    }

    @Test
    void testSuccessfulRegistration() throws Exception {
        // Test scenario: Verifying the registration of a valid user with a valid username and password,
        // and ensuring the user data is correctly saved in the users file.

        // Simulated user input with a valid username and password.
        String input = "testuser\npassword123\n";
        Scanner inputScanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        // Using the parameterized constructor with the test input and user file.
        Register register = new Register(inputScanner, userFile);

        // Start the registration process with the simulated inputs.
        register.startRegistration();

        // Verify the test file exists and contains the registered user data.
        assertTrue(userFile.exists()); // Ensure the file was created during registration.

        try (Scanner fileScanner = new Scanner(userFile)) {
            assertTrue(fileScanner.hasNextLine()); // Check that the file is not empty.
            String savedUser = fileScanner.nextLine(); // Read the first line of the file.
            assertEquals("testuser;password123", savedUser); // Validate the saved user data format and values.
        }
    }
}
