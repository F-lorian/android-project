package activites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.florian.signprojectclient.R;

import adapters.AdapterListViewGroupe;
import modeles.modele.Groupe;
import modeles.modele.Utilisateur;
import modeles.modeleBD.GroupeBD;
import modeles.modeleBD.GroupeUtilisateurBD;
import utilitaires.Config;
import utilitaires.SessionManager;

/**
 * Created by Florian on 05/01/2016.
 */
public class GroupeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView nom;
    private TextView description;
    private Button supprimer;
    private Button modifier;
    private Button rejoindre;
    private Button quitter;
    private Button annuler_demande;
    private LinearLayout info;
    private LinearLayout layout_admin;
    private LinearLayout layout_membre;
    private LinearLayout layout_en_attente;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id_groupe = getIntent().getIntExtra(Config.ID_GROUPE, -1);

        if (id_groupe != -1) {
            setContentView(R.layout.activity_groupe);

            // Set a Toolbar to replace the ActionBar.
            this.toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setTitle(getResources().getString(R.string.titre_toolbar_groupe));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            this.nom = (TextView) findViewById(R.id.nom_groupe);
            this.description = (TextView) findViewById(R.id.description_groupe);

            this.rejoindre = (Button) findViewById(R.id.action_rejoindre_button_group);

            this.layout_admin = (LinearLayout) findViewById(R.id.layout_admin);
            this.modifier = (Button) findViewById(R.id.action_modifier_button_group);
            this.supprimer = (Button) findViewById(R.id.action_supprimer_button_group);

            this.layout_membre = (LinearLayout) findViewById(R.id.layout_membre);
            this.quitter = (Button) findViewById(R.id.action_quitter_button_group);

            this.layout_en_attente = (LinearLayout) findViewById(R.id.layout_en_attente);
            this.annuler_demande = (Button) findViewById(R.id.action_annuler_button_group);

            GroupeBD groupeBD = new GroupeBD(this);
            groupeBD.open();
            Groupe groupe = groupeBD.getGroupe(id_groupe);
            groupeBD.close();

            SessionManager sessionManager = new SessionManager(this);

            int idUser = sessionManager.getUserId();
            if (groupe.getAdmin().getId() == idUser) {
                //onclick
                this.layout_membre.setVisibility(View.GONE);
                this.layout_en_attente.setVisibility(View.GONE);
                this.rejoindre.setVisibility(View.GONE);
                this.modifier.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edit();
                    }
                });
                this.supprimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete();
                    }
                });

            } else {

                GroupeUtilisateurBD groupeUtilisateurBD = new GroupeUtilisateurBD(this);
                groupeUtilisateurBD.open();
                String s = groupeUtilisateurBD.isInGroup(idUser, id_groupe);
                groupeUtilisateurBD.close();

                if (s != null && s.equals(GroupeUtilisateurBD.ETAT_APPARTIENT)) {
                    //onclick
                    this.layout_admin.setVisibility(View.GONE);
                    this.rejoindre.setVisibility(View.GONE);
                    this.layout_en_attente.setVisibility(View.GONE);

                    this.quitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            quit();
                        }
                    });

                } else if (s != null && s.equals(GroupeUtilisateurBD.ETAT_ATTENTE)) {
                    this.layout_admin.setVisibility(View.GONE);
                    this.rejoindre.setVisibility(View.GONE);

                    this.annuler_demande.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelDemand();
                        }
                    });

                } else {
                    this.layout_admin.setVisibility(View.GONE);
                    this.layout_membre.setVisibility(View.GONE);
                    this.layout_en_attente.setVisibility(View.GONE);

                    this.rejoindre.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendDemand();
                        }
                    });
                }
            }

            this.nom.setText(groupe.getNom());
            this.description.setText(groupe.getDescription());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void edit() {

        int id_groupe = getIntent().getIntExtra(Config.ID_GROUPE, -1);

        Intent intent = new Intent(this, ModificationGroupeActivity.class);
        intent.putExtra(Config.ID_GROUPE, id_groupe);
        startActivity(intent);

    }
    public void delete() {

        int id_groupe = getIntent().getIntExtra(Config.ID_GROUPE, -1);

    }
    public void quit() {

        int id_groupe = getIntent().getIntExtra(Config.ID_GROUPE, -1);

    }
    public void sendDemand() {

        int id_groupe = getIntent().getIntExtra(Config.ID_GROUPE, -1);


    }

    public void cancelDemand() {

        int id_groupe = getIntent().getIntExtra(Config.ID_GROUPE, -1);


    }
}
