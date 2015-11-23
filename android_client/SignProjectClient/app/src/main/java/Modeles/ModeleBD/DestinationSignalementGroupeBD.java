package Modeles.ModeleBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Modeles.Modele.Arret;
import Modeles.Modele.Groupe;
import Modeles.Modele.Ligne;
import Modeles.Modele.Signalement;
import Modeles.Modele.SignalementGroupe;
import Modeles.Modele.SignalementPublic;
import Modeles.Modele.TypeSignalement;
import Modeles.Modele.Utilisateur;

import static java.text.DateFormat.*;

/**
 * Created by Axel_2 on 11/11/2015.
 */
public class DestinationSignalementGroupeBD {

    protected static final String TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_RECU = "DESTINATION_SIGNALEMENT_GROUPE_RECU";

    protected static final String TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_A_ENVOYER = "DESTINATION_SIGNALEMENT_GROUPE_A_ENVOYER";

    public static final String ID="id";
    public static final String ID_SIGNALEMENT_GROUPE="id_signalement_groupe";
    public static final String ID_GROUPE_DESTINATION="id_groupe_destination";

    public static final String CREATE_TABLE_DESTINATION_SIGNALEMENT_GROUPE_RECU = "CREATE TABLE "+TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_RECU+
            " (" +
            " "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+ID_SIGNALEMENT_GROUPE+" INTEGER," +
            " "+ID_GROUPE_DESTINATION+" INTEGER" +
            " FOREIGN KEY ("+ID_SIGNALEMENT_GROUPE+") REFERENCES "+SignalementBD.TABLE_NAME_SIGNALEMENT_RECU+"("+SignalementBD.ID_SIGNALEMENT+")," +
            " FOREIGN KEY ("+ID_GROUPE_DESTINATION+") REFERENCES "+GroupeBD.TABLE_NAME+"("+GroupeBD.ID_GROUPE+")" +
            ");";

    public static final String CREATE_TABLE_DESTINATION_SIGNALEMENT_GROUPE_A_ENVOYER = "CREATE TABLE "+TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_A_ENVOYER+
            " (" +
            " "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+ID_SIGNALEMENT_GROUPE+" INTEGER," +
            " "+ID_GROUPE_DESTINATION+" INTEGER" +
            " FOREIGN KEY ("+ID_SIGNALEMENT_GROUPE+") REFERENCES "+SignalementBD.TABLE_NAME_SIGNALEMENT_A_ENVOYER+"("+SignalementBD.ID_SIGNALEMENT+")," +
            " FOREIGN KEY ("+ID_GROUPE_DESTINATION+") REFERENCES "+GroupeBD.TABLE_NAME+"("+GroupeBD.ID_GROUPE+")" +
            ");";

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public DestinationSignalementGroupeBD(Context context)
    {
        maBaseSQLite = MySQLite.getInstance(context);
    }

    public void open()
    {
        //on ouvre la table en lecture/écriture
        db = maBaseSQLite.getWritableDatabase();
    }

    public void close()
    {
        //on ferme l'accès à la BDD
        db.close();
    }

    public long addDestinationSignalementGroupeRecu(int idSignalement, int idGroupe) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(ID_SIGNALEMENT_GROUPE, idSignalement);
        values.put(ID_GROUPE_DESTINATION, idGroupe);

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_RECU, null, values);
    }

    public long addDestinationSignalementGroupeAEnvoyer(int idSignalement, int idGroupe) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(ID_SIGNALEMENT_GROUPE, idSignalement);
        values.put(ID_GROUPE_DESTINATION, idGroupe);

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_A_ENVOYER, null, values);
    }

    public int deleteDestinationSignalementGroupeRecu(int idSignalement) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_SIGNALEMENT_GROUPE+" = ?";
        String[] whereArgs = {idSignalement+""};

        return db.delete(TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_RECU, where, whereArgs);
    }

    public int deleteDestinationSignalementGroupeAEnvoyer(int idSignalement) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_SIGNALEMENT_GROUPE+" = ?";
        String[] whereArgs = {idSignalement+""};

        return db.delete(TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_A_ENVOYER, where, whereArgs);
    }

    public ArrayList<Signalement> getSignalementRecu(int idGroupe) {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_RECU+", "+SignalementBD.TABLE_NAME_SIGNALEMENT_RECU+", "+ArretBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+TypeSignalementBD.TABLE_NAME+" WHERE "+ID_GROUPE_DESTINATION+"="+idGroupe+" AND "+ID_SIGNALEMENT_GROUPE+"="+SignalementBD.ID_SIGNALEMENT+" AND "+SignalementBD.ARRET_SIGNALEMENT+"="+ArretBD.ID_ARRET+" AND "+SignalementBD.EMETTEUR_SIGNALEMENT+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+SignalementBD.TYPE_SIGNALEMENT+"="+TypeSignalementBD.ID_TYPE_SIGNALEMENT, null);

        ArrayList<Signalement> signalements = new ArrayList<Signalement>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Signalement s = new SignalementGroupe(0,"","",null,null,null,null,null);

                s.setId(c.getInt(c.getColumnIndex(ID_SIGNALEMENT_GROUPE)));
                s.setContenu(c.getString(c.getColumnIndex(SignalementBD.CONTENU_SIGNALEMENT)));
                s.setRemarques(c.getString(c.getColumnIndex(SignalementBD.REMARQUE_SIGNALEMENT)));
                s.setArret(new Arret(c.getInt(c.getColumnIndex(SignalementBD.ARRET_SIGNALEMENT)), c.getString(c.getColumnIndex(ArretBD.NOM_ARRET)), c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)), c.getString(c.getColumnIndex(ArretBD.DIRECTION_ARRET)), null, null));
                s.setType(new TypeSignalement(c.getInt(c.getColumnIndex(SignalementBD.TYPE_SIGNALEMENT)), c.getString(c.getColumnIndex(TypeSignalementBD.NOM_TYPE_SIGNALEMENT))));
                s.setEmetteur(new Utilisateur(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));


                try {
                    s.setDate(new SimpleDateFormat().parse(c.getString(c.getColumnIndex(SignalementBD.DATE_SIGNALEMENT))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                signalements.add(s);
                c.moveToNext();
            }
        }

        c.close();

        return signalements;
    }

    public ArrayList<Signalement> getSignalementAEnvoyer(int idGroupe) {
        // sélection de tous les enregistrements de la table


        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_A_ENVOYER+", "+SignalementBD.TABLE_NAME_SIGNALEMENT_A_ENVOYER+", "+ArretBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+TypeSignalementBD.TABLE_NAME+" WHERE "+ID_GROUPE_DESTINATION+"="+idGroupe+" AND "+ID_SIGNALEMENT_GROUPE+"="+SignalementBD.ID_SIGNALEMENT+" AND "+SignalementBD.ARRET_SIGNALEMENT+"="+ArretBD.ID_ARRET+" AND "+SignalementBD.EMETTEUR_SIGNALEMENT+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+SignalementBD.TYPE_SIGNALEMENT+"="+TypeSignalementBD.ID_TYPE_SIGNALEMENT, null);

        ArrayList<Signalement> signalements = new ArrayList<Signalement>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Signalement s = new SignalementGroupe(0,"","",null,null,null,null,null);

                s.setId(c.getInt(c.getColumnIndex(ID_SIGNALEMENT_GROUPE)));
                s.setContenu(c.getString(c.getColumnIndex(SignalementBD.CONTENU_SIGNALEMENT)));
                s.setRemarques(c.getString(c.getColumnIndex(SignalementBD.REMARQUE_SIGNALEMENT)));
                s.setArret(new Arret(c.getInt(c.getColumnIndex(SignalementBD.ARRET_SIGNALEMENT)), c.getString(c.getColumnIndex(ArretBD.NOM_ARRET)), c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)), c.getString(c.getColumnIndex(ArretBD.DIRECTION_ARRET)), null, null));
                s.setType(new TypeSignalement(c.getInt(c.getColumnIndex(SignalementBD.TYPE_SIGNALEMENT)), c.getString(c.getColumnIndex(TypeSignalementBD.NOM_TYPE_SIGNALEMENT))));
                s.setEmetteur(new Utilisateur(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));


                try {
                    s.setDate(new SimpleDateFormat().parse(c.getString(c.getColumnIndex(SignalementBD.DATE_SIGNALEMENT))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                signalements.add(s);
                c.moveToNext();
            }
        }

        c.close();

        return signalements;
    }

    public ArrayList<Groupe> getGroupeSignalementRecu(int idSignalement) {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_RECU+", "+GroupeBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+" WHERE "+ID_SIGNALEMENT_GROUPE+"="+idSignalement+" AND "+ID_GROUPE_DESTINATION+"="+GroupeBD.ID_GROUPE+" AND "+GroupeBD.ADMIN_GROUPE+"="+UtilisateurBD.ID_UTILISATEUR, null);

        ArrayList<Groupe> groupes = new ArrayList<Groupe>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Groupe g = new Groupe(0,"","",null,null,null);

                g.setId(c.getInt(c.getColumnIndex(GroupeBD.ID_GROUPE)));
                g.setNom(c.getString(c.getColumnIndex(GroupeBD.NOM_GROUPE)));
                g.setType(c.getString(c.getColumnIndex(GroupeBD.TYPE_GROUPE)));
                g.setAdmin(new Utilisateur(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));

                groupes.add(g);
                c.moveToNext();
            }
        }

        c.close();

        return groupes;
    }

    public ArrayList<Groupe> getGroupeSignalementAEnvoyer(int idSignalement) {
        // sélection de tous les enregistrements de la table

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_A_ENVOYER+", "+GroupeBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+" WHERE "+ID_SIGNALEMENT_GROUPE+"="+idSignalement+" AND "+ID_GROUPE_DESTINATION+"="+GroupeBD.ID_GROUPE+" AND "+GroupeBD.ADMIN_GROUPE+"="+UtilisateurBD.ID_UTILISATEUR, null);

        ArrayList<Groupe> groupes = new ArrayList<Groupe>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Groupe g = new Groupe(0,"","",null,null,null);

                g.setId(c.getInt(c.getColumnIndex(GroupeBD.ID_GROUPE)));
                g.setNom(c.getString(c.getColumnIndex(GroupeBD.NOM_GROUPE)));
                g.setType(c.getString(c.getColumnIndex(GroupeBD.TYPE_GROUPE)));
                g.setAdmin(new Utilisateur(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));

                groupes.add(g);
                c.moveToNext();
            }
        }

        c.close();

        return groupes;
    }

}
