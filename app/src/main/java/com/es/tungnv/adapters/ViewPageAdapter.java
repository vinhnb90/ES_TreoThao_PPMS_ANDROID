package com.es.tungnv.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ViewPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPageAdapter(FragmentManager manager) {
        super(manager);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        notifyDataSetChanged();
    }

    public void removeTabLayoutPage(int position) {
        if (!mFragmentList.isEmpty() && position<mFragmentList.size()) {
            mFragmentList.remove(position);
            mFragmentTitleList.remove(position);
            this.notifyDataSetChanged();
        }
    }

    public void refreshViewPageApdater(List<Fragment> fragmentList, List<String> titleList)
    {
        if(fragmentList.size()!= 0)
        {
            mFragmentList.clear();
            mFragmentList.addAll(fragmentList);
            mFragmentTitleList.clear();
            mFragmentTitleList.addAll(titleList);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}