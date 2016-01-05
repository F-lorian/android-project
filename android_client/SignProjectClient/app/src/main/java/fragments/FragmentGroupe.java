package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.florian.signprojectclient.R;

import adapters.AdapterListViewGroupe;

/**
 * Created by Florian on 05/01/2016.
 */
public class FragmentGroupe extends Fragment {

    TextView nom;
    TextView description;
    AdapterListViewGroupe adapterListViewGroupe;

    public FragmentGroupe()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_groupe, container, false);

        this.nom = (TextView) view.findViewById(R.id.nom_groupe);
        this.description = (TextView) view.findViewById(R.id.description_groupe);

        /*
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
        */
        return view;

    }

}

