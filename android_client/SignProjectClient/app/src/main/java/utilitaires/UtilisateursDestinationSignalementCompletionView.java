package utilitaires;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.florian.signprojectclient.R;
import com.tokenautocomplete.TokenCompleteTextView;

import modeles.modele.Utilisateur;

/**
 * Created by Axel_2 on 11/12/2015.
 */
public class UtilisateursDestinationSignalementCompletionView extends TokenCompleteTextView<Utilisateur> {


    public UtilisateursDestinationSignalementCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Utilisateur utilisateur) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.token_text_view, (ViewGroup)UtilisateursDestinationSignalementCompletionView.this.getParent(), false);
        ((TextView)view.findViewById(R.id.name)).setText(utilisateur.getPseudo());

        return view;
    }

    @Override
    protected Utilisateur defaultObject(String completionText) {
        return new Utilisateur(0,completionText,"",null,null,null);
    }

}
