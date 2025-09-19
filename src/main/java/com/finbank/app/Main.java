package main.java.com.finbank.app;

import main.java.com.finbank.model.*;
import main.java.service.BankService;
import main.java.com.finbank.exceptions.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static BankService bank = new BankService();

    public static void main(String[] args) {
        seedData();


        while (true) {
            System.out.println("======= WELCOME TO FINBANK SYSTEM =======");
            System.out.println("1. Login as gestionnaire");
            System.out.println("2. Login as client");
            System.out.println("0. EXIT");
            System.out.println("Choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: loginGestionnaireMenu(); break;
                case 2: loginClientMenu(); break;
                case 0: System.exit(0); break;
                default:
                    System.out.println("Invalid choice");
            }
        }

    }

    /** seeding some datz for testing */
    private static void seedData() {
        bank.createClient("C001", "abdo", "elouafi", "abdo@gmail.com", "0000");
        bank.createClient("C002", "amine", "el", "amine@gmail.com", "0000");
        try {
            bank.addAccountToClient("C001", "C1001", TypeCompte.COURANT, 500);
            bank.addAccountToClient("C002", "C1002", TypeCompte.COURANT, 1000);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        Gestionnaire g = new Gestionnaire("G1", "mustapha", "moutaki", "mustapha@manager.com", "admin");
        bank.setGestionnaire(g);
    }

    // ---------------- CLIENT MENU ----------------
    private static void loginClientMenu() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String pwd = scanner.nextLine();

        Client client = bank.loginClient(email, pwd);
        if (client == null) {
            System.out.println("Login failed.");
            return;
        }

        while (true) {
            System.out.println("\n==== MENU CLIENT ====");
            System.out.println("1. Consulter mes comptes");
            System.out.println("2. Consulter solde");
            System.out.println("3. Deposit");
            System.out.println("4. withdraw");
            System.out.println("5. transfer");
            System.out.println("6. Consulter relevé bancaire");
            System.out.println("0. Logout");
            System.out.print("Choix: ");
            int ch = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (ch) {
                    case 1:
                        for (Compte c : client.getAllComptes()) {
                            System.out.println(c.getNumeroCompte() + " (" + c.getTypeCompte() + ") - Solde: " + c.getSolde());
                        }
                        break;
                    case 2:
                        System.out.print("Numéro compte: ");
                        String num = scanner.nextLine();
                        Compte c = client.getCompte(num);
                        if (c != null) {
                            System.out.println("Solde: " + c.getSolde());
                        } else {
                            System.out.println("account not found.");
                        }
                        break;
                    case 3:
                        System.out.print("Compte: ");
                        String cDep = scanner.nextLine();
                        System.out.print("Montant: ");
                        double mDep = scanner.nextDouble();
                        scanner.nextLine();
                        bank.deposit(client.getIdClient(), cDep, mDep);
                        System.out.println("Deposit successful.");
                        break;
                    case 4:
                        System.out.print("Compte: ");
                        String cRet = scanner.nextLine();
                        System.out.print("Montant: ");
                        double mRet = scanner.nextDouble();
                        scanner.nextLine();
                        bank.withdraw(client.getIdClient(), cRet, mRet);
                        System.out.println("withdraw successful");
                        break;
                    case 5:
                        System.out.print("Compte source: ");
                        String cSrc = scanner.nextLine();
                        System.out.print("Compte destination: ");
                        String cDest = scanner.nextLine();
                        System.out.print("Montant: ");
                        double mVir = scanner.nextDouble();
                        scanner.nextLine();
                        bank.transfer(client.getIdClient(), cSrc, client.getIdClient(), cDest, mVir);
                        System.out.println("transfer successful");
                        break;
                    case 6:
                        System.out.print("Numéro compte: ");
                        String cStat = scanner.nextLine();
                        List<String> lines = bank.readStatement(client.getIdClient(), cStat);
                        for (String line : lines) System.out.println(line);
                        break;
                    case 0: return; // logout
                    default: System.out.println("invalid choice");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    // GESTIONNAIRE MENU
    private static void loginGestionnaireMenu() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String pwd = scanner.nextLine();

        Gestionnaire g = bank.loginGestionnaire(email, pwd);
        if (g == null) {
            System.out.println("Login failed.");
            return;
        }

        while (true) {
            System.out.println("\n==== MENU GESTIONNAIRE ====");
            System.out.println("1. create account for client");
            System.out.println("2. close account for client");
            System.out.println("3. Modifier infos client");
            System.out.println("4. Consulter relevé client");
            System.out.println("0. logout");
            System.out.print("choice: ");
            int ch = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (ch) {
                    case 1:
                        System.out.print("ID client: ");
                        String id = scanner.nextLine();
                        System.out.print("Numéro compte: ");
                        String num = scanner.nextLine();
                        System.out.print("Type (1=COURANT, 2=EPARGNE): ");
                        int t = scanner.nextInt();
                        scanner.nextLine();
                        TypeCompte type = (t == 1) ? TypeCompte.COURANT : TypeCompte.EPARGNE;
                        bank.addAccountToClient(id, num, type, 0);
                        System.out.println("Account createdc.");
                        break;
                    case 2:
                        System.out.print("ID client: ");
                        String idC = scanner.nextLine();
                        System.out.print("Numéro compte: ");
                        String numC = scanner.nextLine();
                        bank.closeAccount(idC, numC);
                        System.out.println("account is closed.");
                        break;
                    case 3:
                        System.out.print("ID client: ");
                        String idU = scanner.nextLine();
                        System.out.print("new last name: ");
                        String n = scanner.nextLine();
                        System.out.print("new first name: ");
                        String p = scanner.nextLine();
                        System.out.print("new email: ");
                        String e = scanner.nextLine();
                        bank.updateClientInfo(idU, n, p, e);
                        System.out.println("infos updated ");
                        break;
                    case 4:
                        System.out.print("ID client: ");
                        String idS = scanner.nextLine();
                        System.out.print("Numéro compte: ");
                        String numS = scanner.nextLine();
                        List<String> lines = bank.readStatement(idS, numS);
                        for (String line : lines) System.out.println(line);
                        break;
                    case 0: return;
                    default: System.out.println("invalid choice");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }
}




// TESTS FOR ALL THE METHODS BEFORE ADDING THE MENU

//BankService bank = new BankService();
//
//        // Create clients
//        bank.createClient("CL1001", "Doe", "John", "john@example.com", "pwd");
//        bank.createClient("CL1002", "Smith", "Anna", "anna@example.com", "pwd");
//
//        try {
//            // Create accounts
//            bank.addAccountToClient("CL1001", "C1001", TypeCompte.COURANT, 500.0); // initial deposit
//            bank.addAccountToClient("CL1002", "C1002", TypeCompte.COURANT, 100.0);
//
//            // Client actions
//            bank.deposit("CL1001", "C1001", 100.0);   // +100
//            bank.withdraw("CL1001", "C1001", 50.0);   // -50
//            bank.transfer("CL1001", "C1001", "CL1002", "C1002", 200.0); // transfer 200
//
//            // Read statements
//            System.out.println("Statement for C1001:");
//            List<String> s1 = bank.readStatement("CL1001", "C1001");
//            for (String line : s1) System.out.println(line);
//
//            System.out.println("\nStatement for C1002:");
//            List<String> s2 = bank.readStatement("CL1002", "C1002");
//            for (String line : s2) System.out.println(line);
//
//            // Update client info (manager action)
//            bank.updateClientInfo("CL1002", "Smith", "Annabelle", "annabelle@example.com");
//
//        } catch (AccountNotFoundException | NegativeAmountException | InsufficientFundsException | FileAccessException |
//                 IOException e) {
//            System.err.println("Erreur: " + e.getMessage());
//        }
