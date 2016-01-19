package modeles.modeleBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import modeles.modele.Arret;
import modeles.modele.Signalement;
import modeles.modele.SignalementGroupe;
import modeles.modele.SignalementPublic;
import modeles.modele.TypeSignalement;
import modeles.modele.Utilisateur;

/**
 * Created by Axel_2 on 11/11/2015.
 */
public class SignalementBD {

    public static final String TABLE_NAME_SIGNALEMENT_RECU = "SIGNALEMENT_RECU";

    public static final String TABLE_NAME_SIGNALEMENT_A_ENVOYER = "SIGNALEMENT_A_ENVOYER";

    public static final String ID_SIGNALEMENT="id_signalement";
    public static final String CONTENU_SIGNALEMENT="nom_signalement";
    public static final String REMARQUE_SIGNALEMENT="coordonnees_signalement";
    public static final String DATE_SIGNALEMENT="date_signalement"; //String au format : YYYY-MM-DD HH:MM:SS.SSS
    public static final String VU_SIGNALEMENT="vu_signalement";
    public static final String TYPE_DIFFUSION_SIGNALEMENT="type_diffusion_signalement";
    public static final String ARRET_SIGNALEMENT="arret_signalement";
    public static final String TYPE_SIGNALEMENT="type_signalement";
    public static final String EMETTEUR_SIGNALEMENT="emetteur_signalement";

    public static final String CREATE_TABLE_SIGNALEMENT_RECU = "CREATE TABLE "+TABLE_NAME_SIGNALEMENT_RECU+
            " (" +
            " "+ID_SIGNALEMENT+" INTEGER PRIMARY KEY," +
            " "+CONTENU_SIGNALEMENT+" TEXT," +
            " "+REMARQUE_SIGNALEMENT+" TEXT," +
            " "+DATE_SIGNALEMENT+" TEXT," +
            " "+VU_SIGNALEMENT+" INTEGER," +
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
            " "+VU_SIGNALEMENT+" INTEGER," +
            " "+TYPE_DIFFUSION_SIGNALEMENT+" TEXT," +
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

    public long addSignalement(Signalement signalement, String table) {
        // Ajout d'un enregistrement dans la table

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        ContentValues values = new ContentValues();

        if (table.equals(TABLE_NAME_SIGNALEMENT_RECU))
        {
            values.put(ID_SIGNALEMENT, signalement.getId());
        }

        values.put(CONTENU_SIGNALEMENT, signalement.getContenu());
        values.put(REMARQUE_SIGNALEMENT, signalement.getRemarques());
        values.put(DATE_SIGNALEMENT, dateFormat.format(signalement.getDate()));
        values.put(VU_SIGNALEMENT, 0);
        values.put(ARRET_SIGNALEMENT, signalement.getArret().getId());
        values.put(TYPE_SIGNALEMENT, signalement.getType().getId());
        values.put(EMETTEUR_SIGNALEMENT, signalement.getEmetteur().getId());

        if (signalement instanceof SignalementGroupe)
        {
            values.put(TYPE_DIFFUSION_SIGNALEMENT, SignalementGroupe.TYPE_DESTINATAIRE);
        }
        else
        {
            values.put(TYPE_DIFFUSION_SIGNALEMENT, SignalementPublic.TYPE_DESTINATAIRE);
        }


        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(table, null, values);
    }

    public int updateSignalement(Signalement signalement, String table) {
        // modification d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la requête

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        ContentValues values = new ContentValues();
        values.put(CONTENU_SIGNALEMENT, signalement.getContenu());
        values.put(REMARQUE_SIGNALEMENT, signalement.getRemarques());
        values.put(DATE_SIGNALEMENT, dateFormat.format(signalement.getDate()));
        values.put(ARRET_SIGNALEMENT, signalement.getArret().getId());
        values.put(TYPE_SIGNALEMENT, signalement.getType().getId());
        values.put(EMETTEUR_SIGNALEMENT, signalement.getEmetteur().getId());

        if (signalement instanceof SignalementGroupe)
        {
            values.put(TYPE_DIFFUSION_SIGNALEMENT, SignalementGroupe.TYPE_DESTINATAIRE);
        }
        else
        {
            values.put(TYPE_DIFFUSION_SIGNALEMENT, SignalementPublic.TYPE_DESTINATAIRE);
        }

        if (signalement.isVu())
        {
            values.put(VU_SIGNALEMENT, 1);
        }
        else
        {
            values.put(VU_SIGNALEMENT, 0);
        }

        String where = ID_SIGNALEMENT+" = ?";
        String[] whereArgs = {signalement.getId()+""};

        return db.update(table, values, where, whereArgs);
    }

    public int deleteSignalement(Signalement signalement, String table) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID_SIGNALEMENT+" = ?";
        String[] whereArgs = {signalement.getId()+""};

        return db.delete(table, where, whereArgs);
    }

    public Signalement getSignalement(int id, String table) {
        // Retourne l'enregistrement dont l'id est passé en paramètre

        Signalement s = null;

        Cursor c = db.rawQuery("SELECT * FROM "+table+", "+ArretBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+TypeSignalementBD.TABLE_NAME+" WHERE "+ID_SIGNALEMENT+"="+id+" AND "+ARRET_SIGNALEMENT+"="+ArretBD.ID_ARRET+" AND "+EMETTEUR_SIGNALEMENT+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+TYPE_SIGNALEMENT+"="+TypeSignalementBD.ID_TYPE_SIGNALEMENT, null);

        if (c.moveToFirst()) {

            String type = c.getString(c.getColumnIndex(TYPE_DIFFUSION_SIGNALEMENT));

            if (type.equals(SignalementGroupe.TYPE_DESTINATAIRE))
            {
                s = new SignalementGroupe(0,"","",null,false,null,null,null,null);
            }
            else
            {
                s = new SignalementPublic(0,"","",null,false,null,null,null,null);
            }

            s.setId(c.getInt(c.getColumnIndex(ID_SIGNALEMENT)));
            s.setContenu(c.getString(c.getColumnIndex(CONTENU_SIGNALEMENT)));
            s.setRemarques(c.getString(c.getColumnIndex(REMARQUE_SIGNALEMENT)));
            s.setArret(new Arret(c.getInt(c.getColumnIndex(ARRET_SIGNALEMENT)), c.getString(c.getColumnIndex(ArretBD.NOM_ARRET)), c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)), c.getString(c.getColumnIndex(ArretBD.DIRECTION_ARRET)), null, null));
            s.setType(new TypeSignalement(c.getInt(c.getColumnIndex(TYPE_SIGNALEMENT)), c.getString(c.getColumnIndex(TypeSignalementBD.NOM_TYPE_SIGNALEMENT))));
            s.setEmetteur(new Utilisateur(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)),c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)),"",null,null,null));

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            try {
                s.setDate(df.parse(c.getString(c.getColumnIndex(DATE_SIGNALEMENT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int vu = c.getInt(c.getColumnIndex(VU_SIGNALEMENT));
            if (vu == 0)
            {
                s.setVu(false);
            }
            else
            {
                s.setVu(true);
            }

        }

        c.close();

        return s;
    }

    public ArrayList<Signalement> getSignalements(String table) {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+table+", "+ArretBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+TypeSignalementBD.TABLE_NAME+" WHERE "+ARRET_SIGNALEMENT+"="+ArretBD.ID_ARRET+" AND "+EMETTEUR_SIGNALEMENT+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+TYPE_SIGNALEMENT+"="+TypeSignalementBD.ID_TYPE_SIGNALEMENT+" ORDER BY datetime("+DATE_SIGNALEMENT+") DESC", null);

        ArrayList<Signalement> signalements = new ArrayList<Signalement>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Signalement s = null;

                String type = c.getString(c.getColumnIndex(TYPE_DIFFUSION_SIGNALEMENT));

                if (type.equals(SignalementGroupe.TYPE_DESTINATAIRE))
                {
                    s = new SignalementGroupe(0,"","",null,false,null,null,null,null);
                }
                else
                {
                    s = new SignalementPublic(0,"","",null,false,null,null,null,null);
                }

                s.setId(c.getInt(c.getColumnIndex(ID_SIGNALEMENT)));
                s.setContenu(c.getString(c.getColumnIndex(CONTENU_SIGNALEMENT)));
                s.setRemarques(c.getString(c.getColumnIndex(REMARQUE_SIGNALEMENT)));
                s.setArret(new Arret(c.getInt(c.getColumnIndex(ARRET_SIGNALEMENT)), c.getString(c.getColumnIndex(ArretBD.NOM_ARRET)), c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)), c.getString(c.getColumnIndex(ArretBD.DIRECTION_ARRET)), null, null));
                s.setType(new TypeSignalement(c.getInt(c.getColumnIndex(TYPE_SIGNALEMENT)), c.getString(c.getColumnIndex(TypeSignalementBD.NOM_TYPE_SIGNALEMENT))));
                s.setEmetteur(new Utilisateur(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                try {
                    s.setDate(df.parse(c.getString(c.getColumnIndex(DATE_SIGNALEMENT))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int vu = c.getInt(c.getColumnIndex(VU_SIGNALEMENT));
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

    public ArrayList<Signalement> getSignalementsByType(String table, String type) {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+table+", "+ArretBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+TypeSignalementBD.TABLE_NAME+" WHERE "+ARRET_SIGNALEMENT+"="+ArretBD.ID_ARRET+" AND "+EMETTEUR_SIGNALEMENT+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+TypeSignalementBD.ID_TYPE_SIGNALEMENT+"="+TYPE_SIGNALEMENT+" AND "+TypeSignalementBD.NOM_TYPE_SIGNALEMENT+"='"+type+"' ORDER BY datetime("+DATE_SIGNALEMENT+") DESC", null);

        ArrayList<Signalement> signalements = new ArrayList<Signalement>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Signalement s = null;

                String typeDestination = c.getString(c.getColumnIndex(TYPE_DIFFUSION_SIGNALEMENT));

                if (typeDestination.equals(SignalementGroupe.TYPE_DESTINATAIRE))
                {
                    s = new SignalementGroupe(0,"","",null,false,null,null,null,null);
                }
                else
                {
                    s = new SignalementPublic(0,"","",null,false,null,null,null,null);
                }

                s.setId(c.getInt(c.getColumnIndex(ID_SIGNALEMENT)));
                s.setContenu(c.getString(c.getColumnIndex(CONTENU_SIGNALEMENT)));
                s.setRemarques(c.getString(c.getColumnIndex(REMARQUE_SIGNALEMENT)));
                s.setArret(new Arret(c.getInt(c.getColumnIndex(ARRET_SIGNALEMENT)), c.getString(c.getColumnIndex(ArretBD.NOM_ARRET)), c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)), c.getString(c.getColumnIndex(ArretBD.DIRECTION_ARRET)), null, null));
                s.setType(new TypeSignalement(c.getInt(c.getColumnIndex(TYPE_SIGNALEMENT)), c.getString(c.getColumnIndex(TypeSignalementBD.NOM_TYPE_SIGNALEMENT))));
                s.setEmetteur(new Utilisateur(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                try {
                    s.setDate(df.parse(c.getString(c.getColumnIndex(DATE_SIGNALEMENT))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int vu = c.getInt(c.getColumnIndex(VU_SIGNALEMENT));
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

    public ArrayList<Signalement> getSignalementsProches(String table, Location location, float distanceMax) {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+table+", "+ArretBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+TypeSignalementBD.TABLE_NAME+" WHERE "+ARRET_SIGNALEMENT+"="+ArretBD.ID_ARRET+" AND "+EMETTEUR_SIGNALEMENT+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+TYPE_SIGNALEMENT+"="+TypeSignalementBD.ID_TYPE_SIGNALEMENT+" ORDER BY datetime("+DATE_SIGNALEMENT+") DESC", null);

        ArrayList<Signalement> signalements = new ArrayList<Signalement>();

        double[] coordinates = new double[2];
        float[] results = new float[1];

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Signalement s = null;

                String[] coordinatesString = c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)).split(",");

                coordinates[0] = Double.valueOf(coordinatesString[1]);
                coordinates[1] = Double.valueOf(coordinatesString[0]);

                Location.distanceBetween(location.getLatitude(), location.getLongitude(), coordinates[0], coordinates[1], results);

                if (results[0] <= distanceMax) {

                    String type = c.getString(c.getColumnIndex(TYPE_DIFFUSION_SIGNALEMENT));

                    if (type.equals(SignalementGroupe.TYPE_DESTINATAIRE)) {
                        s = new SignalementGroupe(0, "", "", null, false, null, null, null, null);
                    } else {
                        s = new SignalementPublic(0, "", "", null, false, null, null, null, null);
                    }

                    s.setId(c.getInt(c.getColumnIndex(ID_SIGNALEMENT)));
                    s.setContenu(c.getString(c.getColumnIndex(CONTENU_SIGNALEMENT)));
                    s.setRemarques(c.getString(c.getColumnIndex(REMARQUE_SIGNALEMENT)));
                    s.setArret(new Arret(c.getInt(c.getColumnIndex(ARRET_SIGNALEMENT)), c.getString(c.getColumnIndex(ArretBD.NOM_ARRET)), c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)), c.getString(c.getColumnIndex(ArretBD.DIRECTION_ARRET)), null, null));
                    s.setType(new TypeSignalement(c.getInt(c.getColumnIndex(TYPE_SIGNALEMENT)), c.getString(c.getColumnIndex(TypeSignalementBD.NOM_TYPE_SIGNALEMENT))));
                    s.setEmetteur(new Utilisateur(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    try {
                        s.setDate(df.parse(c.getString(c.getColumnIndex(DATE_SIGNALEMENT))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    int vu = c.getInt(c.getColumnIndex(VU_SIGNALEMENT));
                    if (vu == 0) {
                        s.setVu(false);
                    } else {
                        s.setVu(true);
                    }


                    signalements.add(s);
                }

                c.moveToNext();
            }
        }

        c.close();

        return signalements;
    }

    public ArrayList<Signalement> getSignalementsProchesNonVu(String table, Location location, float distanceMax) {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+table+", "+ArretBD.TABLE_NAME+", "+UtilisateurBD.TABLE_NAME+", "+TypeSignalementBD.TABLE_NAME+" WHERE "+ARRET_SIGNALEMENT+"="+ArretBD.ID_ARRET+" AND "+EMETTEUR_SIGNALEMENT+"="+UtilisateurBD.ID_UTILISATEUR+" AND "+TYPE_SIGNALEMENT+"="+TypeSignalementBD.ID_TYPE_SIGNALEMENT+" AND "+VU_SIGNALEMENT+"=0 ORDER BY datetime("+DATE_SIGNALEMENT+") DESC", null);

        ArrayList<Signalement> signalements = new ArrayList<Signalement>();

        double[] coordinates = new double[2];
        float[] results = new float[1];

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {

                Signalement s = null;

                String[] coordinatesString = c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)).split(",");

                coordinates[0] = Double.valueOf(coordinatesString[1]);
                coordinates[1] = Double.valueOf(coordinatesString[0]);

                Location.distanceBetween(location.getLatitude(), location.getLongitude(), coordinates[0], coordinates[1], results);

                if (results[0] <= distanceMax) {

                    String type = c.getString(c.getColumnIndex(TYPE_DIFFUSION_SIGNALEMENT));

                    if (type.equals(SignalementGroupe.TYPE_DESTINATAIRE)) {
                        s = new SignalementGroupe(0, "", "", null, false, null, null, null, null);
                    } else {
                        s = new SignalementPublic(0, "", "", null, false, null, null, null, null);
                    }

                    s.setId(c.getInt(c.getColumnIndex(ID_SIGNALEMENT)));
                    s.setContenu(c.getString(c.getColumnIndex(CONTENU_SIGNALEMENT)));
                    s.setRemarques(c.getString(c.getColumnIndex(REMARQUE_SIGNALEMENT)));
                    s.setArret(new Arret(c.getInt(c.getColumnIndex(ARRET_SIGNALEMENT)), c.getString(c.getColumnIndex(ArretBD.NOM_ARRET)), c.getString(c.getColumnIndex(ArretBD.COORDONNEES_ARRET)), c.getString(c.getColumnIndex(ArretBD.DIRECTION_ARRET)), null, null));
                    s.setType(new TypeSignalement(c.getInt(c.getColumnIndex(TYPE_SIGNALEMENT)), c.getString(c.getColumnIndex(TypeSignalementBD.NOM_TYPE_SIGNALEMENT))));
                    s.setEmetteur(new Utilisateur(c.getInt(c.getColumnIndex(UtilisateurBD.ID_UTILISATEUR)), c.getString(c.getColumnIndex(UtilisateurBD.PSEUDO_UTILISATEUR)), "", null, null, null));

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    try {
                        s.setDate(df.parse(c.getString(c.getColumnIndex(DATE_SIGNALEMENT))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    int vu = c.getInt(c.getColumnIndex(VU_SIGNALEMENT));
                    if (vu == 0) {
                        s.setVu(false);
                    } else {
                        s.setVu(true);
                    }

                    signalements.add(s);
                }

                c.moveToNext();
            }
        }

        c.close();

        return signalements;
    }


}
