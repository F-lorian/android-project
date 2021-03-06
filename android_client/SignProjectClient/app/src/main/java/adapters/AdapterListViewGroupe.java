package adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.florian.signprojectclient.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import activites.AjoutGroupeActivity;
import activites.GroupeActivity;
import activites.PositionSignalementMapsActivity;
import modeles.modele.Groupe;
import modeles.modele.Signalement;
import modeles.modeleBD.GroupeUtilisateurBD;
import utilitaires.Config;
import utilitaires.SessionManager;

/**
 * Created by Florian on 05/01/2016.
 */
public class AdapterListViewGroupe extends BaseAdapter {

    private List<Groupe> groupes;

    private Activity mActivity;

    private LayoutInflater mInflater;

    private AlertDialog.Builder alert;

    public AdapterListViewGroupe(Activity mActivity, List<Groupe> groupes) {
        this.mActivity = mActivity;
        this.groupes = groupes;
        this.mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return this.groupes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.groupes.get(position);
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
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.adapter_listview_groupes, parent, false);
        }
        else
        {
            layoutItem = (LinearLayout) convertView;
        }

        this.alert = new AlertDialog.Builder(mActivity);
        this.alert.setTitle(mActivity.getResources().getString(R.string.titre_alert_dialog_erreur));
        this.alert.setIcon(R.drawable.ic_action_error);

        TextView nom = (TextView) layoutItem.findViewById(R.id.nom_adapter_groupe);
        ImageView iv = (ImageView) layoutItem.findViewById(R.id.image_type);
        TextView type = (TextView) layoutItem.findViewById(R.id.type_adapter_groupe);
        TextView description = (TextView) layoutItem.findViewById(R.id.description_adapter_groupe);

        LinearLayout groupe = (LinearLayout) layoutItem.findViewById(R.id.layout_groupe);
        TextView etat_utilisateur = (TextView) layoutItem.findViewById(R.id.user_adapter_etat);
        LinearLayout layout_etat = (LinearLayout) layoutItem.findViewById(R.id.layout_state);
        TextView nb_membres = (TextView) layoutItem.findViewById(R.id.members_adapter_groupe);
        TextView nb_demandes = (TextView) layoutItem.findViewById(R.id.nb_demandes);

        nb_demandes.setVisibility(View.GONE);

        String nomGroupe = this.groupes.get(position).getNom();
        //nom.setText(nomGroupe.toUpperCase());
        nom.setText(nomGroupe);
        //nom.setTextColor(Color.BLACK);

        String descriptionGroupe = this.groupes.get(position).getDescription();
        description.setText(descriptionGroupe);

        String typeGroupe = this.groupes.get(position).getType();
        String typeString = "";

        if(typeGroupe.equals(Groupe.TYPE_PUBLIC)){
            iv.setImageDrawable(ContextCompat.getDrawable(this.mActivity, R.drawable.ic_eye));
            typeString = mActivity.getResources().getString(R.string.type_public);
        }
        else if(typeGroupe.equals(Groupe.TYPE_PRIVE)){
            iv.setImageDrawable(ContextCompat.getDrawable(this.mActivity, R.drawable.ic_closed_eye));
            typeString = mActivity.getResources().getString(R.string.type_prive);
        }

        type.setText(typeString);

        nb_membres.setText(Integer.toString(this.groupes.get(position).getNbMembres()));

        groupe.setTag(position);
        groupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Config.isNetworkAvailable(mActivity)) {

                    int indice = ((Integer) v.getTag()).intValue();
                    int id = groupes.get(indice).getId();

                    //aller vers l'activité pour voir un groupe
                    Intent intent = new Intent(mActivity, GroupeActivity.class);
                    intent.putExtra(Config.ID_GROUPE, id);
                    mActivity.startActivityForResult(intent, 1);
                } else{
                    displayErrorInternet();
                }

            }
        });

        SessionManager sessionManager = new SessionManager(mActivity);

        int id_groupe = this.groupes.get(position).getId();
        int id_admin = this.groupes.get(position).getAdmin().getId();
        int idUser = sessionManager.getUserId();


        if (id_admin == idUser) {
            etat_utilisateur.setText(mActivity.getResources().getString(R.string.admin));
            int nb = this.groupes.get(position).getNbDemandes();
            if(nb > 0){
                nb_demandes.setText("+"+nb);
                nb_demandes.setVisibility(View.VISIBLE);
            }
        } else {

            String s = this.groupes.get(position).getUserState();

            if (s != null && s.equals(GroupeUtilisateurBD.ETAT_APPARTIENT)) {
                etat_utilisateur.setText(mActivity.getResources().getString(R.string.membre));
            } else if (s != null && s.equals(GroupeUtilisateurBD.ETAT_ATTENTE)) {
                etat_utilisateur.setText(mActivity.getResources().getString(R.string.en_attente));
            } else if (s != null && s.equals(GroupeUtilisateurBD.ETAT_INVITE)) {
                etat_utilisateur.setText(mActivity.getResources().getString(R.string.invite));
            }else {
                layout_etat.setVisibility(View.GONE);
            }
        }

        return layoutItem;

    }

    public void displayErrorInternet(){
        alert.setNegativeButton(mActivity.getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        displayAlertError(mActivity.getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
    }

    private void displayAlertError(String message){
        alert.setMessage(message);
        AlertDialog alertInternet = alert.create();
        alertInternet.show();
    }
}
