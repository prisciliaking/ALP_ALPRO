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
                    register(); //manggil function regis
                    break;
                case 2:
                    login(); //manggil function login
                    break;
                case 3:
                    System.out.println("Thankyou for coming :) !"); //out lgsg bre
                    System.exit(0);
            }
        } while (pick != 3); //ngulang pokokny kl gk 1 2 3
    }

    public static void register() {
        String inp_unm, inp_pass, inp_name;
        // penggunaan boolean ini buat ngecek kalau inputan user itu udah bener atau belum
        // mirip buat penginisialisasian doang trus ngecek
        boolean unm_valid = false, pass_valid = false;
        System.out.println("===================\n==== WELCOME ! ====\n===================\n");
        System.out.print("Full Name : ");
        inp_name = scan.next() + scan.nextLine();
        do {
            System.out.print("Username : ");
            inp_unm = scan.next();

            //to check if the username already input or not
            if (username.contains(inp_unm)) {
                System.out.println("Username sudah ada. Silakan masukkan username lain.");
                return;
            }
            if (!username.contains(inp_unm)) { //if username array not contain the input's
                unm_valid = true;
                break;
            }
        } while (!unm_valid);

        do {
            System.out.print("Password : ");
            inp_pass = scan.next();
            //ini namanya regular expression (regex).
            // regex is use for = 
            if (inp_pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")) {
                fullname.add(inp_name); //.add ini biar inputan masuk ke array
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
        System.out.println("===================\n=== H E L L O ! ===\n===================");
        do {
            System.out.print("Username : ");
            System.out.println("\n=========== \nForgot username? type exit");
            unm = scan.next();
            for (int i = 0; i < username.size(); i++) { //cek username di dalam array index ke berpa
                if (username.get(i).toString().equals(unm)) { 
                    //ini ngecek unm ada sama gk dgn array username
                    check_index = i; //for save the index if ada
                    unm_true = true;
                    break;
                }
            }
            if (unm_true == false) { // if unm is wrong
                System.out.println("Username invalid ! \n");
                //kalau ini invalid bakalan ngulang ngisi username lagi
            }
            if (unm.equalsIgnoreCase("exit")) {
                return; //return ini bikin langsung balik ke menu
            }
        } while (!unm_true);

        do {
            System.out.print("Password : ");
            System.out.println("\n========== \nForgot or Reset password? type exit or reset");
            pass = scan.next();

            for (int j = 0; j < password.size(); j++) {
                if (password.get(j).toString().equals(pass)) {
                    //buat ngecek inputan pass ada smaa gk dgn array password
                    check_index = j; //for save the index if ada
                    pass_true = true;
                    System.out.println("Welcome, " + fullname.get(check_index) + " !");
                    //fullname ini buat manggil nama diindex yg udh disimpen
                    break;
                }
            }
            if (pass.equalsIgnoreCase("reset")) {
                resetPass();
                return; //ini biar passwordnya ke reset 
            }
            if (pass.equalsIgnoreCase("exit")) {
                return; //langsung balik ke menu
            }
            if (pass_true == false) { //if pass is wrong
                System.out.println("Password invalid ! \n");
            }
        } while (!pass_true);
    }

    public static void resetPass() {
        String reset, newpass;
        boolean reset_valid = false, newpass_valid = false;
        do {
            System.out.print("\nEnter the username : ");
            reset = scan.next();
            for (int k = 0; k < username.size(); k++) {
                if (username.get(k).equals(reset)) {
                    do {
                        System.out.print("\nReset password : ");
                        newpass = scan.next();
                        if (newpass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")) {
                            newpass_valid = true;
                            password.set(k, newpass);
                            System.out.println("\nSuccessfully ! Please login again.\n");
                            return; //biar naik kepilihan menu lagi
                        } else {
                            System.out.println("Password must atleast contain 1 lowercase, 1 uppercase, and 1 digit");
                        }
                    } while (!newpass_valid); // do while disni biar klau pass slah lgsg ngulang
                } else {
                    System.out.println("Username not found! Please input the right one."); //ngulang isi username
                }
            }
        } while (!reset_valid);

    }
}
