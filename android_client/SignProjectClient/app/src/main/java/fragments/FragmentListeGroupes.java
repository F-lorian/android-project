package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.florian.signprojectclient.R;

import java.util.ArrayList;
import java.util.Date;

import activites.AjoutGroupeActivity;
import activites.AjoutSignalementActivity;
import adapters.AdapterListViewGroupe;
import adapters.AdapterListViewSimpleSignalement;
import modeles.modele.Groupe;
import modeles.modele.Signalement;
import modeles.modele.SignalementPublic;
import modeles.modele.Utilisateur;
import modeles.modeleBD.DestinationSignalementGroupeBD;
import modeles.modeleBD.DestinationSignalementPublicBD;
import modeles.modeleBD.GroupeBD;
import modeles.modeleBD.SignalementBD;
import utilitaires.SessionManager;

/**
 * Created by Florian on 04/01/2016.
 */
public class FragmentListeGroupes extends Fragment {

    ListView listeGroupes;
    FloatingActionButton FabAjoutGroupe;
    ArrayList<Groupe> groupes;
    public static final String TYPE_SIGNALEMENT_BUNDLE = "TYPE_SIGNALEMENT";
    AdapterListViewGroupe adapterListViewGroupe;
    Thread mTimeUpdateThread;

    public FragmentListeGroupes()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_liste_groupes, container, false);

        this.listeGroupes = (ListView) view.findViewById(R.id.listeGroupes);
        this.FabAjoutGroupe = (FloatingActionButton) view.findViewById(R.id.FabAjoutGroupe);

        SessionManager sessionManager = new SessionManager(this.getActivity());
        GroupeBD groupeBD = new GroupeBD(this.getActivity());
        groupeBD.open();

        System.out.println(sessionManager.getUserId());
        this.groupes = groupeBD.getGroupesByIdUser(sessionManager.getUserId());
        groupeBD.close();

        this.adapterListViewGroupe = new AdapterListViewGroupe(this.getActivity(),this.groupes);
        this.listeGroupes.setAdapter(this.adapterListViewGroupe);

        this.FabAjoutGroupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AjoutGroupeActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }

}
