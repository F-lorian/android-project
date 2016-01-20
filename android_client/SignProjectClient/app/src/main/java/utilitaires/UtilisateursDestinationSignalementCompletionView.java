package utilitaires;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.florian.signprojectclient.R;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;

import modeles.modele.Utilisateur;

/**
 * Created by Axel_2 on 11/12/2015.
 */
public class UtilisateursDestinationSignalementCompletionView extends TokenCompleteTextView<Utilisateur> {

    ArrayList<Utilisateur> all;

    public UtilisateursDestinationSignalementCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Utilisateur utilisateur) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.token_text_view, (ViewGroup) UtilisateursDestinationSignalementCompletionView.this.getParent(), false);
        TextView token = ((TextView)view.findViewById(R.id.name));
        token.setText(utilisateur.getPseudo());

        if(this.all.contains(utilisateur)){
            this.setError(null);
        } else {
            token.setError(getResources().getString(R.string.message_alert_dialog_erreur_utilisateur_introuvable));
            view.findViewById(R.id.layout_token).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_important));
            token.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_important));
        }

        return view;
    }

    public void setList(ArrayList<Utilisateur> all){
        this.all=all;
    }

    @Override
    protected Utilisateur defaultObject(String completionText) {
        return new Utilisateur(0,completionText,"",null,null,null);
    }

}
