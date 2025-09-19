package main.java.service;

import main.java.com.finbank.model.*;
import main.java.com.finbank.exceptions.*;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Simple bank service doing:
 * - manage clients
 * - create/close accounts
 * - deposit/withdraw/transfer
 * - read statements
 * All methods throw the simple custom exceptions so the caller (Main) can handle them.
 */



public class BankService {
    private Map<String, Client> clients = new HashMap<>();

    /** Create a client and add to the bank registry */
    public void createClient(String idClient, String nom, String prenom, String email, String motDePasse) {
        Client c = new Client(idClient, nom, prenom, email, motDePasse);
        clients.put(idClient, c);
    }

    public Optional<Client> getClient(String idClient) {
        return Optional.of(clients.get(idClient));
    }

    /** Create an account for a client. If initialDeposit > 0, it will be deposited. */

    public void addAccountToClient(String idClient, String numeroCompte, TypeCompte type, double initialDeposit)
            throws AccountNotFoundException, NegativeAmountException, FileAccessException, IOException {
        Optional<Client> clientOp = getClient(idClient);

        if(clientOp.isEmpty()) throw new AccountNotFoundException("client not found: " + idClient);

        Compte compte = new Compte(numeroCompte, type, 0);
        clientOp.get().addCompte(compte);

        if (initialDeposit > 0) {
            compte.deposit(initialDeposit);
        }

    }







    public void closeAccount(String idClient, String numeroCompte) throws AccountNotFoundException {
        Client c = clients.get(idClient);
        if (c == null) throw new AccountNotFoundException("Client introuvable: " + idClient);
        c.removeCompte(numeroCompte);

    }

    public void deposit(String idClient, String numeroCompte, double montant)
            throws AccountNotFoundException, NegativeAmountException, FileAccessException, IOException {
        Compte compte = findCompteOfClient(idClient, numeroCompte);
        compte.deposit(montant);
    }

    public void withdraw(String idClient, String numeroCompte, double montant)
            throws AccountNotFoundException, NegativeAmountException, InsufficientFundsException, FileAccessException, IOException {
        Compte compte = findCompteOfClient(idClient, numeroCompte);
        compte.withdraw(montant);
    }


    public void transfer(String fromClientId, String fromNumero, String toClientId, String toNumero, double montant)
            throws AccountNotFoundException, NegativeAmountException, InsufficientFundsException, FileAccessException, IOException {
        if (montant <= 0) throw new NegativeAmountException("Montant doit être positif.");
        Compte from = findCompteOfClient(fromClientId, fromNumero);
        Compte to = findCompteOfClient(toClientId, toNumero);

        if (from.getSolde() < montant) throw new InsufficientFundsException("Solde insuffisant pour le virement.");

        // adjust balances
        from.setSolde(from.getSolde() - montant);
        to.setSolde(to.getSolde() + montant);

        // create one transaction and register it in both accounts
        Transaction t = new Transaction(TypeTransaction.VIREMENT, montant, fromNumero, toNumero);
        from.addTransactionOnly(t);
        to.addTransactionOnly(t);
    }


    public java.util.List<String> readStatement(String idClient, String numeroCompte)
            throws AccountNotFoundException, FileAccessException {
        Compte compte = findCompteOfClient(idClient, numeroCompte);
        return compte.readStatement();
    }

    private Compte findCompteOfClient(String idClient, String numeroCompte) throws AccountNotFoundException {
        Client c = clients.get(idClient);
        if (c == null) throw new AccountNotFoundException("Client introuvable: " + idClient);
        Compte compte = c.getCompte(numeroCompte);
        if (compte == null) throw new AccountNotFoundException("Compte introuvable: " + numeroCompte);
        return compte;
    }

    /** Update simple client information */
    public void updateClientInfo(String idClient, String newNom, String newPrenom, String newEmail) throws AccountNotFoundException {
        Client c = clients.get(idClient);
        if (c == null) throw new AccountNotFoundException("Client introuvable: " + idClient);
        c.setNom(newNom);
        c.setPrenom(newPrenom);
        c.setEmail(newEmail);
    }



    public Client loginClient(String email, String motDePasse) {
        for (Client c : clients.values()) {
            if (c.getEmail().equals(email) && c.getMotDePasse().equals(motDePasse)) {
                return c;
            }
        }
        return null;
    }


    private Gestionnaire gestionnaire;

    public void setGestionnaire(Gestionnaire g) {
        this.gestionnaire = g;
    }

    public Gestionnaire loginGestionnaire(String email, String motDePasse) {
        if (gestionnaire != null &&
                gestionnaire.getEmail().equals(email) &&
                gestionnaire.getMotDePasse().equals(motDePasse)) {
            return gestionnaire;
        }
        return null;
    }



}

