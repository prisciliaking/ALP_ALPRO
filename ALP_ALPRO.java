
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class ALP_ALPRO {

    // semua ini disimpen ke private static biar langsung kesimpen jadi 1, jadi nanti di function lainny kalau mau
    // dipake gk ush di declare/dipanggil lagi kedepannya krn udh ke simpen deluan sejak awal
    private static ArrayList<String> username = new ArrayList<String>();
    private static ArrayList<String> password = new ArrayList<String>();
    private static ArrayList<String> fullname = new ArrayList<String>();
    private static ArrayList<String> business = new ArrayList<String>();
    // =====================================================================
    private static ArrayList<String> namebhnBaku = new ArrayList<String>();
    private static ArrayList<Double> pricebhnBaku = new ArrayList<Double>();
    private static ArrayList<String> nametngKrj = new ArrayList<String>();
    private static ArrayList<Double> pricetngKrj = new ArrayList<Double>();
    private static ArrayList<String> namebyLain = new ArrayList<String>();
    private static ArrayList<Double> pricebyLain = new ArrayList<Double>();
    private static ArrayList<String> nameFixCost = new ArrayList<String>();
    private static ArrayList<Double> priceFixCost = new ArrayList<Double>();
    // =====================================================================
    private static ArrayList<Double> ttlbhnBaku = new ArrayList<Double>();
    private static ArrayList<Double> ttltngKerja = new ArrayList<Double>();
    private static ArrayList<Double> ttlLainnya = new ArrayList<Double>();
    private static ArrayList<Double> ttlVarCost = new ArrayList<Double>();
    private static ArrayList<Double> ttlFixCost = new ArrayList<Double>();
    private static ArrayList<Double> priceProduk = new ArrayList<Double>();
    private static ArrayList<Integer> fixPortion = new ArrayList<Integer>();
    // =====================================================================
    private static ArrayList<Double> totalHPP = new ArrayList<Double>();
    private static ArrayList<Double> sellProduct = new ArrayList<Double>();
    private static ArrayList<Float> ttlUntung = new ArrayList<Float>();
    private static ArrayList<Integer> eachUnit = new ArrayList<Integer>();
    private static ArrayList<Integer> marginCont = new ArrayList<Integer>();
    private static ArrayList<Integer> beQuantity = new ArrayList<Integer>();
    private static ArrayList<Integer> eachPoint = new ArrayList<Integer>();
    // =====================================================================
    private static Scanner scan = new Scanner(System.in);
    private static boolean login = false;
    // =====================================================================
    // ini biar nanti pas di output bkalan jadi rupiah indonesia ada titik komanya
    private static Locale indonesia = new Locale("id", "ID");
    private static NumberFormat rp = NumberFormat.getCurrencyInstance(indonesia);
    // =====================================================================

    public static void main(String[] args) {
        login = true;
        main();
    }

    private static void main() {
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
                        register(); //manggil function regis
                        break;

                    case 2:
                        login(); //manggil function login
                        isLogin();
                        break;

                    case 3:
                        System.out.println("Thankyou for coming :) !"); //out lgsg bre
                        System.exit(0);
                }
            } while (pick != 3); //ngulang pokokny kl gk 1 2 3
        }

    }

    private static void isLogin() {
        int pick;
        // ==================================================================
        System.out.println("===========================");
        System.out.println("== 1. Mencari Harga Jual ==");
        System.out.println("== 2. History Bahan      ==");
        System.out.println("== 3. Edit Bahan Baku    ==");
        System.out.println("== 4. Logout             ==");
        System.out.println("===========================");
        System.out.println("Choose : ");
        pick = scan.nextInt();
        // ===================================================================
        switch (pick) {
            case 1:
                jmlhPorsi();
                varCost();
                fixedCost();
                hargaPP();
                perProduct();
                marginContribution();
                breakEQ();
                breakEP();
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

    private static void editVarFix() {
        int pick;
        System.out.println("===========================");
        System.out.println("== 1. Edit Variable Cost ==");
        System.out.println("== 2. Edit Fixed Cost    ==");
        System.out.println("== 3. Kembali            ==");
        System.out.println("== 4. Cek Harga Produk   ==");
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
                isLogin();
                break;
            // belum kebaca
            case 4:
                totalBhnBaku();
                totalKerja();
                totalLainnya();
                totalVariabel();
                totalFixed();
                pricePerProduct();
                hargaPP();
                perProduct();
                marginContribution();
                breakEQ();
                breakEP();
                isLogin();
        }
    }

    private static void register() {
        String inp_unm, inp_pass, inp_name, inp_bsname;
        // penggunaan boolean ini buat ngecek kalau inputan user itu udah bener atau belum
        // mirip buat penginisialisasian doang trus ngecek
        boolean unm_valid = false, pass_valid = false, bsname_valid = false;
        System.out.println("===================\n==== WELCOME ! ====\n===================\n");
        System.out.print("Full Name : ");
        inp_name = scan.next() + scan.nextLine();
        do {
            System.out.print("Username : ");
            inp_unm = scan.next().toLowerCase();

            //to check if the username already input or not
            if (username.contains(inp_unm)) {
                System.out.println("Username sudah ada. Silakan masukkan username lain.");
            }
            if (!username.contains(inp_unm)) { //if username array not contain the input's
                unm_valid = true;
                break;
            }
        } while (!unm_valid || username.contains(inp_unm));

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

        do {
            System.out.println("\nInsert your Business name : ");
            inp_bsname = scan.next() + scan.nextLine().toLowerCase();

            if (business.contains(inp_bsname)) {
                System.out.println("The Business name already exist. \nPlease input another one !");
            }
            if (!business.contains(inp_bsname)) {
                bsname_valid = true;
                business.add(inp_bsname); //nyimpen nama usaha
                break;
            }
        } while (!bsname_valid || business.contains(inp_bsname));
    }

    private static void login() {
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
                    System.out.println("\nWelcome, " + fullname.get(check_index) + " !");
                    //fullname ini buat manggil nama diindex yg udh disimpenfor (int l = 0; l < business.size(); l++) {
                    System.out.println("\nHello, " + business.get(check_index) + ".\nLet's start!");
                    login = true;
                    break;
                }
            }

            if (pass.equalsIgnoreCase("reset")) {
                resetPass();
                login(); //ini biar passwordnya ke reset 
            }
            if (pass.equalsIgnoreCase("exit")) {
                main(); //langsung balik ke menu
            }
            if (pass_true == false) { //if pass is wrong
                System.out.println("Password invalid ! \n");
            }
        } while (!pass_true);
    }

    private static void resetPass() {
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

    private static Integer jmlhPorsi() {
        int jmlhPorsi = 0;
        boolean Porsi_valid = false;
        do { // buat masukin porsi
            try {
                System.out.println("Untuk berapa porsi? ");
                jmlhPorsi = scan.nextInt();

                if (jmlhPorsi <= 0) {
                    System.out.println("You need to add portion\n");
                    continue; // Ask for input again
                }
                Porsi_valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Input invalid. Please re-enter a valid number\n");
                scan.nextLine(); // Clear the input buffer
            }
            fixPortion.add(jmlhPorsi);
        } while (!Porsi_valid);
        return 0;
    }

    //fitur pertama
    private static void varCost() {
        String namaBhnBaku, ans, namaTngKrj, namaByLain;
        double hrgBhnBaku = 0, hrgTngKrj = 0, hrgByLain = 0;
        double totBhnBaku = 0, totKerja = 0, totLainnya = 0;
        boolean BhnBaku_valid = false, TngKrj_valid = false, ByLain_valid = false;
        boolean yesnoBaku_valid = false, yesnoKerja_valid = false, yesnoLainnya_valid = false;

        do { //buat bahan baku
            System.out.println("\n=== Masukkan Bahan Baku ===");
            scan.nextLine();
            System.out.print("Nama : ");
            namaBhnBaku = scan.nextLine();
            namebhnBaku.add(namaBhnBaku);
            while (!BhnBaku_valid) {
                try {
                    System.out.print("Harga : Rp ");
                    hrgBhnBaku = scan.nextDouble();
                    if (hrgBhnBaku <= 0) {
                        System.out.println("Can't be 0 or negative!\n");
                        continue;
                    }
                    BhnBaku_valid = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! try again.\n");
                    scan.nextLine();
                }
            }
            pricebhnBaku.add(hrgBhnBaku);
            // ===============================
            do {
                System.out.println("Masih ada inputan? y/n");
                ans = scan.next();
                if (ans.equalsIgnoreCase("y")) {
                    BhnBaku_valid = false;
                    yesnoBaku_valid = true;
                } else if (ans.equalsIgnoreCase("n")) {
                    BhnBaku_valid = true;
                    yesnoBaku_valid = true;
                }
            } while (!yesnoBaku_valid);
        } while (!BhnBaku_valid);

        do { //buat tenaga kerja
            System.out.println("\n=== Masukkan Tenaga Kerja ===");
            scan.nextLine();
            System.out.print("Nama  : ");
            namaTngKrj = scan.nextLine();
            nametngKrj.add(namaTngKrj);

            while (!TngKrj_valid) {
                try {
                    System.out.print("Harga : Rp ");
                    hrgTngKrj = scan.nextDouble();

                    if (hrgTngKrj <= 0) {
                        System.out.println("Price cannot be negative or 0!\n");
                        continue;
                    }
                    TngKrj_valid = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! try again.\n");
                    scan.nextLine();
                }
            }
            pricetngKrj.add(hrgTngKrj);
            // ===========================
            do {
                System.out.println("Masih ada inputan? y/n");
                ans = scan.next();
                if (ans.equalsIgnoreCase("y")) {
                    TngKrj_valid = false;
                    yesnoKerja_valid = true;
                } else if (ans.equalsIgnoreCase("n")) {
                    TngKrj_valid = true;
                    yesnoKerja_valid = true;
                }
            } while (!yesnoKerja_valid);
        } while (!TngKrj_valid);

        do { // buat biaya lain lain
            System.out.println("\n=== Biaya Lain-Lain ===");
            scan.nextLine();
            System.out.print("Nama : ");
            namaByLain = scan.nextLine();
            namebyLain.add(namaByLain);
            while (!ByLain_valid) {
                try {
                    System.out.print("Harga : Rp ");
                    hrgByLain = scan.nextDouble();
                    if (hrgByLain <= 0) {
                        System.out.println("Price cannot be negative or 0!\n");
                        continue;
                    }
                    ByLain_valid = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! try again.\n");
                    scan.nextLine();
                }
            }
            pricebyLain.add(hrgByLain);
            // ===========================
            do {
                System.out.println("Masih ada inputan? y/n");
                ans = scan.next();
                if (ans.equalsIgnoreCase("y")) {
                    ByLain_valid = false;
                    yesnoLainnya_valid = true;
                } else if (ans.equalsIgnoreCase("n")) {
                    ByLain_valid = true;
                    yesnoLainnya_valid = true;
                }
            } while (!yesnoLainnya_valid);
        } while (!ByLain_valid);

        totalBhnBaku();
        totalKerja();
        totalLainnya();
        totalVariabel();
//        System.out.println("total variable cost : Rp  " + ttlVarCost.get(0));
        pricePerProduct();
        // ================
        fixedCost();
    }

    private static void fixedCost() {
        String namaFixCost, ans;
        double hrgFixCost = 0;
        boolean FixedCost_valid = false, yesnoFixed_valid = false;

        System.out.println("=== Masukkan Fixed Cost (Biaya tetap) ===");
        do {
            scan.nextLine();
            System.out.print("Nama : ");
            namaFixCost = scan.nextLine();
            nameFixCost.add(namaFixCost);
            while (!FixedCost_valid) {
                try {
                    System.out.print("Harga : Rp ");
                    hrgFixCost = scan.nextDouble();
                    if (hrgFixCost <= 0) {
                        System.out.println("Price cannot be negative! try again.\n");
                        continue;
                    }
                    FixedCost_valid = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! try again\n");
                    scan.nextLine();
                }
            }
            // ================================
            priceFixCost.add(hrgFixCost);
            // ================================
            do {
                System.out.println("Masih ada inputan? y/n");
                ans = scan.next();
                if (ans.equalsIgnoreCase("y")) {
                    FixedCost_valid = false;
                    yesnoFixed_valid = true;
                } else if (ans.equalsIgnoreCase("n")) {
                    FixedCost_valid = true;
                    yesnoFixed_valid = true;
                }
            } while (!yesnoFixed_valid);
        } while (!FixedCost_valid);

        //simpan smua fix cost
        totalFixed();
//        System.out.println("total fix cost : " + ttlFixCost.get(0));
        hargaPP();
    }
    // ======================================================
    //perhitungan
    private static void totalBhnBaku() {
        double totBaku = 0;
        for (int i = 0; i < pricebhnBaku.size(); i++) {
            totBaku += pricebhnBaku.get(i);
        }
        // ==============================
        ttlbhnBaku.add(totBaku);
        // ==============================
    }

    private static void totalKerja() {
        double totTenaga = 0;
        for (int k = 0; k < pricetngKrj.size(); k++) {
            totTenaga += pricetngKrj.get(k);
        }
        // =============================
        ttltngKerja.add(totTenaga);
        // =============================
    }

    private static void totalLainnya() {
        double totLainnya = 0;
        for (int l = 0; l < pricebyLain.size(); l++) {
            totLainnya += pricebyLain.get(l);
        }
        // =============================
        ttlLainnya.add(totLainnya);
        // =============================
    }

    private static void totalVariabel() {
        double totVariabel = 0;
        totVariabel = ttlbhnBaku.get(0) + ttltngKerja.get(0) + ttlLainnya.get(0);
        // ============================
        ttlVarCost.add(totVariabel);
        // ============================
    }

    private static void totalFixed() {
        double totFix = 0;
        for (int i = 0; i < priceFixCost.size(); i++) {
            totFix += priceFixCost.get(i);
        }
        // ==============================
        ttlFixCost.add(totFix);
        // ==============================
    }
    
    private static void pricePerProduct() {
        //Harga per satuan product//
        double perProduk = 0;
        perProduk = (ttlVarCost.get(0) / fixPortion.get(0));;
        priceProduk.add(perProduk); //simpen per produk
    }
    // ====================================================
    // fitur ke dua
    private static void listVarCost() {
        double totListBaku = 0, totListTng = 0, totListLain = 0, totListVar = 0;
        String RpListVar;
        System.out.println("Jumlah Porsi : " + fixPortion.get(0));
        System.out.println("\n=== Variabel Cost ===");
        System.out.println("Nama Bahan Baku :\tHarga");
        for (int j = 0; j < pricebhnBaku.size(); j++) {
            System.out.println(namebhnBaku.get(j) + " : \t" + rp.format(pricebhnBaku.get(j)));
            totListBaku += pricebhnBaku.get(j);
        }

        System.out.println("total bahan baku : " + totListBaku);
        // =======================================================
        System.out.println("\nNama Tenaga Kerja :\tHarga");
        for (int k = 0; k < pricetngKrj.size(); k++) {
            System.out.println(nametngKrj.get(k) + " : \t" + rp.format(pricetngKrj.get(k)));
            totListTng += pricetngKrj.get(k);
        }

        System.out.println("total tenaga kerja : " + totListTng);
        // =======================================================
        System.out.println("\nNama Biaya Lainnya :\tHarga");
        for (int l = 0; l < pricebyLain.size(); l++) {
            System.out.println(namebyLain.get(l) + " : \t" + rp.format(pricebyLain.get(l)));
            totListLain += pricebyLain.get(l);
        }

        System.out.println("total biaya lainnya : " + totListLain);
        // =======================================================
        totListVar = (totListBaku + totListTng + totListLain);
        // =======================================================
        RpListVar = rp.format((int) totListVar);
        System.out.println("\nTotal Variable Cost : " + RpListVar);
        listFixCost();
    }

    private static void listFixCost() {
        double totListFix = 0;
        String RpListFix;
        System.out.println("\n=== Fixed Cost ===");
        System.out.println("Nama Biaya Lainnya :\tHarga");
        for (int i = 0; i < priceFixCost.size(); i++) {
            System.out.println(nameFixCost.get(i) + " : \t" + rp.format(priceFixCost.get(i)));
            totListFix += priceFixCost.get(i);
        }

        RpListFix = rp.format((int) totListFix);
        System.out.println("\nTotal Fixed Cost : " + RpListFix);
        isLogin();
    }

    //perhitungannya

    private static void hargaPP() { //harga pokok produksi
        float perUntung, totUntung = 0;
        double totHPP = 0, perUnit = 0;
        double indtotHPP;
        int hasilUnit;
        String RphslUnit, RpHPP;
        System.out.println("\n=======================================================");
        System.out.println("Total Variable Cost : Rp  " + ttlVarCost.get(0));
        System.out.println("Harga per Produk : Rp " + priceProduk.get(0));
        System.out.println("Total Fixed Cost : Rp " + ttlFixCost.get(0));
        System.out.println("=======================================================");
        totHPP = ttlFixCost.get(0) + ttlVarCost.get(0);
        totalHPP.add(totHPP);
        RpHPP = rp.format((int) totHPP);
        System.out.println("Total Harga Pokok Produksi : " + RpHPP);

        // harga pokok produksi per unit
        for (int k = 0; k < totalHPP.size(); k++) {
            perUnit += (totalHPP.get(k) / fixPortion.get(k));
        }
        hasilUnit = (int) Math.ceil(perUnit);
        eachUnit.add(hasilUnit);
        // =========================================
        RphslUnit = rp.format((int) hasilUnit);
        System.out.println("\nHarga Pokok Produksi per Unit : " + RphslUnit);
        // =========================================
        persenUntung();
    }

    private static void persenUntung() {
        float perUntung, totUntung;
        //mencari persen keuntungan
        System.out.print("\nMau untung berapa persen? (1-100) : ");
        perUntung = scan.nextFloat();
        totUntung = (perUntung / 100);
        ttlUntung.add(totUntung); //simpan persenan untung
        perProduct();
    }

    private static void perProduct() { // harga jual product
        double jualProduct = 0, totjualProduct;
        int hasilProduct;
        String RptotProduct;

        for (int k = 0; k < totalHPP.size(); k++) {
            jualProduct += eachUnit.get(k) + (eachUnit.get(k) * ttlUntung.get(k));
        }
        System.out.println("=======================================");
        hasilProduct = (int) Math.ceil(jualProduct);
        System.out.println("Harga Asli : Rp " + rp.format((int) jualProduct));
        totjualProduct = ((hasilProduct + 999) / 1000) * 1000; // to round up
        sellProduct.add(totjualProduct);
        //====================================================
        RptotProduct = rp.format((int) totjualProduct); //ada Rp di depannya nanti
        System.out.println("=======================================");
        System.out.println("Harga Jual Produk (pembulatan) : " + RptotProduct);
        marginContribution();
    }

    private static void marginContribution() {
        double mrgCont = 0;
        int hasilMargin;
        String RpMargin;
        for (int l = 0; l < sellProduct.size(); l++) {
            mrgCont = sellProduct.get(l) - priceProduk.get(l);
        }
        hasilMargin = (int) Math.ceil(mrgCont);
        marginCont.add(hasilMargin);
        // ==================================================
        RpMargin = rp.format((int) hasilMargin);
        System.out.println("margin contribution : " + RpMargin);
        breakEQ();
    }

    private static void breakEQ() { // break even quantity (yg hrus dijual)
        //biar balik modal
        double perQuantity = 0;
        int hasilQuantity;
        for (int i = 0; i < marginCont.size(); i++) {
            perQuantity = (ttlFixCost.get(i) / marginCont.get(i));
        }
        hasilQuantity = (int) Math.ceil(perQuantity);
        beQuantity.add(hasilQuantity);
        // ===================================================
        System.out.println("Break even Quantity : " + hasilQuantity);
        breakEP();
    }

    private static void breakEP() { //break even point (keuntungan minima)
        double perPoint = 0;
        int hasilPoint;
        String RpPoint;
        for (int k = 0; k < beQuantity.size(); k++) {
            perPoint = beQuantity.get(k) * sellProduct.get(k);
        }
        hasilPoint = (int) Math.ceil(perPoint);
        eachPoint.add(hasilPoint);
        // =================================================
        RpPoint = rp.format((int) hasilPoint);
        System.out.println("Break even Point : " + RpPoint);
        main();
    }

    // fitur ketiga
    private static void editVarCost() {
        int pick, pilihBaku, pilihKerja, pilihLain;
        String editBhnBaku, editTngKrj, editLainnya;
        double hrgnewBaku, hrgnewKerja, hrgnewLain;
        boolean newBaku_valid = false, newKerja_valid = false, newLain_valid = false;
        do {
            // ==================================================================
            System.out.println("==============================");
            System.out.println("== 1. Edit Bahan Baku       ==");
            System.out.println("== 2. Edit Tenaga Kerja     ==");
            System.out.println("== 3. Edit Bahan Lainnnya  ==");
            System.out.println("== 4. Kembali               ==");
            System.out.println("==============================");
            System.out.println("Choose : ");
            pick = scan.nextInt();
            scan.nextLine();
            // ===================================================================
            switch (pick) {
                case 1: // buah bahan baku
                    for (int i = 0; i < namebhnBaku.size(); i++) {
                        System.out.println((i + 1) + ". " + namebhnBaku.get(i));
                    }
                    System.out.println("Pilih yang mau diubah ");
                    pilihBaku = scan.nextInt() - 1;
                    try {
                        System.out.print("Masukkan harga baru : Rp ");
                        hrgnewBaku = scan.nextDouble();
                        if (hrgnewBaku <= 0) {
                            System.out.println("\nCan't be negative or 0! try again.");
                            return;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Harus input angka");
                        scan.nextLine();
                        return;
                    }
                    // ===============================================
                    editBaku(pilihBaku, hrgnewBaku);
                    // ===============================================
                    editVarCost();
                    break;

                case 2: //buat tenaga kerja
                    for (int i = 0; i < nametngKrj.size(); i++) {
                        System.out.println((i + 1) + ". " + nametngKrj.get(i));
                    }
                    System.out.println("Pilih yang mau diubah ");
                    pilihKerja = scan.nextInt() - 1;
                    try {
                        System.out.print("Masukkan harga baru : Rp ");
                        hrgnewKerja = scan.nextDouble();
                        if (hrgnewKerja <= 0) {
                            System.out.println("\nCan't be negative or 0! try again.");
                            return;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Harus input angka");
                        scan.nextLine();
                        return;
                    }
                    // ===============================================
                    editKerja(pilihKerja, hrgnewKerja);
                    // ===============================================
                    editVarCost();
                    break;
                case 3: // buat bahan lainnya
                    for (int i = 0; i < namebyLain.size(); i++) {
                        System.out.println((i + 1) + ". " + namebyLain.get(i));
                    }
                    System.out.println("Pilih yang mau diubah ");
                    pilihLain = scan.nextInt() - 1;
                    try {
                        System.out.print("Masukkan harga baru : Rp ");
                        hrgnewLain = scan.nextDouble();
                        if (hrgnewLain <= 0) {
                            System.out.println("\nCan't be negative or 0! try again.");
                            return;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Harus input angka");
                        scan.nextLine();
                        return;
                    }
                    // ===============================================
                    editLain(pilihLain, hrgnewLain);
                    // ===============================================
                    editVarCost();
                    break;
                case 4: // kembali 1 step
                    editVarFix();
                    break;
            }
        } while (true);
    }

    private static String editBaku(int nameBaku, double priceBaku) {
        pricebhnBaku.set(nameBaku, priceBaku);
        return "\nSukses diubah!";
    }

    private static String editKerja(int nameKerja, double priceKerja) {
        pricetngKrj.set(nameKerja, priceKerja);
        return "\nSukses diubah!";
    }

    private static String editLain(int nameLain, double priceLain) {
        pricetngKrj.set(nameLain, priceLain);
        return "\nSukses diubah!";
    }

    private static void editFixCost() {
        String res;
        int pick, pilihFix;
        double hrgnewFix = 0;
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
                for (int i = 0; i < nameFixCost.size(); i++) {
                    System.out.println((i + 1) + ". " + nameFixCost.get(i));
                }
                System.out.println("Pilih yang mau diubah ");
                pilihFix = scan.nextInt() - 1;
                try {
                    System.out.print("Masukkan harga baru : Rp ");
                    hrgnewFix = scan.nextDouble();
                    if (hrgnewFix <= 0) {
                        System.out.println("\nCan't be negative or 0! try again.");
                        return;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Harus input angka");
                    scan.nextLine();
                    return;
                }
                editFix(pilihFix, hrgnewFix);
                editFixCost();
                break;
            case 2:
                editVarFix();
                break;
        }

    }

    private static String editFix(int namaFix, double priceFix) {
        priceFixCost.set(namaFix, priceFix);
        return "\nSukses diubah!";
    }

}
