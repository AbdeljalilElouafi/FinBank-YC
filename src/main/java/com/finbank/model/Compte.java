package main.java.com.finbank.model;

import main.java.com.finbank.exceptions.FileAccessException;
import main.java.com.finbank.exceptions.NegativeAmountException;
import main.java.com.finbank.exceptions.InsufficientFundsException;

import javax.naming.InsufficientResourcesException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;



public class Compte {
    private String numeroCompte;
    private double solde;
    private TypeCompte typeCompte;
    private HashSet<Transaction> historiqueTransactions = new HashSet<>();

    public Compte(String numeroCompte, TypeCompte typeCompte, double soldeInitial) {
        this.numeroCompte = numeroCompte;
        this.typeCompte = typeCompte;
        this.solde = soldeInitial;
    }

    public String getNumeroCompte() { return numeroCompte; }
    public double getSolde() { return solde; }
    public void setSolde(double solde) { this.solde = solde; }
    public TypeCompte getTypeCompte() { return typeCompte; }

    /**
     * Deposit: validate amount (checking if it's positive), update balance, add transaction, append to file.
     */




    public void deposit(double montant) throws NegativeAmountException, FileAccessException, IOException {
        if(montant <= 0) throw new NegativeArraySizeException("amount should be positive");
        solde += montant;
        Transaction t = new Transaction(TypeTransaction.DEPOT, montant, null, numeroCompte);
        historiqueTransactions.add(t);
        writeTransactionToFile(t);
    }



    /**
     * Withdraw: validate amount and sufficient funds, update balance, add transaction, append to file.
     */




    public void withdraw(double montant) throws NegativeAmountException, InsufficientFundsException, FileAccessException, IOException {
        if(montant <= 0) throw new NegativeAmountException("amount should be positive");
        if(solde < montant) throw new InsufficientFundsException("you don't have this amount");
        solde -= montant;
        Transaction t = new Transaction(TypeTransaction.RETRAIT, montant, numeroCompte, null);
        historiqueTransactions.add(t);
        writeTransactionToFile(t);
    }


    /**
     * Add a transaction to history and append to file (used for transfers).
     * Does not change balance. Balance adjustments should be done by the caller.
     */

    public void addTransactionOnly(Transaction t) throws FileAccessException, IOException {
        historiqueTransactions.add(t);
        writeTransactionToFile(t);
    }

    private String getStatementFilePath() {
        return "data/statements/" + numeroCompte + ".txt";
    }





    private void writeTransactionToFile(Transaction t) throws FileAccessException, IOException {
        File dir = new File ("data/statements");
        if(!dir.exists()) dir.mkdirs();
        File file = new File(getStatementFilePath());
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
            bw.write(t.toString());
            bw.newLine();
        } catch(IOException e){
            throw new FileAccessException("there's an error in writing the file for the account " + numeroCompte, e);

        }
    }


    /**
     * now I read the file lines for the specified account, it should return empty list if there's no file.
     */





    public List<String> readStatement() throws FileAccessException {
        List<String> lines = new ArrayList<>();
        File file = new File(getStatementFilePath());
        if(!file.exists()) return lines;
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                lines.add(line);
            }

            return lines;

        }catch(IOException e){

            throw new FileAccessException("ERROR reading file for the account " + numeroCompte, e);

        }



    }

}
