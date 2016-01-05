package utilitaires;

import android.graphics.Color;

/**
 * Created by Axel_2 on 29/12/2015.
 */
public class Config {

    public static final String SERVEUR_URL = "http://florian-m.fr/sign-project/server.php";
    public static final String API_KEY = "AIzaSyA_O8dXv4VMFnIOxNnrIytx7He6k5EhLEQ";

    public static final String CONTROLEUR = "Controleur";
    public static final String HORAIRES = "Horaires";
    public static final String ACCIDENTS = "Accidents";
    public static final String AUTRES = "Autres";

    public static final String TYPE_SIGNALEMENT = "TYPE_SIGNALEMENT";

    public static final float LIGNE_WIDTH = 8;

    public static final int LIGNE_1_COLOR = Color.rgb(54, 68, 219);
    public static final int LIGNE_2_COLOR = Color.rgb(240, 134, 65);
    public static final int LIGNE_3_COLOR = Color.rgb(68, 175, 98);
    public static final int LIGNE_4_COLOR = Color.rgb(75, 54, 34);

    public static final long DISTANCE_MAJ_MIN_TIME = 1000;
    public static final float DISTANCE_MAJ_MIN_DISTANCE = 0;

    public static final long DISTANCE_MAJ_MIN_TIME_SIGNALEMENTS_PROCHES = 5000;
    public static final float DISTANCE_MAJ_MIN_DISTANCE_SIGNALEMENTS_PROCHES = 0;

    public static final float DISTANCE_MAX_SIGNALEMENTS_PROCHES = 3000;


}
