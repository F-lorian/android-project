package activites;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.florian.signprojectclient.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.Map;

import adapters.AdapterListViewSimpleSignalement;
import modeles.modele.Arret;
import modeles.modele.Ligne;
import modeles.modeleBD.LigneArretBD;

public class PositionSignalementMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Map.Entry<Ligne,Arret> ligneArret;
    private String typeSignalement;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_signalement_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_position_signalement_maps));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolbar_signalement_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LigneArretBD ligneArretBD = new LigneArretBD(this);
        ligneArretBD.open();
        this.ligneArret = ligneArretBD.getLigneArret(getIntent().getIntExtra(AdapterListViewSimpleSignalement.ID_LIGNE_ARRET,-1));
        ligneArretBD.close();

        this.typeSignalement = getIntent().getStringExtra(AdapterListViewSimpleSignalement.TYPE_SIGNALEMENT);

        LatLng positionArret = this.createArret();
        this.createLigne();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(positionArret)
                .zoom(19).build();
        this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public LatLng createArret()
    {
        Arret a = this.ligneArret.getValue();

        LatLng positionArret = this.getCoordinate(a.getCoordonnees());

        MarkerOptions signalement = new MarkerOptions();
        signalement.position(positionArret);
        signalement.title(a.getNom());
        signalement.snippet(this.typeSignalement.toUpperCase());

        if (this.typeSignalement.equals(this.getResources().getString(R.string.controleur_spinner)))
        {
            signalement.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_controleur));
        }
        else if (this.typeSignalement.equals(this.getResources().getString(R.string.horaire_spinner)))
        {
            signalement.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_time));
        }
        else if (this.typeSignalement.equals(this.getResources().getString(R.string.accident_spinner)))
        {
            signalement.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_accident));
        }
        else
        {
            signalement.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_autre));
        }

        this.mMap.addMarker(signalement);

        return positionArret;

    }

    public void createLigne()
    {
        Ligne l = this.ligneArret.getKey();
        String[] coordinates = l.getCoordonnees().split(" ");
        PolylineOptions ligne = new PolylineOptions();

        for (int i=0; i<coordinates.length; i++)
        {
            LatLng position = this.getCoordinate(coordinates[i]);
            ligne.add(position);
        }

        if (l.getNom().equals("Ligne 1"))
        {
            ligne.color(Color.rgb(54, 68, 219));
        }
        else if (l.getNom().equals("Ligne 2"))
        {
            ligne.color(Color.rgb(240, 134, 65));
        }
        else if (l.getNom().equals("Ligne 3"))
        {
            ligne.color(Color.rgb(68, 175, 98));
        }
        else
        {
            ligne.color(Color.rgb(75, 54, 34));
        }

        ligne.width(8);

        this.mMap.addPolyline(ligne);
    }

    public LatLng getCoordinate(String coordinate)
    {
        String[] coordinatesString = coordinate.split(",");
        double[] coordinates = new double[2];

        coordinates[0] = Double.valueOf(coordinatesString[1]);
        coordinates[1] = Double.valueOf(coordinatesString[0]);

        return new LatLng(coordinates[0],coordinates[1]);
    }
}
