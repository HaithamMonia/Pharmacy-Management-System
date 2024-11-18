import java.io.File;
import java.util.Scanner;

/**
 * This class represents a login system for a pharmacy application.
 */
public class Login {
    private String username;
    private String password;
    private Scanner scan;
    private File userFile;

    /**
     * Constructs a Login object with the given username and password using the default "users.txt" file.
     *
     * @param username The username for the login.
     * @param password The password for the login.
     */
    public Login(String username, String password) {
        this.username = username;
        this.password = password;
        this.userFile = new File("users.txt"); // Default file for validation
    }

    /**
     * Overloaded constructor for testing. Allows specifying a custom file for user validation.
     *
     * @param username The username for the login.
     * @param password The password for the login.
     * @param userFile The file to use for user validation.
     */
    public Login(String username, String password, File userFile) {
        this.username = username;
        this.password = password;
        this.userFile = userFile; // Custom file for validation
    }

    /**
     * Validates a user by checking if the given username and password match the ones stored in the user file.
     *
     * @param un The username to validate.
     * @param pw The password to validate.
     * @return True if the username and password match, false otherwise.
     */
    public boolean validateUser(String un, String pw) {
        try {
            scan = new Scanner(userFile); // Use the specified file
            if (!scan.hasNextLine()) {
                return false; // Empty file, no users
            }
            String[] user;
            while (scan.hasNextLine()) {
                user = scan.nextLine().split(";");
                if (user[0].equalsIgnoreCase(un) && user[1].equals(pw))
                    return true;
            }
        } catch (Exception e) {
            System.out.println("ERROR: Validating User... " + e.getMessage());
            // Optional: Handle the exception or retry logic
        } finally {
            if (scan != null) {
                scan.close();
            }
        }
        return false;
    }

    // Getters for username and password, if needed
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
