package activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.florian.signprojectclient.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fragments.FragmentListeGroupes;
import fragments.FragmentListeGroupesRecherche;
import fragments.FragmentListeSignalementsHoraires;
import fragments.FragmentListeSignalementsProches;
import fragments.FragmentListeSignalementsSimples;
import modeles.modeleBD.LigneArretBD;
import utilitaires.Config;
import utilitaires.InitDataTask;
import utilitaires.JeuDeDonnees;
import adapters.PageAdapterSignalementAutresAccidents;
import utilitaires.PostRequest;
import utilitaires.RequestPostTask;
import utilitaires.SessionManager;

/**
 * Created by Axel_2 on 27/11/2015.
 */
public class AccueilUserActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    public NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private MenuItem oldMenuItem;
    private AlertDialog.Builder buildAlert;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_user);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        SessionManager sessionManager = new SessionManager(this);

        View headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_header);
        TextView headerNav = (TextView) headerLayout.findViewById(R.id.header_nav);

        headerNav.setText(headerNav.getText().toString() + " " + sessionManager.getUserPseudo());

        this.oldMenuItem = null;

        this.buildAlert = new AlertDialog.Builder(this);
        this.buildAlert.setTitle(getResources().getString(R.string.action_toolbar_deconnnexion));
        this.buildAlert.setIcon(R.drawable.ic_action_warning);
        this.buildAlert.setMessage(getResources().getString(R.string.message_alert_dialog_deconnexion));
        this.buildAlert.setPositiveButton(getResources().getString(R.string.btn_valider), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AccueilUserActivity.this.deconnection();
            }
        });
        this.buildAlert.setNegativeButton(getResources().getString(R.string.btn_Annuler), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        //MenuItem mi = nvDrawer.getMenu().getItem(0).getSubMenu().getItem(0);
        //this.selectDrawerItem(mi);
        this.InitilisationDesDonnees();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("requestCode : " + requestCode);
        if (requestCode == 1) {
            System.out.println("resultCode : " + resultCode);
            if(resultCode == Activity.RESULT_OK){

                System.out.println("RESULT OK");
                this.currentFragment.onActivityResult(requestCode,resultCode,data);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Do nothing
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                AlertDialog alertDeconnection = this.buildAlert.create();
                alertDeconnection.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
	}

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_accueil_user, menu);
        return true;
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {

        if (this.oldMenuItem != null && this.oldMenuItem.getItemId() != menuItem.getItemId())
        {
            this.changeViewOnSelectMenuItem(menuItem);
            this.oldMenuItem.setChecked(false);
        }
        else if (this.oldMenuItem == null)
        {
            this.changeViewOnSelectMenuItem(menuItem);
        }

        this.oldMenuItem = menuItem;
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private void changeViewOnSelectMenuItem(MenuItem menuItem)
    {
        this.currentFragment = null;
        Bundle args = new Bundle();

        switch(menuItem.getItemId()) {
            case R.id.item_signalementsProches:
                this.currentFragment = new FragmentListeSignalementsProches();
                break;
            case R.id.item_signalementsControleurs:
                args.putString(Config.TYPE_SIGNALEMENT, Config.CONTROLEUR);
                this.currentFragment = new FragmentListeSignalementsSimples();
                break;
            case R.id.item_signalementsHoraires:
                this.currentFragment = new FragmentListeSignalementsHoraires();
                break;
            case R.id.item_mesGroupes:
                this.currentFragment = new FragmentListeGroupes();
                break;
            case R.id.item_rejoindreGroupe:
                this.currentFragment = new FragmentListeGroupesRecherche();
                break;
            default:
                break;
        }

        if (menuItem.getItemId() == R.id.item_signalementsAccidentsAutres)
        {
            this.updateViewToolbar(this.getResources().getString(R.string.menu_item_listSignalementsAccidentsAutres));
            ViewPager viewPager = (ViewPager) findViewById(R.id.pager_fragment_listview);
            viewPager.setAdapter(new PageAdapterSignalementAutresAccidents(getSupportFragmentManager(), this));

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_signalements_autres_accidents);
            tabLayout.setupWithViewPager(viewPager);
        }
        else
        {
            this.updateViewToolbar(this.getResources().getString(R.string.menu_item_listSignalementsControleurs));
            this.currentFragment.setArguments(args);
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, this.currentFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDeconnection = this.buildAlert.create();
        alertDeconnection.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void InitilisationDesDonnees()
    {
        InitDataTask initDataTask = new InitDataTask(this);
        initDataTask.execute();
    }


    public void updateViewToolbar(String menuChoice)
    {
        if (menuChoice.equals(this.getResources().getString(R.string.menu_item_listSignalementsAccidentsAutres)))
        {
            View v = findViewById(R.id.flContent);
            ViewGroup parent = (ViewGroup) v.getParent();
            View tab_layout = getLayoutInflater().inflate(R.layout.view_tab_layout_signalements_autres_accidents,null);

            parent.removeView(v);
            parent.addView(tab_layout);
        }
        else
        {
            View v = findViewById(R.id.layout_tab_layout_signalements_autres_accidents);

            if (v != null)
            {
                ViewGroup parent = (ViewGroup) v.getParent();

                FrameLayout fl = new FrameLayout(this);
                fl.setId(R.id.flContent);
                fl.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                parent.removeView(v);
                parent.addView(fl);
            }


        }
    }

    private void deconnection()
    {
        SessionManager sessionManager = new SessionManager(this);

        Map<String, String> params = new HashMap<>();
        params.put("pseudo", sessionManager.getUserPseudo());
        params.put("id", Integer.toString(sessionManager.getUserId()));

        sessionManager.logoutUser();

        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                AccueilUserActivity.this.finish();
            }
        };
        RequestPostTask.sendRequest("deconnection", params, mHandler, this, this.getResources().getString(R.string.progress_dialog_message_deconnection));

    }
}
