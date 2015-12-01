package modeles.Modele;

import java.util.Date;

/**
 * Created by Axel_2 on 10/11/2015.
 */
public abstract class Signalement {

    protected int id;
    protected String contenu;
    protected String remarques;
    protected Date date;

    protected boolean vu;
    protected Arret arret;
    protected TypeSignalement type;
    protected Utilisateur emetteur;

    public Signalement(int id, String contenu, String remarques, Date date, boolean vu, Arret arret, TypeSignalement type, Utilisateur emetteur) {
        this.id = id;
        this.contenu = contenu;
        this.remarques = remarques;
        this.date = date;
        this.vu = vu;
        this.arret = arret;
        this.type = type;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isVu() {
        return vu;
    }

    public void setVu(boolean vu) {
        this.vu = vu;
    }

    public Arret getArret() {
        return arret;
    }

    public void setArret(Arret arret) {
        this.arret = arret;
    }

    public TypeSignalement getType() {
        return type;
    }

    public void setType(TypeSignalement type) {
        this.type = type;
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
                ", date=" + date +
                ", vu=" + vu +
                ", arret=" + arret +
                ", type=" + type +
                ", emetteur=" + emetteur +
                '}';
    }
}
