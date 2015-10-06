package com.brena.ulsacommunity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by DanielBrena on 25/09/15.
 */
public class PageAdapterPerfil extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Bundle params;

    public PageAdapterPerfil(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PerfilInfoFragment tab1 = new PerfilInfoFragment();
                tab1.setArguments(params);
                return tab1;
            case 1:
                PhotosInfoFragment tab2 = new PhotosInfoFragment();
                tab2.setArguments(params);
                return tab2;
            default:
                return null;
        }
    }

    public Bundle getParams() {
        return params;
    }

    public void setParams(Bundle params) {
        this.params = params;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
