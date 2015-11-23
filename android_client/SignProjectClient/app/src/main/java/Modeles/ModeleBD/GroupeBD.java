package Modeles.ModeleBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import Modeles.Modele.Arret;
import Modeles.Modele.Groupe;
import Modeles.Modele.Utilisateur;

/**
 * Created by Axel_2 on 11/11/2015.
 */
public class GroupeBD {

    protected static final String TABLE_NAME = "GROUPE";
    public static final String ID_GROUPE="id_groupe";
    public static final String NOM_GROUPE="nom_groupe";
    public static final String TYPE_GROUPE="type_groupe";
    public static final String ADMIN_GROUPE="admin_groupe";

    public static final String CREATE_TABLE_ARRET = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+ID_GROUPE+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+NOM_GROUPE+" TEXT," +
            " "+TYPE_GROUPE+" TEXT," +
            " "+ADMIN_GROUPE+" INTEGER," +
            " FOREIGN KEY ("+ADMIN_GROUPE+") REFERENCES "+UtilisateurBD.TABLE_NAME+"("+UtilisateurBD.ID_UTILISATEUR+"));";


    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public GroupeBD(Context context)
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

    public long add(Groupe groupe) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(NOM_GROUPE, groupe.getNom());
        values.put(TYPE_GROUPE, groupe.getType());
        values.put(ADMIN_GROUPE, groupe.getAdmin().getId());

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME,null,values);
    }

    public int update(Groupe groupe) {
        // modification d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la requête

        ContentValues values = new ContentValues();
        values.put(NOM_GROUPE, groupe.getNom());
        values.put(TYPE_GROUPE, groupe.getType());
        values.put(ADMIN_GROUPE, groupe.getAdmin().getId());

        String where = ID_GROUPE+" = ?";
        String[] whereArgs = {groupe.getId()+""};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    public int delete(Groupe groupe) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_GROUPE+" = ?";
        String[] whereArgs = {groupe.getId()+""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public Groupe getGroupe(int id) {
        // Retourne l'enregistrement dont l'id est passé en paramètre

        Groupe g = new Groupe(0,"","",null,null,null);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ID_GROUPE+"="+id+" AND "+ADMIN_GROUPE+"="+UtilisateurBD.ID_UTILISATEUR, null);
        if (c.moveToFirst()) {
            g.setId(c.getInt(c.getColumnIndex(ID_GROUPE)));
            g.setNom(c.getString(c.getColumnIndex(NOM_GROUPE)));
            g.setType(c.getString(c.getColumnIndex(TYPE_GROUPE)));
            g.setAdmin(new Utilisateur(c.getInt(c.getColumnIndex(ADMIN_GROUPE)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));


        }

        c.close();

        return g;
    }

    public ArrayList<Groupe> getGroupes() {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ADMIN_GROUPE+"="+UtilisateurBD.ID_UTILISATEUR, null);

        ArrayList<Groupe> groupes = new ArrayList<Groupe>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Groupe g = new Groupe(0,"","",null,null,null);
                g.setId(c.getInt(c.getColumnIndex(ID_GROUPE)));
                g.setNom(c.getString(c.getColumnIndex(NOM_GROUPE)));
                g.setType(c.getString(c.getColumnIndex(TYPE_GROUPE)));
                g.setAdmin(new Utilisateur(c.getInt(c.getColumnIndex(ADMIN_GROUPE)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));


                groupes.add(g);
                c.moveToNext();
            }
        }

        c.close();

        return groupes;
    }

}
