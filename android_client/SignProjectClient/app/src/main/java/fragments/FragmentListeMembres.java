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
import android.view.ViewGroup;
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
import modeles.modele.Groupe;
import modeles.modele.RequestPostTask;
import modeles.modele.Utilisateur;
import modeles.modeleBD.GroupeBD;
import utilitaires.Config;

/**
 * Created by Florian on 15/01/2016.
 */
public class FragmentListeMembres extends DialogFragment {

    ListView listeMembres;
    FloatingActionButton FabAjoutMembre;
    ArrayList<Utilisateur> membres;
    AdapterListViewMembre adapterListViewMembre;

    private AlertDialog.Builder alert;

    public static FragmentListeMembres newInstance(int id_groupe) {
        FragmentListeMembres frag = new FragmentListeMembres();
        Bundle args = new Bundle();
        args.putInt("id_groupe", id_groupe);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_liste_membres, null, false);

        this.listeMembres = (ListView) view.findViewById(R.id.liste_membres);
        this.FabAjoutMembre = (FloatingActionButton) view.findViewById(R.id.FabAjoutMembre);
        int id_groupe = getArguments().getInt("id_groupe");

        if (Config.isNetworkAvailable(getActivity()))
        {

            this.FabAjoutMembre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Config.isNetworkAvailable(getActivity())) {
                        Intent intent = new Intent(getActivity(), AjoutGroupeActivity.class);
                        startActivity(intent);
                    } else

                    {
                        alert.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
                        AlertDialog alertInscriptionInvalide = alert.create();
                        alertInscriptionInvalide.show();
                    }
                }
            });

            Map<String, String> params = new HashMap<>();
            params.put("group_id", Integer.toString(id_groupe));

            Handler mHandler = getMembresHandler();
            RequestPostTask.sendRequest("getMembers", params, mHandler, getActivity());
        }
        else
        {
            alert.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
            alert.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    FragmentListeMembres.this.dismiss();
                }
            });
            AlertDialog alertInscriptionInvalide = alert.create();
            alertInscriptionInvalide.show();
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
                        ((GroupeActivity) getActivity()).doPositiveClick();
                    }
                });

        return builder.create();
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
                            alert.setMessage(getResources().getString(R.string.message_alert_dialog_inscription_error));
                            AlertDialog alertInscriptionInvalide = alert.create();
                            alertInscriptionInvalide.show();
                        }
                    }
                    else if (json instanceof JSONArray){
                        JSONArray jsonArray = new JSONArray((String) msg.obj);

                        membres = jsonToListMembres(jsonArray);
                        adapterListViewMembre = new AdapterListViewMembre(getActivity(), membres);
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
