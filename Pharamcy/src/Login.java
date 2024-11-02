import java.io.File;
import java.io.PrintWriter;
import java.rmi.server.ExportException;
import java.util.Scanner;

public class Login {
    private String username;
    private String password;
    private Scanner scan = new Scanner(System.in);
    public Login(String username, String password){
       this.username = username;
       this.password = password;
    }

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