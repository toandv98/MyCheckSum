package com.toandv98.checksum.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.toandv98.checksum.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_main)
    MaterialToolbar mToolbar;
    @BindView(R.id.vp_main)
    ViewPager2 mViewPager;
    @BindView(R.id.tbl_main)
    TabLayout mTabLayout;

    private static final String BOTTOM_SHEET_TAG = "about";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setupView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_about) {
            AboutBottomSheet.newInstance().show(getSupportFragmentManager(), BOTTOM_SHEET_TAG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(this);
        mViewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> {
            tab.setText(position == 0 ? R.string.tab_title_file : R.string.tab_title_text);
        }).attach();
    }
}
