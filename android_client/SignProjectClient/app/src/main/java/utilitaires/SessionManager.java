package utilitaires;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import activites.AccueilActivity;

/**
 * Created by Axel_2 on 26/11/2015.
 */
public class SessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AndroidSessionUserPref";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_ID_USER = "id";

    public static final String KEY_PSEUDO_USER = "pseudo";

    public static final String KEY_REGID_GSM = "regid";

    // Constructor
    public SessionManager(Context context){
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(int id, String pseudo, String regidGSM){

        editor.putBoolean(IS_LOGIN, true);

        editor.putInt(KEY_ID_USER, id);

        editor.putString(KEY_PSEUDO_USER, pseudo);

        editor.putString(KEY_REGID_GSM, regidGSM);

        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Accueil Activity
            Intent i = new Intent(context, AccueilActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);
        }

    }

    /**
     * Get the id of user session
     * @return
     */
    public int getUserId()
    {
        return pref.getInt(KEY_ID_USER,-1);
    }

    /**
     * Get the pseudo of user session
     * @return
     */
    public String getUserPseudo()
    {
        return pref.getString(KEY_PSEUDO_USER,null);
    }

    public String getRegidGsm() {
        return pref.getString(KEY_REGID_GSM,null);
    }
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Accueil Activity
        Intent i = new Intent(context, AccueilActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}