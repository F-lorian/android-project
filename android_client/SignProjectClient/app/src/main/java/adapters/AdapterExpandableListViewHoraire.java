package adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.florian.signprojectclient.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import activites.PositionSignalementMapsActivity;
import modeles.modele.Signalement;
import utilitaires.Config;
import utilitaires.ContenuSignalement;

/**
 * Created by Axel_2 on 26/12/2015.
 */
public class AdapterExpandableListViewHoraire extends BaseExpandableListAdapter{

    private Context context;
    private List<Signalement> signalementsHoraires;
    private HashMap<Signalement,List<String>> listOfChilds;

    public AdapterExpandableListViewHoraire(Context c, List<Signalement> signalements, HashMap<Signalement,List<String>> childs)
    {
        this.context = c;
        this.signalementsHoraires = signalements;
        this.listOfChilds = childs;
    }

    public void setSignalementsHoraires(List<Signalement> signalementsHoraires) {
        this.signalementsHoraires = signalementsHoraires;
    }

    public void setListOfChilds(HashMap<Signalement, List<String>> listOfChilds) {
        this.listOfChilds = listOfChilds;
    }

    @Override
    public int getGroupCount() {
        return this.signalementsHoraires.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listOfChilds.get(this.signalementsHoraires.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.signalementsHoraires.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listOfChilds.get(this.signalementsHoraires.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Signalement signalement = (Signalement) this.getGroup(groupPosition);

        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.adapter_listview_signalements,null);
        }

        TextView titre = (TextView) convertView.findViewById(R.id.titre_adapter_signalement);
        TextView arret = (TextView) convertView.findViewById(R.id.arret_adapter_signalement);
        TextView heure = (TextView) convertView.findViewById(R.id.text_adapter_heure);
        ImageButton positionBtn = (ImageButton) convertView.findViewById(R.id.Btn_adapter_CarteSignalement);
        ImageButton detailBtn = (ImageButton) convertView.findViewById(R.id.Btn_adapter_DetailSignalement);

        String typeSignalement = signalement.getType().getType();

        if (typeSignalement.equals(Config.CONTROLEUR))
        {
            titre.setText(this.context.getResources().getString(R.string.controleur_spinner).toUpperCase());
            titre.setTextColor(Color.RED);
        }
        else if (typeSignalement.equals(Config.AUTRES) || typeSignalement.equals(Config.ACCIDENTS))
        {
            if (typeSignalement.equals(Config.AUTRES))
            {
                titre.setText(this.context.getResources().getString(R.string.autres_spinner).toUpperCase());
            }
            else
            {
                titre.setText(this.context.getResources().getString(R.string.accident_spinner).toUpperCase());
            }
            titre.setTextColor(Color.BLUE);
        }
        else
        {
            titre.setText(this.context.getResources().getString(R.string.horaire_spinner).toUpperCase());
            titre.setTextColor(Color.GREEN);
        }

        ContenuSignalement contenuSignalement = new ContenuSignalement(signalement.getContenu());
        arret.setText(contenuSignalement.getLigneArret());

        Date dt2 = new Date();
        long diff = dt2.getTime() - signalement.getDate().getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);

        heure.setText(diffHours + "h " + diffMinutes + "min " + diffSeconds + "s");

        positionBtn.setTag(groupPosition);
        positionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indice = ((Integer) v.getTag()).intValue();
                int idLigneArret = new ContenuSignalement(AdapterExpandableListViewHoraire.this.signalementsHoraires.get(indice).getContenu()).getIdLigneArret();
                Intent intent = new Intent(AdapterExpandableListViewHoraire.this.context, PositionSignalementMapsActivity.class);
                intent.putExtra(AdapterListViewSimpleSignalement.ID_LIGNE_ARRET, idLigneArret);
                intent.putExtra(AdapterListViewSimpleSignalement.TYPE_SIGNALEMENT, AdapterExpandableListViewHoraire.this.signalementsHoraires.get(indice).getType().getType());
                AdapterExpandableListViewHoraire.this.context.startActivity(intent);
            }
        });

        detailBtn.setTag(groupPosition);
        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = AdapterExpandableListViewHoraire.this.context;

                final Dialog dialogDetailSignalement = new Dialog(c);
                dialogDetailSignalement.setTitle(c.getResources().getString(R.string.dialog_titre_detail_signalement));
                dialogDetailSignalement.setContentView(R.layout.dialog_detail_signalement);

                TextView arret = (TextView) dialogDetailSignalement.findViewById(R.id.dialog_textview_arret);
                TextView remarque = (TextView) dialogDetailSignalement.findViewById(R.id.dialog_textarea_remarques);
                remarque.setMovementMethod(new ScrollingMovementMethod());
                TextView date = (TextView) dialogDetailSignalement.findViewById(R.id.dialog_date_detail_signalement);
                TextView heure = (TextView) dialogDetailSignalement.findViewById(R.id.dialog_heure_detail_signalement);
                //TextView pseudo = (TextView) dialogDetailSignalement.findViewById(R.id.dialog_pseudo_detail_signalement);
                Button fermer = (Button) dialogDetailSignalement.findViewById(R.id.dialog_btn_detail_signalement);

                int indice = ((Integer)v.getTag()).intValue();
                List<Signalement> signalements = AdapterExpandableListViewHoraire.this.signalementsHoraires;
                ContenuSignalement contenuSignalement = new ContenuSignalement(signalements.get(indice).getContenu());
                arret.setText(contenuSignalement.getLigneArret());

                remarque.setText(signalements.get(indice).getRemarques());

                SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
                date.setText(sdfDate.format(signalements.get(indice).getDate()));

                Calendar cal = Calendar.getInstance();
                cal.setTime(signalements.get(indice).getDate());
                heure.setText(cal.get(Calendar.HOUR_OF_DAY) + "h" + cal.get(Calendar.MINUTE));

                //pseudo.setText(signalements.get(indice).getEmetteur().getPseudo());

                fermer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogDetailSignalement.dismiss();
                    }
                });

                dialogDetailSignalement.show();

            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String horaires = (String) this.getChild(groupPosition,childPosition);

        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.adapter_child_signalements_horaires,null);
        }

        TextView arret = (TextView) convertView.findViewById(R.id.textViewArretHoraire);
        TextView heure = (TextView) convertView.findViewById(R.id.textViewMinuteHoraire);

        String[] arretHoraire = horaires.split(" - ");
        arret.setText(arretHoraire[0]);
        heure.setText(arretHoraire[1]+"MN");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
