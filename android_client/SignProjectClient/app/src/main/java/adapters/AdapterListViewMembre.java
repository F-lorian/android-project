package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.florian.signprojectclient.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activites.GroupeActivity;
import fragments.FragmentListeGroupes;
import fragments.FragmentListeMembres;
import modeles.modele.Groupe;
import modeles.modele.Utilisateur;
import modeles.modeleBD.GroupeUtilisateurBD;
import utilitaires.Config;
import utilitaires.RequestPostTask;
import utilitaires.SessionManager;

/**
 * Created by Florian on 15/01/2016.
 */
public class AdapterListViewMembre extends BaseAdapter {

    private List<Utilisateur> membres;
    private int id_groupe;
    private boolean admin;
    private String state;
    private Activity mActivity;
    private LayoutInflater mInflater;
    private AlertDialog.Builder alert;

    public AdapterListViewMembre(Activity mActivity, int id_groupe, List<Utilisateur> membres, String state, boolean admin) {
        this.mActivity = mActivity;
        this.id_groupe = id_groupe;
        this.membres = membres;
        this.admin = admin;
        this.state = state;
        this.mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return this.membres.size();
    }

    @Override
    public Object getItem(int position) {
        return this.membres.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;

        if (convertView == null)
        {
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.adapter_listview_membres, parent, false);
        }
        else
        {
            layoutItem = (LinearLayout) convertView;
        }

        this.alert = new AlertDialog.Builder(mActivity);
        this.alert.setTitle(mActivity.getResources().getString(R.string.titre_alert_dialog_erreur));
        this.alert.setIcon(R.drawable.ic_action_error);

        TextView pseudo = (TextView) layoutItem.findViewById(R.id.pseudo_adapter_membre);
        ImageView accept = (ImageView) layoutItem.findViewById(R.id.image_accept);
        ImageView refuse = (ImageView) layoutItem.findViewById(R.id.image_refuse);
        final LinearLayout membre = (LinearLayout) layoutItem.findViewById(R.id.layout_membre);

        String pseudoText = this.membres.get(position).getPseudo();
        pseudo.setText(pseudoText);

        if(this.admin) {
            membre.setTag(position);
            if(this.state.equals(GroupeUtilisateurBD.ETAT_ATTENTE)) {
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Config.isNetworkAvailable(mActivity))
                        {
                            acceptMember(membre);
                        }
                        else
                        {
                            displayErrorInternet();
                        }

                    }
                });

                refuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Config.isNetworkAvailable(mActivity))
                        {
                            refuseMember(membre);
                        }
                        else
                        {
                            displayErrorInternet();
                        }
                    }
                });
            } else if (this.state.equals(GroupeUtilisateurBD.ETAT_APPARTIENT)){
                accept.setVisibility(View.GONE);
                refuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Config.isNetworkAvailable(mActivity))
                        {
                            removeMember(membre);
                        }
                        else
                        {
                            displayErrorInternet();
                        }
                    }
                });
            }
        }

        return layoutItem;

    }

    public void displayErrorInternet(){
        alert.setMessage(mActivity.getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
        alert.setNegativeButton(mActivity.getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertInternet = alert.create();
        alertInternet.show();
    }

    public void acceptMember(View v){

        int indice = ((Integer) v.getTag()).intValue();
        int id = this.membres.get(indice).getId();

        Map<String, String> params = new HashMap<>();
        params.put("group_id", Integer.toString(this.id_groupe));
        params.put("user_id", Integer.toString(id));

        Handler mHandler = getHandler(v, mActivity.getResources().getString(R.string.ajoute));
        RequestPostTask.sendRequest("acceptMember", params, mHandler, mActivity, mActivity.getResources().getString(R.string.progress_dialog_titre));
    }

    public void refuseMember(View v){

        int indice = ((Integer) v.getTag()).intValue();
        int id = this.membres.get(indice).getId();

        Map<String, String> params = new HashMap<>();
        params.put("group_id", Integer.toString(this.id_groupe));
        params.put("user_id", Integer.toString(id));

        Handler mHandler = getHandler(v, mActivity.getResources().getString(R.string.refuse));
        RequestPostTask.sendRequest("refuseMember", params, mHandler, mActivity, mActivity.getResources().getString(R.string.progress_dialog_titre));
    }

    public void removeMember(View v){

        int indice = ((Integer) v.getTag()).intValue();
        int id = this.membres.get(indice).getId();
        String pseudo = this.membres.get(indice).getPseudo();

        Map<String, String> params = new HashMap<>();
        params.put("group_id", Integer.toString(this.id_groupe));
        params.put("user_id", Integer.toString(id));

        Handler mHandler = getHandler(v, mActivity.getResources().getString(R.string.supprime_du_groupe));
        RequestPostTask.sendRequest("removeMember", params, mHandler, mActivity, mActivity.getResources().getString(R.string.progress_dialog_titre));
    }

    public Handler getHandler(final View v,final String action) {
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {
                    String rp = (String) msg.obj;
                    //System.out.println(" MSG : "+rp);
                    JSONObject jsonObject = new JSONObject(rp);

                    if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                    {
                        alert.setMessage(mActivity.getResources().getString(R.string.erreur_serveur));
                        AlertDialog alertInscriptionInvalide = alert.create();
                        alertInscriptionInvalide.show();
                    }
                    else if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED))
                    {

                    } else {
                        int indice = ((Integer) v.getTag()).intValue();
                        String pseudo = membres.get(indice).getPseudo();
                        //membres.remove(indice);


                        v.setVisibility(View.GONE);

                        Toast.makeText(mActivity, pseudo + " " + action, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        return mHandler;
    }
}
