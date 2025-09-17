package main.java.com.finbank.model;

// this abstract class will be extended by client and gestionnaire
public abstract class Person {
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;

    public Person(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // Getters & setters


    public String getNom(String nom) {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }



    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getMotDePasse() { return motDePasse; }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

}
