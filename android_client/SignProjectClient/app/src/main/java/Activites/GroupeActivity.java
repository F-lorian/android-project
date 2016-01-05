package activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.florian.signprojectclient.R;

import adapters.AdapterListViewGroupe;
import modeles.modele.Groupe;
import modeles.modeleBD.GroupeBD;

/**
 * Created by Florian on 05/01/2016.
 */
public class GroupeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView nom;
    private TextView description;

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

        int id_groupe = getIntent().getIntExtra(AdapterListViewGroupe.ID_GROUPE, -1);

        if (id_groupe != -1){
            GroupeBD groupeBD = new GroupeBD(this);
            groupeBD.open();
            Groupe groupe = groupeBD.getGroupe(id_groupe);
            groupeBD.close();

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
