import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Register {
    private String username;
    private String password;

    public Register(){
        // Prompt user to enter username and password during registration
        System.out.print("Enter username: ");
        username = scan.nextLine();
        setUsername(username);  // Validate and set username
        System.out.print("Enter Password: ");
        password = scan.nextLine();
        setPassword(password);  // Validate and set password
        saveUser(username, password);  // Save the user to file
    }

    public Register(String un, String pw){
        // Overloaded constructor to register with specified username and password
        setUsername(un);
        setPassword(pw);
        saveUser(username, password);
    }

    // Scanner object for user input
    Scanner scan = new Scanner(System.in);

    public void setUsername(String un){
        un = un.trim();  // Trim any leading or trailing spaces
        // Loop until a valid username is entered
        do {
            if (un.length() < 3) {  // Username must be at least 3 characters
                System.out.println("ERROR Invalid username!" +
                        "\n(A valid username consists of at least 3 characters)");
            } else if (alreadyTaken(un)) {  // Check if username already exists
                System.out.println(un + " is already taken. Try another one!");
            } else {
                username = un;
                break;  // Exit loop if valid username is set
            }
            System.out.print("\nEnter your username: ");
            un = scan.nextLine();
        } while (true);
    }

    public void setPassword(String pw){
        pw = pw.trim();  // Trim any leading or trailing spaces
        // Loop until a valid password is entered
        do {
            if (pw.length() < 5) {  // Password must be at least 5 characters
                System.out.println("ERROR Invalid Password!" +
                        "\n(A valid password consists of at least 5 characters)");
            } else {
                password = pw;
                break;  // Exit loop if valid password is set
            }
            System.out.print("\nEnter your password: ");
            pw = scan.nextLine();
        } while (true);
    }

    public String getUsername() {
        return username;  // Return the username
    }

    public String getPassword() {
        return password;  // Return the password
    }

    // Method to save the registered user's data in a file
    private void saveUser(String un, String pw) {
        try {
            FileWriter filename = new FileWriter("users.txt", true);  // Open file in append mode
            PrintWriter writer = new PrintWriter(filename);
            writer.println(un + ";" + pw);  // Save username and password separated by a semicolon
            System.out.println("Registered Successfully");
            writer.close();  // Close file writer
        } catch (Exception e) {
            System.out.println("Error: Registration failed. Try again.");
            new PharmacyController();  // Restart registration in case of failure
        }
    }

    // Checks if the entered username is already in use
    private boolean alreadyTaken(String un) {
        boolean found = false;
        try {
            Scanner scan = new Scanner(new File("users.txt"));
            // If file is empty, no users exist
            if (!scan.hasNextLine()) {
                return false;
            }
            scan.nextLine();  // Skip file header line, if any
            String[] line;
            // Loop through each line to check if username is already taken
            while (scan.hasNextLine()) {
                line = scan.nextLine().split(";");  // Split line into username and password
                if (un.equalsIgnoreCase(line[0])) {  // Compare ignoring case
                    found = true;
                    break;  // Stop searching if username is found
                }
            }
            scan.close();  // Close the file scanner
        } catch (Exception e) {
            System.err.println("ERROR occurred. Please try again later...");
            new PharmacyController();  // Restart in case of error
        }

        return found;
    }
}