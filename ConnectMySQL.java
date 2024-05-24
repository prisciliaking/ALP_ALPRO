package ConnextMySQL;

import java.sql.Connection; //konekin sqlnya
import java.sql.DriverManager; // ambi
import java.sql.SQLException; //
import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConnectMySQL {

    private static Scanner scan = new Scanner(System.in);
    private static Connection con = null;
    private static boolean login = false;
    private static String fullname = "";
    private static int uid = 0;
    // =====================================================================
    // ini biar nanti pas di output bkalan jadi rupiah indonesia ada titik komanya
    private static Locale indonesia = new Locale("id", "ID");
    private static NumberFormat rp = NumberFormat.getCurrencyInstance(indonesia);

    // =====================================================================
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        String url = "jdbc:mysql://localhost:3306/cuancost";
        String username = "root";
        String password = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
            // jadi ini nanti bkalan search from username
            pst = con.prepareStatement("SELECT * FROM users");
            rs = pst.executeQuery();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectMySQL.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(ConnectMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        main();
    }

    private static void main() throws SQLException {
        int pick;
        if (login) {
            isLogin();
        } else {
            do {
                System.out.println("===========================");
                System.out.println("====== W E L C O M E ======");
                System.out.println("==== C U A N - C O S T ====");
                System.out.println("=========================== \n");
                System.out.println("===========================");
                System.out.println("== 1. Registration       ==");
                System.out.println("== 2. Login              ==");
                System.out.println("== 3. Exit               ==");
                System.out.println("===========================");
                System.out.print("Choose : ");
                pick = scan.nextInt();

                switch (pick) {
                    case 1:
                        register(); // manggil function regis
                        break;

                    case 2:
                        login(); // manggil function login
                        //GuideBook();
                        isLogin();
                        break;

                    case 3:
                        System.out.println("Thankyou for coming :) !"); // out lgsg bre
                        System.exit(0);
                }
            } while (pick != 3); // ngulang pokokny kl gk 1 2 3
        }

    }
    private static void isLogin() throws SQLException {
        int pick;
        // ==================================================================
        System.out.println("===========================");
        System.out.println("== 1. Mencari Harga Jual ==");
        System.out.println("== 2. History Bahan      ==");
        System.out.println("== 3. Edit Harga         ==");
        System.out.println("== 4. Logout             ==");
        System.out.println("===========================");
        System.out.print("Choose : ");
        pick = scan.nextInt();
        // ===================================================================
        switch (pick) {
            case 1:
                jmlhPorsi();
                varCost();
                fixCost();

                double totalBaku = totalBaku();
                double totalKerja = totalKerja();
                double totalLain = totalLain();
                double totalVariabel = totalVariabel(totalBaku, totalKerja, totalLain);
                double pricePerProduct = pricePerProduct(totalVariabel);
                double totalFixed = totalFixed();
                int fixPorsi = fixPorsi();
                double hargaPP = hargaPP(totalVariabel, pricePerProduct, totalFixed);

                double perUnit = perUnit(hargaPP, fixPorsi);

                float persenUntung = persenUntung();

                double perProduct = perProduct(persenUntung, perUnit);

                double totjualProduct = totjualProduct(perProduct);

                double marginContribution = marginContribution(totjualProduct, pricePerProduct);

                double breakEQ = breakEQ(totalFixed, marginContribution);

                breakEP(totjualProduct, breakEQ);

                isLogin();
                break;
            case 2:
                listVarCost();
                listFixCost();
                break;
            case 3:
                editVarFix();
                break;
            case 4:
                login = false;
                main();
                break;

        }
    }

    private static void register() throws SQLException {
        String inp_unm, inp_pass, inp_name, inp_bsname;
        // penggunaan boolean ini buat ngecek kalau inputan user itu udah bener atau
        // belum
        // mirip buat penginisialisasian doang trus ngecek
        boolean unm_valid = false, pass_valid = false, bsname_valid = false;
        System.out.println("===================\n==== WELCOME ! ====\n===================\n");
        System.out.print("Full Name : ");
        inp_name = scan.next() + scan.nextLine();
        do {
            System.out.print("Username : ");
            inp_unm = scan.next().toLowerCase();
            // to check if the username already input or not
            if (usernameExists(inp_unm)) {
                System.out.println("Username sudah ada. Silakan masukkan username lain.");
            }
            if (!usernameExists(inp_unm)) {
                unm_valid = true;
                break;
            }
        } while (!unm_valid);

        do {
            System.out.print("Password : ");
            inp_pass = scan.next();
            // ini namanya regular expression (regex).
            // regex is use for =
            if (inp_pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")) {
                pass_valid = true;
                System.out.println("You've register, " + inp_name + "! Please login to your account");
            } else {
                System.out.println("Password must atleast contain 1 lowercase, 1 uppercase, and 1 digit");
            }
        } while (!pass_valid);

        do {
            System.out.println("\nInsert your Business name : ");
            inp_bsname = scan.next() + scan.nextLine().toLowerCase();

            if (bussnameExists(inp_bsname)) {
                System.out.println("The Business name already exist. \nPlease input another one !");
            }
            if (!bussnameExists(inp_bsname)) {
                bsname_valid = true;

                String sql = "INSERT INTO users (id, fullname, username, password, bussname) VALUES (NULL, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, inp_name);
                    ps.setString(2, inp_unm);
                    ps.setString(3, inp_pass);
                    ps.setString(4, inp_bsname);
                    ps.executeUpdate();
                    System.out.println("User registered successfully. Welcom!");
                } catch (SQLException e) {
                    System.out.println("Registration failed.");
                }
                break;
            }
        } while (!bsname_valid);
    }

    private static boolean usernameExists(String data) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, data);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        }
        return false;
    }

    private static boolean bussnameExists(String data) throws SQLException {
        String sql = "SELECT * FROM users WHERE bussname = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, data);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        }
        return false;
    }

    private static void login() {
        String unm, pass, name;
        boolean login_valid = false;
        System.out.println("===================\n=== H E L L O ! ===\n===================");
        do {
            System.out.print("Username : ");
            unm = scan.next().toLowerCase();
            System.out.print("Password : ");
            pass = scan.next();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, unm);
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Login successful!");
                    login = true;
                    login_valid = true;
                    fullname = rs.getString("fullname");
                    uid = rs.getInt("id");
                    System.out.println("\nWelcome, " + fullname + ". Let's start!\n");
                } else {
                    System.out.println("Invalid username or password.");
                    login_valid = false;
                }
            } catch (SQLException e) {
                System.out.println("Login failed.");
            }
        } while (!login_valid);
    }

    // =================================================================
    //fitur pertama
    private static Integer jmlhPorsi() throws SQLException {
        int jmlhPorsi = 0;
        boolean Porsi_valid = false;

        do {
            try {
                System.out.println("===========================");
                System.out.println("Untuk berapa porsi? ");
                jmlhPorsi = scan.nextInt();
                System.out.println("===========================");

                if (jmlhPorsi <= 0) {
                    System.out.println("You need to add portion\n");
                    continue; // Ask for input again
                }
                String sql_porsi = "INSERT INTO fixporsi (id, userid, ttlporsi) VALUES (NULL, ?, ?)";
                try (PreparedStatement insPS_porsi = con.prepareStatement(sql_porsi)) {
                    insPS_porsi.setInt(1, uid);
                    insPS_porsi.setInt(2, jmlhPorsi);
                    insPS_porsi.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Gagal");
                }
                Porsi_valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Input invalid. Please re-enter a valid number");
                scan.nextLine(); // Clear the input buffer
            }
        } while (!Porsi_valid);
        return 0;
    }

    private static void varCost() throws SQLException {
        String namaBhnBaku, ans, namaTngKrj, namaByLain;
        String namebaku = "";
        double hrgBhnBaku = 0, hrgTngKrj = 0, hrgByLain = 0;
        boolean BhnBaku_valid = false, TngKrj_valid = false, ByLain_valid = false;
        boolean yesnoBaku_valid = false, yesnoKerja_valid = false, yesnoLainnya_valid = false;

        do {
            String selsql_baku, inpsql_baku, sql_baku;
            ResultSet rs_baku;
            double curPrice_baku, upPrice_baku;
            // buat bikin bahan baku
            System.out.println("=== Masukkan Bahan Baku ===");
            scan.nextLine();
            System.out.print("Nama : ");
            namaBhnBaku = scan.nextLine();

            sql_baku = "SELECT * FROM bahanbaku WHERE namebaku = ?";
            try (PreparedStatement ps_baku = con.prepareStatement(sql_baku)) {
                ps_baku.setString(1, namaBhnBaku);
                rs_baku = ps_baku.executeQuery();
                if (rs_baku.next()) {
                    namebaku = rs_baku.getString("namebaku");
                    if (namaBhnBaku.equalsIgnoreCase(namebaku)) {
                        // kalau misal nama bahan baku sudah ada dalam inputan
                        while (!BhnBaku_valid) {
                            try {
                                System.out.print("Harga : Rp ");
                                hrgBhnBaku = scan.nextDouble();
                                if (hrgBhnBaku <= 0) {
                                    System.out.println("Can't be 0 or negative!\n");
                                    continue;
                                }
                                // buat ngambil di set sesuai nama bahanbaku harga terkini trus ditambah sama harga yang baru diinput
                                curPrice_baku = rs_baku.getDouble("pricebaku");
                                upPrice_baku = curPrice_baku + hrgBhnBaku;

                                selsql_baku = "UPDATE bahanbaku SET pricebaku = ? WHERE namebaku = ?";
                                try (PreparedStatement upPS_baku = con.prepareStatement(selsql_baku)) {
                                    //abis itu bakaan di set dalam databaseny berdasarkan index
                                    upPS_baku.setDouble(1, upPrice_baku);
                                    upPS_baku.setString(2, namaBhnBaku);
                                    upPS_baku.executeUpdate();
                                    BhnBaku_valid = true;
                                } catch (SQLException e) {
                                    System.out.println("Gagal");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input! try again.\n");
                                scan.nextLine();
                            }
                        }
                    } else {
                        // kalau nama input belum ada, bklan set ke index baru
                        while (!BhnBaku_valid) {
                            try {
                                System.out.print("Harga : Rp ");
                                hrgBhnBaku = scan.nextDouble();
                                if (hrgBhnBaku <= 0) {
                                    System.out.println("Can't be 0 or negative!\n");
                                    continue;
                                }
                                inpsql_baku = "INSERT INTO bahanbaku (id, userid, namebaku, pricebaku) VALUES (NULL, ?, ?, ?)";
                                try (PreparedStatement insPS_baku = con.prepareStatement(inpsql_baku)) {
                                    insPS_baku.setInt(1, uid);
                                    insPS_baku.setString(2, namaBhnBaku);
                                    insPS_baku.setDouble(3, hrgBhnBaku);
                                    insPS_baku.executeUpdate();
                                    BhnBaku_valid = true;
                                } catch (SQLException e) {
                                    System.out.println("Gagal");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input! try again.\n");
                                scan.nextLine();
                            }
                        }
                    }
                } else {
                    System.out.println("error");
                }

                do {
                    System.out.println("Masih ada inputan? y/n");
                    ans = scan.next();
                    if (ans.equalsIgnoreCase("y")) {
                        BhnBaku_valid = false;
                        yesnoBaku_valid = true;
                    } else if (ans.equalsIgnoreCase("n")) {
                        BhnBaku_valid = true;
                        yesnoBaku_valid = false;
                        break;
                    } else {
                        System.out.println("Invalid input! Please enter 'y' or 'n'.");
                        yesnoBaku_valid = false;
                    }
                } while (!yesnoBaku_valid);
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } while (!BhnBaku_valid);

        do {// buat tenaga kerja
            String selsql_kerja, inpsql_kerja, sql_kerja;
            ResultSet rs_kerja; //why initialize?
            double curPrice_kerja, upPrice_kerja;
            // ===================================================
            System.out.println("\n=== Masukkan Tenaga Kerja ===");
            scan.nextLine();
            System.out.print("Nama  : ");
            namaTngKrj = scan.nextLine();

            sql_kerja = "SELECT * FROM tenagakerja WHERE namekerja = ?";
            try (PreparedStatement ps_kerja = con.prepareStatement(sql_kerja)) {
                ps_kerja.setString(1, namaTngKrj);
                rs_kerja = ps_kerja.executeQuery();

                String namekerja = rs_kerja.getString("namekerja");

                if (namekerja.equalsIgnoreCase(namaTngKrj)) {
                    while (!TngKrj_valid) {
                        try {
                            System.out.print("Harga : Rp ");
                            hrgTngKrj = scan.nextDouble();
                            if (hrgTngKrj <= 0) {
                                System.out.println("Can't be 0 or negative!\n");
                                continue;
                            }
                            // buat ngambil di set sesuai nama tenagakerja harga terkini trus ditambah sama harga yang baru diinput
                            curPrice_kerja = rs_kerja.getDouble("pricekerja");
                            upPrice_kerja = curPrice_kerja + hrgTngKrj;

                            selsql_kerja = "UPDATE tenagakerja SET pricekerja = ? WHERE namekerja = ?";
                            try (PreparedStatement upPS_kerja = con.prepareStatement(selsql_kerja)) {
                                //abis itu bakalan di set dalam databaseny berdasarkan index
                                upPS_kerja.setDouble(1, upPrice_kerja);
                                upPS_kerja.setString(2, namaTngKrj);
                                upPS_kerja.executeUpdate();
                                TngKrj_valid = true;
                            } catch (SQLException e) {
                                System.out.println("Gagal");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! try again.\n");
                            scan.nextLine();
                        }
                    }
                } else {
                    // kalau nama input belum ada, bklan set ke index baru
                    while (!TngKrj_valid) {
                        try {
                            System.out.print("Harga : Rp ");
                            hrgTngKrj = scan.nextDouble();
                            if (hrgTngKrj <= 0) {
                                System.out.println("Can't be 0 or negative!\n");
                                continue;
                            }
                            inpsql_kerja = "INSERT INTO tenagakerja (id, userid, namekerja, pricekerja) VALUES (NULL, ?, ?, ?)";
                            try (PreparedStatement insPS_kerja = con.prepareStatement(inpsql_kerja)) {
                                insPS_kerja.setInt(1, uid);
                                insPS_kerja.setString(2, namaTngKrj);
                                insPS_kerja.setDouble(3, hrgTngKrj);
                                insPS_kerja.executeUpdate();
                                TngKrj_valid = true;
                            } catch (SQLException e) {
                                System.out.println("Gagal");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! try again.\n");
                            scan.nextLine();
                        }
                    }
                }
                do {
                    System.out.println("Masih ada inputan? y/n");
                    ans = scan.next();
                    if (ans.equalsIgnoreCase("y")) {
                        TngKrj_valid = false;
                        yesnoKerja_valid = true;
                    } else if (ans.equalsIgnoreCase("n")) {
                        TngKrj_valid = true;
                        yesnoKerja_valid = false;
                        break;
                    } else {
                        System.out.println("Invalid input! Please enter 'y' or 'n'.");
                        yesnoKerja_valid = false;
                    }
                } while (!yesnoKerja_valid);

            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } while (!TngKrj_valid);

        do { // buat biaya lain lain
            String selsql_lain, inpsql_lain, sql_lain;
            ResultSet rs_lain;
            double curPrice_lain, upPrice_lain;
            // ===================================================
            System.out.println("\n=== Biaya Lain-Lain ===");
            scan.nextLine();
            System.out.print("Nama : ");
            namaByLain = scan.nextLine();

            sql_lain = "SELECT * FROM biayalain WHERE namelain = ?";
            try (PreparedStatement ps_lain = con.prepareStatement(sql_lain)) {
                ps_lain.setString(1, namaByLain);
                rs_lain = ps_lain.executeQuery();

                String namelain = rs_lain.getString("namelain");
                if (namelain.equalsIgnoreCase(namaByLain)) {
                    while (!ByLain_valid) {
                        try {
                            System.out.print("Harga : Rp ");
                            hrgByLain = scan.nextDouble();
                            if (hrgByLain <= 0) {
                                System.out.println("Can't be 0 or negative!\n");
                                continue;
                            }
                            // buat ngambil di set sesuai nama tenagakerja harga terkini trus ditambah sama harga yang baru diinput
                            curPrice_lain = rs_lain.getDouble("pricelain");
                            upPrice_lain = curPrice_lain + hrgByLain;

                            selsql_lain = "UPDATE biayalain SET pricelain = ? WHERE namelain = ?";
                            try (PreparedStatement upPS_lain = con.prepareStatement(selsql_lain)) {
                                //abis itu bakalan di set dalam databaseny berdasarkan index
                                upPS_lain.setDouble(1, upPrice_lain);
                                upPS_lain.setString(2, namaByLain);
                                upPS_lain.executeUpdate();
                                ByLain_valid = true;
                            } catch (SQLException e) {
                                System.out.println("Gagal");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! try again.\n");
                            scan.nextLine();
                        }
                    }
                } else {
                    // kalau nama input belum ada, bklan set ke index baru
                    while (!ByLain_valid) {
                        try {
                            System.out.print("Harga : Rp ");
                            hrgByLain = scan.nextDouble();
                            if (hrgByLain <= 0) {
                                System.out.println("Can't be 0 or negative!\n");
                                continue;
                            }
                            inpsql_lain = "INSERT INTO biayalain (id, userid, namelain, pricelain) VALUES (NULL, ?, ?, ?)";
                            try (PreparedStatement insPS_lain = con.prepareStatement(inpsql_lain)) {
                                insPS_lain.setInt(1, uid);
                                insPS_lain.setString(2, namaByLain);
                                insPS_lain.setDouble(3, hrgByLain);
                                insPS_lain.executeUpdate();
                                ByLain_valid = true;
                            } catch (SQLException e) {
                                System.out.println("Gagal");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! try again.\n");
                            scan.nextLine();
                        }
                    }
                }
                do {
                    System.out.println("Masih ada inputan? y/n");
                    ans = scan.next();
                    if (ans.equalsIgnoreCase("y")) {
                        ByLain_valid = false;
                        yesnoLainnya_valid = true;
                    } else if (ans.equalsIgnoreCase("n")) {
                        ByLain_valid = true;
                        yesnoLainnya_valid = false;
                        break;
                    } else {
                        System.out.println("Invalid input! Please enter 'y' or 'n'.");
                        yesnoLainnya_valid = false;
                    }
                } while (!yesnoLainnya_valid);
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } while (!ByLain_valid);

    }

    private static void fixCost() throws SQLException {
        String namaFixCost, ans;
        double hrgFixCost;
        boolean FixedCost_valid = false, yesnoFixed_valid = false;
        System.out.println("=== Masukkan Fixed Cost (Biaya tetap) ===");
        do { // buat biaya lain lain
            String selsql_fix, inpsql_fix, sql_fix;
            ResultSet rs_fix;
            double curPrice_fix, upPrice_fix;
            // ===================================================
            scan.nextLine();
            System.out.print("Nama : ");
            namaFixCost = scan.nextLine();

            sql_fix = "SELECT * FROM fixcost WHERE namefix = ?";
            try (PreparedStatement ps_fix = con.prepareStatement(sql_fix)) {
                ps_fix.setString(1, namaFixCost);
                rs_fix = ps_fix.executeQuery();

                String namefix = rs_fix.getString("namebaku");
                if (namefix.equalsIgnoreCase(namaFixCost)) {
                    while (!FixedCost_valid) {
                        try {

                            System.out.print("Harga : Rp ");
                            hrgFixCost = scan.nextDouble();
                            if (hrgFixCost <= 0) {
                                System.out.println("Can't be 0 or negative!\n");
                                continue;
                            }
                            // buat ngambil di set sesuai nama tenagakerja harga terkini trus ditambah sama harga yang baru diinput
                            curPrice_fix = rs_fix.getDouble("pricefix");
                            upPrice_fix = curPrice_fix + hrgFixCost;

                            selsql_fix = "UPDATE fixcost SET pricefix = ? WHERE namefix = ?";
                            try (PreparedStatement upPS_fix = con.prepareStatement(selsql_fix)) {
                                //abis itu bakalan di set dalam databaseny berdasarkan index
                                upPS_fix.setDouble(1, upPrice_fix);
                                upPS_fix.setString(2, namaFixCost);
                                upPS_fix.executeUpdate();
                                FixedCost_valid = true;
                            } catch (SQLException e) {
                                System.out.println("Gagal");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! try again.\n");
                            scan.nextLine();
                        }
                    }
                } else {
                    // kalau nama input belum ada, bklan set ke index baru
                    while (!FixedCost_valid) {
                        try {
                            System.out.print("Harga : Rp ");
                            hrgFixCost = scan.nextDouble();
                            if (hrgFixCost <= 0) {
                                System.out.println("Can't be 0 or negative!\n");
                                continue;
                            }

                            inpsql_fix = "INSERT INTO fixcost (id, userid, namefix, pricefix) VALUES (NULL, ?, ?, ?)";
                            try (PreparedStatement insPS_fix = con.prepareStatement(inpsql_fix)) {
                                insPS_fix.setInt(1, uid);
                                insPS_fix.setString(2, namaFixCost);
                                insPS_fix.setDouble(3, hrgFixCost);
                                insPS_fix.executeUpdate();
                                FixedCost_valid = true;
                            } catch (SQLException e) {
                                System.out.println("Gagal");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! try again.\n");
                            scan.nextLine();
                        }
                    }
                }
                do {
                    System.out.println("Masih ada inputan? y/n");
                    ans = scan.next();
                    if (ans.equalsIgnoreCase("y")) {
                        FixedCost_valid = false;
                        yesnoFixed_valid = true;
                    } else if (ans.equalsIgnoreCase("n")) {
                        FixedCost_valid = true;
                        yesnoFixed_valid = false;
                        break;
                    } else {
                        System.out.println("Invalid input! Please enter 'y' or 'n'.");
                        FixedCost_valid = false;
                    }
                } while (!yesnoFixed_valid);
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } while (!FixedCost_valid);
    }

    // ==================================================================
    // perhitungannya
    private static Double totalBaku() throws SQLException {
        String totalbaku = "SELECT SUM(pricebaku) FROM bahanbaku WHERE userid = ?";
        double total_Baku = 0;
        try (PreparedStatement ps_ttlbaku = con.prepareStatement(totalbaku)) {
            ps_ttlbaku.setInt(1, uid);
            try (ResultSet rs_ttlbaku = ps_ttlbaku.executeQuery()) {

                if (rs_ttlbaku.next()) {
                    return rs_ttlbaku.getDouble(1);

                }
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        return 0.0;

    }

    private static Double totalKerja() throws SQLException {
        String totalkerja = "SELECT SUM(pricekerja) FROM tenagakerja WHERE userid = ?";
        double total_Kerja = 0;
        try (PreparedStatement ps_ttlkerja = con.prepareStatement(totalkerja)) {
            ps_ttlkerja.setInt(1, uid);
            try (ResultSet rs_ttlkerja = ps_ttlkerja.executeQuery()) {

                if (rs_ttlkerja.next()) {
//                    total_Kerja = rs_ttlkerja.getDouble(1);
                    return rs_ttlkerja.getDouble(1);

                }
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        return 0.0;
    }

    private static Double totalLain() throws SQLException {
        String totallain = "SELECT SUM(pricelain) FROM biayalain WHERE userid = ?";
        double total_Lain = 0;
        try (PreparedStatement ps_ttllain = con.prepareStatement(totallain)) {
            ps_ttllain.setInt(1, uid);
            try (ResultSet rs_ttllain = ps_ttllain.executeQuery()) {

                if (rs_ttllain.next()) {
//                    total_Lain = rs_ttllain.getDouble(1);
                    return rs_ttllain.getDouble(1);

                }
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        return 0.0;
    }

    private static Double totalVariabel(double totalBaku, double totalKerja, double totalLain) {
        Double totVariabel = totalBaku + totalKerja + totalLain;
        //System.out.println(rp.format(totVariabel));
        return totVariabel;
    }

    private static Double totalFixed() throws SQLException {
        Double totFixed;

        String totalfix = "SELECT SUM(pricefix) FROM fixcost WHERE userid = ?";

        try (PreparedStatement ps_ttlfix = con.prepareStatement(totalfix)) {
            ps_ttlfix.setInt(1, uid);
            try (ResultSet rs_ttlfix = ps_ttlfix.executeQuery()) {

                if (rs_ttlfix.next()) {
                    totFixed = rs_ttlfix.getDouble(1);
                    //System.out.println("total fix : " + rp.format(totFixed));
                    return rs_ttlfix.getDouble(1);
                }
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        return 0.0;
    }

    private static Double pricePerProduct(double totalVariabel) throws SQLException {
        double perProduk;
        double porsi = 0;
        String fixPorsi = "SELECT SUM(ttlporsi) FROM fixporsi WHERE userid = ?";
        try (PreparedStatement ps_porsi = con.prepareStatement(fixPorsi)) {
            ps_porsi.setInt(1, uid);
            try (ResultSet rs_porsi = ps_porsi.executeQuery()) {
                if (rs_porsi.next()) {
                    porsi = rs_porsi.getInt(1);
                }
                //System.out.println(porsi);
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        perProduk = totalVariabel / porsi;
        //System.out.println("per produk by variabel cost : " + perProduk);

        return perProduk;
    }

    private static Integer fixPorsi() throws SQLException {
        String fixPorsi = "SELECT SUM(ttlporsi) FROM fixporsi WHERE userid = ?";
        try (PreparedStatement ps_porsi = con.prepareStatement(fixPorsi)) {
            ps_porsi.setInt(1, uid);
            try (ResultSet rs_porsi = ps_porsi.executeQuery()) {
                if (rs_porsi.next()) {
                    return rs_porsi.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        return 0;
    }

    private static Double hargaPP(double totalVariabel, double pricePerProduct, double totalFixed) {
        Double totHPP;
        System.out.println("\n===============================");
        System.out.println("Total Variable Cost : " + rp.format(totalVariabel));
        System.out.println("Harga per Produk : " + rp.format(pricePerProduct));
        System.out.println("Total Fixed Cost : " + rp.format(totalFixed));
        System.out.println("===============================");
        // harga pokok produksi
        totHPP = totalVariabel + totalFixed;
        System.out.println("Total Harga Pokok Produksi : " + rp.format(totHPP));

        return totHPP;
    }

    private static Double perUnit(Double hargaPP, Integer fixPorsi) {
        Double perUnit, hasilUnit;
        //harga pokok produksi per unit
        perUnit = hargaPP / fixPorsi;

        hasilUnit = Math.ceil(perUnit);
        System.out.println("\n Harga Pokok Produksi per Unit : " + rp.format(perUnit));

        return hasilUnit;
    }

    private static Float persenUntung() {
        float perUntung, totUntung = 0;
        boolean persen_valid = false;
        // mencari persen keuntungan
        while (!persen_valid) {
            try {
                System.out.print("\nMau untung berapa persen? (1-100) : ");
                perUntung = scan.nextFloat();
                if (perUntung < 1) {
                    System.out.println("Tidak boleh negatif!");
                    continue;
                } else if (perUntung > 100) {
                    System.out.println("Tidak boleh lebih dari 100!");
                    continue;
                } else {
                    persen_valid = true;
                }
                totUntung = (perUntung / 100);
            } catch (InputMismatchException e) {
                System.out.println("Must be number!");
            }
        }
        return totUntung;
    }

    private static Double perProduct(Float persenUntung, Double perUnit) {
        double perProduct;

        perProduct = perUnit + (perUnit * persenUntung);
        System.out.println("===============================");
        System.out.println("Harga Asli Product : " + rp.format(perProduct));
        return perProduct;
    }

    private static Double totjualProduct(Double perProduct) {
        double totjualProduct;
        int hasilProduct;

        hasilProduct = (int) Math.ceil(perProduct);
        totjualProduct = ((hasilProduct + 999) / 1000) * 1000; // to round up
        System.out.println("===============================");
        System.out.println("Harga jual produk : " + rp.format(totjualProduct));
        return totjualProduct;
    }

    private static Double marginContribution(Double totjualProduct, Double pricePerProduct) {
        double mrgCont = 0, hasilMargin;

        mrgCont = totjualProduct - pricePerProduct;
        hasilMargin = (int) Math.ceil(mrgCont);

        System.out.println("Margin kontribusi : " + rp.format(hasilMargin));
        return hasilMargin;
    }

    private static Double breakEQ(Double totalFixed, Double marginContribution) {
        double perQuantity = 0, hasilQuantity;

        perQuantity = totalFixed / marginContribution;
        hasilQuantity = (int) Math.ceil(perQuantity);

        System.out.println("Break even Quantity : " + hasilQuantity);
        return hasilQuantity;
    }

    private static Double breakEP(Double totjualProduct, Double breakEQ) {
        double perPoint = 0, hasilPoint;

        perPoint = breakEQ * totjualProduct;
        System.out.println("Break even Point : " + rp.format(perPoint) + "\n===============================\n\n");
        return perPoint;
    }
    // =======================================================================================

    // fitur kedua
    // =======================================================================================
    private static void listVarCost() throws SQLException {
        double totListBaku = 0, totListTng = 0, totListLain = 0, totListVar = 0;
        String RpListVar;

        double fixporsi = fixPorsi();
        System.out.println("Total Porsi " + fixporsi);
        System.out.println("\n=== Variabel Cost ===");
        // ==================================================================================
        System.out.println("Nama Bahan Baku :\tHarga");

        String all_baku = "SELECT * FROM bahanbaku WHERE userid = ?";
        try (PreparedStatement ps_baku = con.prepareStatement(all_baku)) {
            ps_baku.setInt(1, uid);

            try (ResultSet rs_listBaku = ps_baku.executeQuery()) {
                while (rs_listBaku.next()) {
                    String name_Baku = rs_listBaku.getString("namebaku");
                    Double price_baku = rs_listBaku.getDouble("pricebaku");
                    System.out.println(name_Baku + " : \t " + rp.format(price_baku));
                    totListBaku += price_baku;
                }
                System.out.println("====================================");
                System.out.println("Total Bahan Baku : " + rp.format(totListBaku));
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        System.out.println("====================================");
        // ==================================================================================
        System.out.println("\nNama Tenaga Kerja :\tHarga");

        String all_kerja = "SELECT * FROM tenagakerja WHERE userid = ?";
        try (PreparedStatement ps_kerja = con.prepareStatement(all_kerja)) {
            ps_kerja.setInt(1, uid);

            try (ResultSet rs_listKerja = ps_kerja.executeQuery()) {
                while (rs_listKerja.next()) {
                    String name_kerja = rs_listKerja.getString("namekerja");
                    Double price_kerja = rs_listKerja.getDouble("pricekerja");
                    System.out.println(name_kerja + " : \t " + rp.format(price_kerja));
                    totListTng += price_kerja;
                }
                System.out.println("====================================");
                System.out.println("Total Tenaga Kerja : " + rp.format(totListTng));
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
            System.out.println("====================================");
            // ==================================================================================
            System.out.println("\nNama Biaya Lainnya :\tHarga");

            String all_lain = "SELECT * FROM biayalain WHERE userid = ?";
            try (PreparedStatement ps_lain = con.prepareStatement(all_lain)) {
                ps_lain.setInt(1, uid);

                try (ResultSet rs_listlain = ps_lain.executeQuery()) {
                    while (rs_listlain.next()) {
                        String name_lain = rs_listlain.getString("namelain");
                        Double price_lain = rs_listlain.getDouble("pricelain");
                        System.out.println(name_lain + " : \t " + rp.format(price_lain));
                        totListLain += price_lain;
                    }
                    System.out.println("====================================");
                    System.out.println("Total biaya lainnya : " + rp.format(totListLain));
                }
                System.out.println("====================================");
                // =======================================================
                totListVar = (totListBaku + totListTng + totListLain);
                // =======================================================
                RpListVar = rp.format((int) totListVar);
                System.out.println("Total Variable Cost : " + RpListVar + "\n");
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
    }

    private static void listFixCost() throws SQLException {
        double totListFix = 0, price_allFix;
        String RpListFix, name_allFix, all_fix;
        System.out.println("\n=== Fixed Cost ===");
        System.out.println("Nama Biaya Fixed Cost : \t Harga");

        all_fix = "SELECT * FROM fixcost WHERE userid = ?";
        // setelah di cari dalam all_fix resultny di set
        try (PreparedStatement ps_fix = con.prepareStatement(all_fix)) {
            ps_fix.setInt(1, uid);

            try (ResultSet rs_fixcost = ps_fix.executeQuery()) {
                //INT NO = 1;
                while (rs_fixcost.next()) {
                    // disimpen dalam variabel baru
                    name_allFix = rs_fixcost.getString("namefix");
                    //   System.out.println(NO  + "name_allFix");
                    price_allFix = rs_fixcost.getDouble("pricefix");
                    System.out.println(name_allFix + " : \t " + rp.format(price_allFix));
                    totListFix += price_allFix;
                    //   NO++;
                }
                RpListFix = rp.format(totListFix);
                System.out.println("\nTotal Fixed Cost : " + RpListFix);
                isLogin();
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
    }

    // fitur ketiga
    // =======================================================================================
    private static void editBaku() throws SQLException {
        String edit_baku;
        int noBaku = 1, pilihBaku, editid_baku;
        boolean editbaku_valid = false;
        double price_editBaku = 0;

        String all_editBaku = "SELECT * FROM bahanbaku WHERE userid = ?";

        try (PreparedStatement ps_editBaku = con.prepareStatement(all_editBaku)) {
            ps_editBaku.setInt(1, uid);
            try (ResultSet rs_editBaku = ps_editBaku.executeQuery()) {
                if (rs_editBaku.next()) {
                    editid_baku = rs_editBaku.getInt("id");
                    edit_baku = rs_editBaku.getString("namebaku");
                    double price_baku = rs_editBaku.getDouble("pricebaku");
                    System.out.println("========== No " + noBaku + " ==========" + "\nID \t: " + editid_baku + "\nNama \t: " + edit_baku + "\nHarga \t: " + rp.format(price_baku));
                    noBaku++;
                } else {
                    System.out.println("ID tidak ditemukan.");
                    editbaku_valid = false;
                }
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }

        System.out.print("Pilih (ID) yang mau di edit : ");
        pilihBaku = scan.nextInt();
        boolean idbaku_Exists = bahanbakuExists(pilihBaku);
        if (!idbaku_Exists) {
            System.out.println("ID not found. Please enter a valid ID.\n");
            editBaku();
        }

        while (!editbaku_valid) {
            try {
                System.out.println("Input harga baru : ");
                price_editBaku = scan.nextDouble();
                editbaku_valid = true;
                if (price_editBaku <= 0) {
                    System.out.println("Input tidak boleh 0 atau negatif!\n");
                    editbaku_valid = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Harus input angka!\n");
                scan.nextLine();
            }
            edit_baku = "UPDATE bahanbaku SET pricebaku = ? WHERE id = ?";
            try (PreparedStatement upPS_editbaku = con.prepareStatement(edit_baku)) {
                //abis itu bakalan di set dalam databaseny berdasarkan index
                upPS_editbaku.setDouble(1, price_editBaku);
                upPS_editbaku.setInt(2, pilihBaku);
                upPS_editbaku.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        }
        editVarCost();
    }

    private static void editKerja() throws SQLException {
        String edit_kerja;
        int noKerja = 1, pilihKerja, editid_kerja;
        boolean editkerja_valid = false;
        double price_editKerja = 0;

        String all_editKerja = "SELECT * FROM tenagakerja WHERE userid = ?";

        try (PreparedStatement ps_editKerja = con.prepareStatement(all_editKerja)) {
            ps_editKerja.setInt(1, uid);
            try (ResultSet rs_editKerja = ps_editKerja.executeQuery()) {
                while (rs_editKerja.next()) {
                    editid_kerja = rs_editKerja.getInt("id");
                    edit_kerja = rs_editKerja.getString("namekerja");
                    double price_kerja = rs_editKerja.getDouble("pricekerja");
                    System.out.println("========== No " + noKerja + " ==========" + "\nID \t: " + editid_kerja + "\nNama \t: " + edit_kerja + "\nHarga \t: " + rp.format(price_kerja));
                    noKerja++;
                }
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        System.out.print("Pilih (ID) yang mau di edit : ");
        pilihKerja = scan.nextInt();
        boolean idkerja_Exists = tenagakerjaExists(pilihKerja);
        if (!idkerja_Exists) {
            System.out.println("ID not found. Please enter a valid ID.\n");
            editBaku();
        }

        while (!editkerja_valid) {
            try {
                System.out.println("\nInput harga baru : ");
                price_editKerja = scan.nextDouble();
                editkerja_valid = true;
                if (price_editKerja <= 0) {
                    System.out.println("Input tidak boleh 0 atau negatif!\n");
                    editkerja_valid = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Harus input angka!\n");
                scan.nextLine();
            }
        }
        edit_kerja = "UPDATE tenagakerja SET pricekerja = ? WHERE id = ?";
        try (PreparedStatement upPS_editkerja = con.prepareStatement(edit_kerja)) {
            //abis itu bakalan di set dalam databaseny berdasarkan index
            upPS_editkerja.setDouble(1, price_editKerja);
            upPS_editkerja.setInt(2, pilihKerja);
            upPS_editkerja.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        editVarCost();
    }

    private static void editLain() throws SQLException {
        String edit_lain;
        int noLain = 1, pilihLain, editid_lain;
        boolean editlain_valid = false;
        double price_editLain = 0;

        String all_editLain = "SELECT * FROM biayalain WHERE userid = ?";

        try (PreparedStatement ps_editLain = con.prepareStatement(all_editLain)) {
            ps_editLain.setInt(1, uid);
            try (ResultSet rs_editLain = ps_editLain.executeQuery()) {
                while (rs_editLain.next()) {
                    editid_lain = rs_editLain.getInt("id");
                    edit_lain = rs_editLain.getString("namelain");
                    double price_lain = rs_editLain.getDouble("pricelain");
                    System.out.println("========== No " + noLain + " ==========" + "\nID \t: " + editid_lain + "\nNama \t: " + edit_lain + "\nHarga \t: " + rp.format(price_lain));
                    noLain++;
                }
            } catch (SQLException e) {
                System.out.println("Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        System.out.print("Pilih (ID) yang mau di edit : ");
        pilihLain = scan.nextInt();
        boolean idlain_Exists = biayalainExists(pilihLain);
        if (!idlain_Exists) {
            System.out.println("ID not found. Please enter a valid ID.\n");
            editLain();
        }

        while (!editlain_valid) {
            try {
                System.out.println("\nInput harga baru : ");
                price_editLain = scan.nextDouble();
                editlain_valid = true;
                if (price_editLain <= 0) {
                    System.out.println("Input tidak boleh 0 atau negatif!\n");
                    editlain_valid = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Harus input angka!\n");
                scan.nextLine();
            }
        }
        edit_lain = "UPDATE biayalain SET pricelain = ? WHERE id = ?";
        try (PreparedStatement upPS_editlain = con.prepareStatement(edit_lain)) {
            //abis itu bakalan di set dalam databaseny berdasarkan index
            upPS_editlain.setDouble(1, price_editLain);
            upPS_editlain.setInt(2, pilihLain);
            upPS_editlain.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        editVarCost();
    }

    private static void editFixCost() throws SQLException {
        int pick, pilihFix, noFix = 1, editid_fix;
        double price_editFix = 0;
        boolean edit_fixed = false;
        String edit_fix;
        // ==================================================================
        System.out.println("==============================");
        System.out.println("== 1. Edit Fixed Cost       ==");
        System.out.println("== 2. Kembali               ==");
        System.out.println("==============================");
        System.out.println("Choose : ");
        pick = scan.nextInt();
        scan.nextLine();
        // ===================================================================
        switch (pick) {
            case 1:
                String all_editFix = "SELECT * FROM fixcost WHERE userid = ?";

                try (PreparedStatement ps_editFix = con.prepareStatement(all_editFix)) {
                    ps_editFix.setInt(1, uid);
                    try (ResultSet rs_editFix = ps_editFix.executeQuery()) {
                        while (rs_editFix.next()) {
                            editid_fix = rs_editFix.getInt("id");
                            edit_fix = rs_editFix.getString("namefix");
                            double price_fix = rs_editFix.getDouble("pricefix");
                            System.out.println("========== No " + noFix + " ==========" + "\nID \t: " + editid_fix + "\nNama \t: " + edit_fix + "\nHarga \t: " + rp.format(price_fix));
                            noFix++;
                        }
                    } catch (SQLException e) {
                        System.out.println("Gagal");
                    }
                } catch (SQLException e) {
                    System.out.println("Gagal");
                }
                System.out.print("Pilih (ID) yang mau di edit : ");
                pilihFix = scan.nextInt();
                while (!edit_fixed) {
                    try {
                        System.out.println("\nInput harga baru : ");
                        price_editFix = scan.nextDouble();
                        edit_fixed = true;
                        if (price_editFix <= 0) {
                            System.out.println("Input tidak boleh 0 atau negatif!\n");
                            edit_fixed = false;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Harus input angka!\n");
                        scan.nextLine();
                    }
                }
                String edit_Fix = "UPDATE fixcost SET pricefix = ? WHERE id = ?";
                try (PreparedStatement upPS_editfix = con.prepareStatement(edit_Fix)) {
                    //abis itu bakalan di set dalam databaseny berdasarkan index
                    upPS_editfix.setDouble(1, price_editFix);
                    upPS_editfix.setInt(2, pilihFix);
                    upPS_editfix.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Gagal");
                }
                editFixCost();
                break;
            case 2:
                editVarCost();
        }
    }

    private static void editVarCost() throws SQLException {
        int pick;
        // ==================================================================
        System.out.println("==============================");
        System.out.println("== 1. Edit Bahan Baku       ==");
        System.out.println("== 2. Edit Tenaga Kerja     ==");
        System.out.println("== 3. Edit Bahan Lainnnya   ==");
        System.out.println("== 4. Kembali               ==");
        System.out.println("==============================");
        System.out.println("Choose : ");
        pick = scan.nextInt();
        scan.nextLine();
        // ===================================================================
        switch (pick) {
            case 1:
                editBaku();
                break;
            case 2:
                editKerja();
                break;
            case 3:
                editLain();
                break;
            case 4:
                editVarFix();
        }
    }

    private static void editVarFix() throws SQLException {
        int pick;
        System.out.println("===========================");
        System.out.println("== 1. Edit Variable Cost ==");
        System.out.println("== 2. Edit Fixed Cost    ==");
        System.out.println("== 3. Edit Harga Produk  ==");
        System.out.println("== 4. Kembali            ==");
        System.out.println("===========================");
        System.out.print("Pilih : ");
        pick = scan.nextInt();
        switch (pick) {
            case 1:
                editVarCost();
                break;
            case 2:
                editFixCost();
                break;
            case 3:
                double totalBaku = totalBaku();
                double totalKerja = totalKerja();
                double totalLain = totalLain();
                double totalVariabel = totalVariabel(totalBaku, totalKerja, totalLain);
                double pricePerProduct = pricePerProduct(totalVariabel);
                double totalFixed = totalFixed();
                int fixPorsi = fixPorsi();
                double hargaPP = hargaPP(totalVariabel, pricePerProduct, totalFixed);

                double perUnit = perUnit(hargaPP, fixPorsi);

                float persenUntung = persenUntung();

                double perProduct = perProduct(persenUntung, perUnit);

                double totjualProduct = totjualProduct(perProduct);

                double marginContribution = marginContribution(totjualProduct, pricePerProduct);

                double breakEQ = breakEQ(totalFixed, marginContribution);

                breakEP(totjualProduct, breakEQ);
                ;

                editVarFix();
                break;
            case 4:
                isLogin();
        }
    }

    // =======================================================================================\
    private static boolean bahanbakuExists(int id) throws SQLException {
        String sql = "SELECT * FROM bahanbaku WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        return false;
    }

    private static boolean tenagakerjaExists(int id) throws SQLException {
        String sql = "SELECT * FROM tenagakeja WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        return false;
    }

    private static boolean biayalainExists(int id) throws SQLException {
        String sql = "SELECT * FROM biayalain WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Gagal");
        }
        return false;
    }

}
