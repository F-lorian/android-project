package modeles.modeleBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import modeles.modele.Arret;
import modeles.modele.Groupe;
import modeles.modele.Signalement;
import modeles.modele.SignalementGroupe;
import modeles.modele.SignalementPublic;
import modeles.modele.TypeSignalement;
import modeles.modele.Utilisateur;
import utilitaires.SessionManager;

/**
 * Created by Axel_2 on 11/11/2015.
 */
public class GroupeBD {

    //à vérifier
    private Context context;

    protected static final String TABLE_NAME = "GROUPE";
    public static final String ID_GROUPE="id_groupe";
    public static final String NOM_GROUPE="nom_groupe";
    public static final String TYPE_GROUPE="type_groupe";
    public static final String DESCRIPTION_GROUPE="description_groupe";
    public static final String ADMIN_GROUPE="admin_groupe";

    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+ID_GROUPE+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+NOM_GROUPE+" TEXT," +
            " "+TYPE_GROUPE+" TEXT," +
            " "+DESCRIPTION_GROUPE+" TEXT," +
            " "+ADMIN_GROUPE+" INTEGER," +
            " FOREIGN KEY ("+ADMIN_GROUPE+") REFERENCES "+UtilisateurBD.TABLE_NAME+"("+UtilisateurBD.ID_UTILISATEUR+"));";


    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public GroupeBD(Context context)
    {
        maBaseSQLite = MySQLite.getInstance(context);
        this.context = context;
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
        System.out.println("description : " + groupe.getDescription());

        ContentValues values = new ContentValues();
        values.put(NOM_GROUPE, groupe.getNom());
        values.put(TYPE_GROUPE, groupe.getType());
        values.put(DESCRIPTION_GROUPE, groupe.getDescription());
        values.put(ADMIN_GROUPE, groupe.getAdmin().getId());

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        long id = db.insert(TABLE_NAME,null,values);

        if(id != -1){

            GroupeUtilisateurBD groupUtilisateurBD = new GroupeUtilisateurBD(this.context);
            groupUtilisateurBD.open();
            groupUtilisateurBD.add(groupe.getAdmin().getId(), id, GroupeUtilisateurBD.ETAT_APPARTIENT);
            groupUtilisateurBD.close();
        }

        return id;
    }

    public int update(Groupe groupe) {
        // modification d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la requête

        ContentValues values = new ContentValues();
        values.put(NOM_GROUPE, groupe.getNom());
        values.put(TYPE_GROUPE, groupe.getType());
        values.put(DESCRIPTION_GROUPE, groupe.getDescription());
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

    public Groupe getGroupeAdmin(int id) {
        // Retourne l'enregistrement dont l'id est passé en paramètre

        Groupe g = new Groupe(0,"","",null,null,null);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ID_GROUPE+"="+id+" AND "+ADMIN_GROUPE+"="+UtilisateurBD.ID_UTILISATEUR, null);
        if (c.moveToFirst()) {
            g.setId(c.getInt(c.getColumnIndex(ID_GROUPE)));
            g.setNom(c.getString(c.getColumnIndex(NOM_GROUPE)));
            g.setType(c.getString(c.getColumnIndex(TYPE_GROUPE)));
            g.setDescription(c.getString(c.getColumnIndex(DESCRIPTION_GROUPE)));
            g.setAdmin(new Utilisateur(c.getInt(c.getColumnIndex(ADMIN_GROUPE)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));


        }

        c.close();

        return g;
    }

    public Groupe getGroupe(int id) {
        // Retourne l'enregistrement dont l'id est passé en paramètre

        System.out.println("ID : "+id);
        Groupe g = new Groupe(0,"","",null,null,null);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+" WHERE "+ID_GROUPE+"="+id+" AND "+ADMIN_GROUPE+"="+UtilisateurBD.ID_UTILISATEUR, null);
        if (c.moveToFirst()) {
            g.setId(c.getInt(c.getColumnIndex(ID_GROUPE)));
            g.setNom(c.getString(c.getColumnIndex(NOM_GROUPE)));
            g.setType(c.getString(c.getColumnIndex(TYPE_GROUPE)));
            g.setDescription(c.getString(c.getColumnIndex(DESCRIPTION_GROUPE)));
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
                g.setDescription(c.getString(c.getColumnIndex(DESCRIPTION_GROUPE)));
                g.setAdmin(new Utilisateur(c.getInt(c.getColumnIndex(ADMIN_GROUPE)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));


                groupes.add(g);
                c.moveToNext();
            }
        }

        c.close();

        return groupes;
    }

    public ArrayList<Groupe> getGroupesByIdUser(int idUser) {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+GroupeUtilisateurBD.TABLE_NAME
                + " WHERE "+ID_GROUPE+"="+GroupeUtilisateurBD.ID_GROUPE
                + " AND "+UtilisateurBD.ID_UTILISATEUR+"="+GroupeUtilisateurBD.ID_GROUPE
                //+ " AND "+GroupeUtilisateurBD.ETAT_GROUPE+"="+GroupeUtilisateurBD.ETAT_APPARTIENT
                //+ "' ORDER BY datetime("+DATE_SIGNALEMENT+") DESC"  ajouter la date d'ajout de l'utilisateur au groupe
                , null);

        ArrayList<Groupe> groupes = new ArrayList<Groupe>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Groupe g = new Groupe();

                int id = c.getInt(c.getColumnIndex(ID_GROUPE));
                String nom = c.getString(c.getColumnIndex(NOM_GROUPE));
                String type = c.getString(c.getColumnIndex(TYPE_GROUPE));
                String Description = c.getString(c.getColumnIndex(DESCRIPTION_GROUPE));
                Utilisateur admin = new Utilisateur(c.getInt(c.getColumnIndex(ADMIN_GROUPE)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null);

                g.setId(id);
                g.setNom(nom);
                g.setType(type);
                g.setDescription(Description);
                g.setAdmin(admin);

                /*
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    g.setDate(df.parse(c.getString(c.getColumnIndex(DATE_SIGNALEMENT))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                */

                groupes.add(g);
                c.moveToNext();
            }
        }

        c.close();

        return groupes;
    }


}
