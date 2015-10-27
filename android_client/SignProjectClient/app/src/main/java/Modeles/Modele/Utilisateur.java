package Modeles.Modele;

import java.util.ArrayList;

/**
 * Created by Axel_2 on 27/10/2015.
 */
public class Utilisateur {

    protected int id;
    protected String pseudo;
    protected String mdp;
    protected ArrayList<Groupe> groupes;


    public Utilisateur(int id, String pseudo, String mdp, ArrayList<Groupe> groupes) {
        this.id = id;
        this.pseudo = pseudo;
        this.mdp = mdp;
        this.groupes = groupes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public ArrayList<Groupe> getGroupes() {
        return groupes;
    }

    public void setGroupes(ArrayList<Groupe> groupes) {
        this.groupes = groupes;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                ", mdp='" + mdp + '\'' +
                ", groupes=" + groupes +
                '}';
    }
}
