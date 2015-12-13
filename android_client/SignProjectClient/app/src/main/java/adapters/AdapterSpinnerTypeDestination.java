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

/**
 * Created by Axel_2 on 10/12/2015.
 */
public class AdapterSpinnerTypeDestination extends BaseAdapter {

    private List<String> typeDestination;

    private Context mContext;

    private LayoutInflater mInflater;

    public AdapterSpinnerTypeDestination(Context context, List<String> typeDestination)
    {
        this.mContext = context;
        this.typeDestination = typeDestination;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.typeDestination.size();
    }

    @Override
    public Object getItem(int position) {
        return this.typeDestination.get(position);
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

        if (this.typeDestination.get(position).equals(this.mContext.getResources().getString(R.string.public_spinner)))
        {
            tv.setText(this.mContext.getResources().getString(R.string.public_spinner));
            iv.setImageDrawable(ContextCompat.getDrawable(this.mContext, R.drawable.ic_public));
        }
        else if (this.typeDestination.get(position).equals(this.mContext.getResources().getString(R.string.groupe_spinner)))
        {
            tv.setText(this.mContext.getResources().getString(R.string.groupe_spinner));
            iv.setImageDrawable(ContextCompat.getDrawable(this.mContext,R.drawable.groupe));
        }
        else
        {
            tv.setText(this.mContext.getResources().getString(R.string.autres_personnes_spinner));
            iv.setImageDrawable(ContextCompat.getDrawable(this.mContext,R.drawable.personne));
        }

        return layoutItem;
    }
}
