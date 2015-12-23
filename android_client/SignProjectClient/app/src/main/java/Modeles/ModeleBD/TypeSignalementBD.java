package modeles.modeleBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import modeles.modele.TypeSignalement;

/**
 * Created by Axel_2 on 11/11/2015.
 */
public class TypeSignalementBD {

    protected static final String TABLE_NAME = "TYPE_SIGNALEMENT";
    public static final String ID_TYPE_SIGNALEMENT="id_type_signalement";
    public static final String NOM_TYPE_SIGNALEMENT="nom_type_signalement";


    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+ID_TYPE_SIGNALEMENT+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+NOM_TYPE_SIGNALEMENT+" TEXT" +
            ");";

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public TypeSignalementBD(Context context)
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

    public long add(TypeSignalement typeSignalement) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(NOM_TYPE_SIGNALEMENT, typeSignalement.getType());

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME, null, values);
    }

    public int update(TypeSignalement typeSignalement) {
        // modification d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la requête

        ContentValues values = new ContentValues();
        values.put(NOM_TYPE_SIGNALEMENT, typeSignalement.getType());

        String where = ID_TYPE_SIGNALEMENT+" = ?";
        String[] whereArgs = {typeSignalement.getId()+""};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    public int delete(TypeSignalement typeSignalement) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_TYPE_SIGNALEMENT+" = ?";
        String[] whereArgs = {typeSignalement.getId()+""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public TypeSignalement getTypeSignalement(int id) {
        // Retourne l'enregistrement dont l'id est passé en paramètre

        TypeSignalement t = new TypeSignalement(0,"");

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ID_TYPE_SIGNALEMENT+"="+id, null);
        if (c.moveToFirst()) {
            t.setId(c.getInt(c.getColumnIndex(ID_TYPE_SIGNALEMENT)));
            t.setType(c.getString(c.getColumnIndex(NOM_TYPE_SIGNALEMENT)));

        }

        c.close();

        return t;
    }

    public ArrayList<TypeSignalement> getTypeSignalements() {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        ArrayList<TypeSignalement> typeSignalements = new ArrayList<TypeSignalement>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                TypeSignalement t = new TypeSignalement(0,"");
                t.setId(c.getInt(c.getColumnIndex(ID_TYPE_SIGNALEMENT)));
                t.setType(c.getString(c.getColumnIndex(NOM_TYPE_SIGNALEMENT)));


                typeSignalements.add(t);
                c.moveToNext();
            }
        }

        c.close();

        return typeSignalements;
    }

    public long getCount()
    {
        return DatabaseUtils.queryNumEntries(this.db, TABLE_NAME);
    }

}
