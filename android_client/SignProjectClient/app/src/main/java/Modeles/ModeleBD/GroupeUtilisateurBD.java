package modeles.modeleBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import modeles.modele.Groupe;
import modeles.modele.Utilisateur;

/**
 * Created by Axel_2 on 11/11/2015.
 */
public class GroupeUtilisateurBD {

    protected static final String TABLE_NAME = "GROUPE_UTILISATEUR";

    public static final String ID="id_gu";
    public static final String ID_UTILISATEUR="id_utilisateur_gu";
    public static final String ID_GROUPE="id_groupe_gu";
    public static final String ETAT_GROUPE="etat_groupe_gu";

    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+ID_UTILISATEUR+" INTEGER," +
            " "+ID_GROUPE+" INTEGER," +
            " "+ETAT_GROUPE+" TEXT," +
            " FOREIGN KEY ("+ID_UTILISATEUR+") REFERENCES "+UtilisateurBD.TABLE_NAME+"("+UtilisateurBD.ID_UTILISATEUR+")," +
            " FOREIGN KEY ("+ID_GROUPE+") REFERENCES "+GroupeBD.TABLE_NAME+"("+GroupeBD.ID_GROUPE+")" +
            ");";

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    public static final String ETAT_ATTENTE = "en attente";
    public static final String ETAT_APPARTIENT = "appartient";

    // Constructeur
    public GroupeUtilisateurBD(Context context)
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

    public long add(long idUtilisateur, long idGroupe, String etat) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(ID_UTILISATEUR, idUtilisateur);
        values.put(ID_GROUPE, idGroupe);
        values.put(ETAT_GROUPE, etat);

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME, null, values);
    }

    public int delete(int idUtilisateur, int idGroupe) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_UTILISATEUR + " = ? AND " + ID_GROUPE + " = ?";
        String[] whereArgs = {idUtilisateur + "",idGroupe + ""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }


    public ArrayList<Utilisateur> getUtilisateur(int idGroupe, String etat) {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+" WHERE "+ID_GROUPE+"="+idGroupe+" AND "+ID_UTILISATEUR+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+ETAT_GROUPE+"='"+etat+"'", null);

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

    public ArrayList<Groupe> getGroupe(int idUtilisateur, String etat) {
        // sélection de tous les enregistrements de la table

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+", "+GroupeBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+" WHERE "+ID_UTILISATEUR+"="+idUtilisateur+" AND "+ID_GROUPE+"="+GroupeBD.ID_GROUPE+" AND "+ETAT_GROUPE+"='"+etat+"' AND "+UtilisateurBD.ID_UTILISATEUR+"="+GroupeBD.ADMIN_GROUPE, null);

        ArrayList<Groupe> groupes = new ArrayList<Groupe>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Groupe g = new Groupe(0,"","",null,null,null);
                g.setId(c.getInt(c.getColumnIndex(GroupeBD.ID_GROUPE)));
                g.setNom(c.getString(c.getColumnIndex(GroupeBD.NOM_GROUPE)));
                g.setType(c.getString(c.getColumnIndex(GroupeBD.TYPE_GROUPE)));
                g.setAdmin(new Utilisateur(c.getInt(c.getColumnIndex(GroupeBD.ADMIN_GROUPE)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));


                groupes.add(g);
                c.moveToNext();
            }
        }

        c.close();

        return groupes;
    }

    public long getCount()
    {
        return DatabaseUtils.queryNumEntries(this.db, TABLE_NAME);
    }

    public String isInGroup(int idUtilisateur, int idGroupe) {
        // sélection de tous les enregistrements de la table

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ID_UTILISATEUR+"="+idUtilisateur+" AND "+ID_GROUPE+"="+idGroupe+" AND "+ETAT_GROUPE+"='"+ETAT_APPARTIENT+"'", null);

        ArrayList<Groupe> groupes = new ArrayList<Groupe>();

        if (c.moveToFirst()) {
            String etat = c.getString(c.getColumnIndex(GroupeUtilisateurBD.ETAT_GROUPE));
            c.close();
            return etat;
        }

        c.close();

        return null;
    }
}
