package Modeles.Modele;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Axel_2 on 10/11/2015.
 */
public class SignalementPublic extends Signalement{

    public static final String type = "public";
    protected ArrayList<Utilisateur> utilisateursDestinateurs;

    public SignalementPublic(int id, String contenu, String remarques, Date date, Arret arret, TypeSignalement type, Utilisateur emetteur, ArrayList<Utilisateur> utilisateursDestinateurs) {
        super(id, contenu, remarques, date, arret, type, emetteur);
        this.utilisateursDestinateurs = utilisateursDestinateurs;
    }

    public ArrayList<Utilisateur> getUtilisateursDestinateurs() {
        return utilisateursDestinateurs;
    }

    public void setUtilisateursDestinateurs(ArrayList<Utilisateur> utilisateursDestinateurs) {
        this.utilisateursDestinateurs = utilisateursDestinateurs;
    }

    @Override
    public String toString() {
        return "SignalementPublic{" +
                "type=" + type +
                "utilisateursDestinateurs=" + utilisateursDestinateurs +
                '}';
    }
}
