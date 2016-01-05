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
 * Created by Florian on 04/01/2016.
 */
public class AdapterSpinnerTypeGroupe extends BaseAdapter {

    private List<String>  mListTypeGroupe;

    private Context mContext;

    private LayoutInflater mInflater;

    public AdapterSpinnerTypeGroupe(Context context, List<String> typeGroupes)
    {
        this.mContext = context;
        this.mListTypeGroupe = typeGroupes;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.mListTypeGroupe.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mListTypeGroupe.get(position);
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

        if (this.mListTypeGroupe.get(position).equals(this.mContext.getResources().getString(R.string.type_public)))
        {
            tv.setText(this.mContext.getResources().getString(R.string.type_public));
            iv.setImageDrawable(ContextCompat.getDrawable(this.mContext, R.drawable.ic_eye));
        }
        else if (this.mListTypeGroupe.get(position).equals(this.mContext.getResources().getString(R.string.type_prive)))
        {
            tv.setText(this.mContext.getResources().getString(R.string.type_prive));
            iv.setImageDrawable(ContextCompat.getDrawable(this.mContext,R.drawable.ic_closed_eye));
        }
        else
        {
            tv.setText(this.mContext.getResources().getString(R.string.autres_spinner));
            iv.setImageDrawable(ContextCompat.getDrawable(this.mContext,R.drawable.ic_autre));
        }

        return layoutItem;
    }
}
