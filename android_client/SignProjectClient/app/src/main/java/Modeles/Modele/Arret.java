package modeles.modele;

import java.util.ArrayList;

/**
 * Created by Axel_2 on 10/11/2015.
 */
public class Arret {

    protected int id;
    protected String nom;
    protected String coordonnees;
    protected String direction;
    protected ArrayList<Signalement> signalements;
    protected ArrayList<Ligne> lignes;

    public Arret(int id, String nom, String coordonnees, String direction, ArrayList<Signalement> signalements, ArrayList<Ligne> lignes) {
        this.id = id;
        this.nom = nom;
        this.coordonnees = coordonnees;
        this.direction = direction;
        this.signalements = signalements;
        this.lignes = lignes;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public ArrayList<Signalement> getSignalements() {
        return signalements;
    }

    public void setSignalements(ArrayList<Signalement> signalements) {
        this.signalements = signalements;
    }

    public ArrayList<Ligne> getLignes() {
        return lignes;
    }

    public void setLignes(ArrayList<Ligne> lignes) {
        this.lignes = lignes;
    }

    @Override
    public String toString() {
        return "Arret{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", coordonnees='" + coordonnees + '\'' +
                ", direction='" + direction + '\'' +
                ", signalements=" + signalements +
                ", lignes=" + lignes +
                '}';
    }
}
