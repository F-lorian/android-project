package activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.florian.signprojectclient.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapters.AdapterSpinnerTypeGroupe;
import modeles.modele.Groupe;
import utilitaires.RequestPostTask;
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
    private LinearLayout layout;

    private Groupe groupe;

    private ArrayList<String> typeGroupes;

    private AlertDialog.Builder buildAlertContenuInvalide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_groupe);
        this.layout = (LinearLayout) findViewById(R.id.layoutFormAjoutSignalement);
        this.layout.setVisibility(View.GONE);

        int id_groupe = getIntent().getIntExtra(Config.ID_GROUPE, -1);
        if (id_groupe != -1) {
            /*
            GroupeBD groupeBD = new GroupeBD(this);
            groupeBD.open();
            this.groupe = groupeBD.getGroupe(id_groupe);
            groupeBD.close();
               */
            this.buildAlertContenuInvalide = new AlertDialog.Builder(this);
            this.buildAlertContenuInvalide.setTitle(getResources().getString(R.string.titre_alert_dialog_erreur));
            this.buildAlertContenuInvalide.setIcon(R.drawable.ic_action_error);
            this.buildAlertContenuInvalide.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            // Set a Toolbar to replace the ActionBar.
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setTitle(getResources().getString(R.string.titre_toolbar_modifier_groupe));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            Handler mHandler = getGroupeHandler();
            Map<String, String> params = new HashMap<>();
            params.put("group_id", Integer.toString(id_groupe));
            RequestPostTask.sendRequest("getGroup", params, mHandler, this, this.getResources().getString(R.string.progress_dialog_message));


        }else{
            this.buildAlertContenuInvalide.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    ModificationGroupeActivity.this.finish();
                }
            });
            this.buildAlertContenuInvalide.setMessage("groupe introuvable");
            AlertDialog alertInternet = buildAlertContenuInvalide.create();
            alertInternet.show();
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

    public void init() {

        this.layout.setVisibility(View.VISIBLE);

        this.editTextNom = (EditText) findViewById(R.id.champNomGroupe);
        this.editTextDescription = (EditText) findViewById(R.id.champDescriptionGroupe);
        this.spinnerType = (Spinner) findViewById(R.id.spinnerTypeGroupe);

        this.typeGroupes = new ArrayList<String>();
        this.typeGroupes.add(getResources().getString(R.string.type_public));
        this.typeGroupes.add(getResources().getString(R.string.type_prive));


        AdapterSpinnerTypeGroupe adapterSpinnerTypeGroupe = new AdapterSpinnerTypeGroupe(this, this.typeGroupes);
        this.spinnerType.setAdapter(adapterSpinnerTypeGroupe);

        this.spinnerType.setSelection(getIndiceByType(this.groupe.getType()));
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
/*
        this.buildAlertContenuInvalide.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
*/
    }

    public void valid() {
        int indiceType = spinnerType.getSelectedItemPosition();
        String nom = ModificationGroupeActivity.this.editTextNom.getText().toString();
        String type = ModificationGroupeActivity.this.typeGroupes.get(indiceType);
        String description = ModificationGroupeActivity.this.editTextDescription.getText().toString();
        String typeConst = "";
        if (type.equals(getResources().getString(R.string.type_public))) {
            typeConst = Groupe.TYPE_PUBLIC;
        } else if (type.equals(getResources().getString(R.string.type_prive))) {
            typeConst = Groupe.TYPE_PRIVE;
        }

        if (!Groupe.nomValide(nom)) {
            /*buildAlertContenuInvalide.setMessage(getResources().getString(R.string.erreur_nom_groupe));
            AlertDialog alertInscriptionInvalide = buildAlertContenuInvalide.create();
            alertInscriptionInvalide.show();*/

        } else if (!Groupe.descriptionValide(description)) {
            /*buildAlertContenuInvalide.setMessage(getResources().getString(R.string.erreur_description_groupe));
            AlertDialog alertInscriptionInvalide = buildAlertContenuInvalide.create();
            alertInscriptionInvalide.show();*/

        } else {

            if (Config.isNetworkAvailable(this)) {

                Map<String, String> params = new HashMap<>();
                params.put("name", nom);
                params.put("type", typeConst);
                params.put("description", description);
                params.put("group_id", Integer.toString(this.groupe.getId()));
    /*
                        buildAlertContenuInvalide.setMessage(description);
                        AlertDialog alertContenuInvalide = buildAlertContenuInvalide.create();
                        alertContenuInvalide.show();
                        System.out.println("description : "+description);
             */
                Handler mHandler = getUpdateHandler();
                RequestPostTask.sendRequest("editGroup", params, mHandler, this, this.getResources().getString(R.string.progress_dialog_message_modif));

            } else {
                this.buildAlertContenuInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
                AlertDialog alertInternet = this.buildAlertContenuInvalide.create();
                alertInternet.show();
            }

        }
    }

    public int getIndiceByType(String typeCons) {
        int ind = 0;
        String typeGroupe = "";
        if (typeCons.equals(Groupe.TYPE_PUBLIC)) {
            typeGroupe = getResources().getString(R.string.type_public);
        } else if (typeCons.equals(Groupe.TYPE_PRIVE)) {
            typeGroupe = getResources().getString(R.string.type_prive);
        }

        for (int i = 0; i < this.typeGroupes.size(); i++) {
            if (this.typeGroupes.get(i).equals(typeGroupe)) {
                return i;
            }
        }

        return ind;
    }

    public Handler getUpdateHandler() {
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {

                    System.out.println(" MSG : "+(String) msg.obj);
                    String rp = (String) msg.obj;
                    JSONObject jsonObject = new JSONObject(rp);

                    if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED)) {
                        buildAlertContenuInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_inscription_denied));
                        AlertDialog alertInscriptionInvalide = buildAlertContenuInvalide.create();
                        alertInscriptionInvalide.show();
                    } else if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR)) {
                        buildAlertContenuInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_ajout_groupe_bd));
                        AlertDialog alertInscriptionInvalide = buildAlertContenuInvalide.create();
                        alertInscriptionInvalide.show();
                    } else {
                        int indiceType = spinnerType.getSelectedItemPosition();
                        String nom = editTextNom.getText().toString();
                        String type = typeGroupes.get(indiceType);
                        String description = editTextDescription.getText().toString();
                        String typeConst = "";
                        if (type.equals(getResources().getString(R.string.type_public))) {
                            typeConst = Groupe.TYPE_PUBLIC;
                        } else if (type.equals(getResources().getString(R.string.type_prive))) {
                            typeConst = Groupe.TYPE_PRIVE;
                        }
                        SessionManager sessionManager = new SessionManager(ModificationGroupeActivity.this);
                        int id_admin = sessionManager.getUserId();

                        saveLocal(nom, typeConst, description, id_admin);

                        Toast.makeText(ModificationGroupeActivity.this, ModificationGroupeActivity.this.getResources().getString(R.string.groupe_ajoute), Toast.LENGTH_LONG).show();
                        ModificationGroupeActivity.this.finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        return mHandler;
    }


    public Handler getGroupeHandler() {
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {

                    //System.out.println(" MSG : "+(String) msg.obj);
                    String rp = (String) msg.obj;
                    JSONObject jsonObject = new JSONObject(rp);

                    if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED)) {
                        buildAlertContenuInvalide.setMessage("groupe introuvable");
                        buildAlertContenuInvalide.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                ModificationGroupeActivity.this.finish();
                            }
                        });
                        AlertDialog alertInscriptionInvalide = buildAlertContenuInvalide.create();
                        alertInscriptionInvalide.show();
                    }else if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR)) {
                        buildAlertContenuInvalide.setMessage("^paramètre manquant");
                        buildAlertContenuInvalide.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                ModificationGroupeActivity.this.finish();
                            }
                        });
                        AlertDialog alertInscriptionInvalide = buildAlertContenuInvalide.create();
                        alertInscriptionInvalide.show();
                    } else {

                        JSONObject data = jsonObject.getJSONObject("data");
                        String nom = data.getString("name");
                        String type = data.getString("type");
                        String description = data.getString("description");
                        int id =  data.getInt("id");
                        int id_admin = data.getInt("creator");
                        Utilisateur admin = new Utilisateur(id_admin, "", "", null, null, null);
                        groupe = new Groupe();
                        groupe.setId(id);
                        groupe.setNom(nom);
                        groupe.setType(type);
                        groupe.setDescription(description);
                        groupe.setAdmin(admin);
                        System.out.println(groupe);
                        init();

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
        SessionManager sessionManager = new SessionManager(ModificationGroupeActivity.this);
        groupe.setAdmin(new Utilisateur(id_user, "", "", null, null, null));

        System.out.println("XXXXXXXXXXXXXXXXXX : " + sessionManager.getUserId());
        System.out.println(groupe);

        GroupeBD groupeBD = new GroupeBD(ModificationGroupeActivity.this);
        groupeBD.open();
        int nbmodifs = groupeBD.update(groupe);
        groupeBD.close();

        if (nbmodifs != -1)
        {

        }
        /*else
        {
            buildAlertContenuInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_ajout_groupe_bd));
            AlertDialog alertContenuInvalide = buildAlertContenuInvalide.create();
            alertContenuInvalide.show();
        }*/
    }
}
