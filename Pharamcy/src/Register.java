import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Register {
    private String username;
    private String password;
    private Scanner inputScanner;
    private File userFile;

    // Constructor for normal use (no arguments)
    public Register() {
        this.inputScanner = new Scanner(System.in); // Use System.in for user input
        this.userFile = new File("users.txt");     // Use default file
    }

    // Constructor for testing (arguments for testability)
    public Register(Scanner inputScanner, File userFile) {
        this.inputScanner = inputScanner;
        this.userFile = userFile;
    }

    public void startRegistration() {
        System.out.print("Enter username: ");
        setUsername(inputScanner.nextLine());
        System.out.print("Enter Password: ");
        setPassword(inputScanner.nextLine());
        saveUser(username, password);
    }

    public void setUsername(String un) {
        un = un.trim();
        while (true) {
            if (un.length() < 3) {
                System.out.println("ERROR Invalid username!" +
                        "\n(A valid username consists of at least 3 characters)");
            } else if (alreadyTaken(un)) {
                System.out.println(un + " is already taken. Try another one!");
            } else {
                this.username = un;
                return;
            }
            System.out.print("Enter your username: ");
            un = inputScanner.nextLine();
        }
    }

    public void setPassword(String pw) {
        pw = pw.trim();
        while (true) {
            if (pw.length() < 5) {
                System.out.println("ERROR Invalid Password!" +
                        "\n(A valid password consists of at least 5 characters)");
                System.out.print("Enter your password: ");
                pw = inputScanner.nextLine();
            } else {
                this.password = pw;
                return;
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private void saveUser(String un, String pw) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(userFile, true))) {
            writer.println(un + ";" + pw);
            System.out.println("Registered Successfully");
        } catch (Exception e) {
            System.out.println("Error: Registration failed. Try again.");
        }
    }

    private boolean alreadyTaken(String un) {
        try (Scanner fileScanner = new Scanner(userFile)) {
            while (fileScanner.hasNextLine()) {
                String[] line = fileScanner.nextLine().split(";");
                if (un.equalsIgnoreCase(line[0])) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading file. Assuming username is available.");
        }
        return false;
    }
}
