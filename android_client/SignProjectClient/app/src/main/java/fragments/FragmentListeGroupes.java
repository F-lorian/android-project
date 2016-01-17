package fragments;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.ListView;

import com.example.florian.signprojectclient.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONTokener;

import activites.AjoutGroupeActivity;
import activites.ModificationGroupeActivity;
import adapters.AdapterListViewGroupe;
import modeles.modele.Groupe;
import utilitaires.RequestPostTask;
import modeles.modele.Utilisateur;
import modeles.modeleBD.GroupeBD;
import utilitaires.Config;
import utilitaires.SessionManager;

/**
 * Created by Florian on 04/01/2016.
 */
public class FragmentListeGroupes extends Fragment {

    ListView listeGroupes;
    FloatingActionButton FabAjoutGroupe;
    ArrayList<Groupe> groupes;
    AdapterListViewGroupe adapterListViewGroupe;

    private AlertDialog.Builder alert;

    public FragmentListeGroupes()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_liste_groupes, container, false);

        this.listeGroupes = (ListView) view.findViewById(R.id.listeGroupes);
        this.FabAjoutGroupe = (FloatingActionButton) view.findViewById(R.id.FabAjoutGroupe);

        this.alert = new AlertDialog.Builder(getActivity());
        this.alert.setTitle(getActivity().getResources().getString(R.string.titre_alert_dialog_erreur));
        this.alert.setIcon(R.drawable.ic_action_error);
        alert.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });


        if (Config.isNetworkAvailable(getActivity()))
        {

            this.FabAjoutGroupe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Config.isNetworkAvailable(getActivity())) {

                        Intent intent = new Intent(getActivity(), AjoutGroupeActivity.class);
                        startActivityForResult(intent, 1);

                    } else

                    {
                        displayErrorInternet();
                    }
                }
            });

            refresh();
        }
        else
        {
            SessionManager sessionManager = new SessionManager(this.getActivity());
            String id_user = Integer.toString(sessionManager.getUserId());

            GroupeBD groupeBD = new GroupeBD(getActivity());
            groupeBD.open();
            this.groupes = groupeBD.getGroupesByIdUser(sessionManager.getUserId());
            groupeBD.close();

            this.adapterListViewGroupe = new AdapterListViewGroupe(this.getActivity(),this.groupes);
            this.listeGroupes.setAdapter(this.adapterListViewGroupe);
        }

        return view;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if(resultCode == Activity.RESULT_OK){
                refresh();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Do nothing
            }
        }
    }


    public void refresh(){
        SessionManager sessionManager = new SessionManager(this.getActivity());
        String id_user = Integer.toString(sessionManager.getUserId());

        Map<String, String> params = new HashMap<>();
        params.put("user_id", id_user);

        Handler mHandler = getGroupesHandler();
        RequestPostTask.sendRequest("getGroups", params, mHandler, getActivity(), getResources().getString(R.string.progress_dialog_titre));
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

                    /*
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        g.setDate(df.parse(c.getString(c.getColumnIndex(DATE_SIGNALEMENT))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    */
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

    public void displayErrorInternet(){
        displayAlertError(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
    }

    private void displayAlertError(String message){
        alert.setMessage(message);
        AlertDialog alertInternet = alert.create();
        alertInternet.show();
    }


}
