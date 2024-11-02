import java.io.File;
import java.io.PrintWriter;
import java.rmi.server.ExportException;
import java.util.Scanner;

/**
 * This class represents a login system for a pharmacy application.
 */
public class Login {
    private String username;
    private String password;
    private Scanner scan = new Scanner(System.in);

    /**
     * Constructs a Login object with the given username and password.
     *
     * @param username The username for the login.
     * @param password The password for the login.
     */
    public Login(String username, String password){
       this.username = username;
       this.password = password;
    }

    /**
     * Validates a user by checking if the given username and password match the ones stored in the "users.txt" file.
     *
     * @param un The username to validate.
     * @param pw The password to validate.
     * @return True if the username and password match, false otherwise.
     */
    public boolean validateUser(String un,String pw){

        try {
            scan = new Scanner(new File("users.txt"));
            if(!scan.hasNextLine()){
                return false;
            }
            String[] user;
            while(scan.hasNextLine()){
               user= scan.nextLine().split(";");
               if (user[0].equalsIgnoreCase(un)&&user[1].equals(pw))
                   return true;
            }
            scan.close();
        }catch (Exception e){
            System.out.println("ERROR: Validating User..."+e.getMessage());
            new PharmacyController();
        }
        return false;
    }
}