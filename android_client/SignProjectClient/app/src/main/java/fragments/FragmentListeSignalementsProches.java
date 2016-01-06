package fragments;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import activites.AjoutSignalementActivity;
import adapters.AdapterExpandableListViewHoraire;
import modeles.modele.Signalement;
import modeles.modele.SignalementPublic;
import modeles.modeleBD.DestinationSignalementGroupeBD;
import modeles.modeleBD.DestinationSignalementPublicBD;
import modeles.modeleBD.SignalementBD;
import utilitaires.Config;

/**
 * Created by Axel_2 on 24/12/2015.
 */
public class FragmentListeSignalementsProches extends Fragment implements LocationListener{

    ExpandableListView listeSignalements;
    FloatingActionButton fabAjoutSignalement;
    ArrayList<Signalement> signalements;
    HashMap<Signalement,List<String>> horairesSignalements;
    AdapterExpandableListViewHoraire adapterExpandableListViewHoraire;
    Thread mTimeUpdateThread;

    LocationManager locationManager;

    public FragmentListeSignalementsProches()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_liste_signalement_horaires, container, false);
        this.listeSignalements = (ExpandableListView) view.findViewById(R.id.listeSignalementsHoraires);
        this.fabAjoutSignalement = (FloatingActionButton) view.findViewById(R.id.FabAjoutSignalement);

        this.locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        SignalementBD signalementBD = new SignalementBD(getActivity());
        signalementBD.open();
        this.signalements = signalementBD.getSignalements(SignalementBD.TABLE_NAME_SIGNALEMENT_RECU);
        signalementBD.close();

        this.initData();

        this.adapterExpandableListViewHoraire = new AdapterExpandableListViewHoraire(getActivity(),this.signalements,this.horairesSignalements);
        this.listeSignalements.setAdapter(this.adapterExpandableListViewHoraire);

        this.fabAjoutSignalement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AjoutSignalementActivity.class);
                startActivity(intent);
            }
        });

        this.expandAll();
        this.updateHoraireThread();
        this.abonnementNetwork();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.desabonnementNetwork();
        mTimeUpdateThread.interrupt();
    }

    private void initData()
    {
        this.horairesSignalements = new HashMap<Signalement,List<String>>();

        for (int i=0; i<this.signalements.size(); i++)
        {
            Signalement signalement = this.signalements.get(i);
            String[] horaires = signalement.getContenu().split("\n");

            List<String> listHoraires = new ArrayList<>();

            for(int j=2; j<horaires.length; j++)
            {
                listHoraires.add(horaires[j]);
            }

            this.horairesSignalements.put(signalement,listHoraires);
        }
    }

    private void updateHoraireThread(){
        final int exampleIntervall = 1000;
        mTimeUpdateThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!mTimeUpdateThread.isInterrupted()) {
                        Thread.sleep(exampleIntervall);
                        FragmentListeSignalementsProches.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (FragmentListeSignalementsProches.this.adapterExpandableListViewHoraire != null)
                                {
                                    for (Map.Entry<Signalement,List<String>> entry : FragmentListeSignalementsProches.this.horairesSignalements.entrySet())
                                    {
                                        Date dt2 = new Date();
                                        for (int i=0; i<entry.getValue().size(); i++)
                                        {
                                            long diff = dt2.getTime() - entry.getKey().getDate().getTime();
                                            long diffMinutes = diff / (60 * 1000) % 60;
                                            long diffHours = diff / (60 * 60 * 1000);

                                            String[] arretHoraire = entry.getValue().get(i).split(" - ");

                                            long minute = -1;
                                            if (arretHoraire.length == 2)
                                            {
                                                minute = Long.valueOf(arretHoraire[1]);
                                            }
                                            else
                                            {
                                                minute = Long.valueOf(arretHoraire[2]);
                                            }

                                            if (minute - diffMinutes <= 0 || diffHours >= 1)
                                            {
                                                entry.getValue().remove(i);
                                            }
                                            else
                                            {
                                                entry.getValue().set(i,arretHoraire[0] + " - " + (minute - diffMinutes) + " - " + minute);
                                            }
                                        }
                                    }

                                    Date dt2 = new Date();

                                    for (int i=0;i<FragmentListeSignalementsProches.this.signalements.size();i++)
                                    {
                                        Signalement signalement = FragmentListeSignalementsProches.this.signalements.get(i);

                                        long diff = dt2.getTime() - signalement.getDate().getTime();
                                        long diffDays = diff / (60 * 60 * 1000 * 24);

                                        if ((FragmentListeSignalementsProches.this.horairesSignalements.get(signalement).size() == 0 && signalement.getType().getType().equals(Config.HORAIRES)) || (diffDays >= 1))
                                        {
                                            SignalementBD signalementBD = new SignalementBD(FragmentListeSignalementsProches.this.getActivity());
                                            signalementBD.open();
                                            signalementBD.deleteSignalement(signalement,SignalementBD.TABLE_NAME_SIGNALEMENT_RECU);
                                            signalementBD.close();

                                            if (signalement instanceof SignalementPublic)
                                            {
                                                DestinationSignalementPublicBD destinationSignalementPublicBD = new DestinationSignalementPublicBD(FragmentListeSignalementsProches.this.getActivity());
                                                destinationSignalementPublicBD.open();
                                                destinationSignalementPublicBD.deleteDestinationSignalementUtilisateur(signalement.getId(),destinationSignalementPublicBD.TABLE_NAME_DESTINATION_SIGNALEMENT_UTILISATEUR_RECU);
                                                destinationSignalementPublicBD.close();
                                            }
                                            else
                                            {
                                                DestinationSignalementGroupeBD destinationSignalementGroupeBD= new DestinationSignalementGroupeBD(FragmentListeSignalementsProches.this.getActivity());
                                                destinationSignalementGroupeBD.open();
                                                destinationSignalementGroupeBD.deleteDestinationSignalementGroupe(signalement.getId(), destinationSignalementGroupeBD.TABLE_NAME_DESTINATION_SIGNALEMENT_GROUPE_RECU);
                                                destinationSignalementGroupeBD.close();
                                            }

                                            FragmentListeSignalementsProches.this.horairesSignalements.remove(signalement);
                                            FragmentListeSignalementsProches.this.signalements.remove(i);

                                        }
                                    }

                                    FragmentListeSignalementsProches.this.adapterExpandableListViewHoraire.notifyDataSetChanged();
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

    private void expandAll()
    {
        for (int i=0; i<this.adapterExpandableListViewHoraire.getGroupCount(); i++)
        {
            if (this.adapterExpandableListViewHoraire.getChildrenCount(i) > 0)
            {
                this.listeSignalements.expandGroup(i);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        SignalementBD signalementBD = new SignalementBD(this.getActivity());
        signalementBD.open();
        this.signalements = signalementBD.getSignalementsProches(SignalementBD.TABLE_NAME_SIGNALEMENT_RECU,location,Config.DISTANCE_MAX_SIGNALEMENTS_PROCHES);
        signalementBD.close();

        System.out.println(this.signalements + " on location changed ");

        this.initData();

        this.adapterExpandableListViewHoraire.setSignalementsHoraires(this.signalements);
        this.adapterExpandableListViewHoraire.setListOfChilds(this.horairesSignalements);

        this.adapterExpandableListViewHoraire.notifyDataSetChanged();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider.equals(LocationManager.NETWORK_PROVIDER))
        {
            this.abonnementNetwork();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.NETWORK_PROVIDER))
        {
            this.desabonnementNetwork();
        }
    }

    public void abonnementNetwork() {
       // this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Config.DISTANCE_MAJ_MIN_TIME_SIGNALEMENTS_PROCHES_NETWORK, Config.DISTANCE_MAJ_MIN_DISTANCE_SIGNALEMENTS_PROCHES_NETWORK, this);
    }

    public void desabonnementNetwork() {
       // this.locationManager.removeUpdates(this);
    }

}
