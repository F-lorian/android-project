package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.florian.signprojectclient.R;

import java.util.ArrayList;

import activites.AjoutGroupeActivity;
import adapters.AdapterListViewGroupe;
import modeles.modele.Groupe;
import modeles.modeleBD.GroupeBD;
import utilitaires.SessionManager;

/**
 * Created by Florian on 05/01/2016.
 */
public class FragmentListeGroupesRecherche extends Fragment {

    EditText recherche;
    ImageButton valider;
    ListView listeGroupes;
    ArrayList<Groupe> groupes;
    AdapterListViewGroupe adapterListViewGroupe;

    public FragmentListeGroupesRecherche()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_liste_groupes_recherche, container, false);

        this.listeGroupes = (ListView) view.findViewById(R.id.listeGroupes);

        this.recherche = (EditText) view.findViewById(R.id.champ_recherche_groupe);
        this.valider = (ImageButton) view.findViewById(R.id.button_recherche_groupe);




        this.valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SessionManager sessionManager = new SessionManager(FragmentListeGroupesRecherche.this.getActivity());
                GroupeBD groupeBD = new GroupeBD(FragmentListeGroupesRecherche.this.getActivity());

                groupeBD.open();
                String s = FragmentListeGroupesRecherche.this.recherche.getText().toString();
                FragmentListeGroupesRecherche.this.groupes = groupeBD.searchGroupes(s);

                System.out.println("nb groupes : "+groupeBD.getCount());
                groupeBD.close();

                FragmentListeGroupesRecherche.this.adapterListViewGroupe = new AdapterListViewGroupe(FragmentListeGroupesRecherche.this.getActivity(),FragmentListeGroupesRecherche.this.groupes);
                FragmentListeGroupesRecherche.this.listeGroupes.setAdapter(FragmentListeGroupesRecherche.this.adapterListViewGroupe);

            }
        });

        return view;

    }

}
