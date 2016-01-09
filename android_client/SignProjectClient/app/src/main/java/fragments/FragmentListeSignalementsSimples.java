package fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.florian.signprojectclient.R;

import java.util.ArrayList;
import java.util.Date;

import activites.AjoutSignalementActivity;
import adapters.AdapterListViewSimpleSignalement;
import modeles.modele.Signalement;
import modeles.modele.SignalementPublic;
import modeles.modeleBD.DestinationSignalementGroupeBD;
import modeles.modeleBD.DestinationSignalementPublicBD;
import modeles.modeleBD.SignalementBD;
import utilitaires.Config;

/**
 * Created by Axel_2 on 30/11/2015.
 */
public class FragmentListeSignalementsSimples extends Fragment {

    ListView listeSignalements;
    FloatingActionButton fabAjoutSignalement;
    ArrayList<Signalement> signalements;
    AdapterListViewSimpleSignalement adapterListViewSimpleSignalement;
    Thread mTimeUpdateThread;

    public FragmentListeSignalementsSimples()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_liste_signalement_simple, container, false);

        this.listeSignalements = (ListView) view.findViewById(R.id.listeSignalementsSimples);
        this.fabAjoutSignalement = (FloatingActionButton) view.findViewById(R.id.FabAjoutSignalement);

        SignalementBD signalementBD = new SignalementBD(this.getActivity());
        signalementBD.open();
        this.signalements = signalementBD.getSignalementsByType(SignalementBD.TABLE_NAME_SIGNALEMENT_RECU,getArguments().getString(Config.TYPE_SIGNALEMENT));
        signalementBD.close();

        this.updateSignalements();

        this.adapterListViewSimpleSignalement = new AdapterListViewSimpleSignalement(this.getActivity(),this.signalements);

        this.listeSignalements.setAdapter(this.adapterListViewSimpleSignalement);

        this.fabAjoutSignalement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AjoutSignalementActivity.class);
                intent.putExtra(Config.TYPE_SIGNALEMENT, FragmentListeSignalementsSimples.this.getArguments().getString(Config.TYPE_SIGNALEMENT));
                startActivity(intent);
            }
        });

        this.updateTimeThread();

        return view;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTimeUpdateThread.interrupt();
    }

    private void updateTimeThread(){
        final int exampleIntervall = 1000;
        mTimeUpdateThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!mTimeUpdateThread.isInterrupted()) {
                        Thread.sleep(exampleIntervall);
                        FragmentListeSignalementsSimples.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FragmentListeSignalementsSimples.this.updateSignalements();
                                FragmentListeSignalementsSimples.this.adapterListViewSimpleSignalement.notifyDataSetChanged();
                            }
                        });
                    }
                }
                catch (InterruptedException e) {
                }
            }
        };

        mTimeUpdateThread.start();
    }

    private void updateSignalements()
    {
        if (this.adapterListViewSimpleSignalement != null)
        {
            Date dt2 = new Date();
            for (int i=0; i<this.signalements.size(); i++)
            {
                long diff = dt2.getTime() - this.signalements.get(i).getDate().getTime();
                long diffDays = diff / (60 * 60 * 1000 * 24);

                if (diffDays >= 1)
                {
                    Signalement signalement = this.signalements.get(i);

                    SignalementBD signalementBD = new SignalementBD(this.getActivity());
                    signalementBD.open();
                    signalementBD.deleteSignalement(signalement,SignalementBD.TABLE_NAME_SIGNALEMENT_RECU);
                    signalementBD.close();

                    if (signalement instanceof SignalementPublic)
                    {
                        DestinationSignalementPublicBD destinationSignalementPublicBD = new DestinationSignalementPublicBD(this.getActivity());
                        destinationSignalementPublicBD.open();
                        destinationSignalementPublicBD.deleteDestinationSignalementUtilisateur(signalement.getId(),destinationSignalementPublicBD.TABLE_NAME_DESTINATION_SIGNALEMENT_UTILISATEUR_RECU);
                        destinationSignalementPublicBD.close();
                    }
                    else
                    {
                        DestinationSignalementGroupeBD destinationSignalementGroupeBD= new DestinationSignalementGroupeBD(this.getActivity());
                        destinationSignalementGroupeBD.open();
                        destinationSignalementGroupeBD.deleteDestinationSignalementGroupe(signalement.getId(), destinationSignalementGroupeBD.TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_RECU);
                        destinationSignalementGroupeBD.close();
                    }

                    this.signalements.remove(i);
                    i--;
                }
            }
        }
    }
}
