package adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.florian.signprojectclient.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import activites.PositionSignalementMapsActivity;
import modeles.modele.Signalement;
import utilitaires.Config;

/**
 * Created by Axel_2 on 25/12/2015.
 */
public class AdapterListViewSimpleSignalement extends BaseAdapter{

    private List<Signalement> signalements;

    private Context mContext;

    private LayoutInflater mInflater;

    public static final String ID_LIGNE_ARRET = "ID_LIGNE_ARRET";
    public static final String TYPE_SIGNALEMENT = "TYPE_SIGNALEMENT";

    public AdapterListViewSimpleSignalement(Context mContext, List<Signalement> signalements) {
        this.mContext = mContext;
        this.signalements = signalements;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return this.signalements.size();
    }

    @Override
    public Object getItem(int position) {
        return this.signalements.get(position);
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
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.adapter_listview_signalements, parent, false);
        }
        else
        {
            layoutItem = (LinearLayout) convertView;
        }

        TextView titre = (TextView) layoutItem.findViewById(R.id.titre_adapter_signalement);
        TextView arret = (TextView) layoutItem.findViewById(R.id.arret_adapter_signalement);
        TextView heure = (TextView) layoutItem.findViewById(R.id.text_adapter_heure);
        ImageButton positionBtn = (ImageButton) layoutItem.findViewById(R.id.Btn_adapter_CarteSignalement);
        ImageButton detailBtn = (ImageButton) layoutItem.findViewById(R.id.Btn_adapter_DetailSignalement);

        String typeSignalement = this.signalements.get(position).getType().getType();


        if (typeSignalement.equals(Config.CONTROLEUR))
        {
            titre.setText(this.mContext.getResources().getString(R.string.controleur_spinner).toUpperCase());
            titre.setTextColor(Color.RED);
        }
        else if (typeSignalement.equals(Config.AUTRES) || typeSignalement.equals(Config.ACCIDENTS))
        {
            if (typeSignalement.equals(Config.AUTRES))
            {
                titre.setText(this.mContext.getResources().getString(R.string.autres_spinner).toUpperCase());
            }
            else
            {
                titre.setText(this.mContext.getResources().getString(R.string.accident_spinner).toUpperCase());
            }
            titre.setTextColor(Color.BLUE);
        }

        String contenu = this.signalements.get(position).getContenu();
        arret.setText(contenu.split("\n")[0]);


        Date dt2 = new Date();
        long diff = dt2.getTime() - this.signalements.get(position).getDate().getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);

        heure.setText(diffHours + "h " + diffMinutes + "min " + diffSeconds + "s");



        positionBtn.setTag(position);
        positionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indice = ((Integer)v.getTag()).intValue();
                int idLigneArret = Integer.valueOf(AdapterListViewSimpleSignalement.this.signalements.get(indice).getContenu().split("\n")[1]).intValue();
                Intent intent = new Intent(AdapterListViewSimpleSignalement.this.mContext, PositionSignalementMapsActivity.class);
                intent.putExtra(AdapterListViewSimpleSignalement.this.ID_LIGNE_ARRET,idLigneArret);
                intent.putExtra(AdapterListViewSimpleSignalement.this.TYPE_SIGNALEMENT,AdapterListViewSimpleSignalement.this.signalements.get(indice).getType().getType());
                AdapterListViewSimpleSignalement.this.mContext.startActivity(intent);
            }
        });

        detailBtn.setTag(position);
        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = AdapterListViewSimpleSignalement.this.mContext;

                final Dialog dialogDetailSignalement = new Dialog(c);
                dialogDetailSignalement.setTitle(c.getResources().getString(R.string.dialog_titre_detail_signalement));
                dialogDetailSignalement.setContentView(R.layout.dialog_detail_signalement);

                TextView arret = (TextView) dialogDetailSignalement.findViewById(R.id.dialog_textview_arret);
                TextView remarque = (TextView) dialogDetailSignalement.findViewById(R.id.dialog_textarea_remarques);
                remarque.setMovementMethod(new ScrollingMovementMethod());
                TextView date = (TextView) dialogDetailSignalement.findViewById(R.id.dialog_date_detail_signalement);
                TextView heure = (TextView) dialogDetailSignalement.findViewById(R.id.dialog_heure_detail_signalement);
                TextView pseudo = (TextView) dialogDetailSignalement.findViewById(R.id.dialog_pseudo_detail_signalement);
                Button fermer = (Button) dialogDetailSignalement.findViewById(R.id.dialog_btn_detail_signalement);

                int indice = ((Integer)v.getTag()).intValue();
                List<Signalement> signalements = AdapterListViewSimpleSignalement.this.signalements;
                String contenu = signalements.get(indice).getContenu();
                arret.setText(contenu.split("\n")[0]);

                remarque.setText(signalements.get(indice).getRemarques());

                SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
                date.setText(sdfDate.format(signalements.get(indice).getDate()));

                Calendar cal = Calendar.getInstance();
                cal.setTime(signalements.get(indice).getDate());
                heure.setText(cal.get(Calendar.HOUR_OF_DAY) + "h" + cal.get(Calendar.MINUTE));

                pseudo.setText(signalements.get(indice).getEmetteur().getPseudo());

                fermer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogDetailSignalement.dismiss();
                    }
                });

                dialogDetailSignalement.show();

            }
        });

        return layoutItem;

    }
}
