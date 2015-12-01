package activites;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.florian.signprojectclient.R;

import modeles.Modele.Utilisateur;

/**
 * Created by Axel_2 on 28/11/2015.
 */
public class ConnectionActivity extends Activity {

    private EditText pseudo;
    private EditText mdp;
    private Button seConnecter;
    private Button annuler;
    private int alphaBtnSeConnecter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        this.pseudo = (EditText) findViewById(R.id.editTextPseudoConnection);
        this.mdp = (EditText) findViewById(R.id.editTextMdpConnection);
        this.seConnecter = (Button) findViewById(R.id.BtnSeConnecterConnection);
        this.annuler = (Button) findViewById(R.id.BtnAnnulerConnection);

        this.alphaBtnSeConnecter = 255;

        this.seConnecter.setEnabled(false);
        seConnecter.setAlpha(64f / 255f);

        pseudo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                System.out.println(PixelFormat.TRANSPARENT +" "+ alphaBtnSeConnecter);
                if (!Utilisateur.pseudoValide(s.toString())) {
                    pseudo.setError(getResources().getString(R.string.erreur_pseudo));
                    seConnecter.setEnabled(false);
                    seConnecter.setAlpha(64f/255f);
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
                    seConnecter.setAlpha(64f / 255f);
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

                //A FAIRE : verif pseudo et mdp BD sur serveur

                Intent intent = new Intent(ConnectionActivity.this, AccueilUserActivity.class);
                startActivity(intent);
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
}
