package modeles.Modele;

import java.util.ArrayList;

/**
 * Created by Axel_2 on 11/11/2015.
 */
public class Ligne {

    protected int id;
    protected String nom;
    protected String coordonnees;
    protected String description;
    protected ArrayList<Arret> arrets;

    public Ligne(int id, String nom, String coordonnees, String description, ArrayList<Arret> arrets) {
        this.id = id;
        this.nom = nom;
        this.coordonnees = coordonnees;
        this.description = description;
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

    public String getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(String coordonnees) {
        this.coordonnees = coordonnees;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                ", coordonnees='" + coordonnees + '\'' +
                ", description='" + description + '\'' +
                ", arrets=" + arrets +
                '}';
    }
}
