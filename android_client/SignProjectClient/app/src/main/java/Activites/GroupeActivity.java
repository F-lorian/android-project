package activites;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.florian.signprojectclient.R;

import adapters.AdapterListViewGroupe;
import modeles.modele.Groupe;
import modeles.modele.Utilisateur;
import modeles.modeleBD.GroupeBD;
import modeles.modeleBD.GroupeUtilisateurBD;
import utilitaires.SessionManager;

/**
 * Created by Florian on 05/01/2016.
 */
public class GroupeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView nom;
    private TextView description;
    private Button action;
    private TextView info;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe);

        // Set a Toolbar to replace the ActionBar.
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getResources().getString(R.string.titre_toolbar_groupe));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.nom = (TextView) findViewById(R.id.nom_groupe);
        this.description = (TextView) findViewById(R.id.description_groupe);
        this.action = (Button) findViewById(R.id.action_button_group);
        this.info = (TextView) findViewById(R.id.info_groupe);

        int id_groupe = getIntent().getIntExtra(AdapterListViewGroupe.ID_GROUPE, -1);

        if (id_groupe != -1) {
            GroupeBD groupeBD = new GroupeBD(this);
            groupeBD.open();
            Groupe groupe = groupeBD.getGroupe(id_groupe);
            groupeBD.close();

            SessionManager sessionManager = new SessionManager(this);
            GroupeUtilisateurBD groupeUtilisateurBD = new GroupeUtilisateurBD(this);
            int idUser = sessionManager.getUserId();
            if (groupe.getAdmin().getId() == idUser) {
                action.setText(getResources().getString(R.string.btn_supprimer_groupe));
                info.setVisibility(View.GONE);
            } else {
                if (groupeUtilisateurBD.isInGroup(idUser, id_groupe).equals(GroupeUtilisateurBD.ETAT_APPARTIENT)) {
                    action.setText(getResources().getString(R.string.btn_quitter_groupe));
                    info.setVisibility(View.GONE);
                } else if (groupeUtilisateurBD.isInGroup(idUser, id_groupe).equals(GroupeUtilisateurBD.ETAT_ATTENTE)) {
                    action.setVisibility(View.GONE);
                } else {
                    info.setVisibility(View.GONE);
                    action.setText(getResources().getString(R.string.btn_rejoindre_groupe));
                    action.setBackgroundColor(ContextCompat.getColor(this, R.color.wallet_holo_blue_light));
                }
            }

            nom.setText(groupe.getNom());
            description.setText(groupe.getDescription());

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
}
