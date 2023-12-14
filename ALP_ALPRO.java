package FORALP;

import java.util.ArrayList;
import java.util.Scanner;

public class ALP_ALPRO {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String inp_name = "", inp_unm = "", inp_pass = "";
        ArrayList<String> username = new ArrayList<String>();
        ArrayList<String> password = new ArrayList<String>();
        ArrayList<String> fullname = new ArrayList<String>();

        System.out.println("===========================");
        System.out.println("====== W E L C O M E ======");
        System.out.println("==== C U A N - C O S T ====");
        System.out.println("=========================== \n");

        System.out.println("===========================");
        System.out.println("== 1. Regitration        ==");
        System.out.println("== 2. Login              ==");
        System.out.println("== 3. Exit               ==");
        System.out.println("===========================");
        System.out.println("Choose : ");
        int pick = scan.nextInt();

        switch (pick) {
            case 1:
                register(scan, username, password, fullname);
                break;
                
        }
    }

    public static void register(Scanner scan, ArrayList<String> username, ArrayList<String> password, ArrayList<String> fullname) {
        String inp_unm, inp_pass, inp_name;
        boolean isRegister = false;
        System.out.println("===================");
        System.out.println("=== H E L L O ! ===");
        System.out.println("===================");
        System.out.println("Please input your :");
        do {
            System.out.println("Username : ");
            inp_unm = scan.next();
            System.out.println("Password : ");
            inp_pass = scan.next();

            scan.nextLine();
            System.out.println("Full Name : ");
            inp_name = scan.nextLine();

            if (username.contains(inp_unm)) {
                System.out.println("Username sudah ada. Silakan masukkan username lain.");

            } else {
                username.add(inp_unm);
                password.add(inp_pass);
                fullname.add(inp_name);
                isRegister = true;
                break;
            }
            
            if (password.contains(inp_pass)) {
                
            }
        } while (!isRegister);
        return;
    }

    //        System.out.println("Full Name : ");
    //        inp_name = scan.next();
    //        fullname.add(inp_name);
    //        
    //        System.out.println("Username : ");
    //        inp_unm = scan.next();
    //        username.add(inp_unm);
    //        
    //        System.out.println("Password : ");
    //        inp_pass = scan.next();
    //        password.add(inp_pass);
}
