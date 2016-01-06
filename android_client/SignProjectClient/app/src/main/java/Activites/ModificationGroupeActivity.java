package activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.florian.signprojectclient.R;

import java.util.ArrayList;

import adapters.AdapterListViewGroupe;
import adapters.AdapterSpinnerTypeGroupe;
import modeles.modele.Groupe;
import modeles.modele.Utilisateur;
import modeles.modeleBD.GroupeBD;
import utilitaires.Config;
import utilitaires.SessionManager;

/**
 * Created by Florian on 06/01/2016.
 */
public class ModificationGroupeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText editTextNom;
    private Spinner spinnerType;
    private EditText editTextDescription;

    private Groupe groupe;

    private ArrayList<String> typeGroupes;

    private AlertDialog.Builder buildAlertContenuInvalide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id_groupe = getIntent().getIntExtra(Config.ID_GROUPE, -1);
        if (id_groupe != -1) {
            GroupeBD groupeBD = new GroupeBD(this);
            groupeBD.open();
            this.groupe = groupeBD.getGroupe(id_groupe);
            groupeBD.close();

            setContentView(R.layout.activity_ajout_groupe);

            // Set a Toolbar to replace the ActionBar.
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setTitle(getResources().getString(R.string.titre_toolbar_modifier_groupe));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            this.editTextNom = (EditText) findViewById(R.id.champNomGroupe);
            this.editTextDescription = (EditText) findViewById(R.id.champDescriptionGroupe);

            this.typeGroupes = new ArrayList<String>();
            this.typeGroupes.add(getResources().getString(R.string.type_public));
            this.typeGroupes.add(getResources().getString(R.string.type_prive));

            this.spinnerType = (Spinner) findViewById(R.id.spinnerTypeGroupe);
            AdapterSpinnerTypeGroupe adapterSpinnerTypeGroupe = new AdapterSpinnerTypeGroupe(this,this.typeGroupes);
            this.spinnerType.setAdapter(adapterSpinnerTypeGroupe);

            this.spinnerType.setSelection(this.getIndiceByType(Groupe.getStringWithType(this.groupe.getType())));
            this.editTextNom.setText(this.groupe.getNom());
            this.editTextDescription.setText(this.groupe.getDescription());

            this.editTextNom.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (!Groupe.nomValide(s.toString())) {
                        editTextNom.setError(getResources().getString(R.string.erreur_nom_groupe));
                    } else {
                        editTextNom.setError(null);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            this.editTextDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (!Groupe.descriptionValide(s.toString())) {
                        editTextDescription.setError(getResources().getString(R.string.erreur_description_groupe));
                    } else {
                        editTextDescription.setError(null);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            this.buildAlertContenuInvalide = new AlertDialog.Builder(this);
            this.buildAlertContenuInvalide.setTitle(getResources().getString(R.string.titre_alert_dialog_erreur));
            this.buildAlertContenuInvalide.setIcon(R.drawable.ic_action_error);
            this.buildAlertContenuInvalide.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_ajout_groupe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case R.id.toolbar_ajouter_groupe:
                valid();
                break;
            case android.R.id.home:
                this.finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void valid() {
        int indiceType = spinnerType.getSelectedItemPosition();

        String nom = ModificationGroupeActivity.this.editTextNom.getText().toString();
        String type = ModificationGroupeActivity.this.typeGroupes.get(indiceType);
        String description = ModificationGroupeActivity.this.editTextDescription.getText().toString();

        if (!Groupe.nomValide(nom)) {
            /*buildAlertContenuInvalide.setMessage(getResources().getString(R.string.erreur_nom_groupe));
            AlertDialog alertInscriptionInvalide = buildAlertContenuInvalide.create();
            alertInscriptionInvalide.show();*/

        }else if(!Groupe.descriptionValide(description)) {
            /*buildAlertContenuInvalide.setMessage(getResources().getString(R.string.erreur_description_groupe));
            AlertDialog alertInscriptionInvalide = buildAlertContenuInvalide.create();
            alertInscriptionInvalide.show();*/

        }else{
            this.groupe.setNom(nom);
            this.groupe.setType(Groupe.getTypeWithString(type));
            this.groupe.setDescription(description);
/*
                    buildAlertContenuInvalide.setMessage(description);
                    AlertDialog alertContenuInvalide = buildAlertContenuInvalide.create();
                    alertContenuInvalide.show();
                    System.out.println("description : "+description);
         */
            SessionManager sessionManager = new SessionManager(ModificationGroupeActivity.this);
            groupe.setAdmin(new Utilisateur(sessionManager.getUserId(), "", "", null, null, null));

            System.out.println("XXXXXXXXXXXXXXXXXX : "+sessionManager.getUserId());
            System.out.println(groupe);

            GroupeBD groupeBD = new GroupeBD(ModificationGroupeActivity.this);
            groupeBD.open();
            int nbmodifs = groupeBD.update(this.groupe);
            groupeBD.close();

            System.out.println("description : " + groupe.getDescription());

            if (nbmodifs >= 0)
            {
                Toast.makeText(ModificationGroupeActivity.this, ModificationGroupeActivity.this.getResources().getString(R.string.toast_signalement_envoye), Toast.LENGTH_LONG).show();
                ModificationGroupeActivity.this.finish();
            }
            else
            {
                buildAlertContenuInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_ajout_groupe_bd));
                AlertDialog alertContenuInvalide = buildAlertContenuInvalide.create();
                alertContenuInvalide.show();
            }

        }
    }

    public int getIndiceByType(String typeGroupe)
    {
        int ind = 0;

        for (int i = 0; i<this.typeGroupes.size(); i++)
        {
            if (this.typeGroupes.get(i).equals(typeGroupe))
            {
                return i;
            }
        }

        return ind;
    }
}
