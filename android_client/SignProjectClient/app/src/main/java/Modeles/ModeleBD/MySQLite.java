package modeles.modeleBD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Axel_2 on 27/10/2015.
 * Pattern Singleton
 */
public class MySQLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static MySQLite sInstance;

    public static synchronized MySQLite getInstance(Context context) {
        if (sInstance == null) { sInstance = new MySQLite(context); }
        return sInstance;
    }

    private MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Création des tables
        sqLiteDatabase.execSQL(ArretBD.CREATE_TABLE);
        sqLiteDatabase.execSQL(DestinationSignalementGroupeBD.CREATE_TABLE_DESTINATION_SIGNALEMENT_GROUPE_A_ENVOYER);
        sqLiteDatabase.execSQL(DestinationSignalementGroupeBD.CREATE_TABLE_DESTINATION_SIGNALEMENT_GROUPE_RECU);
        sqLiteDatabase.execSQL(DestinationSignalementPublicBD.CREATE_TABLE_DESTINATION_SIGNALEMENT_UTILISATEUR_A_ENVOYER);
        sqLiteDatabase.execSQL(DestinationSignalementPublicBD.CREATE_TABLE_DESTINATION_SIGNALEMENT_UTILISATEUR_RECU);
        sqLiteDatabase.execSQL(GroupeBD.CREATE_TABLE);
        sqLiteDatabase.execSQL(GroupeUtilisateurBD.CREATE_TABLE);
        sqLiteDatabase.execSQL(LigneArretBD.CREATE_TABLE);
        sqLiteDatabase.execSQL(LigneBD.CREATE_TABLE);
        sqLiteDatabase.execSQL(SignalementBD.CREATE_TABLE_SIGNALEMENT_A_ENVOYER);
        sqLiteDatabase.execSQL(SignalementBD.CREATE_TABLE_SIGNALEMENT_RECU);
        sqLiteDatabase.execSQL(TypeSignalementBD.CREATE_TABLE);
        sqLiteDatabase.execSQL(UtilisateurBD.CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        onCreate(sqLiteDatabase);
    }

}
