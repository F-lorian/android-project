package modeles.modele;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Axel_2 on 10/11/2015.
 */
public class SignalementGroupe extends Signalement{

    public static final String type = "groupe";
    protected ArrayList<Groupe> groupesDestinateurs;

    public SignalementGroupe(int id, String contenu, String remarques, Date date, boolean vu, Arret arret, TypeSignalement type, Utilisateur emetteur, ArrayList<Groupe> groupesDestinateurs) {
        super(id, contenu, remarques, date, vu, arret, type, emetteur);
        this.groupesDestinateurs = groupesDestinateurs;
    }

    public SignalementGroupe() {
        super();
    }

    public ArrayList<Groupe> getGroupesDestinateurs() {
        return groupesDestinateurs;
    }

    public void setGroupesDestinateurs(ArrayList<Groupe> groupesDestinateurs) {
        this.groupesDestinateurs = groupesDestinateurs;
    }

    @Override
    public String toString() {
        return "SignalementGroupe{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", remarques='" + remarques + '\'' +
                ", date=" + date +
                ", vu=" + vu +
                ", arret=" + arret +
                ", type=" + type +
                ", emetteur=" + emetteur +
                ", type=" + type +
                ", groupesDestinateurs=" + groupesDestinateurs +
                '}';
    }
}
