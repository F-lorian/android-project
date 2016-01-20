package activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.florian.signprojectclient.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fragments.FragmentInvitationMembre;
import fragments.FragmentListeMembres;
import modeles.modele.Groupe;
import utilitaires.RequestPostTask;
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
    private TextView type ;
    private TextView nb_demandes ;
    private TextView nb_membres ;
    private TextView nb_invitations ;

    private Button supprimer;
    private Button modifier;
    private Button rejoindre;
    private Button quitter;
    private Button annuler_demande;
    private Button accepter;
    private Button refuser;

    private LinearLayout info;
    private LinearLayout layout_admin;
    private LinearLayout layout_membre;
    private LinearLayout layout_en_attente;
    private LinearLayout layout_invite;
    private LinearLayout layout_nb_membres;
    private LinearLayout layout_demandes;
    private LinearLayout layout_invitations;
    private LinearLayout contenu_groupe;
    private LinearLayout layout_inviter;

    private ImageView image_type ;

    private Groupe groupe;
    private boolean admin;

    private AlertDialog.Builder alert;

    private boolean modification;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id_groupe = getIntent().getIntExtra(Config.ID_GROUPE, -1);
        this.modification = getIntent().getBooleanExtra("modif", false);

        if (id_groupe != -1) {
            setContentView(R.layout.activity_groupe);


            this.groupe = new Groupe();
            this.groupe.setId(id_groupe);

            this.alert = new AlertDialog.Builder(this);
            this.alert.setTitle(getResources().getString(R.string.titre_alert_dialog_erreur));
            this.alert.setIcon(R.drawable.ic_action_error);
            this.alert.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

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

            this.layout_invite = (LinearLayout) findViewById(R.id.layout_invite);
            this.accepter = (Button) findViewById(R.id.action_accepter_button_group);
            this.refuser = (Button) findViewById(R.id.action_refuser_button_group);

            this.type = (TextView) findViewById(R.id.type_groupe);
            this.image_type = (ImageView) findViewById(R.id.image_type);

            this.layout_demandes = (LinearLayout) findViewById(R.id.layout_demandes);
            this.nb_demandes = (TextView) findViewById(R.id.nb_demandes);

            this.layout_invitations = (LinearLayout) findViewById(R.id.layout_invitations);
            this.nb_invitations = (TextView) findViewById(R.id.nb_invitations);

            this.layout_nb_membres = (LinearLayout) findViewById(R.id.layout_nb_membres);
            this.nb_membres = (TextView) findViewById(R.id.nb_membres);

            this.contenu_groupe = (LinearLayout) findViewById(R.id.contenu_groupe);

            this.layout_inviter = (LinearLayout) findViewById(R.id.layout_inviter);

            if (Config.isNetworkAvailable(GroupeActivity.this))
            {
                refresh();
            }
            else
            {

                this.layout_membre.setVisibility(View.GONE);
                this.layout_en_attente.setVisibility(View.GONE);
                this.layout_invite.setVisibility(View.GONE);
                this.rejoindre.setVisibility(View.GONE);
                this.layout_admin.setVisibility(View.GONE);
                this.layout_demandes.setVisibility(View.GONE);
                this.contenu_groupe.setVisibility(View.GONE);
                this.layout_inviter.setVisibility(View.GONE);

                SessionManager sessionManager = new SessionManager(GroupeActivity.this);
                int id_user = sessionManager.getUserId();

                //à virer
                GroupeBD groupeBD = new GroupeBD(this);
                groupeBD.open();
                this.groupe = groupeBD.getGroupe(id_groupe);
                groupeBD.close();

                this.admin = this.groupe.getAdmin().getId() == id_user;

                displayGroupe();
            }

        } else {
            displayFatalError();
        }
    }
    /*
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();

        if(getModification()){
            System.out.println("refresh");
            refresh();
        }

    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if(resultCode == RESULT_OK){
                refresh();
                this.setModification(true);
            }
            if (resultCode == RESULT_CANCELED) {
                //Do nothing
            }
        }
    }

    public void setModification(boolean modification){
        this.modification = true;
    }

    public boolean getModification(){
        return this.modification;
    }

    public void refresh(){

        this.layout_membre.setVisibility(View.GONE);
        this.layout_en_attente.setVisibility(View.GONE);
        this.layout_invite.setVisibility(View.GONE);
        this.rejoindre.setVisibility(View.GONE);
        this.layout_admin.setVisibility(View.GONE);
        this.layout_demandes.setVisibility(View.GONE);
        this.layout_invitations.setVisibility(View.GONE);
        this.layout_inviter.setVisibility(View.GONE);
        this.contenu_groupe.setVisibility(View.GONE);

        SessionManager sessionManager = new SessionManager(GroupeActivity.this);
        int id_user = sessionManager.getUserId();
        Map<String, String> params = new HashMap<>();
        params.put("group_id", Integer.toString(this.groupe.getId()));
        params.put("user_id", Integer.toString(id_user));

        Handler mHandler = getGroupeHandler();
        RequestPostTask.sendRequest("getGroupWithRestrict", params, mHandler, this, this.getResources().getString(R.string.progress_dialog_titre));

        setModification(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_groupe, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case R.id.toolbar_refresh:
                this.refresh();
                break;
            case android.R.id.home:
                System.out.println("getModification() : " + getModification());
                if(getModification()){
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                } else {
                    Intent returnIntent = new Intent();
                    setResult(RESULT_CANCELED, returnIntent);
                }

                this.finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void displayGroupe(){

        if (this.admin) {
            //onclick
            this.layout_admin.setVisibility(View.VISIBLE);

            this.layout_inviter.setVisibility(View.VISIBLE);

            if(this.groupe.getNbDemandes() > 0){
                this.nb_demandes.setText(groupe.getNbDemandes()+" "+getResources().getString(R.string.demandes));
                this.layout_demandes.setVisibility(View.VISIBLE);
            }
            if(this.groupe.getNbInvitations() > 0){
                this.nb_invitations.setText(groupe.getNbInvitations()+" "+getResources().getString(R.string.invitations));
                this.layout_invitations.setVisibility(View.VISIBLE);
            }

            this.modifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Config.isNetworkAvailable(GroupeActivity.this)) {
                        edit();
                    } else {
                        displayErrorInternet();
                    }
                }
            });
            this.supprimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Config.isNetworkAvailable(GroupeActivity.this)) {
                        delete();
                    } else {
                        displayErrorInternet();
                    }
                }
            });

            this.layout_demandes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Config.isNetworkAvailable(GroupeActivity.this)) {
                        displayMembersDialog(GroupeUtilisateurBD.ETAT_ATTENTE, admin);
                    } else {
                        displayErrorInternet();
                    }
                }
            });

            this.layout_invitations.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Config.isNetworkAvailable(GroupeActivity.this)) {
                        displayMembersDialog(GroupeUtilisateurBD.ETAT_INVITE, admin);
                    } else {
                        displayErrorInternet();
                    }
                }
            });

            this.layout_inviter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Config.isNetworkAvailable(GroupeActivity.this)) {
                        displayInviteDialog();
                    } else {
                        displayErrorInternet();
                    }
                }
            });

            this.layout_nb_membres.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Config.isNetworkAvailable(GroupeActivity.this)) {
                        displayMembersDialog(GroupeUtilisateurBD.ETAT_APPARTIENT, admin);
                    } else {
                        displayErrorInternet();
                    }
                }
            });

        } else {
            //System.out.println("groupe : "+groupe);
            //System.out.println("ETAT : " + state);

            if (this.groupe.getUserState() != null && this.groupe.getUserState().equals(GroupeUtilisateurBD.ETAT_APPARTIENT)) {
                //onclick
                this.layout_membre.setVisibility(View.VISIBLE);

                this.quitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Config.isNetworkAvailable(GroupeActivity.this)) {

                            if(groupe.getType().equals(Groupe.TYPE_PRIVE)) {
                                AlertDialog.Builder buildAlert = new AlertDialog.Builder(GroupeActivity.this);
                                buildAlert.setTitle(getResources().getString(R.string.action_toolbar_deconnnexion));
                                buildAlert.setIcon(R.drawable.ic_action_warning);
                                buildAlert.setMessage(getResources().getString(R.string.message_alert_dialog_quitter_groupe));
                                buildAlert.setPositiveButton(getResources().getString(R.string.btn_valider), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        quit();
                                    }
                                });
                                buildAlert.setNegativeButton(getResources().getString(R.string.btn_Annuler), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                                buildAlert.show();
                            } else {
                                quit();
                            }

                        } else {
                            displayErrorInternet();
                        }
                    }
                });

                this.layout_nb_membres.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Config.isNetworkAvailable(GroupeActivity.this)) {
                            displayMembersDialog(GroupeUtilisateurBD.ETAT_APPARTIENT, admin);
                        } else {
                            displayErrorInternet();
                        }
                    }
                });

            } else if (this.groupe.getUserState() != null && this.groupe.getUserState().equals(GroupeUtilisateurBD.ETAT_ATTENTE)) {
                this.layout_en_attente.setVisibility(View.VISIBLE);

                this.annuler_demande.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Config.isNetworkAvailable(GroupeActivity.this)) {
                            cancelDemand();
                        } else {
                            displayErrorInternet();
                        }
                    }
                });

            } else if (this.groupe.getUserState() != null && this.groupe.getUserState().equals(GroupeUtilisateurBD.ETAT_INVITE)) {
                this.layout_invite.setVisibility(View.VISIBLE);

                this.accepter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Config.isNetworkAvailable(GroupeActivity.this)) {
                            acceptInvite();
                        } else {
                            displayErrorInternet();
                        }
                    }
                });

                this.refuser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Config.isNetworkAvailable(GroupeActivity.this)) {
                            refuseInvite();
                        } else {
                            displayErrorInternet();
                        }
                    }
                });

            } else {
                this.rejoindre.setVisibility(View.VISIBLE);
                this.rejoindre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Config.isNetworkAvailable(GroupeActivity.this)) {
                            sendDemand();
                        } else {
                            displayErrorInternet();
                        }
                    }
                });
            }
        }

        this.nom.setText(groupe.getNom());
        this.description.setText(groupe.getDescription());
        this.nb_membres.setText(Integer.toString(groupe.getNbMembres()));

        String typeGroupe = groupe.getType();

        if(typeGroupe.equals(Groupe.TYPE_PUBLIC)){
            this.image_type.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_eye));
            this.type.setText(getResources().getString(R.string.type_public));
        } else if(typeGroupe.equals(Groupe.TYPE_PRIVE)){
            this.image_type.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_closed_eye));
            this.type.setText(getResources().getString(R.string.type_prive));
        }

        this.contenu_groupe.setVisibility(View.VISIBLE);
        //this.image_type.setColorFilter(0x0106000b, PorterDuff.Mode.MULTIPLY);
    }

    public Handler getGroupeHandler() {
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {

                    String rp = (String) msg.obj;
                    //System.out.println(" MSG : "+rp);
                    JSONObject jsonObject = new JSONObject(rp);

                    if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED))
                    {
                        displayFatalError();

                    }
                    else if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                    {
                        displayFatalError();
                    } else {
                        SessionManager sessionManager = new SessionManager(GroupeActivity.this);
                        int id_user = sessionManager.getUserId();

                        String nom = jsonObject.getString("name");
                        String type = jsonObject.getString("type");
                        String description = jsonObject.getString("description");
                        String state = jsonObject.getString("state");
                        int id =  jsonObject.getInt("id");
                        int id_admin = jsonObject.getInt("creator");
                        int nb_demandes = jsonObject.getInt("member_request");
                        int nb_membres = jsonObject.getInt("nb_member");
                        int nb_invitations = jsonObject.getInt("member_invite");
                        Utilisateur user_admin = new Utilisateur(id_admin, "", "", null, null, null);
                        groupe = new Groupe();
                        groupe.setId(id);
                        groupe.setNom(nom);
                        groupe.setType(type);
                        groupe.setDescription(description);
                        groupe.setAdmin(user_admin);
                        groupe.setNbDemandes(nb_demandes);
                        groupe.setNbMembres(nb_membres);
                        groupe.setNbInvitations(nb_invitations);
                        admin = id_admin == id_user;
                        groupe.setUserState(state);
                        displayGroupe();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        return mHandler;
    }

    public Handler getResponseHandler(final String message) {
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {

                    String rp = (String) msg.obj;
                    //System.out.println(" MSG : "+rp);
                    JSONObject jsonObject = new JSONObject(rp);

                    if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED))
                    {
                        displayAlertError(getResources().getString(R.string.action_refusee));

                    }
                    else if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                    {
                        displayAlertError(getResources().getString(R.string.erreur_serveur));

                    } else {
                        Toast.makeText(GroupeActivity.this, message, Toast.LENGTH_LONG).show();

                        setModification(true);



                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);

                        refresh();
                        setModification(true);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        return mHandler;
    }

    public Handler getQuitHandler() {
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {

                    String rp = (String) msg.obj;
                    //System.out.println(" MSG : "+rp);
                    JSONObject jsonObject = new JSONObject(rp);

                    if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED))
                    {
                        displayAlertError(getResources().getString(R.string.action_refusee));

                    }
                    else if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                    {
                        displayAlertError(getResources().getString(R.string.erreur_serveur));

                    } else {
                        Toast.makeText(GroupeActivity.this, getResources().getString(R.string.groupe_quitte), Toast.LENGTH_LONG).show();

                        setModification(true);



                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);

                        GroupeActivity.this.finish();



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        return mHandler;
    }

    public Handler getRefuseHandler() {
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {

                    String rp = (String) msg.obj;
                    //System.out.println(" MSG : "+rp);
                    JSONObject jsonObject = new JSONObject(rp);

                    if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED))
                    {
                        displayFatalError();

                    }
                    else if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                    {
                        displayAlertError(getResources().getString(R.string.erreur_serveur));

                    } else {

                        setModification(true);
                        Toast.makeText(GroupeActivity.this, getResources().getString(R.string.groupe_invitation_refusee), Toast.LENGTH_LONG).show();
                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        GroupeActivity.this.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        return mHandler;
    }

    public Handler getDeleteHandler() {
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                try {

                    String rp = (String) msg.obj;
                    //System.out.println(" MSG : "+rp);
                    JSONObject jsonObject = new JSONObject(rp);

                    if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED))
                    {
                        displayFatalError();

                    }
                    else if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                    {
                        displayAlertError(getResources().getString(R.string.erreur_serveur));

                    } else {
                        Toast.makeText(GroupeActivity.this, getResources().getString(R.string.groupe_supprime), Toast.LENGTH_LONG).show();
                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        GroupeActivity.this.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        return mHandler;
    }

    public void displayErrorInternet() {
        displayAlertError(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
    }

    public void displayFatalError(){
        alert.setNegativeButton(getResources().getString(R.string.btn_alert_dialog_erreur), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                GroupeActivity.this.finish();
            }
        });
        displayAlertError("groupe introuvable");
    }

    private void displayAlertError(String message){
        alert.setMessage(message);
        AlertDialog alertInternet = alert.create();
        alertInternet.show();
    }

    void displayMembersDialog(String state, boolean admin) {
        DialogFragment newFragment = FragmentListeMembres.newInstance(groupe.getId(), state, admin);
        newFragment.show(getFragmentManager(), "dialog");
    }

    void displayInviteDialog() {
        DialogFragment newFragment = FragmentInvitationMembre.newInstance(groupe.getId());
        newFragment.show(getFragmentManager(), "dialog");
    }

    public void edit() {

        int id_groupe = this.groupe.getId();

        Intent intent = new Intent(this, ModificationGroupeActivity.class);
        intent.putExtra(Config.ID_GROUPE, id_groupe);
        startActivityForResult(intent, 1);

    }
    public void delete() {

        SessionManager sessionManager = new SessionManager(this);
        int id_user = sessionManager.getUserId();
        int id_groupe = this.groupe.getId();


        Map<String, String> params = new HashMap<>();
        params.put("group_id", Integer.toString(id_groupe));

        Handler mHandler = getDeleteHandler();
        RequestPostTask.sendRequest("removeGroup", params, mHandler, this, this.getResources().getString(R.string.progress_dialog_message));

    }
    public void quit() {


        SessionManager sessionManager = new SessionManager(this);
        int id_user = sessionManager.getUserId();
        int id_groupe = this.groupe.getId();


        Map<String, String> params = new HashMap<>();
        params.put("group_id", Integer.toString(id_groupe));
        params.put("user_id", Integer.toString(id_user));

        Handler mHandler = getQuitHandler();
        RequestPostTask.sendRequest("removeMember", params, mHandler, this, this.getResources().getString(R.string.progress_dialog_message));

    }
    public void sendDemand() {

        SessionManager sessionManager = new SessionManager(this);
        int id_user = sessionManager.getUserId();
        int id_groupe = this.groupe.getId();

        /*
        if(id != -1){
            this.rejoindre.setVisibility(View.GONE);
            this.layout_en_attente.setVisibility(View.VISIBLE);
            this.annuler_demande.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelDemand();
                }
            });
        }*/

        Map<String, String> params = new HashMap<>();
        params.put("group_id", Integer.toString(id_groupe));
        params.put("user_id", Integer.toString(id_user));

        Handler mHandler = getResponseHandler(this.getResources().getString(R.string.demande_envoyee));
        RequestPostTask.sendRequest("requestMember", params, mHandler, this, this.getResources().getString(R.string.progress_dialog_message));

    }

    public void cancelDemand() {

        SessionManager sessionManager = new SessionManager(this);
        int id_user = sessionManager.getUserId();
        int id_groupe = this.groupe.getId();


        Map<String, String> params = new HashMap<>();
        params.put("group_id", Integer.toString(id_groupe));
        params.put("user_id", Integer.toString(id_user));

        Handler mHandler = getResponseHandler(this.getResources().getString(R.string.demande_annulee));
        RequestPostTask.sendRequest("removeMember", params, mHandler, this, this.getResources().getString(R.string.progress_dialog_message));

    }

    public void acceptInvite() {

        SessionManager sessionManager = new SessionManager(this);
        int id_user = sessionManager.getUserId();
        int id_groupe = this.groupe.getId();

        Map<String, String> params = new HashMap<>();
        params.put("group_id", Integer.toString(id_groupe));
        params.put("user_id", Integer.toString(id_user));

        Handler mHandler = getResponseHandler(this.getResources().getString(R.string.groupe_rejoint));
        RequestPostTask.sendRequest("acceptInvite", params, mHandler, this, this.getResources().getString(R.string.progress_dialog_message));

    }

    public void refuseInvite() {

        SessionManager sessionManager = new SessionManager(this);
        int id_user = sessionManager.getUserId();
        int id_groupe = this.groupe.getId();

        Map<String, String> params = new HashMap<>();
        params.put("group_id", Integer.toString(id_groupe));
        params.put("user_id", Integer.toString(id_user));

        Handler mHandler = getRefuseHandler();
        RequestPostTask.sendRequest("removeMember", params, mHandler, this, this.getResources().getString(R.string.progress_dialog_message));

    }
}
