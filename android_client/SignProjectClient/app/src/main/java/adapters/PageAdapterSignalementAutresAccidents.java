package adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.florian.signprojectclient.R;

import fragments.FragmentListeSignalementsSimples;
import utilitaires.Config;

/**
 * Created by Axel_2 on 25/12/2015.
 */
public class PageAdapterSignalementAutresAccidents extends FragmentStatePagerAdapter{
    private int nbPages;
    private Context c;
    private String tabTitles[];

    public PageAdapterSignalementAutresAccidents(FragmentManager fm, Context context)
    {
        super(fm);
        this.c=context;
        this.tabTitles = new String[] { Config.ACCIDENTS, Config.AUTRES};
        this.nbPages=this.tabTitles.length;
    }


    @Override
    public Fragment getItem(int position) {

        Bundle args = new Bundle();

        switch (position) {
            case 0:
                args.putString(Config.TYPE_SIGNALEMENT, this.tabTitles[position]);
                FragmentListeSignalementsSimples fragmentSignalementsAccidents = new FragmentListeSignalementsSimples();
                fragmentSignalementsAccidents.setArguments(args);
                return fragmentSignalementsAccidents;
            case 1:
                args.putString(Config.TYPE_SIGNALEMENT, this.tabTitles[position]);
                FragmentListeSignalementsSimples fragmentSignalementsAutres = new FragmentListeSignalementsSimples();
                fragmentSignalementsAutres.setArguments(args);
                return fragmentSignalementsAutres;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.nbPages;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.tabTitles[position].toUpperCase();
    }
}
