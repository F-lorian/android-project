package activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.AdapterSpinnerTypeGroupe;
import modeles.modele.Groupe;
import utilitaires.RequestPostTask;
import modeles.modele.Utilisateur;
import modeles.modeleBD.GroupeBD;
import utilitaires.Config;
import utilitaires.SessionManager;


/**
 * Created by Florian on 04/01/2016.
 */
public class AjoutGroupeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText editTextNom;
    private Spinner spinnerType;
    private EditText editTextDescription;

    private ArrayList<String> typeGroupes;

    private AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_groupe);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getResources().getString(R.string.titre_toolbar_ajout_groupe));
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

        this.alert = new AlertDialog.Builder(this);
        this.alert.setTitle(getResources().getString(R.string.titre_alert_dialog_erreur));
        this.alert.setIcon(R.drawable.ic_action_error);
        this.alert.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        editTextNom.addTextChangedListener(new TextWatcher() {
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

        editTextDescription.addTextChangedListener(new TextWatcher() {
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

                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                this.finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void valid() {

        int indiceType = spinnerType.getSelectedItemPosition();
        String nom = this.editTextNom.getText().toString();
        String type = this.typeGroupes.get(indiceType);
        String description = this.editTextDescription.getText().toString();
        String typeConst = "";
        if(type.equals(getResources().getString(R.string.type_public))){
            typeConst = Groupe.TYPE_PUBLIC;
        } else if(type.equals(getResources().getString(R.string.type_prive))){
            typeConst = Groupe.TYPE_PRIVE;
        }
        SessionManager sessionManager = new SessionManager(this);
        String id_admin = Integer.toString(sessionManager.getUserId());


        if (!Groupe.nomValide(nom)) {
            /*buildAlertContenuInvalide.setMessage(getResources().getString(R.string.erreur_nom_groupe));
            AlertDialog alertInscriptionInvalide = buildAlertContenuInvalide.create();
            alertInscriptionInvalide.show();*/

        }else if(!Groupe.descriptionValide(description)) {
            /*buildAlertContenuInvalide.setMessage(getResources().getString(R.string.erreur_description_groupe));
            AlertDialog alertInscriptionInvalide = buildAlertContenuInvalide.create();
            alertInscriptionInvalide.show();*/

        }else{
            if (Config.isNetworkAvailable(this))
            {
                Map<String, String> params = new HashMap<>();
                params.put("name", nom);
                params.put("type", typeConst);
                params.put("user_id", id_admin);
                params.put("description", description);

                Handler mhandler = getHandler();
                RequestPostTask.sendRequest("addGroup", params, mhandler, this, this.getResources().getString(R.string.progress_dialog_message_ajout));
            }
            else
            {
                displayErrorInternet();
            }



        }
    }

    public void displayErrorInternet(){
        displayAlertError(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
    }

    private void displayAlertError(String message){
        alert.setMessage(message);
        AlertDialog alertInternet = alert.create();
        alertInternet.show();
    }

    public Handler getHandler() {
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {

                    //System.out.println(" MSG : "+(String) msg.obj);
                    String rp = (String) msg.obj;
                    JSONObject jsonObject = new JSONObject(rp);

                    if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED))
                    {
                        displayAlertError(getResources().getString(R.string.message_alert_dialog_nom_deja_utilise));
                    }
                    else if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                    {
                        displayAlertError(getResources().getString(R.string.message_alert_dialog_erreur_ajout_groupe_bd));
                    }
                    else
                    {
                        int id = jsonObject.getJSONObject(Config.JSON_DATA).getInt("id");

                        int indiceType = spinnerType.getSelectedItemPosition();
                        String nom = editTextNom.getText().toString();
                        String type = typeGroupes.get(indiceType);
                        String description = editTextDescription.getText().toString();
                        String typeConst = "";
                        if(type.equals(getResources().getString(R.string.type_public))){
                            typeConst = Groupe.TYPE_PUBLIC;
                        } else if(type.equals(getResources().getString(R.string.type_prive))){
                            typeConst = Groupe.TYPE_PRIVE;
                        }
                        SessionManager sessionManager = new SessionManager(AjoutGroupeActivity.this);
                        int id_admin = sessionManager.getUserId();

                        saveLocal(nom, typeConst, description, id_admin);

                        Toast.makeText(AjoutGroupeActivity.this, AjoutGroupeActivity.this.getResources().getString(R.string.groupe_ajoute), Toast.LENGTH_LONG).show();

                        //aller vers l'activité pour voir un groupe

                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);

                        Intent intent = new Intent(AjoutGroupeActivity.this, GroupeActivity.class);
                        intent.putExtra(Config.ID_GROUPE, id);
                        startActivity(intent);

                        AjoutGroupeActivity.this.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        return mHandler;
    }

    private void saveLocal(String nom, String typeConst, String description, int id_user){

        Groupe groupe = new Groupe();

        groupe.setNom(nom);
        groupe.setType(typeConst);
        groupe.setDescription(description);
/*
                    buildAlertContenuInvalide.setMessage(description);
                    AlertDialog alertContenuInvalide = buildAlertContenuInvalide.create();
                    alertContenuInvalide.show();
                    System.out.println("description : "+description);
         */
        groupe.setAdmin(new Utilisateur(id_user, "", "", null, null, null));

        GroupeBD groupeBD = new GroupeBD(AjoutGroupeActivity.this);
        groupeBD.open();
        int id = (int) groupeBD.add(groupe);
        groupeBD.close();

        if (id != -1)
        {
            groupe.setId(id);
        }
        /*else
        {
            buildAlertContenuInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_ajout_groupe_bd));
            AlertDialog alertContenuInvalide = buildAlertContenuInvalide.create();
            alertContenuInvalide.show();
        }*/
    }
}
