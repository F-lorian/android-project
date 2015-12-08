package modeles.ModeleBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import modeles.Modele.Arret;
import modeles.Modele.Ligne;

/**
 * Created by Axel_2 on 11/11/2015.
 */
public class LigneArretBD {

    protected static final String TABLE_NAME = "LIGNE_ARRET";

    public static final String ID="id_la";
    public static final String ID_LIGNE="id_ligne_la";
    public static final String ID_ARRET="id_arret_la";

    public static final String CREATE_TABLE= "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+ID_LIGNE+" INTEGER," +
            " "+ID_ARRET+" INTEGER," +
            " FOREIGN KEY ("+ID_LIGNE+") REFERENCES "+LigneBD.TABLE_NAME+"("+LigneBD.ID_LIGNE+")," +
            " FOREIGN KEY ("+ID_ARRET+") REFERENCES "+ArretBD.TABLE_NAME+"("+ArretBD.ID_ARRET+")" +
            ");";

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public LigneArretBD(Context context)
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

    public long add(int idLigne, int idArret) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(ID_LIGNE, idLigne);
        values.put(ID_ARRET, idArret);

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME, null, values);
    }

    public int deleteByArret(int idArret) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_ARRET + " = ?";
        String[] whereArgs = {idArret + ""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public int deleteByLigne(int idLigne) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_LIGNE + " = ?";
        String[] whereArgs = {idLigne + ""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public ArrayList<Arret> getArrets(int idLigne) {
        // sélection de tous les enregistrements de la table

        String query = "SELECT * FROM "+TABLE_NAME+", "+ArretBD.TABLE_NAME+" WHERE "+ID_LIGNE+"="+idLigne+" AND "+ID_ARRET+"="+ArretBD.ID_ARRET;
        Log.d("query", query);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+", "+ArretBD.TABLE_NAME+" WHERE "+ID_LIGNE+"="+idLigne+" AND "+ID_ARRET+"="+ArretBD.ID_ARRET, null);

        ArrayList<Arret> arrets = new ArrayList<Arret>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Arret a = new Arret(0,"","","",null,null);

                a.setId(c.getInt(c.getColumnIndex(ArretBD.ID_ARRET)));
                a.setNom(c.getString(c.getColumnIndex(ArretBD.NOM_ARRET)));
                a.setCoordonnees(c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)));
                a.setDirection(c.getString(c.getColumnIndex(ArretBD.DIRECTION_ARRET)));

                arrets.add(a);
                c.moveToNext();
            }
        }

        c.close();

        return arrets;

    }

    public ArrayList<Ligne> getLignes(int idArret) {
        // sélection de tous les enregistrements de la table

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+", "+LigneBD.TABLE_NAME+" WHERE "+ID_ARRET+"="+idArret+" AND "+ID_LIGNE+"="+LigneBD.ID_LIGNE, null);

        ArrayList<Ligne> lignes = new ArrayList<Ligne>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Ligne l = new Ligne(0,"","","",null);
                l.setId(c.getInt(c.getColumnIndex(LigneBD.ID_LIGNE)));
                l.setNom(c.getString(c.getColumnIndex(LigneBD.NOM_LIGNE)));
                l.setCoordonnees(c.getString(c.getColumnIndex(LigneBD.COORDONNEES_LIGNE)));
                l.setDescription(c.getString(c.getColumnIndex(LigneBD.DESCRIPTION_LIGNE)));

                lignes.add(l);
                c.moveToNext();
            }
        }

        c.close();

        return lignes;
    }

    public long getCount()
    {
        return DatabaseUtils.queryNumEntries(this.db, TABLE_NAME);
    }
}
