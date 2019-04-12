package com.kennedy.hiatus.speak_indictionary.controller;


import com.kennedy.hiatus.speak_indictionary.NoInternetFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewpagerAdapter extends FragmentPagerAdapter {

    private List<String> fragmentTitles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    public ViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return fragmentTitles.size();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments.isEmpty() )
            return new NoInternetFragment();
        else
            return fragments.get(position);
    }

    public void addFragment(Fragment fragments, String fragmentTitles){
        this.fragments.add(fragments);
        this.fragmentTitles.add(fragmentTitles);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);

    }

    public void addFragment(Fragment fragment){
        this.fragments.add(fragment);
    }

}
