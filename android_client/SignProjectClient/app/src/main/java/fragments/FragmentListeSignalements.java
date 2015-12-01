package fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.florian.signprojectclient.R;

/**
 * Created by Axel_2 on 30/11/2015.
 */
public class FragmentListeSignalements extends Fragment {

    ListView listeSignalements;

    public FragmentListeSignalements()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_liste_signalement, container, false);

        this.listeSignalements = (ListView) view.findViewById(R.id.listeSignalements);

        String[] monthsArray = { "Sign1", "Sign2", "Sign3", "Sign4", "Sign5", "Sign6", "Sign7",
                "Sign8", "Sign9", "Sign10", "Sign11", "Sign12" };

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, monthsArray);
        this.listeSignalements.setAdapter(arrayAdapter);

        return view;

    }
}
