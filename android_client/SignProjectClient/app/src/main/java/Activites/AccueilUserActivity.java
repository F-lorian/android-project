package activites;

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

import fragments.FragmentListeSignalementsHoraires;
import fragments.FragmentListeSignalementsSimples;
import modeles.modeleBD.LigneArretBD;
import utilitaires.InitDataTask;
import utilitaires.JeuDeDonnees;
import utilitaires.PageAdapterSignalementAutresAccidents;
import utilitaires.SessionManager;

/**
 * Created by Axel_2 on 27/11/2015.
 */
public class AccueilUserActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private MenuItem oldMenuItem;

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

        this.InitilisationDesDonnees();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
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
        Fragment fragment = null;
        Bundle args = new Bundle();

        switch(menuItem.getItemId()) {
            case R.id.item_signalementsProches:
                fragment = new FragmentListeSignalementsSimples();
                break;
            case R.id.item_signalementsControleurs:
                args.putString(FragmentListeSignalementsSimples.TYPE_SIGNALEMENT_BUNDLE, getResources().getString(R.string.controleur_spinner));
                fragment = new FragmentListeSignalementsSimples();
                break;
            case R.id.item_signalementsHoraires:
                fragment = new FragmentListeSignalementsHoraires();
                break;
            case R.id.item_mesGroupes:
                fragment = new FragmentListeSignalementsSimples();
                break;
            case R.id.item_rejoindreGroupe:
                fragment = new FragmentListeSignalementsSimples();
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
            fragment.setArguments(args);
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }
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
        // A FAIRE : Signaler au serveur la déconnection !!!
        super.onDestroy();
    }

    public void InitilisationDesDonnees()
    {
        JeuDeDonnees j = new JeuDeDonnees(this);

        LigneArretBD la = new LigneArretBD(this);

        la.open();

        if (la.getCount() <= 0)
        {
            InitDataTask initDataTask = new InitDataTask(this,"tam.kml");
            initDataTask.execute();
        }

        la.close();
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
}
