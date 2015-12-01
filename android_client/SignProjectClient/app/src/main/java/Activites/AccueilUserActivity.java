package activites;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.florian.signprojectclient.R;

import java.util.ArrayList;
import java.util.List;

import fragments.FragmentListeSignalements;
import adapters.CustomDrawerNavigationAdapter;
import utilitaires.ItemNavigationDrawer;

/**
 * Created by Axel_2 on 27/11/2015.
 */
public class AccueilUserActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerNavigationAdapter adapter;

    List<ItemNavigationDrawer> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_user);

        //Initialisation
        dataList = new ArrayList<ItemNavigationDrawer>();
        mTitle = this.getTitle();
        mDrawerTitle = mTitle;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        dataList.add(new ItemNavigationDrawer(getResources().getString(R.string.menu_titre_signalement))); // adding a header to the list
        dataList.add(new ItemNavigationDrawer(getResources().getString(R.string.menu_item_carte), R.drawable.ic_action_place));
        dataList.add(new ItemNavigationDrawer(getResources().getString(R.string.menu_item_listAlerte), R.drawable.ic_action_warning));

        dataList.add(new ItemNavigationDrawer(getResources().getString(R.string.menu_titre_groupe))); // adding a header to the list
        dataList.add(new ItemNavigationDrawer(getResources().getString(R.string.menu_item_mesGroupes), R.drawable.ic_action_group));
        dataList.add(new ItemNavigationDrawer(getResources().getString(R.string.menu_item_rejoindreGroupe), R.drawable.ic_action_add_group));

        adapter = new CustomDrawerNavigationAdapter(this, R.layout.custom_item_navigation_drawer, dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            if (dataList.get(0).getTitle() != null) {
                SelectItem(1);
            } else {
                SelectItem(0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_project, menu);
        return true;
    }

    public void SelectItem(int position) {

        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (position) {
            case 1:
                fragment = new FragmentListeSignalements();
//                args.putString(FragmentTwo.ITEM_NAME, dataList.get(position)
//                        .getItemName());
//                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(position)
//                        .getImgResID());
                break;
            case 2:
                fragment = new FragmentListeSignalements();
//                args.putString(FragmentThree.ITEM_NAME, dataList.get(position)
//                        .getItemName());
//                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(position)
//                        .getImgResID());
                break;
            case 4:
                fragment = new FragmentListeSignalements();
//                args.putString(FragmentTwo.ITEM_NAME, dataList.get(position)
//                        .getItemName());
//                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(position)
//                        .getImgResID());
                break;
            case 5:
                fragment = new FragmentListeSignalements();
//                args.putString(FragmentThree.ITEM_NAME, dataList.get(position)
//                        .getItemName());
//                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(position)
//                        .getImgResID());
                break;
            default:
                break;
        }

        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        FragmentTransaction ft = frgManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(dataList.get(position).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            SelectItem(position);

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
        super.onDestroy();
    }
}
