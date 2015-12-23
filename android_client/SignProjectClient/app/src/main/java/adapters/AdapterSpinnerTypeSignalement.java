package adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.florian.signprojectclient.R;

import java.util.List;

import modeles.modele.TypeSignalement;

/**
 * Created by Axel_2 on 10/12/2015.
 */
public class AdapterSpinnerTypeSignalement extends BaseAdapter {

    private List<TypeSignalement> mListTypeSignalement;

    private Context mContext;

    private LayoutInflater mInflater;

    public AdapterSpinnerTypeSignalement(Context context, List<TypeSignalement> typeSignalements)
    {
        this.mContext = context;
        this.mListTypeSignalement = typeSignalements;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.mListTypeSignalement.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mListTypeSignalement.get(position);
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
            layoutItem = (RelativeLayout) mInflater.inflate(R.layout.adapter_spinner_type, parent, false);
        }
        else
        {
            layoutItem = (RelativeLayout) convertView;
        }

        ImageView iv = (ImageView) layoutItem.findViewById(R.id.imageSpinnerType);
        TextView tv = (TextView) layoutItem.findViewById(R.id.textViewSpinnerType);

        if (this.mListTypeSignalement.get(position).getType().equals(this.mContext.getResources().getString(R.string.controleur_spinner)))
        {
            tv.setText(this.mContext.getResources().getString(R.string.controleur_spinner));
            iv.setImageDrawable(ContextCompat.getDrawable(this.mContext,R.drawable.ic_controleur));
        }
        else if (this.mListTypeSignalement.get(position).getType().equals(this.mContext.getResources().getString(R.string.horaire_spinner)))
        {
            tv.setText(this.mContext.getResources().getString(R.string.horaire_spinner));
            iv.setImageDrawable(ContextCompat.getDrawable(this.mContext,R.drawable.ic_action_time));
        }
        else if (this.mListTypeSignalement.get(position).getType().equals(this.mContext.getResources().getString(R.string.accident_spinner)))
        {
            tv.setText(this.mContext.getResources().getString(R.string.accident_spinner));
            iv.setImageDrawable(ContextCompat.getDrawable(this.mContext,R.drawable.ic_accident));
        }
        else
        {
            tv.setText(this.mContext.getResources().getString(R.string.autres_spinner));
            iv.setImageDrawable(ContextCompat.getDrawable(this.mContext,R.drawable.ic_autre));
        }

        return layoutItem;
    }
}
