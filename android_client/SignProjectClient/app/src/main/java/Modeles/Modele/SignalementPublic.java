package modeles.modele;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Axel_2 on 10/11/2015.
 */
public class SignalementPublic extends Signalement{

    public static final String TYPE_DESTINATAIRE = "utilisateur";
    protected ArrayList<Utilisateur> utilisateursDestinateurs;

    public SignalementPublic(int id, String contenu, String remarques, Date date, boolean vu, Arret arret, TypeSignalement type, Utilisateur emetteur, ArrayList<Utilisateur> utilisateursDestinateurs) {
        super(id, contenu, remarques, date, vu, arret, type, emetteur);
        this.utilisateursDestinateurs = utilisateursDestinateurs;
    }

    public SignalementPublic() {
        super();
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
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", remarques='" + remarques + '\'' +
                ", date=" + date +
                ", vu=" + vu +
                ", arret=" + arret +
                ", type=" + type +
                ", emetteur=" + emetteur +
                ", TYPE_DESTINATAIRE=" + TYPE_DESTINATAIRE +
                ", utilisateursDestinateurs=" + utilisateursDestinateurs +
                '}';
    }
}
