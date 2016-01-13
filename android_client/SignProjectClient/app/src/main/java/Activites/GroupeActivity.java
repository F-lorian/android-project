package activites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.florian.signprojectclient.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import adapters.AdapterListViewGroupe;
import modeles.modele.Groupe;
import modeles.modele.RequestPostTask;
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

    private Button supprimer;
    private Button modifier;
    private Button rejoindre;
    private Button quitter;
    private Button annuler_demande;

    private LinearLayout info;
    private LinearLayout layout_admin;
    private LinearLayout layout_membre;
    private LinearLayout layout_en_attente;
    private LinearLayout layout_demandes;
    private LinearLayout contenu_groupe;

    private ImageView image_type ;

    private Groupe groupe;

    private AlertDialog.Builder alert;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id_groupe = getIntent().getIntExtra(Config.ID_GROUPE, -1);

        if (id_groupe != -1) {
            setContentView(R.layout.activity_groupe);

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

            this.type = (TextView) findViewById(R.id.type_groupe);
            this.image_type = (ImageView) findViewById(R.id.image_type);

            this.layout_demandes = (LinearLayout) findViewById(R.id.layout_demandes);
            this.nb_demandes = (TextView) findViewById(R.id.nb_demandes);

            this.nb_membres = (TextView) findViewById(R.id.nb_membres);

            this.contenu_groupe = (LinearLayout) findViewById(R.id.contenu_groupe);

            this.layout_membre.setVisibility(View.GONE);
            this.layout_en_attente.setVisibility(View.GONE);
            this.rejoindre.setVisibility(View.GONE);
            this.layout_admin.setVisibility(View.GONE);
            this.layout_demandes.setVisibility(View.GONE);
            this.contenu_groupe.setVisibility(View.GONE);

            SessionManager sessionManager = new SessionManager(GroupeActivity.this);
            int id_user = sessionManager.getUserId();

            if (Config.isNetworkAvailable(GroupeActivity.this))
            {
                List<NameValuePair> pairsPost = new ArrayList<NameValuePair>();
                pairsPost.add(new BasicNameValuePair("group_id",Integer.toString(id_groupe)));
                pairsPost.add(new BasicNameValuePair("user_id",Integer.toString(id_user)));

                Handler mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        try {

                            String rp = (String) msg.obj;
                            //System.out.println(" MSG : "+rp);
                            JSONObject jsonObject = new JSONObject(rp);

                            if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_DENIED))
                            {
                                alert.setMessage(getResources().getString(R.string.message_alert_dialog_inscription_denied));
                                AlertDialog alertInscriptionInvalide = alert.create();
                                alertInscriptionInvalide.show();
                            }
                            else if (jsonObject.getString(Config.JSON_STATE).equals(Config.JSON_ERROR))
                            {
                                alert.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_ajout_groupe_bd));
                                AlertDialog alertInscriptionInvalide = alert.create();
                                alertInscriptionInvalide.show();
                            }
                            else {
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
                                Utilisateur admin = new Utilisateur(id_admin, "", "", null, null, null);
                                groupe = new Groupe();
                                groupe.setId(id);
                                groupe.setNom(nom);
                                groupe.setType(type);
                                groupe.setDescription(description);
                                groupe.setAdmin(admin);
                                groupe.setNbDemandes(nb_demandes);
                                groupe.setNbMembres(nb_membres);

                                displayGroupe(id_user, groupe, state);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                RequestPostTask requestPostTask = new RequestPostTask("getGroupWithRestrict",pairsPost, mHandler, GroupeActivity.this);
                requestPostTask.execute();
            }
            else
            {

                GroupeBD groupeBD = new GroupeBD(this);
                groupeBD.open();
                this.groupe = groupeBD.getGroupe(id_groupe);
                groupeBD.close();

                GroupeUtilisateurBD groupeUtilisateurBD = new GroupeUtilisateurBD(this);
                groupeUtilisateurBD.open();
                String s = groupeUtilisateurBD.isInGroup(id_user, id_groupe);
                groupeUtilisateurBD.close();

                displayGroupe(id_user, groupe, s);
            }

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

    public void displayGroupe(int id_user, Groupe groupe, String state){

        if (groupe.getAdmin().getId() == id_user) {
            //onclick
            this.layout_admin.setVisibility(View.VISIBLE);

            if(groupe.getNbDemandes() > 0){
                this.nb_demandes.setText(groupe.getNbDemandes()+" "+getResources().getString(R.string.demandes));
                this.layout_demandes.setVisibility(View.VISIBLE);
            }
            this.nb_demandes.setVisibility(View.VISIBLE);

            this.modifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Config.isNetworkAvailable(GroupeActivity.this)) {
                        edit();
                    } else {
                        alert.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
                        AlertDialog alertInscriptionInvalide = alert.create();
                        alertInscriptionInvalide.show();
                    }
                }
            });
            this.supprimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Config.isNetworkAvailable(GroupeActivity.this)) {
                        delete();
                    } else {
                        alert.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
                        AlertDialog alertInscriptionInvalide = alert.create();
                        alertInscriptionInvalide.show();
                    }
                }
            });

            this.layout_demandes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout_demandes.setBackground(getDrawable(R.drawable.drop_shadow_selected));
                    alert.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
                    AlertDialog alertInscriptionInvalide = alert.create();
                    alertInscriptionInvalide.show();

                }
            });

        } else {
            //System.out.println("groupe : "+groupe);
            //System.out.println("ETAT : " + state);

            if (state != null && state.equals(GroupeUtilisateurBD.ETAT_APPARTIENT)) {
                //onclick
                this.layout_membre.setVisibility(View.VISIBLE);

                this.quitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Config.isNetworkAvailable(GroupeActivity.this)) {
                            quit();
                        } else {
                            alert.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
                            AlertDialog alertInscriptionInvalide = alert.create();
                            alertInscriptionInvalide.show();
                        }
                    }
                });

            } else if (state != null && state.equals(GroupeUtilisateurBD.ETAT_ATTENTE)) {
                this.layout_en_attente.setVisibility(View.VISIBLE);

                this.annuler_demande.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Config.isNetworkAvailable(GroupeActivity.this)) {
                            cancelDemand();
                        } else {
                            alert.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
                            AlertDialog alertInscriptionInvalide = alert.create();
                            alertInscriptionInvalide.show();
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
                            alert.setMessage(getResources().getString(R.string.message_alert_dialog_erreur_pas_internet));
                            AlertDialog alertInscriptionInvalide = alert.create();
                            alertInscriptionInvalide.show();
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

        SessionManager sessionManager = new SessionManager(this);
        int id_user = sessionManager.getUserId();
        int id_groupe = getIntent().getIntExtra(Config.ID_GROUPE, -1);

        GroupeUtilisateurBD groupeUtilisateurBD = new GroupeUtilisateurBD(this);
        groupeUtilisateurBD.open();
        long id = groupeUtilisateurBD.add(id_user, id_groupe, GroupeUtilisateurBD.ETAT_ATTENTE);
        String s = groupeUtilisateurBD.isInGroup(id_user, id_groupe);
        System.out.println("ETAT : " + GroupeUtilisateurBD.ETAT_ATTENTE);
        System.out.println("ETAT APRES : " + s);
        System.out.println("id_user : " + id_user);
        System.out.println("id_groupe : " + id_groupe);
        System.out.println("id : " + id);
        groupeUtilisateurBD.close();

        if(id != -1){
            this.rejoindre.setVisibility(View.GONE);
            this.layout_en_attente.setVisibility(View.VISIBLE);
            this.annuler_demande.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelDemand();
                }
            });
        }

    }

    public void cancelDemand() {

        SessionManager sessionManager = new SessionManager(this);
        int id_user = sessionManager.getUserId();
        int id_groupe = getIntent().getIntExtra(Config.ID_GROUPE, -1);

        GroupeUtilisateurBD groupeUtilisateurBD = new GroupeUtilisateurBD(this);
        groupeUtilisateurBD.open();
        long nb_rows = groupeUtilisateurBD.delete(id_user, id_groupe);
        String s = groupeUtilisateurBD.isInGroup(id_user, id_groupe);
        System.out.println("ETAT APRES : " + s);
        System.out.println("id_user : " + id_user);
        System.out.println("id_groupe : " + id_groupe);
        System.out.println("nb_rows : " + nb_rows);
        groupeUtilisateurBD.close();

        if(nb_rows > 0){
            this.rejoindre.setVisibility(View.VISIBLE);
            this.layout_en_attente.setVisibility(View.GONE);
            this.rejoindre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendDemand();
                }
            });
        }


    }
}
