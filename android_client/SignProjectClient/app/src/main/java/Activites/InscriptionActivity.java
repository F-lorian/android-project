package activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.florian.signprojectclient.R;

import modeles.Modele.Utilisateur;

/**
 * Created by Axel_2 on 28/11/2015.
 */
public class InscriptionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        EditText pseudo = (EditText) findViewById(R.id.editTextPseudoInscription);

        pseudo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText pseudo = (EditText) findViewById(R.id.editTextPseudoInscription);
                Button seConnecter = (Button) findViewById(R.id.BtnSeConnecterInscription);

                if (!Utilisateur.pseudoValide(pseudo.getText().toString())) {
                    pseudo.setError(getResources().getString(R.string.erreur_pseudo));
                    seConnecter.setEnabled(false);
                } else {
                    seConnecter.setEnabled(true);
                }

                return true;
            }
        });


        EditText mdp = (EditText) findViewById(R.id.editTextMdpInscription);

        mdp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText mdp = (EditText) findViewById(R.id.editTextMdpInscription);
                Button seConnecter = (Button) findViewById(R.id.BtnSeConnecterInscription);

                if (!Utilisateur.mdpValide(mdp.getText().toString()))
                {
                    mdp.setError(getResources().getString(R.string.erreur_pseudo));
                    seConnecter.setEnabled(false);
                }
                else {
                    seConnecter.setEnabled(true);
                }

                return true;
            }
        });

        EditText mdpConfirmation = (EditText) findViewById(R.id.editTextConfirmationMdpInscription);

        mdp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText mdp = (EditText) findViewById(R.id.editTextConfirmationMdpInscription);
                Button seConnecter = (Button) findViewById(R.id.BtnSeConnecterInscription);

                if (!Utilisateur.mdpValide(mdp.getText().toString()))
                {
                    mdp.setError(getResources().getString(R.string.erreur_pseudo));
                    seConnecter.setEnabled(false);
                }
                else {
                    seConnecter.setEnabled(true);
                }

                return true;
            }
        });

        Button seConnecter = (Button) findViewById(R.id.BtnSeConnecterInscription);

        seConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText pseudo = (EditText) findViewById(R.id.editTextPseudoInscription);
                EditText mdp = (EditText) findViewById(R.id.editTextMdpInscription);
                EditText mdpConfirmation = (EditText) findViewById(R.id.editTextConfirmationMdpInscription);

                if (mdp.getText().toString().equals(mdpConfirmation.getText().toString()))
                {
                    //A FAIRE : verif pseudo et mdp BD sur serveur

                    Intent intent = new Intent(InscriptionActivity.this, AccueilUserActivity.class);
                    startActivity(intent);
                }
                else
                {
                    mdpConfirmation.setError(getResources().getString(R.string.erreur_egaux_mdp));
                }

            }
        });

        Button annuler = (Button) findViewById(R.id.BtnAnnulerConnection);

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText pseudo = (EditText) findViewById(R.id.editTextPseudoInscription);
                EditText mdp = (EditText) findViewById(R.id.editTextMdpInscription);
                EditText mdpConfirmation = (EditText) findViewById(R.id.editTextConfirmationMdpInscription);

                pseudo.setText("");
                mdp.setText("");
                mdpConfirmation.setText("");
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