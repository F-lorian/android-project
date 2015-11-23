package Modeles.ModeleBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import Modeles.Modele.Arret;
import Modeles.Modele.Ligne;

/**
 * Created by Axel_2 on 11/11/2015.
 */
public class LigneBD {

    protected static final String TABLE_NAME = "LIGNE";
    public static final String ID_LIGNE="id_ligne";
    public static final String NOM_LIGNE="nom_ligne";
    public static final String COORDONNEES_LIGNE="coordonnees_ligne";
    public static final String DESCRIPTION_LIGNE="description_ligne";

    public static final String CREATE_TABLE_LIGNE = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+ID_LIGNE+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+NOM_LIGNE+" TEXT," +
            " "+COORDONNEES_LIGNE+" TEXT," +
            " "+DESCRIPTION_LIGNE+" TEXT" +
            ");";

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public LigneBD(Context context)
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

    public long add(Ligne ligne) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(NOM_LIGNE, ligne.getNom());
        values.put(COORDONNEES_LIGNE, ligne.getCoordonnees());
        values.put(DESCRIPTION_LIGNE, ligne.getDescription());

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME, null, values);
    }

    public Ligne getLigne(int id) {
        // Retourne l'enregistrement dont l'id est passé en paramètre

        Ligne l = new Ligne(0,"","","",null);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ID_LIGNE+"="+id, null);
        if (c.moveToFirst()) {
            l.setId(c.getInt(c.getColumnIndex(ID_LIGNE)));
            l.setNom(c.getString(c.getColumnIndex(NOM_LIGNE)));
            l.setCoordonnees(c.getString(c.getColumnIndex(COORDONNEES_LIGNE)));
            l.setDescription(c.getString(c.getColumnIndex(DESCRIPTION_LIGNE)));

        }

        c.close();

        return l;
    }

    public ArrayList<Ligne> getLignes() {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);

        ArrayList<Ligne> lignes = new ArrayList<Ligne>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Ligne l = new Ligne(0,"","","",null);
                l.setId(c.getInt(c.getColumnIndex(ID_LIGNE)));
                l.setNom(c.getString(c.getColumnIndex(NOM_LIGNE)));
                l.setCoordonnees(c.getString(c.getColumnIndex(COORDONNEES_LIGNE)));
                l.setDescription(c.getString(c.getColumnIndex(DESCRIPTION_LIGNE)));


                lignes.add(l);
                c.moveToNext();
            }
        }

        c.close();

        return lignes;
    }

}
