package Modeles.ModeleBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import Modeles.Modele.Arret;

/**
 * Created by Axel_2 on 11/11/2015.
 */
public class ArretBD {

    protected static final String TABLE_NAME = "ARRET";
    public static final String ID_ARRET="id_arret";
    public static final String NOM_ARRET="nom_arret";
    public static final String COORDONNEES_ARRET="coordonnees_arret";
    public static final String DIRECTION_ARRET="direction_arret";

    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+ID_ARRET+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+NOM_ARRET+" TEXT," +
            " "+COORDONNEES_ARRET+" TEXT," +
            " "+DIRECTION_ARRET+" TEXT" +
            ");";

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public ArretBD(Context context)
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

    public long add(Arret arret) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(NOM_ARRET, arret.getNom());
        values.put(COORDONNEES_ARRET, arret.getCoordonnees());
        values.put(DIRECTION_ARRET, arret.getDirection());

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME,null,values);
    }

    public int update(Arret arret) {
        // modification d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la requête

        ContentValues values = new ContentValues();
        values.put(NOM_ARRET, arret.getNom());
        values.put(COORDONNEES_ARRET, arret.getCoordonnees());
        values.put(DIRECTION_ARRET, arret.getDirection());

        String where = ID_ARRET+" = ?";
        String[] whereArgs = {arret.getId()+""};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    public int delete(Arret arret) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_ARRET+" = ?";
        String[] whereArgs = {arret.getId()+""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public Arret getArret(int id) {
        // Retourne l'enregistrement dont l'id est passé en paramètre

        Arret a = new Arret(0,"","","",null,null);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ID_ARRET+"="+id, null);

        if (c.moveToFirst()) {
            a.setId(c.getInt(c.getColumnIndex(ID_ARRET)));
            a.setNom(c.getString(c.getColumnIndex(NOM_ARRET)));
            a.setCoordonnees(c.getString(c.getColumnIndex(COORDONNEES_ARRET)));
            a.setDirection(c.getString(c.getColumnIndex(DIRECTION_ARRET)));
        }

        c.close();

        return a;
    }

    public ArrayList<Arret> getArrets() {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);

        ArrayList<Arret> arrets = new ArrayList<Arret>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Arret a = new Arret(0,"","","",null,null);
                a.setId(c.getInt(c.getColumnIndex(ID_ARRET)));
                a.setNom(c.getString(c.getColumnIndex(NOM_ARRET)));
                a.setCoordonnees(c.getString(c.getColumnIndex(COORDONNEES_ARRET)));
                a.setDirection(c.getString(c.getColumnIndex(DIRECTION_ARRET)));


                arrets.add(a);
                c.moveToNext();
            }
        }

        c.close();

        return arrets;
    }

}
