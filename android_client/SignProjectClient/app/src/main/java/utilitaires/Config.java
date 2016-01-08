package utilitaires;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Axel_2 on 29/12/2015.
 */
public class Config {

    public static final String KML_URL = "http://florian-m.fr/sign-project/tam.kml";
    public static final String SERVEUR_URL = "http://florian-m.fr/sign-project/server.php";
    public static final String API_KEY = "AIzaSyA_O8dXv4VMFnIOxNnrIytx7He6k5EhLEQ";

    public static final String CONTROLEUR = "Controleur";
    public static final String HORAIRES = "Horaires";
    public static final String ACCIDENTS = "Accidents";
    public static final String AUTRES = "Autres";

    public static final String TYPE_SIGNALEMENT = "TYPE_SIGNALEMENT";
    public static final String ID_GROUPE = "ID";

    public static final float LIGNE_WIDTH = 8;

    public static final int LIGNE_1_COLOR = Color.rgb(54, 68, 219);
    public static final int LIGNE_2_COLOR = Color.rgb(240, 134, 65);
    public static final int LIGNE_3_COLOR = Color.rgb(68, 175, 98);
    public static final int LIGNE_4_COLOR = Color.rgb(75, 54, 34);

    public static final long DISTANCE_MAJ_MIN_TIME_NETWORK = 2000;
    public static final float DISTANCE_MAJ_MIN_DISTANCE_NETWORK = 0;


    public static final long DISTANCE_MAJ_MIN_TIME_SIGNALEMENTS_PROCHES_NETWORK = 5000;
    public static final float DISTANCE_MAJ_MIN_DISTANCE_SIGNALEMENTS_PROCHES_NETWORK = 0;

    public static final float DISTANCE_MAX_SIGNALEMENTS_PROCHES = 3000;


    public static final String JSON_STATE = "state";
    public static final String JSON_MESSAGE = "message";
    public static final String JSON_DATA= "data";

    public static final String JSON_SUCCESS = "success";
    public static final String JSON_DENIED = "denied";
    public static final String JSON_ERROR = "error";

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
