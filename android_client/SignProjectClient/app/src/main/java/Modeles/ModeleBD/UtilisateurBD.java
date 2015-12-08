package modeles.ModeleBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import modeles.Modele.Utilisateur;

/**
 * Created by Axel_2 on 11/11/2015.
 */
public class UtilisateurBD {

    protected static final String TABLE_NAME = "UTILISATEUR";
    public static final String ID_UTILISATEUR="id_utilisateur";
    public static final String PSEUDO_UTILISATEUR="pseudo_utilisateur";

    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+ID_UTILISATEUR+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+PSEUDO_UTILISATEUR+" TEXT" +
            ");";

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public UtilisateurBD(Context context)
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

    public long add(Utilisateur utilisateur) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(PSEUDO_UTILISATEUR, utilisateur.getPseudo());

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME,null,values);
    }

    public int update(Utilisateur utilisateur) {
        // modification d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la requête

        ContentValues values = new ContentValues();
        values.put(PSEUDO_UTILISATEUR, utilisateur.getPseudo());

        String where = ID_UTILISATEUR+" = ?";
        String[] whereArgs = {utilisateur.getId()+""};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    public int delete(Utilisateur utilisateur) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_UTILISATEUR+" = ?";
        String[] whereArgs = {utilisateur.getId()+""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public Utilisateur getUtilisateur(String pseudo) {
        // Retourne l'enregistrement dont l'id est passé en paramètre

        Utilisateur u = new Utilisateur(0,"","",null,null,null);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+PSEUDO_UTILISATEUR+"="+pseudo, null);
        if (c.moveToFirst()) {
            u.setId(c.getInt(c.getColumnIndex(ID_UTILISATEUR)));
            u.setPseudo(c.getString(c.getColumnIndex(PSEUDO_UTILISATEUR)));
        }

        c.close();

        return u;
    }

    public ArrayList<Utilisateur> getUtilisateurs() {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);

        ArrayList<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Utilisateur u = new Utilisateur(0,"","",null,null,null);
                u.setId(c.getInt(c.getColumnIndex(ID_UTILISATEUR)));
                u.setPseudo(c.getString(c.getColumnIndex(PSEUDO_UTILISATEUR)));

                utilisateurs.add(u);
                c.moveToNext();
            }
        }

        c.close();

        return utilisateurs;
    }

}
