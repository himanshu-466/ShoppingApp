package com.example.shoppingapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.shoppingapp.Fragments.cancelFragment;
import com.example.shoppingapp.Fragments.confirmfragment;
import com.example.shoppingapp.Fragments.pendingfragment;
import com.google.firebase.database.annotations.NotNull;


public class ViewpagerAdapter extends FragmentPagerAdapter {
    private int tabno;

    public ViewpagerAdapter(@NonNull androidx.fragment.app.FragmentManager fm, int behavior, int tabno) {
        super(fm, behavior);
        this.tabno = tabno;

    }



    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new pendingfragment();
            case 1:
                return new confirmfragment();
            case 2:
                return new cancelFragment();

            default:
                return new pendingfragment();

        }

    }

    @Override
    public int getCount() {
        return tabno;
    }

}
