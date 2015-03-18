package com.example.maxi.swipetabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if (position==0){
            fragment = new Fragment_a();
        }
        if (position==1){
            fragment = new Fragment_b();
        }
        if (position==2){
            fragment = new Fragment_c();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
