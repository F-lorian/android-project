package Modeles.Modele;

/**
 * Created by Axel_2 on 27/10/2015.
 */
public class SignalementPerso extends Signalement {

    protected Utilisateur recepteur;

    public SignalementPerso(int id, String contenu, String remarques, String date, TypeSignalement type, Arret arret, Utilisateur emetteur, Utilisateur recepteur) {
        super(id, contenu, remarques, date, type, arret, emetteur);
        this.recepteur = recepteur;
    }

    public Utilisateur getRecepteur() {
        return recepteur;
    }

    public void setRecepteur(Utilisateur recepteur) {
        this.recepteur = recepteur;
    }

    @Override
    public String toString() {
        return "SignalementPerso{" +
                "recepteur=" + recepteur +
                '}';
    }
}
