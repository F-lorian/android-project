package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.florian.signprojectclient.R;

import java.util.List;

import activites.AjoutSignalementActivity;

/**
 * Created by Axel_2 on 14/12/2015.
 */
public class AdapterListViewHoraireSignalement extends BaseAdapter {

    private List<String> horaire;

    private Context mContext;

    private LayoutInflater mInflater;

    public AdapterListViewHoraireSignalement(Context context, List<String> horaires)
    {
        this.mContext = context;
        this.horaire = horaires;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.horaire.size();
    }

    @Override
    public Object getItem(int position) {
        return this.horaire.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RelativeLayout layoutItem;

        if (convertView == null)
        {
            layoutItem = (RelativeLayout) mInflater.inflate(R.layout.adapter_list_horaires, parent, false);
        }
        else
        {
            layoutItem = (RelativeLayout) convertView;
        }

        TextView direction = (TextView) layoutItem.findViewById(R.id.textViewArretListHoraire);
        TextView minute = (TextView) layoutItem.findViewById(R.id.textViewMinuteListHoraire);
        ImageButton boutonSupp = (ImageButton) layoutItem.findViewById(R.id.buttonRemoveHoraireListView);

        String[] splitHoraire = this.horaire.get(position).split(" - ");

        direction.setText(splitHoraire[0]);
        minute.setText(splitHoraire[1] + "MN");
        boutonSupp.setTag(position);

        boutonSupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer index = (Integer) v.getTag();
                AdapterListViewHoraireSignalement.this.horaire.remove(index.intValue());
                AdapterListViewHoraireSignalement.this.notifyDataSetChanged();

                if (AdapterListViewHoraireSignalement.this.horaire.size() == AjoutSignalementActivity.MAX_CAPACITY_LIST_HORAIRES - 1)
                {
                    ImageButton buttonAddHoraire = (ImageButton) ((Activity)AdapterListViewHoraireSignalement.this.mContext).findViewById(R.id.buttonAddHoraire);
                    buttonAddHoraire.setEnabled(true);
                    buttonAddHoraire.setAlpha(1f);
                }
            }
        });

        return layoutItem;
    }
}
