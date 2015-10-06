package com.brena.ulsacommunity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by DanielBrena on 25/09/15.
 */
public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;


    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MapFragment tab1 = new MapFragment();

                return tab1;
            case 1:
                PhotosFragment tab2 = new PhotosFragment();
                return tab2;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}