package activites;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.florian.signprojectclient.R;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;
import java.util.HashMap;

import adapters.AdapterSpinnerTypeDestination;
import adapters.AdapterSpinnerTypeSignalement;
import modeles.Modele.Groupe;
import modeles.Modele.TypeSignalement;
import modeles.Modele.Utilisateur;
import modeles.ModeleBD.GroupeBD;
import modeles.ModeleBD.GroupeUtilisateurBD;
import modeles.ModeleBD.LigneArretBD;
import modeles.ModeleBD.TypeSignalementBD;
import modeles.ModeleBD.UtilisateurBD;
import utilitaires.SessionManager;
import utilitaires.UtilisateursDestinationSignalementCompletionView;


/**
 * Created by Axel_2 on 08/12/2015.
 */
public class AjoutSignalementActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private Spinner spinnerTypeSignalement;
    private AutoCompleteTextView autoCompleteTextViewArret;
    private Spinner spinnerTypeDestinataire;

    private ArrayList<TypeSignalement> typeSignalements;
    private HashMap<Integer,String> arrets;
    private ArrayList<String> typeDestinataire;

    private ArrayList<Groupe> groupesDestination;
    private ArrayList<Utilisateur> utilisateursDestination;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_signalement);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getResources().getString(R.string.titre_toolbar_ajout_signalement));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TypeSignalementBD typeSignalementBD = new TypeSignalementBD(this);
        typeSignalementBD.open();
        this.typeSignalements = typeSignalementBD.getTypeSignalements();
        typeSignalementBD.close();

        this.spinnerTypeSignalement = (Spinner) findViewById(R.id.spinnerAjoutSignalement);
        AdapterSpinnerTypeSignalement adapterSpinnerTypeSignalement = new AdapterSpinnerTypeSignalement(this,this.typeSignalements);
        this.spinnerTypeSignalement.setAdapter(adapterSpinnerTypeSignalement);


        LigneArretBD ligneArretBD = new LigneArretBD(this);
        ligneArretBD.open();
        this.arrets = ligneArretBD.getStringOfArretLigne();
        ligneArretBD.close();

        this.autoCompleteTextViewArret = (AutoCompleteTextView) findViewById(R.id.champArretSignalement);
        ArrayList<String> listeStringArrets = new ArrayList<String>();
        listeStringArrets.addAll(this.arrets.values());
        ArrayAdapter<String> adapterAutoCompleteTextViewArret = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, listeStringArrets);
        this.autoCompleteTextViewArret.setAdapter(adapterAutoCompleteTextViewArret);

        this.typeDestinataire = new ArrayList<String>();
        this.typeDestinataire.add(this.getResources().getString(R.string.public_spinner));
        this.typeDestinataire.add(this.getResources().getString(R.string.groupe_spinner));
        this.typeDestinataire.add(this.getResources().getString(R.string.autres_personnes_spinner));

        this.spinnerTypeDestinataire = (Spinner) findViewById(R.id.spinnerDestinationAjoutSignalement);
        AdapterSpinnerTypeDestination adapterSpinnerTypeDestination = new AdapterSpinnerTypeDestination(this,this.typeDestinataire);
        this.spinnerTypeDestinataire.setAdapter(adapterSpinnerTypeDestination);

        LinearLayout layoutForm = (LinearLayout) findViewById(R.id.layoutFormAjoutSignalement);
        getLayoutInflater().inflate(R.layout.view_remarques_ajout_signalement,layoutForm);

        this.spinnerTypeSignalement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AjoutSignalementActivity.this.updateViewOnSelectSpinnerTypeItem(AjoutSignalementActivity.this.typeSignalements.get(position).getType());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GroupeUtilisateurBD groupeUtilisateurBD = new GroupeUtilisateurBD(this);
        groupeUtilisateurBD.open();
        SessionManager sessionManager = new SessionManager(this);
        this.groupesDestination = groupeUtilisateurBD.getGroupe(sessionManager.getUserId(), GroupeUtilisateurBD.ETAT_APPARTIENT);
        groupeUtilisateurBD.close();


        UtilisateurBD utilisateurBD = new UtilisateurBD(this);
        utilisateurBD.open();
        this.utilisateursDestination = utilisateurBD.getUtilisateurs();
        utilisateurBD.close();

        this.spinnerTypeDestinataire.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AjoutSignalementActivity.this.updateViewOnSelectSpinnerTypeDestinationItem(AjoutSignalementActivity.this.typeDestinataire.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_ajout_signalement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case R.id.envoyer_signalement_toolbar:
                return true;
            case android.R.id.home:
                this.finish();

        }

        return super.onOptionsItemSelected(item);
    }

    public void updateViewOnSelectSpinnerTypeItem(String type)
    {
        if (type.equals(getResources().getString(R.string.controleur_spinner)) || type.equals(getResources().getString(R.string.accident_autres_spinner)))
        {
            View remarques = findViewById(R.id.layoutRemarquesAjoutSignalement);

            if (remarques == null)
            {
                View horaire = findViewById(R.id.layoutPrincipaleHoraireAjoutSignalement);
                ViewGroup parent = (ViewGroup) horaire.getParent();
                parent.removeView(horaire);
                remarques  = getLayoutInflater().inflate(R.layout.view_remarques_ajout_signalement, parent, false);
                parent.addView(remarques);
            }
        }
        else if (type.equals(getResources().getString(R.string.horaire_spinner)))
        {
            View horaire = findViewById(R.id.layoutPrincipaleHoraireAjoutSignalement);

            if (horaire == null)
            {
                View remarques = findViewById(R.id.layoutRemarquesAjoutSignalement);
                ViewGroup parent = (ViewGroup) remarques.getParent();
                parent.removeView(remarques);
                horaire  = getLayoutInflater().inflate(R.layout.view_horaire_ajout_signalement, parent, false);
                parent.addView(horaire);
            }
        }

    }

    public void updateViewOnSelectSpinnerTypeDestinationItem(String type)
    {
        View v = findViewById(R.id.layoutRemarquesAjoutSignalement);

        if (v == null)
        {
            v = findViewById(R.id.layoutPrincipaleHoraireAjoutSignalement);
        }

        if (type.equals(getResources().getString(R.string.autres_personnes_spinner)))
        {
            View autresPersonnes = findViewById(R.id.layoutPrincipaleUserAjoutSignalement);

            if (autresPersonnes == null)
            {
                View groupe = findViewById(R.id.layoutPrincipaleGroupeUserAjoutSignalement);
                ViewGroup parent = (ViewGroup) v.getParent();

                if (groupe != null)
                {
                    parent.removeView(groupe);
                }

                autresPersonnes  = getLayoutInflater().inflate(R.layout.view_token_utilisateur_ajout_signalement, parent, false);
                parent.addView(autresPersonnes, parent.indexOfChild(v));

                UtilisateursDestinationSignalementCompletionView utilisateursDestinationSignalementCompletionView = (UtilisateursDestinationSignalementCompletionView) findViewById(R.id.tokenUtilisateur);
                ArrayAdapter<Utilisateur> utilisateurArrayAdapter = new ArrayAdapter<Utilisateur>(this,android.R.layout.simple_list_item_1,this.utilisateursDestination);
                utilisateursDestinationSignalementCompletionView.setAdapter(utilisateurArrayAdapter);
                utilisateursDestinationSignalementCompletionView.setPrefix(this.getResources().getString(R.string.prefix_token_ajout_signalement) + " ");
                utilisateursDestinationSignalementCompletionView.setSplitChar(' ');
                utilisateursDestinationSignalementCompletionView.allowDuplicates(false);

            }
        }
        else if (type.equals(getResources().getString(R.string.groupe_spinner)))
        {
            View groupe = findViewById(R.id.layoutPrincipaleGroupeUserAjoutSignalement);

            if (groupe == null)
            {
                View autresPersonnes = findViewById(R.id.layoutPrincipaleUserAjoutSignalement);
                ViewGroup parent = (ViewGroup) v.getParent();

                if (autresPersonnes != null)
                {
                    parent.removeView(autresPersonnes);
                }

                groupe  = getLayoutInflater().inflate(R.layout.view_groupe_user_ajout_signalement, parent, false);
                parent.addView(groupe, parent.indexOfChild(v));

                ListView listViewGroups = (ListView) findViewById(R.id.listGroupUserAjoutSignalement);
                ArrayAdapter<Groupe> arrayAdapter = new ArrayAdapter<Groupe>(this,android.R.layout.simple_list_item_multiple_choice,this.groupesDestination);
                listViewGroups.setAdapter(arrayAdapter);
            }
        }
        else
        {
            View groupe = findViewById(R.id.layoutPrincipaleGroupeUserAjoutSignalement);

            if (groupe != null)
            {
                ViewGroup parent = (ViewGroup) v.getParent();
                parent.removeView(groupe);
            }
            else
            {
                View autresPersonnes = findViewById(R.id.layoutPrincipaleUserAjoutSignalement);

                if (autresPersonnes != null)
                {
                    ViewGroup parent = (ViewGroup) v.getParent();
                    parent.removeView(autresPersonnes);
                }
            }
        }

    }


}
