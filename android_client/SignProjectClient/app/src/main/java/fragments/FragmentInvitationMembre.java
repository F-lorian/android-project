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
import android.widget.Toast;

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
import adapters.AdapterListViewMembre;
import modeles.modele.Utilisateur;
import utilitaires.Config;
import utilitaires.RequestPostTask;

/**
 * Created by Florian on 17/01/2016.
 */
public class FragmentInvitationMembre extends DialogFragment {

    private int id_groupe ;

    EditText champ_invitation_membre;
    ImageButton button_invitation_membre;

    private AlertDialog.Builder alert;

    public static FragmentInvitationMembre newInstance(int id_groupe) {
        FragmentInvitationMembre frag = new FragmentInvitationMembre();
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

        View view = inflater.inflate(R.layout.fragment_invitation_membre, null, false);

        if (Config.isNetworkAvailable(getActivity()))
        {

            this.alert = new AlertDialog.Builder(getActivity());
            this.alert.setTitle(getActivity().getResources().getString(R.string.titre_alert_dialog_erreur));
            this.alert.setIcon(R.drawable.ic_action_error);
            this.alert.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            this.id_groupe = getArguments().getInt("id_groupe");

            this.champ_invitation_membre = (EditText) view.findViewById(R.id.champ_invitation_membre);
            this.button_invitation_membre = (ImageButton) view.findViewById(R.id.button_invitation_membre);

            this.button_invitation_membre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Config.isNetworkAvailable(getActivity())) {
                        String s = champ_invitation_membre.getText().toString();

                        Map<String, String> params = new HashMap<>();
                        params.put("group_id", Integer.toString(id_groupe));
                        params.put("pseudo", s);

                        Handler mHandler = getResponseHandler();

                        RequestPostTask.sendRequest("inviteMember", params, mHandler, getActivity(), getResources().getString(R.string.progress_dialog_titre));
                    } else {
                        displayErrorInternet();
                    }

                }
            });
        }
        else
        {

            displayFatalErrorInternet();
        }

        builder.setView(view)
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
        displayAlertError(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
    }

    public void displayFatalErrorInternet(){
        alert.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                FragmentInvitationMembre.this.dismiss();
            }
        });
        displayAlertError(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
    }

    private void displayAlertError(String message){

        alert.setMessage(message);
        AlertDialog alertInternet = alert.create();
        alertInternet.show();
    }

    public Handler getResponseHandler() {
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {

                    String rp = (String) msg.obj;
                    //System.out.println(" MSG : "+rp);
                    JSONObject jsonObject = new JSONObject(rp);

                    if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED))
                    {
                        displayAlertError(getResources().getString(R.string.erreur_invitation_membre));

                    }
                    else if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                    {
                        displayAlertError(getResources().getString(R.string.erreur_serveur));

                    } else {
                        ((GroupeActivity) getActivity()).setModification(true);
                        Toast.makeText(getActivity(), getResources().getString(R.string.groupe_invitation_ennvoyee), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        return mHandler;
    }

}
