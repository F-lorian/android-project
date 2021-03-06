package fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
public class FragmentListeSignalementsProches extends FragmentListeSignalementsHoraires implements LocationListener{

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

        this.adapterExpandableListViewHoraire = null;

        this.fabAjoutSignalement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AjoutSignalementActivity.class);
                startActivity(intent);
            }
        });


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
                                    FragmentListeSignalementsProches.this.updateSignalements();
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

    @Override
    public void onLocationChanged(Location location) {
        SignalementBD signalementBD = new SignalementBD(this.getActivity());
        signalementBD.open();
        this.signalements = signalementBD.getSignalementsProches(SignalementBD.TABLE_NAME_SIGNALEMENT_RECU,location,Config.DISTANCE_MAX_SIGNALEMENTS_PROCHES);
        signalementBD.close();

        this.initData();

        if (this.adapterExpandableListViewHoraire == null)
        {
            this.adapterExpandableListViewHoraire = new AdapterExpandableListViewHoraire(getActivity(),this.signalements,this.horairesSignalements);

            this.listeSignalements.setAdapter(this.adapterExpandableListViewHoraire);

            this.expandAll();

            this.adapterExpandableListViewHoraire.notifyDataSetChanged();
        }
        else
        {
            this.adapterExpandableListViewHoraire.setSignalementsHoraires(this.signalements);
            this.adapterExpandableListViewHoraire.setListOfChilds(this.horairesSignalements);
            this.adapterExpandableListViewHoraire.notifyDataSetChanged();
        }

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
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Config.DISTANCE_MAJ_MIN_TIME_SIGNALEMENTS_PROCHES_NETWORK, Config.DISTANCE_MAJ_MIN_DISTANCE_SIGNALEMENTS_PROCHES_NETWORK, this);
    }

    public void desabonnementNetwork() {
        if (ContextCompat.checkSelfPermission(this.getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        this.locationManager.removeUpdates(this);
    }

}
