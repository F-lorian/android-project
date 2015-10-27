package Modeles.Modele;

import java.util.ArrayList;

/**
 * Created by Axel_2 on 27/10/2015.
 */
public class Arret {

    protected int id;
    protected String nom;
    protected double longitude;
    protected double latitude;
    protected String direction;
    protected ArrayList<Ligne> lignes;

    public Arret(int id, String nom, double longitude, double latitude, String direction, ArrayList<Ligne> lignes) {
        this.id = id;
        this.nom = nom;
        this.longitude = longitude;
        this.latitude = latitude;
        this.direction = direction;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", direction='" + direction + '\'' +
                ", lignes=" + lignes +
                '}';
    }
}
