package com.toandv98.checksum.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.toandv98.checksum.ui.FileFragment;
import com.toandv98.checksum.ui.TextFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    private static final int NUMBER_OF_ITEM = 2;

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? FileFragment.newInstance() : TextFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return NUMBER_OF_ITEM;
    }
}
