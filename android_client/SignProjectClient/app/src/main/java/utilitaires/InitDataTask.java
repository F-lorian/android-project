package utilitaires;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.florian.signprojectclient.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import activites.AccueilUserActivity;
import modeles.modele.Arret;
import modeles.modele.Groupe;
import modeles.modele.Signalement;
import modeles.modele.SignalementGroupe;
import modeles.modele.SignalementPublic;
import modeles.modele.TypeSignalement;
import modeles.modele.Utilisateur;
import modeles.modeleBD.GroupeBD;
import modeles.modeleBD.LigneArretBD;
import modeles.modeleBD.SignalementBD;

/**
 * Created by Axel_2 on 07/12/2015.
 */
public class InitDataTask extends AsyncTask<Void,Void,Void> {

    protected ProgressDialog progressDialog;
    protected Activity activity;
    protected String urlKml;
    protected boolean reussi;

    public InitDataTask(Activity activity){
        this.activity = activity;
        this.urlKml = Config.KML_URL;
        this.reussi = true;
    }

    @Override
    protected Void doInBackground(Void... params) {

        LigneArretBD la = new LigneArretBD(this.activity);

        la.open();

        if (la.getCount() <= 0) {

            InputStream is = null;

            try {
                is = this.downloadUrl();
                ParserKMLToBD parserKMLToBD = new ParserKMLToBD();
                parserKMLToBD.parse(is);
                this.reussi = parserKMLToBD.addInBD(this.activity);

                //JeuDeDonnees j = new JeuDeDonnees(this.activity);

            } catch (Exception e) {
                e.printStackTrace();
                this.reussi = false;
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        this.reussi = false;
                    }
                }
            }
        }

        la.close();

        this.initialisationSignalementEtGroupe();

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        this.progressDialog = ProgressDialog.show(activity, activity.getResources().getString(R.string.progress_dialog_titre), activity.getResources().getString(R.string.progress_dialog_message));

        this.progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        progressDialog.dismiss();

        if (this.reussi)
        {
            Toast.makeText(this.activity, this.activity.getResources().getString(R.string.popup_progress_dialog_termine), Toast.LENGTH_LONG).show();
            MenuItem mi = ((AccueilUserActivity)this.activity).nvDrawer.getMenu().getItem(0).getSubMenu().getItem(0);
            ((AccueilUserActivity)this.activity).selectDrawerItem(mi);
        }
        else
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.activity)
                    .setTitle(R.string.titre_alert_dialog_erreur)
                    .setMessage(R.string.message_alert_dialog_erreur_chargement)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            InitDataTask.this.activity.finish();
                        }
                    });

            alertDialog.show();
        }

    }

    private InputStream downloadUrl() throws IOException {
        URL url = new URL(this.urlKml);
        return url.openStream();
    }

    private void initialisationSignalementEtGroupe()
    {
        SessionManager sessionManager = new SessionManager(this.activity);
        List<NameValuePair> pairsPost = new ArrayList<>();
        pairsPost.add(new BasicNameValuePair("id", sessionManager.getUserId() + ""));
        PostRequest postRequest = new PostRequest("initialisation",pairsPost);
        postRequest.sendRequest();

        try {

            JSONObject resultatJson = new JSONObject(postRequest.getResultat());
            JSONArray groupesJson = resultatJson.getJSONArray("groupes");
            JSONArray signalementJson = resultatJson.getJSONArray("signalements");

            this.ajoutGroupes(groupesJson);
            this.ajoutSignalements(signalementJson);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void ajoutSignalements(JSONArray signalementJson) throws JSONException, ParseException {
        SignalementBD signalementBD = new SignalementBD(this.activity);
        signalementBD.open();

        signalementBD.deleteAll(SignalementBD.TABLE_NAME_SIGNALEMENT_RECU);

        for (int i=0; i<signalementJson.length(); i++)
        {
            JSONObject jsonObject = signalementJson.getJSONObject(i);
            Signalement signalement = this.getSignalementFromJson(jsonObject);
            signalementBD.addSignalement(signalement, SignalementBD.TABLE_NAME_SIGNALEMENT_RECU);
        }

        signalementBD.close();
    }

    private Signalement getSignalementFromJson(JSONObject jsonObject) throws JSONException, ParseException {
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

        return signalement;
    }

    private void ajoutGroupes(JSONArray groupesJson) throws JSONException, ParseException {
        GroupeBD groupeBD = new GroupeBD(this.activity);
        groupeBD.open();

        groupeBD.deleteAll();

        for (int i=0; i<groupesJson.length(); i++)
        {
            JSONObject jsonObject = groupesJson.getJSONObject(i);
            Groupe groupe = this.getGroupeFromJson(jsonObject);
            groupeBD.add(groupe);
        }

        groupeBD.close();
    }

    private Groupe getGroupeFromJson(JSONObject jsonObject) throws JSONException, ParseException {

        Groupe groupe = new Groupe();
        groupe.setId(jsonObject.getInt("id"));
        groupe.setNom(jsonObject.getString("name"));
        groupe.setType(jsonObject.getString("type"));
        groupe.setAdmin(new Utilisateur(jsonObject.getInt("creator"), "", "", null, null, null));
        groupe.setDescription(jsonObject.getString("description"));

        return groupe;
    }


}
