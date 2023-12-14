package afl_alpro;

import java.util.ArrayList;
import java.util.Scanner;

public class AFL_ALPRO {

    // semua ini disimpen ke private static biar langsung kesimpen jadi 1, jadi nanti di function lainny kalau mau
    // dipake gk ush di declare/dipanggil lagi kedepannya krn udh ke simpen deluan sejak awal
    private static ArrayList<String> username = new ArrayList<String>();
    private static ArrayList<String> password = new ArrayList<String>();
    private static ArrayList<String> fullname = new ArrayList<String>();
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        int pick;
        String inp_name = "", inp_unm = "", inp_pass = "";
        do {
            System.out.println("===========================");
            System.out.println("====== W E L C O M E ======");
            System.out.println("==== C U A N - C O S T ====");
            System.out.println("=========================== \n");
            System.out.println("===========================");
            System.out.println("== 1. Regitration        ==");
            System.out.println("== 2. Login              ==");
            System.out.println("== 3. Exit               ==");
            System.out.println("===========================");
            System.out.print("Choose : ");
            pick = scan.nextInt();

            switch (pick) {
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Thankyou for coming :)");
                    System.exit(0);

            }
        } while (pick != 3);
    }

    public static void register() {
        String inp_unm, inp_pass, inp_name;
        // penggunaan boolean ini buat ngecek kalau inputan user itu udah bener atau belum
        // mirip buat penginisialisasian doang trus ngecek
        boolean unm_valid = false, pass_valid = false;
        System.out.println("===================");
        System.out.println("==== WELCOME ! ====");
        System.out.println("===================");
        System.out.println("Please input your :");
        System.out.print("Masukkan Nama :");
        inp_name = scan.nextLine() + scan.next();
        do {
            System.out.print("Username : ");
            inp_unm = scan.nextLine() + scan.next();

            //to check if the username already input or not
            if (username.contains(inp_unm)) {
                System.out.println("Username sudah ada. Silakan masukkan username lain.");
                return;
            } else { //if username array not contain the input's
                unm_valid = true;
            }
        } while (!unm_valid);

        do {
            System.out.print("Password : ");
            inp_pass = scan.next();

            //ini namanya regular expression (regex).
            // regex is use for = 
            if (inp_pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")) {
                fullname.add(inp_name);
                username.add(inp_unm);
                password.add(inp_pass);
                pass_valid = true;
                System.out.println("You've register, " + inp_name + " ! Please login to your account");
            } else {
                System.out.println("Password must atleast contain 1 lowercase, 1 uppercase, and 1 digit");
            }
        } while (!pass_valid);
        return;
    }

    public static void login() {
        boolean unm_true = false, pass_true = false;
        String unm, pass, name;
        int check_index;
        System.out.println("===================");
        System.out.println("=== H E L L O ! ===");
        System.out.println("===================");

        do {
            System.out.print("Username : ");
            System.out.println("Forgot username? type exit");
            unm = scan.next();

//            if (unm.equalsIgnoreCase("exit")) {
//                // ini aku mau dia balik ke menu langsung
//            }
            for (int i = 0; i < username.size(); i++) { //cek username di dalam array index ke berpa
                if (username.get(i).toString().equals(unm)) {
                    check_index = i; //for save the index if ada
                    unm_true = true;
                    break;
                }
            }
            if (unm_true == false) { // if unm is wrong
                System.out.println("Username invalid !");
                break;
            }
        } while (!unm_true);

        do {
            System.out.print("Password : ");
            System.out.println("Forgot password? type exit");
            pass = scan.next();

//            if () {
//                ini aku mau dia balik ke menu atau kalau bisa dia reset password doang
//            }
            for (int j = 0; j < password.size(); j++) {
                if (password.get(j).toString().equals(pass)) {
                    check_index = j; //for save the index if ada
                    pass_true = true;
                }
            }
            if (pass_true == false) { //if pass is wrong
                System.out.println("Password invalid !");
                break;
            }
        } while (!pass_true);
    }
}
