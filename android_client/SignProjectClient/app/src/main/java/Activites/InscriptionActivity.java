package activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import utilitaires.GestionRegID;
import utilitaires.RequestPostTask;
import modeles.modele.Utilisateur;
import utilitaires.Config;
import utilitaires.SessionManager;

/**
 * Created by Axel_2 on 28/11/2015.
 */
public class InscriptionActivity extends Activity {

    private EditText pseudo;
    private EditText mdp;
    private EditText confirmeMdp;
    private Button seConnecter;
    private Button annuler;
    private static float alphaBtnSeConnecter = 64f/255f;
    private AlertDialog.Builder buildAlertInscriptionInvalide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        this.pseudo = (EditText) findViewById(R.id.editTextPseudoInscription);
        this.mdp = (EditText) findViewById(R.id.editTextMdpInscription);
        this.confirmeMdp = (EditText) findViewById(R.id.editTextConfirmationMdpInscription);
        this.seConnecter = (Button) findViewById(R.id.BtnSeConnecterInscription);
        this.annuler = (Button) findViewById(R.id.BtnAnnulerInscription);

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

                    if (Utilisateur.mdpValide(mdp.getText().toString()) && Utilisateur.mdpValide(confirmeMdp.getText().toString()))
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

                if (!Utilisateur.mdpValide(s.toString())) {
                    mdp.setError(getResources().getString(R.string.erreur_mdp));
                    seConnecter.setEnabled(false);
                    seConnecter.setAlpha(alphaBtnSeConnecter);
                } else {
                    mdp.setError(null);

                    if (Utilisateur.pseudoValide(pseudo.getText().toString()) && Utilisateur.mdpValide(confirmeMdp.getText().toString())) {
                        seConnecter.setEnabled(true);
                        seConnecter.setAlpha(1f);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmeMdp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!Utilisateur.mdpValide(s.toString())) {
                    confirmeMdp.setError(getResources().getString(R.string.erreur_mdp));
                    seConnecter.setEnabled(false);
                    seConnecter.setAlpha(alphaBtnSeConnecter);
                } else {
                    confirmeMdp.setError(null);

                    if (Utilisateur.pseudoValide(pseudo.getText().toString()) && Utilisateur.mdpValide(mdp.getText().toString())) {
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

                if (mdp.getText().toString().equals(confirmeMdp.getText().toString()))
                {
                    if (Config.isNetworkAvailable(InscriptionActivity.this))
                    {
                        List<NameValuePair> pairsPost = new ArrayList<NameValuePair>();
                        pairsPost.add(new BasicNameValuePair("pseudo",InscriptionActivity.this.pseudo.getText().toString()));
                        pairsPost.add(new BasicNameValuePair("password",InscriptionActivity.this.mdp.getText().toString()));


                        Handler mHandler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {

                                try {
                                    JSONObject jsonObject = new JSONObject((String) msg.obj);

                                    if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED))
                                    {
                                        buildAlertInscriptionInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_inscription_denied));
                                        AlertDialog alertInscriptionInvalide = buildAlertInscriptionInvalide.create();
                                        alertInscriptionInvalide.show();
                                    }
                                    else if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                                    {
                                        buildAlertInscriptionInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_inscription_error));
                                        AlertDialog alertInscriptionInvalide = buildAlertInscriptionInvalide.create();
                                        alertInscriptionInvalide.show();
                                    }
                                    else
                                    {
                                        SessionManager sessionManager = new SessionManager(InscriptionActivity.this);

                                        sessionManager.createLoginSession(jsonObject.getJSONObject(Config.JSON_DATA).getInt("id"),jsonObject.getJSONObject(Config.JSON_DATA).getString("pseudo"),"");

                                        Intent intent = new Intent(InscriptionActivity.this, AccueilUserActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        InscriptionActivity.this.finish();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        RequestPostTask requestPostTask = new RequestPostTask("register",pairsPost, mHandler, InscriptionActivity.this, InscriptionActivity.this.getResources().getString(R.string.progress_dialog_message_inscription));
                        requestPostTask.execute();
                    }
                    else
                    {
                        buildAlertInscriptionInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
                        AlertDialog alertInscriptionInvalide = buildAlertInscriptionInvalide.create();
                        alertInscriptionInvalide.show();
                    }
                }
                else
                {
                    buildAlertInscriptionInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_egaux_mdp));
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
                confirmeMdp.setText("");
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
}