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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import activites.PositionSignalementMapsActivity;
import modeles.modele.Groupe;
import modeles.modele.Signalement;

/**
 * Created by Florian on 05/01/2016.
 */
public class AdapterListViewGroupe extends BaseAdapter {

    private List<Groupe> groupes;

    private Context mContext;

    private LayoutInflater mInflater;

    public static final String ID_LIGNE_ARRET = "ID_LIGNE_ARRET";
    public static final String TYPE_SIGNALEMENT = "TYPE_SIGNALEMENT";

    public AdapterListViewGroupe(Context mContext, List<Groupe> groupes) {
        this.mContext = mContext;
        this.groupes = groupes;
        this.mInflater = LayoutInflater.from(mContext);
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

        TextView nom = (TextView) layoutItem.findViewById(R.id.nom_adapter_groupe);
        TextView type = (TextView) layoutItem.findViewById(R.id.type_adapter_groupe);
        LinearLayout groupe = (LinearLayout) layoutItem.findViewById(R.id.layout_groupe);

        String nomGroupe = this.groupes.get(position).getNom();
        nom.setText(nomGroupe.toUpperCase());
        nom.setTextColor(Color.BLACK);

        String typeGroupe = this.groupes.get(position).getType();
        type.setText(typeGroupe);

        /*
        groupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = AdapterListViewGroupe.this.mContext;

                //aller vers l'activité pour voir un groupe

            }
        });
        */
        return layoutItem;

    }
}
