package main.java.com.finbank.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * the transaction explained:
 * - when it's a DEPOT: source = null, destination = account
 * - when it's a RETRAIT: source = account, destination = null
 * - when it's a VIREMENT: both filled
 */

public class Transaction {
    private String idTransaction;
    private TypeTransaction type;
    private double montant;
    private LocalDate date;
    private String compteSource;
    private String compteDestination;

    public Transaction(TypeTransaction type, double montant, String compteSource, String compteDestination) {
        this.idTransaction = UUID.randomUUID().toString();
        this.type = type;
        this.montant = montant;
        this.date = LocalDate.now();
        this.compteSource = compteSource;
        this.compteDestination = compteDestination;
    }

    public String getIdTransaction() { return idTransaction; }
    public TypeTransaction getType() { return type; }
    public double getMontant() { return montant; }
    public LocalDate getDate() { return date; }
    public String getCompteSource() { return compteSource; }
    public String getCompteDestination() { return compteDestination; }





    public String toString() {
        String src = (compteSource == null ? "null" : compteSource);
        String dest = (compteDestination == null ? "null" : compteDestination);
        return date + " | " + type + " | " + montant + " | " + src + " | " + dest;
    }




    // equals/hashCode based on id to allow to be used in HashSet



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return idTransaction.equals(that.idTransaction);
    }



    @Override
    public int hashCode() {
        return idTransaction.hashCode();
    }
}
