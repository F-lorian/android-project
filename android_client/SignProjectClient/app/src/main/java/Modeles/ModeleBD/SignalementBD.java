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
public class SignalementBD {

    protected static final String TABLE_NAME_SIGNALEMENT_RECU = "SIGNALEMENT_RECU";

    protected static final String TABLE_NAME_SIGNALEMENT_A_ENVOYER = "SIGNALEMENT_A_ENVOYER";

    public static final String ID_SIGNALEMENT="id_signalement";
    public static final String CONTENU_SIGNALEMENT="nom_signalement";
    public static final String REMARQUE_SIGNALEMENT="coordonnees_signalement";
    public static final String DATE_SIGNALEMENT="description_signalement"; //String au format : YYYY-MM-DD HH:MM:SS.SSS
    public static final String TYPE_DIFFUSION_SIGNALEMENT="type_diffusion_signalement";
    public static final String ARRET_SIGNALEMENT="arret_signalement";
    public static final String TYPE_SIGNALEMENT="type_signalement";
    public static final String EMETTEUR_SIGNALEMENT="emetteur_signalement";

    public static final String CREATE_TABLE_SIGNALEMENT_RECU = "CREATE TABLE "+TABLE_NAME_SIGNALEMENT_RECU+
            " (" +
            " "+ID_SIGNALEMENT+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+CONTENU_SIGNALEMENT+" TEXT," +
            " "+REMARQUE_SIGNALEMENT+" TEXT," +
            " "+DATE_SIGNALEMENT+" TEXT," +
            " "+TYPE_DIFFUSION_SIGNALEMENT+" TEXT," +
            " "+ARRET_SIGNALEMENT+" INTEGER," +
            " "+TYPE_SIGNALEMENT+" INTEGER," +
            " "+EMETTEUR_SIGNALEMENT+" INTEGER," +
            " FOREIGN KEY ("+ARRET_SIGNALEMENT+") REFERENCES "+ArretBD.TABLE_NAME+"("+ArretBD.ID_ARRET+")," +
            " FOREIGN KEY ("+TYPE_SIGNALEMENT+") REFERENCES "+TypeSignalementBD.TABLE_NAME+"("+TypeSignalementBD.ID_TYPE_SIGNALEMENT+")," +
            " FOREIGN KEY ("+EMETTEUR_SIGNALEMENT+") REFERENCES "+UtilisateurBD.TABLE_NAME+"("+UtilisateurBD.ID_UTILISATEUR+")" +
            ");";

    public static final String CREATE_TABLE_SIGNALEMENT_A_ENVOYER = "CREATE TABLE "+TABLE_NAME_SIGNALEMENT_A_ENVOYER+
            " (" +
            " "+ID_SIGNALEMENT+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+CONTENU_SIGNALEMENT+" TEXT," +
            " "+REMARQUE_SIGNALEMENT+" TEXT," +
            " "+DATE_SIGNALEMENT+" TEXT," +
            " "+TYPE_DIFFUSION_SIGNALEMENT+" TEXT" +
            " "+ARRET_SIGNALEMENT+" INTEGER," +
            " "+TYPE_SIGNALEMENT+" INTEGER," +
            " "+EMETTEUR_SIGNALEMENT+" INTEGER," +
            " FOREIGN KEY ("+ARRET_SIGNALEMENT+") REFERENCES "+ArretBD.TABLE_NAME+"("+ArretBD.ID_ARRET+")," +
            " FOREIGN KEY ("+TYPE_SIGNALEMENT+") REFERENCES "+TypeSignalementBD.TABLE_NAME+"("+TypeSignalementBD.ID_TYPE_SIGNALEMENT+")," +
            " FOREIGN KEY ("+EMETTEUR_SIGNALEMENT+") REFERENCES "+UtilisateurBD.TABLE_NAME+"("+UtilisateurBD.ID_UTILISATEUR+")" +
            ");";

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public SignalementBD(Context context)
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

    public long addSignalementRecu(Signalement signalement) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(CONTENU_SIGNALEMENT, signalement.getContenu());
        values.put(REMARQUE_SIGNALEMENT, signalement.getRemarques());
        values.put(DATE_SIGNALEMENT, signalement.getDate().toString());
        values.put(ARRET_SIGNALEMENT, signalement.getArret().getId());
        values.put(TYPE_SIGNALEMENT, signalement.getType().getId());
        values.put(EMETTEUR_SIGNALEMENT, signalement.getEmetteur().getId());

        if (signalement instanceof SignalementGroupe)
        {
            values.put(TYPE_DIFFUSION_SIGNALEMENT, SignalementGroupe.type);
        }
        else
        {
            values.put(TYPE_DIFFUSION_SIGNALEMENT, SignalementPublic.type);
        }

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME_SIGNALEMENT_RECU, null, values);
    }

    public long addSignalementAEnvoyer(Signalement signalement) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(CONTENU_SIGNALEMENT, signalement.getContenu());
        values.put(REMARQUE_SIGNALEMENT, signalement.getRemarques());
        values.put(DATE_SIGNALEMENT, signalement.getDate().toString());
        values.put(ARRET_SIGNALEMENT, signalement.getArret().getId());
        values.put(TYPE_SIGNALEMENT, signalement.getType().getId());
        values.put(EMETTEUR_SIGNALEMENT, signalement.getEmetteur().getId());

        if (signalement instanceof SignalementGroupe)
        {
            values.put(TYPE_DIFFUSION_SIGNALEMENT, SignalementGroupe.type);
        }
        else
        {
            values.put(TYPE_DIFFUSION_SIGNALEMENT, SignalementPublic.type);
        }

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME_SIGNALEMENT_A_ENVOYER, null, values);
    }

    public int updateSignalementRecu(Signalement signalement) {
        // modification d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la requête

        ContentValues values = new ContentValues();
        values.put(CONTENU_SIGNALEMENT, signalement.getContenu());
        values.put(REMARQUE_SIGNALEMENT, signalement.getRemarques());
        values.put(DATE_SIGNALEMENT, signalement.getDate().toString());
        values.put(ARRET_SIGNALEMENT, signalement.getArret().getId());
        values.put(TYPE_SIGNALEMENT, signalement.getType().getId());
        values.put(EMETTEUR_SIGNALEMENT, signalement.getEmetteur().getId());

        if (signalement instanceof SignalementGroupe)
        {
            values.put(TYPE_DIFFUSION_SIGNALEMENT, SignalementGroupe.type);
        }
        else
        {
            values.put(TYPE_DIFFUSION_SIGNALEMENT, SignalementPublic.type);
        }

        String where = ID_SIGNALEMENT+" = ?";
        String[] whereArgs = {signalement.getId()+""};

        return db.update(TABLE_NAME_SIGNALEMENT_RECU, values, where, whereArgs);
    }

    public int updateSignalementAEnvoyer(Signalement signalement) {
        // modification d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la requête

        ContentValues values = new ContentValues();
        values.put(CONTENU_SIGNALEMENT, signalement.getContenu());
        values.put(REMARQUE_SIGNALEMENT, signalement.getRemarques());
        values.put(DATE_SIGNALEMENT, signalement.getDate().toString());
        values.put(ARRET_SIGNALEMENT, signalement.getArret().getId());
        values.put(TYPE_SIGNALEMENT, signalement.getType().getId());
        values.put(EMETTEUR_SIGNALEMENT, signalement.getEmetteur().getId());

        if (signalement instanceof SignalementGroupe)
        {
            values.put(TYPE_DIFFUSION_SIGNALEMENT, SignalementGroupe.type);
        }
        else
        {
            values.put(TYPE_DIFFUSION_SIGNALEMENT, SignalementPublic.type);
        }

        String where = ID_SIGNALEMENT+" = ?";
        String[] whereArgs = {signalement.getId()+""};

        return db.update(TABLE_NAME_SIGNALEMENT_A_ENVOYER, values, where, whereArgs);
    }

    public int deleteSignalementRecu(Signalement signalement) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_SIGNALEMENT+" = ?";
        String[] whereArgs = {signalement.getId()+""};

        return db.delete(TABLE_NAME_SIGNALEMENT_RECU, where, whereArgs);
    }

    public int deleteSignalementAEnvoyer(Signalement signalement) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_SIGNALEMENT+" = ?";
        String[] whereArgs = {signalement.getId()+""};

        return db.delete(TABLE_NAME_SIGNALEMENT_A_ENVOYER, where, whereArgs);
    }

    public Signalement getSignalementRecu(int id) {
        // Retourne l'enregistrement dont l'id est passé en paramètre

        Signalement s = null;

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_SIGNALEMENT_RECU+", "+ArretBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+TypeSignalementBD.TABLE_NAME+" WHERE "+ID_SIGNALEMENT+"="+id+" AND "+ARRET_SIGNALEMENT+"="+ArretBD.ID_ARRET+" AND "+EMETTEUR_SIGNALEMENT+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+TYPE_SIGNALEMENT+"="+TypeSignalementBD.ID_TYPE_SIGNALEMENT, null);

        if (c.moveToFirst()) {

            String type = c.getString(c.getColumnIndex(TYPE_DIFFUSION_SIGNALEMENT));

            if (type.equals(SignalementGroupe.type))
            {
                s = new SignalementGroupe(0,"","",null,null,null,null,null);
            }
            else
            {
                s = new SignalementPublic(0,"","",null,null,null,null,null);
            }

            s.setId(c.getInt(c.getColumnIndex(ID_SIGNALEMENT)));
            s.setContenu(c.getString(c.getColumnIndex(CONTENU_SIGNALEMENT)));
            s.setRemarques(c.getString(c.getColumnIndex(REMARQUE_SIGNALEMENT)));
            s.setArret(new Arret(c.getInt(c.getColumnIndex(ARRET_SIGNALEMENT)), c.getString(c.getColumnIndex(ArretBD.NOM_ARRET)), c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)), c.getString(c.getColumnIndex(ArretBD.DIRECTION_ARRET)), null, null));
            s.setType(new TypeSignalement(c.getInt(c.getColumnIndex(TYPE_SIGNALEMENT)), c.getString(c.getColumnIndex(TypeSignalementBD.NOM_TYPE_SIGNALEMENT))));
            s.setEmetteur(new Utilisateur(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)),c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)),"",null,null,null));

            try {
                s.setDate(new SimpleDateFormat().parse(c.getString(c.getColumnIndex(DATE_SIGNALEMENT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        c.close();

        return s;
    }

    public Signalement getSignalementAEnvoyer(int id) {
        // Retourne l'enregistrement dont l'id est passé en paramètre

        Signalement s = null;

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_SIGNALEMENT_A_ENVOYER+", "+ArretBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+TypeSignalementBD.TABLE_NAME+" WHERE "+ID_SIGNALEMENT+"="+id+" AND "+ARRET_SIGNALEMENT+"="+ArretBD.ID_ARRET+" AND "+EMETTEUR_SIGNALEMENT+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+TYPE_SIGNALEMENT+"="+TypeSignalementBD.ID_TYPE_SIGNALEMENT, null);

        if (c.moveToFirst()) {

            String type = c.getString(c.getColumnIndex(TYPE_DIFFUSION_SIGNALEMENT));

            if (type.equals(SignalementGroupe.type))
            {
                s = new SignalementGroupe(0,"","",null,null,null,null,null);
            }
            else
            {
                s = new SignalementPublic(0,"","",null,null,null,null,null);
            }

            s.setId(c.getInt(c.getColumnIndex(ID_SIGNALEMENT)));
            s.setContenu(c.getString(c.getColumnIndex(CONTENU_SIGNALEMENT)));
            s.setRemarques(c.getString(c.getColumnIndex(REMARQUE_SIGNALEMENT)));
            s.setArret(new Arret(c.getInt(c.getColumnIndex(ARRET_SIGNALEMENT)), c.getString(c.getColumnIndex(ArretBD.NOM_ARRET)), c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)), c.getString(c.getColumnIndex(ArretBD.DIRECTION_ARRET)), null, null));
            s.setType(new TypeSignalement(c.getInt(c.getColumnIndex(TYPE_SIGNALEMENT)), c.getString(c.getColumnIndex(TypeSignalementBD.NOM_TYPE_SIGNALEMENT))));
            s.setEmetteur(new Utilisateur(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));

            try {
                s.setDate(new SimpleDateFormat().parse(c.getString(c.getColumnIndex(DATE_SIGNALEMENT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        c.close();

        return s;
    }

    public ArrayList<Signalement> getSignalementRecu() {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_SIGNALEMENT_RECU+", "+ArretBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+TypeSignalementBD.TABLE_NAME+" WHERE "+ARRET_SIGNALEMENT+"="+ArretBD.ID_ARRET+" AND "+EMETTEUR_SIGNALEMENT+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+TYPE_SIGNALEMENT+"="+TypeSignalementBD.ID_TYPE_SIGNALEMENT, null);

        ArrayList<Signalement> signalements = new ArrayList<Signalement>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Signalement s = null;

                String type = c.getString(c.getColumnIndex(TYPE_DIFFUSION_SIGNALEMENT));

                if (type.equals(SignalementGroupe.type))
                {
                    s = new SignalementGroupe(0,"","",null,null,null,null,null);
                }
                else
                {
                    s = new SignalementPublic(0,"","",null,null,null,null,null);
                }

                s.setId(c.getInt(c.getColumnIndex(ID_SIGNALEMENT)));
                s.setContenu(c.getString(c.getColumnIndex(CONTENU_SIGNALEMENT)));
                s.setRemarques(c.getString(c.getColumnIndex(REMARQUE_SIGNALEMENT)));
                s.setArret(new Arret(c.getInt(c.getColumnIndex(ARRET_SIGNALEMENT)), c.getString(c.getColumnIndex(ArretBD.NOM_ARRET)), c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)), c.getString(c.getColumnIndex(ArretBD.DIRECTION_ARRET)), null, null));
                s.setType(new TypeSignalement(c.getInt(c.getColumnIndex(TYPE_SIGNALEMENT)), c.getString(c.getColumnIndex(TypeSignalementBD.NOM_TYPE_SIGNALEMENT))));
                s.setEmetteur(new Utilisateur(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));


                try {
                    s.setDate(new SimpleDateFormat().parse(c.getString(c.getColumnIndex(DATE_SIGNALEMENT))));
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

    public ArrayList<Signalement> getSignalementAEnvoyer() {
        // sélection de tous les enregistrements de la table


        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_SIGNALEMENT_A_ENVOYER+", "+ArretBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+TypeSignalementBD.TABLE_NAME+" WHERE "+ARRET_SIGNALEMENT+"="+ArretBD.ID_ARRET+" AND "+EMETTEUR_SIGNALEMENT+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+TYPE_SIGNALEMENT+"="+TypeSignalementBD.ID_TYPE_SIGNALEMENT, null);

        ArrayList<Signalement> signalements = new ArrayList<Signalement>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Signalement s = null;

                String type = c.getString(c.getColumnIndex(TYPE_DIFFUSION_SIGNALEMENT));

                if (type.equals(SignalementGroupe.type))
                {
                    s = new SignalementGroupe(0,"","",null,null,null,null,null);
                }
                else
                {
                    s = new SignalementPublic(0,"","",null,null,null,null,null);
                }

                s.setId(c.getInt(c.getColumnIndex(ID_SIGNALEMENT)));
                s.setContenu(c.getString(c.getColumnIndex(CONTENU_SIGNALEMENT)));
                s.setRemarques(c.getString(c.getColumnIndex(REMARQUE_SIGNALEMENT)));
                s.setArret(new Arret(c.getInt(c.getColumnIndex(ARRET_SIGNALEMENT)), c.getString(c.getColumnIndex(ArretBD.NOM_ARRET)), c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)), c.getString(c.getColumnIndex(ArretBD.DIRECTION_ARRET)), null, null));
                s.setType(new TypeSignalement(c.getInt(c.getColumnIndex(TYPE_SIGNALEMENT)), c.getString(c.getColumnIndex(TypeSignalementBD.NOM_TYPE_SIGNALEMENT))));
                s.setEmetteur(new Utilisateur(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));


                try {
                    s.setDate(new SimpleDateFormat().parse(c.getString(c.getColumnIndex(DATE_SIGNALEMENT))));
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

}
