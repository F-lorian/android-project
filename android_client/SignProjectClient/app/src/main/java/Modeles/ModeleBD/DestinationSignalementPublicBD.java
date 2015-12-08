package modeles.ModeleBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import modeles.Modele.Arret;
import modeles.Modele.Signalement;
import modeles.Modele.SignalementPublic;
import modeles.Modele.TypeSignalement;
import modeles.Modele.Utilisateur;

/**
 * Created by Axel_2 on 11/11/2015.
 */
public class DestinationSignalementPublicBD {

    protected static final String TABLE_NAME_DESTINATION_SIGNALEMENT_UTILISATEUR_RECU = "DESTINATION_SIGNALEMENT_UTILISATEUR_RECU";

    protected static final String TABLE_NAME_DESTINATION_SIGNALEMENT_UTILISATEUR_A_ENVOYER = "DESTINATION_SIGNALEMENT_UTILISATEUR_A_ENVOYER";

    public static final String ID="id";
    public static final String ID_SIGNALEMENT_UTILISATEUR="id_signalement_utilisateur";
    public static final String ID_UTILISATEUR_DESTINATION="id_utilisateur_destination";

    public static final String CREATE_TABLE_DESTINATION_SIGNALEMENT_UTILISATEUR_RECU = "CREATE TABLE "+TABLE_NAME_DESTINATION_SIGNALEMENT_UTILISATEUR_RECU+
            " (" +
            " "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+ID_SIGNALEMENT_UTILISATEUR+" INTEGER," +
            " "+ID_UTILISATEUR_DESTINATION+" INTEGER," +
            " FOREIGN KEY ("+ID_SIGNALEMENT_UTILISATEUR+") REFERENCES "+SignalementBD.TABLE_NAME_SIGNALEMENT_RECU+"("+SignalementBD.ID_SIGNALEMENT+")," +
            " FOREIGN KEY ("+ID_UTILISATEUR_DESTINATION+") REFERENCES "+UtilisateurBD.TABLE_NAME+"("+UtilisateurBD.ID_UTILISATEUR+")" +
            ");";

    public static final String CREATE_TABLE_DESTINATION_SIGNALEMENT_UTILISATEUR_A_ENVOYER = "CREATE TABLE "+TABLE_NAME_DESTINATION_SIGNALEMENT_UTILISATEUR_A_ENVOYER+
            " (" +
            " "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+ID_SIGNALEMENT_UTILISATEUR+" INTEGER," +
            " "+ID_UTILISATEUR_DESTINATION+" INTEGER," +
            " FOREIGN KEY ("+ID_SIGNALEMENT_UTILISATEUR+") REFERENCES "+SignalementBD.TABLE_NAME_SIGNALEMENT_A_ENVOYER+"("+SignalementBD.ID_SIGNALEMENT+")," +
            " FOREIGN KEY ("+ID_UTILISATEUR_DESTINATION+") REFERENCES "+UtilisateurBD.TABLE_NAME+"("+UtilisateurBD.ID_UTILISATEUR+")" +
            ");";

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public DestinationSignalementPublicBD(Context context)
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

    public long addDestinationSignalementUtilisateur(int idSignalement, int idUtilisateur, String table) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(ID_SIGNALEMENT_UTILISATEUR, idSignalement);
        values.put(ID_UTILISATEUR_DESTINATION, idUtilisateur);

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(table, null, values);
    }

    public int deleteDestinationSignalementUtilisateur(int idSignalement, String table) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_SIGNALEMENT_UTILISATEUR+" = ?";
        String[] whereArgs = {idSignalement+""};

        return db.delete(table, where, whereArgs);
    }

    public ArrayList<Signalement> getSignalements(int idUtilisateur, String table) {
        // sélection de tous les enregistrements de la table

        Cursor c = null;

        if (table.equals(TABLE_NAME_DESTINATION_SIGNALEMENT_UTILISATEUR_RECU))
        {
            c = db.rawQuery("SELECT * FROM "+table+", "+SignalementBD.TABLE_NAME_SIGNALEMENT_RECU+", "+ArretBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+TypeSignalementBD.TABLE_NAME+" WHERE "+ID_UTILISATEUR_DESTINATION+"="+idUtilisateur+" AND "+ID_SIGNALEMENT_UTILISATEUR+"="+SignalementBD.ID_SIGNALEMENT+" AND "+SignalementBD.ARRET_SIGNALEMENT+"="+ArretBD.ID_ARRET+" AND "+SignalementBD.EMETTEUR_SIGNALEMENT+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+SignalementBD.TYPE_SIGNALEMENT+"="+TypeSignalementBD.ID_TYPE_SIGNALEMENT, null);
        }
        else
        {
            c = db.rawQuery("SELECT * FROM "+table+", "+SignalementBD.TABLE_NAME_SIGNALEMENT_A_ENVOYER+", "+ArretBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+TypeSignalementBD.TABLE_NAME+" WHERE "+ID_UTILISATEUR_DESTINATION+"="+idUtilisateur+" AND "+ID_SIGNALEMENT_UTILISATEUR+"="+SignalementBD.ID_SIGNALEMENT+" AND "+SignalementBD.ARRET_SIGNALEMENT+"="+ArretBD.ID_ARRET+" AND "+SignalementBD.EMETTEUR_SIGNALEMENT+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+SignalementBD.TYPE_SIGNALEMENT+"="+TypeSignalementBD.ID_TYPE_SIGNALEMENT, null);
        }

        ArrayList<Signalement> signalements = new ArrayList<Signalement>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Signalement s = new SignalementPublic(0,"","",null,false,null,null,null,null);

                s.setId(c.getInt(c.getColumnIndex(ID_SIGNALEMENT_UTILISATEUR)));
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

                int vu = c.getInt(c.getColumnIndex(SignalementBD.VU_SIGNALEMENT));
                if (vu == 0)
                {
                    s.setVu(false);
                }
                else
                {
                    s.setVu(true);
                }

                signalements.add(s);
                c.moveToNext();
            }
        }

        c.close();

        return signalements;
    }

    public ArrayList<Utilisateur> getUtilisateursSignalement(int idSignalement, String table) {
        // sélection de tous les enregistrements de la table

        Cursor c = db.rawQuery("SELECT * FROM "+table+", "+UtilisateurBD.TABLE_NAME+" WHERE "+ID_SIGNALEMENT_UTILISATEUR+"="+idSignalement+" AND "+ID_UTILISATEUR_DESTINATION+"="+UtilisateurBD.ID_UTILISATEUR, null);

        ArrayList<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Utilisateur u = new Utilisateur(0,"","",null,null,null);

                u.setId(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)));
                u.setPseudo(c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)));

                utilisateurs.add(u);
                c.moveToNext();
            }
        }

        c.close();

        return utilisateurs;
    }

}
