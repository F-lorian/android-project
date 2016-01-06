package utilitaires;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.florian.signprojectclient.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import activites.AccueilUserActivity;

/**
 * Created by Axel_2 on 07/12/2015.
 */
public class InitDataTask extends AsyncTask<Void,Void,Void> {

    protected ProgressDialog progressDialog;
    protected Activity activity;
    protected String urlKml;
    protected boolean reussi;

    public InitDataTask(Activity activity, String urlKml){
        this.activity = activity;
        this.urlKml = urlKml;
        this.reussi = true;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            InputStream is = this.activity.getAssets().open(urlKml);
            ParserKMLToBD parserKMLToBD = new ParserKMLToBD();
            parserKMLToBD.parse(is);

            this.reussi = parserKMLToBD.addInBD(this.activity);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

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
        }

    }


}
