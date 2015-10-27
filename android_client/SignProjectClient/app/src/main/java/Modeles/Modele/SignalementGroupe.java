package Modeles.Modele;

/**
 * Created by Axel_2 on 27/10/2015.
 */
public class SignalementGroupe extends Signalement {

    protected Groupe recepteur;

    public SignalementGroupe(int id, String contenu, String remarques, String date, TypeSignalement type, Arret arret, Utilisateur emetteur, Groupe recepteur) {
        super(id, contenu, remarques, date, type, arret, emetteur);
        this.recepteur = recepteur;
    }

    public Groupe getRecepteur() {
        return recepteur;
    }

    public void setRecepteur(Groupe recepteur) {
        this.recepteur = recepteur;
    }

    @Override
    public String toString() {
        return "SignalementGroupe{" +
                "recepteur=" + recepteur +
                '}';
    }
}
