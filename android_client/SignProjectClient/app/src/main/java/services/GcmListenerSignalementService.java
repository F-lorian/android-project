package services;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.florian.signprojectclient.R;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import activites.AccueilUserActivity;
import activites.GroupeActivity;
import modeles.modele.Arret;
import modeles.modele.Signalement;
import modeles.modele.SignalementGroupe;
import modeles.modele.SignalementPublic;
import modeles.modele.TypeSignalement;
import modeles.modele.Utilisateur;
import modeles.modeleBD.ArretBD;
import modeles.modeleBD.SignalementBD;
import utilitaires.Config;
import utilitaires.PostRequest;
import utilitaires.SessionManager;

/**
 * Created by Axel_2 on 18/01/2016.
 */
public class GcmListenerSignalementService extends GcmListenerService implements LocationListener{

    private float distanceDuSignalement;
    private Arret arret;
    private Signalement signalement;
    private LocationManager locationManager;
    private Thread threadPostConfirmation;

    public GcmListenerSignalementService()
    {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.distanceDuSignalement = -1f;
        this.arret = null;
        this.signalement = null;
        this.locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        this.abonnementNetwork();
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {

        System.out.println("$$$$ data gcm serveur $$$ " + data);

        String type = data.getString("type");
        String message = data.getString("message");
        System.out.println("$$$$ signalement gcm serveur $$$ " + message);

        if(type.equals("signalement")){

            this.signalement = addSignalement(message);
            this.arret = getArret(signalement.getId());
            threadPostConfirmation.start();

            threadPostConfirmation.start();

        } else {
            sendNotificationGroupe(type, message);
        }

    }

    private Signalement addSignalement(String msg)
    {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            Signalement signalement = null;
            if (jsonObject.get("diffusion").equals(SignalementPublic.TYPE_DESTINATAIRE))
            {
                signalement = new SignalementPublic();
            }
            else
            {
                signalement = new SignalementGroupe();
            }

            signalement.setId(jsonObject.getInt("id"));
            signalement.setContenu(jsonObject.getString("contenu"));
            signalement.setRemarques(jsonObject.getString("remarque"));
            signalement.setDate(dateFormat.parse((String) jsonObject.get("date")));
            signalement.setArret(new Arret(jsonObject.getInt("arret"), "", "", "", null, null));
            signalement.setType(new TypeSignalement(jsonObject.getInt("type"), ""));
            signalement.setEmetteur(new Utilisateur(jsonObject.getInt("emetteur"), "", "", null, null, null));

            SignalementBD signalementBD = new SignalementBD(this);
            signalementBD.open();
            long id = signalementBD.addSignalement(signalement,SignalementBD.TABLE_NAME_SIGNALEMENT_RECU);
            signalementBD.close();


            if (id == -1)
            {
                return null;
            }

            return signalement;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Arret getArret(int id)
    {
        ArretBD arretBD = new ArretBD(this);
        arretBD.open();
        Arret arret = arretBD.getArret(id);
        arretBD.close();

        return arret;
    }

    private LatLng getCoordinate(String coordinate) {
        String[] coordinatesString = coordinate.split(",");
        double[] coordinates = new double[2];

        coordinates[0] = Double.valueOf(coordinatesString[1]);
        coordinates[1] = Double.valueOf(coordinatesString[0]);

        return new LatLng(coordinates[0], coordinates[1]);
    }

    @Override
    public void onLocationChanged(Location location) {

        SignalementBD signalementBD = new SignalementBD(this);
        signalementBD.open();
        ArrayList<Signalement> signalementsProchesNonVu = signalementBD.getSignalementsProchesNonVu(SignalementBD.TABLE_NAME_SIGNALEMENT_RECU,location,Config.DISTANCE_MAX_SIGNALEMENTS_PROCHES);


        if (signalementsProchesNonVu.size()>0) //TODO : Si plus de 5 signalements, prends les 5 plus proches
        {
            for (int i=0; i<signalementsProchesNonVu.size(); i++)
            {
                Signalement signalement = signalementsProchesNonVu.get(i);

                String messageNotification = this.createMessageNotification(signalement);

                signalement.setVu(true);

                int id = signalementBD.updateSignalement(signalement, SignalementBD.TABLE_NAME_SIGNALEMENT_RECU);

                this.sendNotificationSignalement(messageNotification, signalement.getId());
            }
        }

        signalementBD.close();
        LatLng positionArret = this.getCoordinate(arret.getCoordonnees());

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

    private void sendNotificationSignalement(String message, int idSignalement) {
        Intent intent = new Intent(this, AccueilUserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        /***** A ADAPTER *****/
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification_signalement)
                .setContentTitle(this.getResources().getString(R.string.titre_notification_signalement))
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000})
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(idSignalement, notificationBuilder.build());
    }

    private void sendNotificationGroupe(String type, String message) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
            int id_groupe = jsonObject.getInt("id");

            String title = "";
            String text = "";

            switch(type) {
                case "groupeRequest":
                    title = this.getResources().getString(R.string.titre_notification_groupe_demande);
                    text = this.getResources().getString(R.string.text_notification_groupe_demande);
                    break;
                case "groupeAccept":
                    title = this.getResources().getString(R.string.titre_notification_groupe_accept);
                    text = this.getResources().getString(R.string.text_notification_groupe_accept);
                    break;
                case "groupeInvite":
                    title = this.getResources().getString(R.string.titre_notification_groupe_invite);
                    text = this.getResources().getString(R.string.text_notification_groupe_invite);
                    break;
                case "inviteAccept":
                    title = this.getResources().getString(R.string.titre_notification_groupe_invite_accept);
                    text = this.getResources().getString(R.string.text_notification_groupe_invite_accept);
                    break;
            }


            Intent intent = new Intent(this, GroupeActivity.class);
            intent.putExtra(Config.ID_GROUPE, id_groupe);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            /***** A ADAPTER *****/
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notification_signalement)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000, 1000})
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(id_groupe, notificationBuilder.build());


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String createMessageNotification(Signalement signalement)
    {
        String type = "";

        String typeSignalement = signalement.getType().getType();

        if (typeSignalement.equals(Config.CONTROLEUR))
        {
            type = getResources().getString(R.string.controleur_spinner);
        }
        else if (typeSignalement.equals(Config.AUTRES))
        {
            type = getResources().getString(R.string.autres_spinner);
        }
        else if (typeSignalement.equals(Config.ACCIDENTS))
        {
            type = getResources().getString(R.string.accident_spinner);
        }
        else
        {
            type = getResources().getString(R.string.horaire_spinner);
        }

        return type + " - " + signalement.getArret().getNom();
    }

    private void updateHoraireThread(){

        threadPostConfirmation = new Thread() {

            @Override
            public void run() {
                SessionManager sessionManager = new SessionManager(GcmListenerSignalementService.this);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_id",sessionManager.getUserId()+""));
                params.add(new BasicNameValuePair("signalement_id",GcmListenerSignalementService.this.signalement.getId()+""));
                PostRequest postRequest = new PostRequest("signalementRecu",params);
            }
        };
    }

}
