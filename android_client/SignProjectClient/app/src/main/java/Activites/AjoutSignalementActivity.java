package activites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.florian.signprojectclient.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.AdapterListViewHoraireSignalement;
import adapters.AdapterSpinnerTypeDestination;
import adapters.AdapterSpinnerTypeSignalement;
import modeles.modele.Arret;
import modeles.modele.Groupe;
import modeles.modele.Signalement;
import modeles.modele.SignalementGroupe;
import modeles.modele.SignalementPublic;
import modeles.modele.TypeSignalement;
import modeles.modele.Utilisateur;
import modeles.modeleBD.LigneArretBD;
import modeles.modeleBD.SignalementBD;
import modeles.modeleBD.TypeSignalementBD;
import utilitaires.Config;
import utilitaires.RequestPostTask;
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
    private ArrayList<String> arretsArray;
    private ArrayList<String> typeDestinataire;

    private ArrayList<Groupe> groupesDestination;
    private ArrayList<Utilisateur> utilisateursDestination;
    private ArrayList<String> horaires;

    private AdapterListViewHoraireSignalement adapterListViewHoraireSignalement;

    private static final int MIN_VALUE_NUMBER_PICKER = 0;
    private static final int MAX_VALUE_NUMBER_PICKER = 60;
    public static final int MAX_CAPACITY_LIST_HORAIRES = 5;

    private static final float OPACITY_BTN_ADD_HORAIRE = 64f/255f;
    private AlertDialog.Builder buildAlertInscriptionInvalide;

    private int idLigneArretCourant;
    private SessionManager sessionManager;

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

        this.spinnerTypeSignalement.setSelection(this.getIndiceByTypeSignalementValue(this.getIntent().getStringExtra(Config.TYPE_SIGNALEMENT)));

        LigneArretBD ligneArretBD = new LigneArretBD(this);
        ligneArretBD.open();
        this.arrets = ligneArretBD.getStringOfArretLigne();
        ligneArretBD.close();

        this.autoCompleteTextViewArret = (AutoCompleteTextView) findViewById(R.id.champArretSignalement);
        this.arretsArray = new ArrayList<String>();
        this.arretsArray.addAll(this.arrets.values());
        ArrayAdapter<String> adapterAutoCompleteTextViewArret = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, this.arretsArray);
        this.autoCompleteTextViewArret.setAdapter(adapterAutoCompleteTextViewArret);
        this.autoCompleteTextViewArret.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (AjoutSignalementActivity.this.horaires.size()>0)
                {
                    AjoutSignalementActivity.this.horaires.clear();
                    AjoutSignalementActivity.this.adapterListViewHoraireSignalement.notifyDataSetChanged();

                }
            }
        });

        this.typeDestinataire = new ArrayList<String>();
        this.typeDestinataire.add(this.getResources().getString(R.string.public_spinner));
        this.typeDestinataire.add(this.getResources().getString(R.string.groupe_spinner));
        this.typeDestinataire.add(this.getResources().getString(R.string.autres_personnes_spinner));

        this.spinnerTypeDestinataire = (Spinner) findViewById(R.id.spinnerDestinationAjoutSignalement);
        AdapterSpinnerTypeDestination adapterSpinnerTypeDestination = new AdapterSpinnerTypeDestination(this,this.typeDestinataire);
        this.spinnerTypeDestinataire.setAdapter(adapterSpinnerTypeDestination);

        LinearLayout layoutForm = (LinearLayout) findViewById(R.id.layoutFormAjoutSignalement);
        getLayoutInflater().inflate(R.layout.view_remarques_ajout_signalement, layoutForm);

        this.spinnerTypeSignalement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AjoutSignalementActivity.this.updateViewOnSelectSpinnerTypeItem(AjoutSignalementActivity.this.typeSignalements.get(position).getType());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.spinnerTypeDestinataire.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AjoutSignalementActivity.this.updateViewOnSelectSpinnerTypeDestinationItem(AjoutSignalementActivity.this.typeDestinataire.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.horaires = new ArrayList<String>();
        this.adapterListViewHoraireSignalement = new AdapterListViewHoraireSignalement(this,this.horaires);

        this.buildAlertInscriptionInvalide = new AlertDialog.Builder(this);
        this.buildAlertInscriptionInvalide.setTitle(getResources().getString(R.string.titre_alert_dialog_erreur));
        this.buildAlertInscriptionInvalide.setIcon(R.drawable.ic_action_error);
        this.buildAlertInscriptionInvalide.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        sessionManager = new SessionManager(this);

        this.initDataForm();
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
                int indiceTypeSignalement = this.spinnerTypeSignalement.getSelectedItemPosition();
                int indiceTypeDestination = this.spinnerTypeDestinataire.getSelectedItemPosition();
                AutoCompleteTextView autoCompleteTextViewArret = (AutoCompleteTextView) AjoutSignalementActivity.this.findViewById(R.id.champArretSignalement);
                this.idLigneArretCourant = AjoutSignalementActivity.this.getIdLigneArret(autoCompleteTextViewArret.getText().toString());

                Signalement signalement = null;
                if (this.typeDestinataire.get(indiceTypeDestination).equals(this.getResources().getString(R.string.groupe_spinner)))
                {
                    signalement = new SignalementGroupe();
                }
                else
                {
                    signalement = new SignalementPublic();
                }

                if (this.idLigneArretCourant != ListView.INVALID_POSITION)
                {
                    LigneArretBD ligneArretBD = new LigneArretBD(this);
                    ligneArretBD.open();
                    Arret arret = ligneArretBD.getArret(this.idLigneArretCourant);
                    ligneArretBD.close();

                    signalement.setArret(arret);

                    String contenu = autoCompleteTextViewArret.getText().toString() + "\n" + this.idLigneArretCourant;

                    if (this.typeSignalements.get(indiceTypeSignalement).getType().equals(this.getResources().getString(R.string.horaire_spinner)))
                    {
                        if (this.horaires.size()>0)
                        {
                            for (int i=0; i<this.horaires.size(); i++)
                            {
                                contenu = contenu + "\n" + this.horaires.get(i);
                            }
                        }
                        else
                        {
                            buildAlertInscriptionInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_horaire_signalement));
                            AlertDialog alertInscriptionInvalide = buildAlertInscriptionInvalide.create();
                            alertInscriptionInvalide.show();
                        }
                    }

                    signalement.setContenu(contenu);

                    if (this.typeDestinataire.get(indiceTypeDestination).equals(this.getResources().getString(R.string.groupe_spinner)))
                    {
                        ListView listViewGroupe = (ListView) findViewById(R.id.listGroupUserAjoutSignalement);
                        SparseBooleanArray indiceGroupeChecked = listViewGroupe.getCheckedItemPositions();
                        ArrayList<Groupe> groupesSelected = new ArrayList<Groupe>();

                        for (int i=0; i<listViewGroupe.getCount(); i++)
                        {
                            if (indiceGroupeChecked.get(i))
                            {
                                groupesSelected.add(this.groupesDestination.get(i));
                            }
                        }

                        if (groupesSelected.size() > 0)
                        {
                            ((SignalementGroupe)signalement).setGroupesDestinateurs(groupesSelected);
                        }
                        else
                        {
                            buildAlertInscriptionInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_groupe_signalement));
                            AlertDialog alertInscriptionInvalide = buildAlertInscriptionInvalide.create();
                            alertInscriptionInvalide.show();
                        }
                    }
                    else if (this.typeDestinataire.get(indiceTypeDestination).equals(this.getResources().getString(R.string.autres_personnes_spinner)))
                    {
                        UtilisateursDestinationSignalementCompletionView utilisateursDestinationSignalementCompletionView = (UtilisateursDestinationSignalementCompletionView) findViewById(R.id.tokenUtilisateur);

                        if (utilisateursDestinationSignalementCompletionView.getObjects().size()>0)
                        {
                            ((SignalementPublic)signalement).setUtilisateursDestinateurs((ArrayList<Utilisateur>) utilisateursDestinationSignalementCompletionView.getObjects());
                        }
                        else
                        {
                            buildAlertInscriptionInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_utilisateur_signalement));
                            AlertDialog alertInscriptionInvalide = buildAlertInscriptionInvalide.create();
                            alertInscriptionInvalide.show();
                        }
                    }
                    else
                    {
                        //CAS PUBLIC
                        ((SignalementPublic)signalement).setUtilisateursDestinateurs(null);
                    }

                    signalement.setRemarques(((EditText) findViewById(R.id.textAreaAjoutSignalement)).getText().toString());
                    signalement.setType(this.typeSignalements.get(indiceTypeSignalement));
                    signalement.setEmetteur(new Utilisateur(sessionManager.getUserId(), "", "", null, null, null));

                    System.out.println(signalement);

                    if (Config.isNetworkAvailable(this))
                    {
                        this.envoyerSignalement(signalement);
                    }
                    else
                    {
                        SignalementBD signalementBD = new SignalementBD(this);
                        signalementBD.open();
                        int id = (int) signalementBD.addSignalement(signalement,SignalementBD.TABLE_NAME_SIGNALEMENT_A_ENVOYER);
                        signalementBD.close();

                        if (id > 0)
                        {
                            Toast.makeText(this, this.getResources().getString(R.string.toast_signalement_envoye), Toast.LENGTH_LONG).show();
                            this.finish();
                        }
                        else
                        {
                            buildAlertInscriptionInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_ajout_signalement_bd));
                            AlertDialog alertInscriptionInvalide = buildAlertInscriptionInvalide.create();
                            alertInscriptionInvalide.show();
                        }
                    }
                }
                else
                {
                    buildAlertInscriptionInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_arret_signalement));
                    AlertDialog alertInscriptionInvalide = buildAlertInscriptionInvalide.create();
                    alertInscriptionInvalide.show();
                }
                break;
            case android.R.id.home:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateViewOnSelectSpinnerTypeItem(String type)
    {
        if (type.equals(Config.HORAIRES))
        {
            View horaire = findViewById(R.id.layoutPrincipaleHoraireAjoutSignalement);

            if (horaire == null)
            {
                View remarques = findViewById(R.id.layoutRemarquesAjoutSignalement);
                ViewGroup parent = (ViewGroup) remarques.getParent();
                int index = parent.indexOfChild(remarques);
                horaire  = getLayoutInflater().inflate(R.layout.view_horaire_ajout_signalement, parent, false);
                parent.addView(horaire, index);

                final ImageButton buttonAddHoraire = (ImageButton) findViewById(R.id.buttonAddHoraire);

                buttonAddHoraire.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {

                        AutoCompleteTextView autoCompleteTextViewArret = (AutoCompleteTextView) AjoutSignalementActivity.this.findViewById(R.id.champArretSignalement);
                        AjoutSignalementActivity.this.idLigneArretCourant = AjoutSignalementActivity.this.getIdLigneArret(autoCompleteTextViewArret.getText().toString());

                        if (AjoutSignalementActivity.this.idLigneArretCourant != ListView.INVALID_POSITION) {
                            final Dialog dialogAjoutHoraire = new Dialog(AjoutSignalementActivity.this);
                            dialogAjoutHoraire.setTitle(AjoutSignalementActivity.this.getResources().getString(R.string.dialog_titre_ajout_horaire_signalement));
                            dialogAjoutHoraire.setContentView(R.layout.dialog_horaire_ajout_signalement);

                            final NumberPicker numberPicker = (NumberPicker) dialogAjoutHoraire.findViewById(R.id.numberPickerDialogAjoutHoraireSignalement);
                            numberPicker.setMinValue(MIN_VALUE_NUMBER_PICKER);
                            numberPicker.setMaxValue(MAX_VALUE_NUMBER_PICKER);
                            numberPicker.setWrapSelectorWheel(false);

                            final Spinner spinnerLigne = (Spinner) dialogAjoutHoraire.findViewById(R.id.spinnerDialogAjoutHoraireSignalement);
                            ArrayList<String> directions = AjoutSignalementActivity.this.getDirectionOfArret(AjoutSignalementActivity.this.idLigneArretCourant);
                            ArrayAdapter<String> arrayAdapterDirections = new ArrayAdapter<String>(dialogAjoutHoraire.getContext(), android.R.layout.simple_list_item_1, directions);
                            spinnerLigne.setAdapter(arrayAdapterDirections);

                            Button buttonOk = (Button) dialogAjoutHoraire.findViewById(R.id.buttonOKDialogAjoutHoraireSignalement);
                            buttonOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ListView listViewHoraire = (ListView) AjoutSignalementActivity.this.findViewById(R.id.listHoraireAjoutSignalement);
                                    AjoutSignalementActivity.this.horaires.add(spinnerLigne.getSelectedItem() + " - " + numberPicker.getValue());
                                    AjoutSignalementActivity.this.adapterListViewHoraireSignalement.notifyDataSetChanged();
                                    listViewHoraire.setAdapter(AjoutSignalementActivity.this.adapterListViewHoraireSignalement);
                                    listViewHoraire.setOnTouchListener(new ListView.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            int action = event.getAction();
                                            switch (action) {
                                                case MotionEvent.ACTION_DOWN:
                                                    // Disallow ScrollView to intercept touch events.
                                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                                    break;

                                                case MotionEvent.ACTION_UP:
                                                    // Allow ScrollView to intercept touch events.
                                                    v.getParent().requestDisallowInterceptTouchEvent(false);
                                                    break;
                                            }

                                            // Handle ListView touch events.
                                            v.onTouchEvent(event);
                                            return true;
                                        }
                                    });

                                    if (AjoutSignalementActivity.this.horaires.size() >= AjoutSignalementActivity.MAX_CAPACITY_LIST_HORAIRES) {
                                        buttonAddHoraire.setEnabled(false);
                                        buttonAddHoraire.setAlpha(OPACITY_BTN_ADD_HORAIRE);
                                    }

                                    dialogAjoutHoraire.dismiss();
                                }
                            });

                            Button buttonAnnuler = (Button) dialogAjoutHoraire.findViewById(R.id.buttonAnnulerDialogAjoutHoraireSignalement);
                            buttonAnnuler.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialogAjoutHoraire.dismiss();
                                }
                            });

                            dialogAjoutHoraire.show();
                        }

                    }
                });
            }
        }
        else
        {
            View horaire = findViewById(R.id.layoutPrincipaleHoraireAjoutSignalement);

            if (horaire != null)
            {
                View remarques = findViewById(R.id.layoutRemarquesAjoutSignalement);
                ViewGroup parent = (ViewGroup) remarques.getParent();
                parent.removeView(horaire);
                this.horaires.clear();
                this.adapterListViewHoraireSignalement.notifyDataSetChanged();
            }
        }
    }

    public void updateViewOnSelectSpinnerTypeDestinationItem(String type)
    {
        if (type.equals(getResources().getString(R.string.autres_personnes_spinner)))
        {
            View v = findViewById(R.id.layoutRemarquesAjoutSignalement);

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
            View v = findViewById(R.id.layoutPrincipaleHoraireAjoutSignalement);
            if (v==null)
            {
                v = findViewById(R.id.layoutRemarquesAjoutSignalement);
            }

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
                listViewGroups.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listViewGroups.setAdapter(arrayAdapter);
                listViewGroups.setOnTouchListener(new ListView.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                    }
                });
            }
        }
        else
        {
            View v = findViewById(R.id.layoutRemarquesAjoutSignalement);
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

    public int getIdLigneArret(String editTextArret)
    {
        for (Map.Entry<Integer,String> entry : this.arrets.entrySet())
        {
            if (entry.getValue().equals(editTextArret))
            {
                return entry.getKey().intValue();
            }
        }

        return -1;
    }

    public ArrayList<String> getDirectionOfArret(int id)
    {
        LigneArretBD ligneArretBD = new LigneArretBD(this);
        ligneArretBD.open();
        Arret arret = ligneArretBD.getArret(id);
        ligneArretBD.close();

        ArrayList<String> arrayListDirection = new ArrayList<String>();

        for (int i = 0; i<this.arretsArray.size(); i++)
        {
            String[] splitArret = this.arretsArray.get(i).split(" - ");
            if (splitArret[0].equals(arret.getNom()) && arret.getDirection().contains(splitArret[splitArret.length-1]))
            {
                arrayListDirection.add(splitArret[splitArret.length-2] + " " + splitArret[splitArret.length-1]);
            }
        }

        return arrayListDirection;
    }

    public int getIndiceByTypeSignalementValue(String typeSignalementValue)
    {
        int ind = 0;

        for (int i = 0; i<this.typeSignalements.size(); i++)
        {
            if (this.typeSignalements.get(i).getType().equals(typeSignalementValue))
            {
                return i;
            }
        }

        return ind;
    }

    private void initDataForm() {

        List<NameValuePair> pairsPost = new ArrayList<NameValuePair>();
        pairsPost.add(new BasicNameValuePair("user_id", sessionManager.getUserId() + ""));

        android.os.Handler mHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {
                    JSONObject jsonObject = new JSONObject((String) msg.obj);

                    AjoutSignalementActivity ajoutSignalementActivity = AjoutSignalementActivity.this;

                    if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED) || jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR)) {
                        buildAlertInscriptionInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_chargement));
                        AlertDialog alertInscriptionInvalide = buildAlertInscriptionInvalide.create();
                        alertInscriptionInvalide.show();
                    } else {
                        ajoutSignalementActivity.getGroupsFromJson(jsonObject);
                        ajoutSignalementActivity.getUtilisateursFromJson(jsonObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        RequestPostTask requestPostTask = new RequestPostTask("getDataFormAddSignalement", pairsPost, mHandler, this, this.getResources().getString(R.string.progress_dialog_message));
        requestPostTask.execute();
    }

    private void getGroupsFromJson(JSONObject jsonObject) throws JSONException {
        this.groupesDestination = new ArrayList<>();

        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("groupes");
        for (int i=0; i<jsonArray.length(); i++)
        {
            Groupe g = new Groupe();
            g.setId(jsonArray.getJSONObject(i).getInt("id"));
            g.setNom(jsonArray.getJSONObject(i).getString("name"));
            this.groupesDestination.add(g);
        }

    }

    private void getUtilisateursFromJson(JSONObject jsonObject) throws JSONException {
        this.utilisateursDestination = new ArrayList<>();

        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("users");
        for (int i=0; i<jsonArray.length(); i++)
        {
            Utilisateur u = new Utilisateur();
            u.setId(jsonArray.getJSONObject(i).getInt("id"));
            u.setPseudo(jsonArray.getJSONObject(i).getString("pseudo"));
            this.utilisateursDestination.add(u);
        }
    }

    private List<NameValuePair> convertSignalementToSend(Signalement signalement) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        List<NameValuePair> pairsPost = new ArrayList<NameValuePair>();
        pairsPost.add(new BasicNameValuePair("contenu", signalement.getContenu()));
        pairsPost.add(new BasicNameValuePair("remarque", signalement.getRemarques()));
        pairsPost.add(new BasicNameValuePair("date", dateFormat.format(signalement.getDate())));
        pairsPost.add(new BasicNameValuePair("arret", signalement.getArret().getId() + ""));
        pairsPost.add(new BasicNameValuePair("type", signalement.getType().getId() + ""));
        pairsPost.add(new BasicNameValuePair("emetteur", signalement.getEmetteur().getId() + ""));

        if (signalement instanceof SignalementPublic)
        {
            pairsPost.add(new BasicNameValuePair("diffusion", SignalementPublic.TYPE_DESTINATAIRE));
            if (((SignalementPublic)signalement).getUtilisateursDestinateurs() != null)
            {
                JSONArray jsonArray = new JSONArray();

                boolean auteurPresent = false;
                for (int i=0; i<((SignalementPublic)signalement).getUtilisateursDestinateurs().size(); i++)
                {
                    if (((SignalementPublic)signalement).getUtilisateursDestinateurs().get(i).getId() == sessionManager.getUserId())
                    {
                        auteurPresent = true;
                    }
                    jsonArray.put(((SignalementPublic)signalement).getUtilisateursDestinateurs().get(i).getId());
                }

                if (!auteurPresent)
                {
                    jsonArray.put(sessionManager.getUserId());
                }

                pairsPost.add(new BasicNameValuePair("destinataires", jsonArray.toString()));
            }
        }
        else
        {
            pairsPost.add(new BasicNameValuePair("diffusion", SignalementGroupe.TYPE_DESTINATAIRE));

            JSONArray jsonArray = new JSONArray();

            for (int i=0; i<((SignalementGroupe)signalement).getGroupesDestinateurs().size(); i++)
            {
                jsonArray.put(((SignalementGroupe)signalement).getGroupesDestinateurs().get(i).getId());
            }

            pairsPost.add(new BasicNameValuePair("destinataires", jsonArray.toString()));
        }
        return pairsPost;
    }

    private void envoyerSignalement(Signalement signalement)
    {
        List<NameValuePair> pairsPost = this.convertSignalementToSend(signalement);

        android.os.Handler mHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {
                    JSONObject jsonObject = new JSONObject((String) msg.obj);

                    AjoutSignalementActivity ajoutSignalementActivity = AjoutSignalementActivity.this;

                    if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED) || jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                    {
                        buildAlertInscriptionInvalide.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_ajout_signalement_bd));
                        AlertDialog alertInscriptionInvalide = buildAlertInscriptionInvalide.create();
                        alertInscriptionInvalide.show();
                    }
                    else
                    {
                        Toast.makeText(ajoutSignalementActivity, ajoutSignalementActivity.getResources().getString(R.string.toast_signalement_envoye), Toast.LENGTH_LONG).show();
                        ajoutSignalementActivity.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        RequestPostTask requestPostTask = new RequestPostTask("addSignalement", pairsPost, mHandler, this, this.getResources().getString(R.string.progress_dialog_message_envoi_signalement));
        requestPostTask.execute();
    }
}
