package Modeles.Modele;

import java.util.ArrayList;

/**
 * Created by Axel_2 on 27/10/2015.
 */
public class Groupe {

    protected int id;
    protected String nom;
    protected String type;
    protected Utilisateur admin;
    protected ArrayList<Utilisateur> utilisateurs;

    public Groupe(int id, String nom, String type, Utilisateur admin, ArrayList<Utilisateur> utilisateurs) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.admin = admin;
        this.utilisateurs = utilisateurs;
    }

    @Override
    public String toString() {
        return "Groupe{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", admin=" + admin +
                ", utilisateurs=" + utilisateurs +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Utilisateur getAdmin() {
        return admin;
    }

    public void setAdmin(Utilisateur admin) {
        this.admin = admin;
    }

    public ArrayList<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(ArrayList<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }


}
