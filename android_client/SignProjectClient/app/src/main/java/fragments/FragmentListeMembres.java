package fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.florian.signprojectclient.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import activites.AjoutGroupeActivity;
import activites.GroupeActivity;
import adapters.AdapterListViewGroupe;
import adapters.AdapterListViewMembre;
import modeles.modeleBD.GroupeBD;
import utilitaires.RequestPostTask;
import modeles.modele.Utilisateur;
import utilitaires.Config;
import utilitaires.SessionManager;

/**
 * Created by Florian on 15/01/2016.
 */
public class FragmentListeMembres extends DialogFragment {

    private boolean modification;

    private ListView listeMembres;
    private ArrayList<Utilisateur> membres;
    private int id_groupe ;
    private String state ;
    private boolean admin ;
    private AdapterListViewMembre adapterListViewMembre;

    EditText recherche;
    ImageButton valider;

    private AlertDialog.Builder alert;

    public static FragmentListeMembres newInstance(int id_groupe, String state, boolean admin) {
        FragmentListeMembres frag = new FragmentListeMembres();
        Bundle args = new Bundle();
        args.putInt("id_groupe", id_groupe);
        args.putString("state", state);
        args.putBoolean("admin", admin);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_liste_membres, null, false);

        if (Config.isNetworkAvailable(getActivity()))
        {
            this.modification = false;
            this.alert = new AlertDialog.Builder(getActivity());
            this.alert.setTitle(getActivity().getResources().getString(R.string.titre_alert_dialog_erreur));
            this.alert.setIcon(R.drawable.ic_action_error);
            this.alert.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            this.listeMembres = (ListView) view.findViewById(R.id.liste_membres);
            this.id_groupe = getArguments().getInt("id_groupe");
            this.state = getArguments().getString("state");
            this.admin = getArguments().getBoolean("admin");

            this.recherche = (EditText) view.findViewById(R.id.champ_recherche_membre);
            this.valider = (ImageButton) view.findViewById(R.id.button_recherche_membre);

            this.valider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Config.isNetworkAvailable(getActivity()))
                    {
                        String s = recherche.getText().toString();

                        Map<String, String> params = new HashMap<>();
                        params.put("group_id", Integer.toString(id_groupe));
                        params.put("state", state);
                        params.put("search", s);
                        System.out.println("id_groupe : "+id_groupe);
                        Handler mHandler = getMembresHandler();

                        RequestPostTask.sendRequest("getMembers", params, mHandler, getActivity(), getResources().getString(R.string.progress_dialog_titre));
                    }
                    else
                    {
                        displayErrorInternet();
                    }

                }
            });

            Map<String, String> params = new HashMap<>();
            params.put("group_id", Integer.toString(id_groupe));
            params.put("state", state);
            System.out.println("id_groupe : "+id_groupe);
            Handler mHandler = getMembresHandler();

            RequestPostTask.sendRequest("getMembers", params, mHandler, getActivity(), getResources().getString(R.string.progress_dialog_titre));

        }
        else
        {

            displayFatalErrorInternet();
        }
    /*
        this.membres = new ArrayList<>();
        Utilisateur test = new Utilisateur(0,"user","",null,null,null);
        this.membres.add(test);
    */

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setNegativeButton(R.string.btn_alert_dialog_erreur, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(((GroupeActivity) getActivity()).getModification()){
                            ((GroupeActivity) getActivity()).refresh();
                        }
                    }
                });

        return builder.create();
    }

    public void displayErrorInternet(){
        displayAlertErrorInternet();
    }

    public void displayFatalErrorInternet(){
        alert.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                FragmentListeMembres.this.dismiss();
            }
        });
        displayAlertErrorInternet();
    }

    private void displayAlertErrorInternet(){
        alert.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
        AlertDialog alertInternet = alert.create();
        alertInternet.show();
    }

    public ArrayList<Utilisateur> jsonToListMembres(JSONArray j){
        Map<String, Object> out = new HashMap<String, Object>();
        JSONObject jsonobj;
        ArrayList<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
        try{
            for (int i = 0; i < j.length(); i++) {

                jsonobj = j.getJSONObject(i);
                Utilisateur u = new Utilisateur();

                int id =   jsonobj.getInt("id");
                String pseudo = jsonobj.getString("pseudo");

                u.setId(id);
                u.setPseudo(pseudo);

                    /*
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        g.setDate(df.parse(c.getString(c.getColumnIndex(DATE_SIGNALEMENT))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    */
                utilisateurs.add(u);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return utilisateurs;

    }

    public Handler getMembresHandler() {
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {

                    Object json = new JSONTokener((String) msg.obj).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject((String) msg.obj);
                        if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                        {
                            alert.setMessage(getResources().getString(R.string.erreur_serveur));
                            AlertDialog alertInscriptionInvalide = alert.create();
                            alertInscriptionInvalide.show();
                        }
                    }
                    else if (json instanceof JSONArray){
                        JSONArray jsonArray = new JSONArray((String) msg.obj);

                        membres = jsonToListMembres(jsonArray);
                        adapterListViewMembre = new AdapterListViewMembre(getActivity(), id_groupe, membres, state, admin);
                        listeMembres.setAdapter(adapterListViewMembre);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        return mHandler;
    }

}
