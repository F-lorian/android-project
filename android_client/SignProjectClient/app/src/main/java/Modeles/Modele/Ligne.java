package Modeles.Modele;

import java.util.ArrayList;

/**
 * Created by Axel_2 on 27/10/2015.
 */
public class Ligne {

    protected int id;
    protected String nom;
    protected String ville;
    protected ArrayList<Arret> arrets;

    public Ligne(int id, String nom, String ville, ArrayList<Arret> arrets) {
        this.id = id;
        this.nom = nom;
        this.ville = ville;
        this.arrets = arrets;
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

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }


    public ArrayList<Arret> getArrets() {
        return arrets;
    }

    public void setArrets(ArrayList<Arret> arrets) {
        this.arrets = arrets;
    }

    @Override
    public String toString() {
        return "Ligne{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", ville='" + ville + '\'' +
                ", arrets=" + arrets +
                '}';
    }
}
