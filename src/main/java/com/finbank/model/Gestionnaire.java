package main.java.com.finbank.model;

public class Gestionnaire extends Person {
    private String idGestionnaire;



    public Gestionnaire(String idGestionnaire, String nom, String prenom, String email, String mtDePasse){
        super(nom, prenom, email, mtDePasse);
        this.idGestionnaire = idGestionnaire;
    }


    public String getIdGestionnaire() { return idGestionnaire; }
}
