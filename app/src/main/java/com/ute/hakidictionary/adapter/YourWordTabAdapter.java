package com.ute.hakidictionary.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ute.hakidictionary.fragment.HistoryEVFragment;
import com.ute.hakidictionary.fragment.HistoryVEFragment;
import com.ute.hakidictionary.fragment.YourWordEVFragment;
import com.ute.hakidictionary.fragment.YourWordVEFragment;

public class YourWordTabAdapter extends FragmentStateAdapter {
    //    String id;
    public YourWordTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new YourWordEVFragment();
            case 1:
                return new YourWordVEFragment();
        }
        return new YourWordEVFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
