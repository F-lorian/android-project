package activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.florian.signprojectclient.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import modeles.modele.Utilisateur;
import modeles.modeleBD.UtilisateurBD;
import utilitaires.Config;
import utilitaires.PostRequest;
import utilitaires.SessionManager;

/**
 * Created by Axel_2 on 28/11/2015.
 */
public class ConnectionActivity extends Activity {

    private EditText pseudo;
    private EditText mdp;
    private Button seConnecter;
    private Button annuler;
    private static float alphaBtnSeConnecter = 64f/255f;
    private AlertDialog.Builder buildAlertInscriptionInvalide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        this.pseudo = (EditText) findViewById(R.id.editTextPseudoConnection);
        this.mdp = (EditText) findViewById(R.id.editTextMdpConnection);
        this.seConnecter = (Button) findViewById(R.id.BtnSeConnecterConnection);
        this.annuler = (Button) findViewById(R.id.BtnAnnulerConnection);

        this.seConnecter.setEnabled(false);
        this.seConnecter.setAlpha(alphaBtnSeConnecter);

        this.buildAlertInscriptionInvalide = new AlertDialog.Builder(this);
        this.buildAlertInscriptionInvalide.setTitle(getResources().getString(R.string.titre_alert_dialog_erreur));
        this.buildAlertInscriptionInvalide.setIcon(R.drawable.ic_action_error);
        this.buildAlertInscriptionInvalide.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        pseudo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!Utilisateur.pseudoValide(s.toString())) {
                    pseudo.setError(getResources().getString(R.string.erreur_pseudo));
                    seConnecter.setEnabled(false);
                    seConnecter.setAlpha(alphaBtnSeConnecter);
                } else {
                    pseudo.setError(null);

                    if (Utilisateur.mdpValide(mdp.getText().toString()))
                    {
                        seConnecter.setEnabled(true);
                        seConnecter.setAlpha(1f);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mdp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!Utilisateur.mdpValide(s.toString()))
                {
                    mdp.setError(getResources().getString(R.string.erreur_mdp));
                    seConnecter.setEnabled(false);
                    seConnecter.setAlpha(alphaBtnSeConnecter);
                }
                else {
                    mdp.setError(null);

                    if (Utilisateur.pseudoValide(pseudo.getText().toString()))
                    {
                        seConnecter.setEnabled(true);
                        seConnecter.setAlpha(1f);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        seConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Config.isNetworkAvailable(ConnectionActivity.this))
                {
                    List<NameValuePair> pairsPost = new ArrayList<NameValuePair>();
                    pairsPost.add(new BasicNameValuePair("pseudo",ConnectionActivity.this.pseudo.getText().toString()));
                    pairsPost.add(new BasicNameValuePair("password", ConnectionActivity.this.mdp.getText().toString()));
                    pairsPost.add(new BasicNameValuePair("regId", ""));

                    RequestPostTask requestPostTask = new RequestPostTask("connection",pairsPost,ConnectionActivity.this);
                    requestPostTask.execute();
                }
                else
                {
                    buildAlertInscriptionInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
                    AlertDialog alertInscriptionInvalide = buildAlertInscriptionInvalide.create();
                    alertInscriptionInvalide.show();
                }

            }
        });



        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pseudo.setText("");
                mdp.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class RequestPostTask extends AsyncTask<Void,Void,Void> {

        private PostRequest postRequest;
        private ProgressDialog progressDialog;
        private Activity activity;

        public RequestPostTask(String action, List pairs, Activity activity){
            this.postRequest = new PostRequest(action,pairs);
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... params) {
            this.postRequest.sendRequest();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.progressDialog = ProgressDialog.show(activity, activity.getResources().getString(R.string.progress_dialog_titre), activity.getResources().getString(R.string.progress_dialog_message_connection));

            this.progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            progressDialog.dismiss();
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(this.postRequest.getResultat());

                if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED) || jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                {
                    buildAlertInscriptionInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_pseudo_mdp));
                    AlertDialog alertInscriptionInvalide = buildAlertInscriptionInvalide.create();
                    alertInscriptionInvalide.show();
                }
                else
                {
                    SessionManager sessionManager = new SessionManager(ConnectionActivity.this);

                    /***** REGID GCM A IMPLEMENTER *****/
                    String regidGCM = "";
                    sessionManager.createLoginSession(jsonObject.getJSONObject(Config.JSON_DATA).getInt("id"), jsonObject.getJSONObject(Config.JSON_DATA).getString("pseudo"), regidGCM);

                    Intent intent = new Intent(ConnectionActivity.this, AccueilUserActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    ConnectionActivity.this.finish();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
