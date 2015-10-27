package Modeles.Modele;

/**
 * Created by Axel_2 on 27/10/2015.
 */
public class Signalement {

    protected int id;
    protected String contenu;
    protected String remarques;
    protected String date;
    protected TypeSignalement type;
    protected Arret arret;
    protected Utilisateur emetteur;

    public Signalement(int id, String contenu, String remarques, String date, TypeSignalement type, Arret arret, Utilisateur emetteur) {
        this.id = id;
        this.contenu = contenu;
        this.remarques = remarques;
        this.date = date;
        this.type = type;
        this.arret = arret;
        this.emetteur = emetteur;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TypeSignalement getType() {
        return type;
    }

    public void setType(TypeSignalement type) {
        this.type = type;
    }

    public Arret getArret() {
        return arret;
    }

    public void setArret(Arret arret) {
        this.arret = arret;
    }

    public Utilisateur getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(Utilisateur emetteur) {
        this.emetteur = emetteur;
    }

    @Override
    public String toString() {
        return "Signalement{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", remarques='" + remarques + '\'' +
                ", date='" + date + '\'' +
                ", type=" + type +
                ", arret=" + arret +
                ", emetteur=" + emetteur +
                '}';
    }
}
