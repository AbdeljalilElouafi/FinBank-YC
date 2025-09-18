package main.java.com.finbank.model;

import java.util.Collection;
import java.util.HashMap;

public class Client extends Person {
    private String idClient;
    private HashMap<String, Compte> comptes = new HashMap<>();

    public Client(String idClient, String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        this.idClient = idClient;
    }


    public String getIdClient() { return idClient; }

    public void addCompte(Compte compte) {
        comptes.put(compte.getNumeroCompte(), compte);
    }

    public Compte getCompte(String numero) {
        return comptes.get(numero);
    }

    public Compte removeCompte(String numero) {
        return comptes.remove(numero);
    }

    public Collection<Compte> getAllComptes() {
        return comptes.values();
    }
}
