package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.florian.signprojectclient.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import activites.AjoutSignalementActivity;
import adapters.AdapterExpandableListViewHoraire;
import modeles.modele.Signalement;
import modeles.modele.SignalementPublic;
import modeles.modeleBD.DestinationSignalementGroupeBD;
import modeles.modeleBD.DestinationSignalementPublicBD;
import modeles.modeleBD.SignalementBD;
import utilitaires.Config;
import utilitaires.ContenuSignalement;

/**
 * Created by Axel_2 on 24/12/2015.
 */
public class FragmentListeSignalementsHoraires extends Fragment{

    ExpandableListView listeSignalements;
    FloatingActionButton fabAjoutSignalement;
    ArrayList<Signalement> signalements;
    HashMap<Signalement,List<String>> horairesSignalements;
    AdapterExpandableListViewHoraire adapterExpandableListViewHoraire;
    Thread mTimeUpdateThread;
    Thread mSignalementUpdateThread;

    public FragmentListeSignalementsHoraires()
    {
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_liste_signalement_horaires, container, false);
        this.listeSignalements = (ExpandableListView) view.findViewById(R.id.listeSignalementsHoraires);
        this.fabAjoutSignalement = (FloatingActionButton) view.findViewById(R.id.FabAjoutSignalement);

        SignalementBD signalementBD = new SignalementBD(getActivity());
        signalementBD.open();
        this.signalements = signalementBD.getSignalementsByType(SignalementBD.TABLE_NAME_SIGNALEMENT_RECU,Config.HORAIRES);
        signalementBD.close();

        this.initData();
        this.updateSignalements();

        this.adapterExpandableListViewHoraire = new AdapterExpandableListViewHoraire(getActivity(),this.signalements,this.horairesSignalements);

        this.listeSignalements.setAdapter(this.adapterExpandableListViewHoraire);

        this.fabAjoutSignalement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AjoutSignalementActivity.class);
                intent.putExtra(Config.TYPE_SIGNALEMENT, Config.HORAIRES);
                startActivity(intent);
            }
        });

        this.expandAll();
        this.updateHoraireThread();
        this.removeObsoleteHorairesThread();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (mTimeUpdateThread != null && mTimeUpdateThread.isAlive())
        {
            mTimeUpdateThread.interrupt();
        }

        if (mSignalementUpdateThread != null && mSignalementUpdateThread.isAlive())
        {
            mSignalementUpdateThread.interrupt();
        }

    }

    protected void initData()
    {
        this.horairesSignalements = new HashMap<Signalement,List<String>>();

        for (int i=0; i<this.signalements.size(); i++)
        {
            Signalement signalement = this.signalements.get(i);
            signalement.setVu(true);
            ContenuSignalement contenuSignalement = new ContenuSignalement(signalement.getContenu());
            this.horairesSignalements.put(signalement,contenuSignalement.getTempsAttente());
        }
    }

    private void removeObsoleteHorairesThread(){
        final int exampleIntervall = 1000;
        mTimeUpdateThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!mTimeUpdateThread.isInterrupted()) {
                        Thread.sleep(exampleIntervall);
                        FragmentListeSignalementsHoraires.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (FragmentListeSignalementsHoraires.this.adapterExpandableListViewHoraire != null)
                                {
                                    FragmentListeSignalementsHoraires.this.updateSignalements();
                                    FragmentListeSignalementsHoraires.this.adapterExpandableListViewHoraire.notifyDataSetChanged();
                                }
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

    private void updateHoraireThread(){
        final int exampleIntervall = 5000;
        mSignalementUpdateThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!mSignalementUpdateThread.isInterrupted()) {
                        Thread.sleep(exampleIntervall);

                        FragmentListeSignalementsHoraires.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SignalementBD signalementBD = new SignalementBD(FragmentListeSignalementsHoraires.this.getActivity());
                                signalementBD.open();
                                FragmentListeSignalementsHoraires.this.signalements = signalementBD.getSignalementsByType(SignalementBD.TABLE_NAME_SIGNALEMENT_RECU, Config.HORAIRES);
                                signalementBD.close();

                                FragmentListeSignalementsHoraires.this.initData();

                                FragmentListeSignalementsHoraires.this.adapterExpandableListViewHoraire.setSignalementsHoraires(FragmentListeSignalementsHoraires.this.signalements);
                                FragmentListeSignalementsHoraires.this.adapterExpandableListViewHoraire.setListOfChilds(FragmentListeSignalementsHoraires.this.horairesSignalements);

                                FragmentListeSignalementsHoraires.this.adapterExpandableListViewHoraire.notifyDataSetChanged();
                            }
                        });
                    }
                }
                catch (InterruptedException e) {
                }
            }
        };

        mSignalementUpdateThread.start();
    }

    protected void expandAll()
    {
        for (int i=0; i<this.adapterExpandableListViewHoraire.getGroupCount(); i++)
        {
            if (this.adapterExpandableListViewHoraire.getChildrenCount(i) > 0)
            {
                this.listeSignalements.expandGroup(i);
            }
        }
    }

    protected void updateSignalements()
    {
        this.updateTempsAttentes();
        this.removeObsoleteSignalementsHoraires();
    }

    protected void updateTempsAttentes()
    {
        SignalementBD signalementBD = new SignalementBD(this.getActivity());
        signalementBD.open();

        for (Map.Entry<Signalement,List<String>> entry : this.horairesSignalements.entrySet())
        {

            if (entry.getValue().size() > 0) {

                Date dt2 = new Date();
                for (int i = 0; i < entry.getValue().size(); i++) {
                    long diff = dt2.getTime() - entry.getKey().getDate().getTime();
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000);

                    String[] arretHoraire = entry.getValue().get(i).split(" - ");

                    long minute = -1;
                    if (arretHoraire.length == 2) {
                        minute = Long.valueOf(arretHoraire[1]);
                    } else {
                        minute = Long.valueOf(arretHoraire[2]);
                    }

                    if (minute - diffMinutes <= 0 || diffHours >= 1) {
                        entry.getValue().remove(i);
                        i--;
                    } else {
                        entry.getValue().set(i, arretHoraire[0] + " - " + (minute - diffMinutes) + " - " + minute);
                    }
                }

                ContenuSignalement contenuSignalement = new ContenuSignalement(entry.getKey().getContenu());
                contenuSignalement.setTempsAttente(entry.getValue());

                entry.getKey().setContenu(contenuSignalement.getJson().toString());

                signalementBD.updateSignalement(entry.getKey(), SignalementBD.TABLE_NAME_SIGNALEMENT_RECU);
            }
        }

        signalementBD.close();
    }

    protected void removeObsoleteSignalementsHoraires()
    {
        Date dt2 = new Date();

        for (int i=0;i<this.signalements.size();i++)
        {
            Signalement signalement = this.signalements.get(i);

            long diff = dt2.getTime() - signalement.getDate().getTime();
            long diffDays = diff / (60 * 60 * 1000 * 24);

            if ((this.horairesSignalements.get(signalement).size() == 0 && signalement.getType().getType().equals(Config.HORAIRES)) || (diffDays >= 1))
            {
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
                    DestinationSignalementGroupeBD destinationSignalementGroupeBD = new DestinationSignalementGroupeBD(this.getActivity());
                    destinationSignalementGroupeBD.open();
                    destinationSignalementGroupeBD.deleteDestinationSignalementGroupe(signalement.getId(), destinationSignalementGroupeBD.TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_RECU);
                    destinationSignalementGroupeBD.close();
                }

                this.horairesSignalements.remove(signalement);
                this.signalements.remove(i);

            }
        }
    }

}
