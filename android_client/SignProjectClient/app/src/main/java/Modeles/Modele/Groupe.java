package modeles.modele;

import com.example.florian.signprojectclient.R;

import java.util.ArrayList;

/**
 * Created by Axel_2 on 10/11/2015.
 */
public class Groupe {

    protected int id;
    protected String nom;
    protected String type; // Enum type = {public, privé}
    protected String description;
    protected ArrayList<SignalementGroupe> signalements;
    protected ArrayList<Utilisateur> membres;
    protected Utilisateur admin;
    protected int nb_membres;
    protected int nb_demandes;

    public static final String TYPE_PUBLIC = "public";
    public static final String TYPE_PRIVE = "private";

    public Groupe(int id, String nom, String type, ArrayList<SignalementGroupe> signalements, ArrayList<Utilisateur> membres, Utilisateur admin, int nb_membres, int nb_demandes) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.signalements = signalements;
        this.membres = membres;
        this.admin = admin;
        this.nb_membres = nb_membres;
        this.nb_demandes = nb_demandes;
    }

    public Groupe(){

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbDemandes() {
        return nb_demandes;
    }

    public void setNbDemandes(int nb_demandes) {
        this.nb_demandes = nb_demandes;
    }

    public int getNbMembres() {
        return nb_membres;
    }

    public void setNbMembres(int nb_membres) {
        this.nb_membres = nb_membres;
    }

    public ArrayList<SignalementGroupe> getSignalements() {
        return signalements;
    }

    public void setSignalements(ArrayList<SignalementGroupe> signalements) {
        this.signalements = signalements;
    }

    public ArrayList<Utilisateur> getMembres() {
        return membres;
    }

    public void setMembres(ArrayList<Utilisateur> membres) {
        this.membres = membres;
    }

    public Utilisateur getAdmin() {
        return admin;
    }

    public void setAdmin(Utilisateur admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return nom;
    }

    public static boolean nomValide(String nom)
    {
        // chiffre ou MAJ ou MINUS de 6 à 16 caractere
        return nom.matches("^[0-9A-Za-z_-]{6,16}$");
    }

    public static boolean descriptionValide(String description)
    {
        // chiffre ou MAJ ou MINUS de 6 à 16 caractere
        return description.matches("[0-9A-Za-z_-]{0,500}$");
    }

}
