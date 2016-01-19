package activites;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Map;

import adapters.AdapterListViewSimpleSignalement;
import modeles.modele.Arret;
import modeles.modele.Ligne;
import modeles.modeleBD.LigneArretBD;
import utilitaires.Config;

public class PositionSignalementMapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;

    private Map.Entry<Ligne, Arret> ligneArret;
    private String typeSignalement;

    private Toolbar toolbar;

    private Marker arret;
    private Polyline ligne;
    private Marker currentPosition;
    private LocationManager locationManager;
    private Location bestCurrentLocation;

    private static final int MAX_TIME_BEST_CURRENT_LOCATION = 1000 * 60 * 2;

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
    protected void onDestroy() {
        super.onDestroy();
        this.desabonnementNetwork();
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

        this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
    }

    public Marker createArret() {
        Arret a = this.ligneArret.getValue();

        LatLng positionArret = this.getCoordinate(a.getCoordonnees());

        MarkerOptions signalement = new MarkerOptions();
        signalement.position(positionArret);
        signalement.title(a.getNom());
        signalement.snippet(this.typeSignalement.toUpperCase());

        if (this.typeSignalement.equals(Config.CONTROLEUR)) {
            signalement.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_controleur));
        } else if (this.typeSignalement.equals(Config.HORAIRES)) {
            signalement.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_time));
        } else if (this.typeSignalement.equals(Config.ACCIDENTS)) {
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
            ligne.color(Config.LIGNE_1_COLOR);
        } else if (l.getNom().equals("Ligne 2")) {
            ligne.color(Config.LIGNE_2_COLOR);
        } else if (l.getNom().equals("Ligne 3")) {
            ligne.color(Config.LIGNE_3_COLOR);
        } else {
            ligne.color(Config.LIGNE_4_COLOR);
        }

        ligne.width(Config.LIGNE_WIDTH);

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

        if (this.isBetterLocation(location, this.bestCurrentLocation)) {
            this.bestCurrentLocation = location;

            if (this.currentPosition != null) {
                this.currentPosition.remove();
            }

            float[] results = new float[1];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                    this.arret.getPosition().latitude, this.arret.getPosition().longitude, results);

            String distance = getResources().getString(R.string.snippet_position_courante_1) + " " + results[0] + " " + getResources().getString(R.string.snippet_position_courante_2);

            MarkerOptions markerOptionsCurrentPosition = new MarkerOptions();
            markerOptionsCurrentPosition.title(getResources().getString(R.string.titre_position_courante));
            markerOptionsCurrentPosition.snippet(distance);
            markerOptionsCurrentPosition.position(new LatLng(location.getLatitude(), location.getLongitude()));
            this.currentPosition = this.mMap.addMarker(markerOptionsCurrentPosition);
            this.currentPosition.showInfoWindow();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
            this.abonnementNetwork();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
            this.desabonnementNetwork();
        }
    }

    public void abonnementNetwork() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Config.DISTANCE_MAJ_MIN_TIME_NETWORK, Config.DISTANCE_MAJ_MIN_DISTANCE_NETWORK, this);
    }

    public void desabonnementNetwork() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > MAX_TIME_BEST_CURRENT_LOCATION;
        boolean isSignificantlyOlder = timeDelta < -MAX_TIME_BEST_CURRENT_LOCATION;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            System.out.println("isSignificantlyNewer");
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            System.out.println("isMoreAccurate");
            return true;
        } else if (isNewer && !isLessAccurate) {
            System.out.println("isNewer && !isLessAccurate");
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            System.out.println("isNewer && !isSignificantlyLessAccurate && isFromSameProvider");
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
