package com.ute.hakidictionary.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ute.hakidictionary.fragment.HistoryEVFragment;
import com.ute.hakidictionary.fragment.HistoryVEFragment;

public class HistoryTabAdapter extends FragmentStateAdapter {
//    String id;
    public HistoryTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HistoryEVFragment();
            case 1:
                return new HistoryVEFragment();
        }
        return new HistoryEVFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
