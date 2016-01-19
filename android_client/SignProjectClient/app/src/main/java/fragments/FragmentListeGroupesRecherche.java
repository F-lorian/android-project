package fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import adapters.AdapterListViewGroupe;
import modeles.modele.Groupe;
import modeles.modele.Utilisateur;
import modeles.modeleBD.GroupeBD;
import utilitaires.Config;
import utilitaires.RequestPostTask;
import utilitaires.SessionManager;

/**
 * Created by Florian on 05/01/2016.
 */
public class FragmentListeGroupesRecherche extends Fragment {

    EditText recherche;
    ImageButton valider;
    ListView listeGroupes;
    ArrayList<Groupe> groupes;
    AdapterListViewGroupe adapterListViewGroupe;

    private AlertDialog.Builder alert;

    public FragmentListeGroupesRecherche()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_liste_groupes_recherche, container, false);

        this.listeGroupes = (ListView) view.findViewById(R.id.listeGroupes);

        this.recherche = (EditText) view.findViewById(R.id.champ_recherche_groupe);
        this.valider = (ImageButton) view.findViewById(R.id.button_recherche_groupe);

        this.alert = new AlertDialog.Builder(getActivity());
        this.alert.setTitle(getActivity().getResources().getString(R.string.titre_alert_dialog_erreur));
        this.alert.setIcon(R.drawable.ic_action_error);
        alert.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        this.valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Config.isNetworkAvailable(getActivity()))
                {

                    //recherche.clearFocus();

                    SessionManager sessionManager = new SessionManager(getActivity());
                    String id_user = Integer.toString(sessionManager.getUserId());
                    String s = FragmentListeGroupesRecherche.this.recherche.getText().toString();

                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", id_user);
                    params.put("search", s);

                    Handler mHandler = getGroupesHandler();
                    RequestPostTask.sendRequest("getGroups", params, mHandler, getActivity(), getResources().getString(R.string.progress_dialog_titre));
                }
                else
                {
                    displayErrorInternet();
                }

            }
        });

        return view;

    }

    public void displayErrorInternet(){
        displayAlertError(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
    }

    private void displayAlertError(String message){
        alert.setMessage(message);
        AlertDialog alertInternet = alert.create();
        alertInternet.show();
    }

    public ArrayList<Groupe> jsonToListGroup(JSONArray j){
        Map<String, Object> out = new HashMap<String, Object>();
        JSONObject jsonobj;
        ArrayList<Groupe> groupes = new ArrayList<Groupe>();
        try{
            for (int i = 0; i < j.length(); i++) {

                jsonobj = j.getJSONObject(i);
                Groupe g = new Groupe();

                String nom = jsonobj.getString("name");
                String type = jsonobj.getString("type");
                String description = jsonobj.getString("description");
                int id =   jsonobj.getInt("id");
                int id_admin = jsonobj.getInt("creator");
                int nb_demandes = jsonobj.getInt("member_request");
                int nb_membres = jsonobj.getInt("nb_member");
                Utilisateur admin = new Utilisateur(id_admin, "", "", null, null, null);
                g.setId(id);
                g.setNom(nom);
                g.setType(type);
                g.setDescription(description);
                g.setAdmin(admin);
                g.setNbDemandes(nb_demandes);
                g.setNbMembres(nb_membres);

                groupes.add(g);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return groupes;

    }

    public Handler getGroupesHandler() {
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {

                    Object json = new JSONTokener((String) msg.obj).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject((String) msg.obj);
                        if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                        {
                            displayAlertError(getResources().getString(R.string.erreur_serveur));
                        }
                    }
                    else if (json instanceof JSONArray){
                        JSONArray jsonArray = new JSONArray((String) msg.obj);

                        groupes = jsonToListGroup(jsonArray);
                        adapterListViewGroupe = new AdapterListViewGroupe(getActivity(), groupes);
                        listeGroupes.setAdapter(adapterListViewGroupe);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        return mHandler;
    }

}
