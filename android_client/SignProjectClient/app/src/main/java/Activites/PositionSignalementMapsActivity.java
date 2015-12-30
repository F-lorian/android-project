package activites;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Map;

import adapters.AdapterListViewSimpleSignalement;
import modeles.modele.Arret;
import modeles.modele.Ligne;
import modeles.modeleBD.LigneArretBD;

public class PositionSignalementMapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;

    private Map.Entry<Ligne, Arret> ligneArret;
    private String typeSignalement;

    private Toolbar toolbar;

    private Marker arret;
    private Polyline ligne;
    private Marker currentPosition;
    private LocationManager locationManager;

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

        this.locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        this.abonnementNetwork();
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
        this.mMap = googleMap;

        LigneArretBD ligneArretBD = new LigneArretBD(this);
        ligneArretBD.open();
        this.ligneArret = ligneArretBD.getLigneArret(getIntent().getIntExtra(AdapterListViewSimpleSignalement.ID_LIGNE_ARRET, -1));
        ligneArretBD.close();

        this.typeSignalement = getIntent().getStringExtra(AdapterListViewSimpleSignalement.TYPE_SIGNALEMENT);

        this.arret = this.createArret();
        this.ligne = this.createLigne();
        this.currentPosition = this.mMap.addMarker(new MarkerOptions().title(getResources().getString(R.string.titre_position_courante)).position(new LatLng(0, 0)));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(this.arret.getPosition())
                .zoom(19).build();

        this.currentPosition.showInfoWindow();

        this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public Marker createArret() {
        Arret a = this.ligneArret.getValue();

        LatLng positionArret = this.getCoordinate(a.getCoordonnees());

        MarkerOptions signalement = new MarkerOptions();
        signalement.position(positionArret);
        signalement.title(a.getNom());
        signalement.snippet(this.typeSignalement.toUpperCase());

        if (this.typeSignalement.equals(this.getResources().getString(R.string.controleur_spinner))) {
            signalement.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_controleur));
        } else if (this.typeSignalement.equals(this.getResources().getString(R.string.horaire_spinner))) {
            signalement.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_time));
        } else if (this.typeSignalement.equals(this.getResources().getString(R.string.accident_spinner))) {
            signalement.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_accident));
        } else {
            signalement.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_autre));
        }

        return this.mMap.addMarker(signalement);
    }

    public Polyline createLigne() {
        Ligne l = this.ligneArret.getKey();
        String[] coordinates = l.getCoordonnees().split(" ");
        PolylineOptions ligne = new PolylineOptions();

        for (int i = 0; i < coordinates.length; i++) {
            LatLng position = this.getCoordinate(coordinates[i]);
            ligne.add(position);
        }

        if (l.getNom().equals("Ligne 1")) {
            ligne.color(Color.rgb(54, 68, 219));
        } else if (l.getNom().equals("Ligne 2")) {
            ligne.color(Color.rgb(240, 134, 65));
        } else if (l.getNom().equals("Ligne 3")) {
            ligne.color(Color.rgb(68, 175, 98));
        } else {
            ligne.color(Color.rgb(75, 54, 34));
        }

        ligne.width(8);

        return this.mMap.addPolyline(ligne);
    }

    public LatLng getCoordinate(String coordinate) {
        String[] coordinatesString = coordinate.split(",");
        double[] coordinates = new double[2];

        coordinates[0] = Double.valueOf(coordinatesString[1]);
        coordinates[1] = Double.valueOf(coordinatesString[0]);

        return new LatLng(coordinates[0], coordinates[1]);
    }

    @Override
    public void onLocationChanged(Location location) {

        float[] results = new float[1];
        Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                this.arret.getPosition().latitude, this.arret.getPosition().longitude, results);
        String distance = getResources().getString(R.string.snippet_position_courante_1) + " " + results[0] + " " + getResources().getString(R.string.snippet_position_courante_2);
        this.currentPosition.setSnippet(distance);
        this.currentPosition.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        this.currentPosition.showInfoWindow();

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
        this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
    }

    public void desabonnementNetwork() {
        this.locationManager.removeUpdates(this);
    }
}
