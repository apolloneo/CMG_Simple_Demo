package com.example.cmg_simple_demo;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cmg_simple_demo.fragments.GraphicsFragment;
import com.example.cmg_simple_demo.fragments.SettingsFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private static int NUM_FRAGS = 2;

    private final Context mContext;

    public PagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SettingsFragment setting_fragment;
                setting_fragment = new SettingsFragment();
                return setting_fragment;
            case 1:
                GraphicsFragment graphicsFragment = new GraphicsFragment();
                return graphicsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_FRAGS;
    }
}
