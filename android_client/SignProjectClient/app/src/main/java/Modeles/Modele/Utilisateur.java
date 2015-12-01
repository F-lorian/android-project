package modeles.Modele;

import java.util.ArrayList;

/**
 * Created by Axel_2 on 10/11/2015.
 */
public class Utilisateur {

    protected int id;
    protected String pseudo;
    protected String mdp;
    protected ArrayList<SignalementPublic> signalements;
    protected ArrayList<Groupe> groupesAppartient;
    protected ArrayList<Groupe> groupesAdmin;

    public Utilisateur(int id, String pseudo, String mdp, ArrayList<SignalementPublic> signalements, ArrayList<Groupe> groupesAppartient, ArrayList<Groupe> groupesAdmin) {
        this.id = id;
        this.pseudo = pseudo;
        this.mdp = mdp;
        this.signalements = signalements;
        this.groupesAppartient = groupesAppartient;
        this.groupesAdmin = groupesAdmin;
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

    public ArrayList<SignalementPublic> getSignalements() {
        return signalements;
    }

    public void setSignalements(ArrayList<SignalementPublic> signalements) {
        this.signalements = signalements;
    }

    public ArrayList<Groupe> getGroupesAppartient() {
        return groupesAppartient;
    }

    public void setGroupesAppartient(ArrayList<Groupe> groupesAppartient) {
        this.groupesAppartient = groupesAppartient;
    }

    public ArrayList<Groupe> getGroupesAdmin() {
        return groupesAdmin;
    }

    public void setGroupesAdmin(ArrayList<Groupe> groupesAdmin) {
        this.groupesAdmin = groupesAdmin;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                ", mdp='" + mdp + '\'' +
                ", signalements=" + signalements +
                ", groupesAppartient=" + groupesAppartient +
                ", groupesAdmin=" + groupesAdmin +
                '}';
    }

    public static boolean pseudoValide(String pseudo)
    {
        // chiffre ou MAJ ou MINUS de 3 à 16 caractere
        return pseudo.matches("^[0-9A-Za-z_-]{3,16}$");
    }

    public static boolean mdpValide(String mdp)
    {
        // mdp de plus de 8 char + au moins une MAJ + au moins une MINUS + au moins un chiffre
        return mdp.matches("(?=^.{8,}$)(?=.*\\d)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
        //return mdp.length() > 8 && mdp.matches("@[A-Z]@") && mdp.matches("@[a-z]@") && mdp.matches("@[0-9]@");
    }
}
