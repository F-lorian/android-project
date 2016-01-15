package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.florian.signprojectclient.R;

import java.util.List;

import modeles.modele.Utilisateur;

/**
 * Created by Florian on 15/01/2016.
 */
public class AdapterListViewMembre extends BaseAdapter {

    private List<Utilisateur> membres;

    private Context mContext;

    private LayoutInflater mInflater;

    public AdapterListViewMembre(Context mContext, List<Utilisateur> membres) {
        this.mContext = mContext;
        this.membres = membres;
        this.mInflater = LayoutInflater.from(mContext);
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

        TextView pseudo = (TextView) layoutItem.findViewById(R.id.pseudo_adapter_membre);
        ImageView accept = (ImageView) layoutItem.findViewById(R.id.image_accept);
        ImageView refuse = (ImageView) layoutItem.findViewById(R.id.image_refuse);
        LinearLayout membre = (LinearLayout) layoutItem.findViewById(R.id.layout_membre);

        String pseudoText = this.membres.get(position).getPseudo();
        pseudo.setText(pseudoText);

        accept.setTag(position);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = AdapterListViewMembre.this.mContext;

                int indice = ((Integer) v.getTag()).intValue();
                int id = membres.get(indice).getId();

                System.out.println("ACCEPTE TAG : " + indice);
            }
        });

        refuse.setTag(position);
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = AdapterListViewMembre.this.mContext;

                int indice = ((Integer)v.getTag()).intValue();
                int id = membres.get(indice).getId();

                System.out.println("REFUSE TAG : " + indice);
            }
        });

        return layoutItem;

    }
}
