package com.kuan.kuangeapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.kuan.base.autoservice.KuanGeServiceLoader;
import com.kuan.common.autoservice.INewsService;
import com.kuan.kuangeapp.databinding.ActivityMainBinding;
import com.kuan.kuangeapp.fragment.OtherFragment;

import java.lang.reflect.Field;

import q.rorbin.badgeview.QBadgeView;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding mBinding;
    private Fragment mCurFragment;
    private Fragment mHomeFragment;
    private Fragment mCategoriesFragment;
    private Fragment mServicesFragment;
    private Fragment mAccountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initFragment();
        mCurFragment = mHomeFragment;

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.menu_home));

        /**
         * Disable shift method require for to prevent shifting icon.
         * When you select any icon then remain all icon shift
         * ??????icon??????
         * @param view
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            disableShiftMode(mBinding.bottomView);
        }

        mBinding.bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragCategory = null;
                // init corresponding fragment
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        fragCategory = mHomeFragment;
                        break;
                    case R.id.menu_categories:
                        fragCategory = mCategoriesFragment;
                        break;
                    case R.id.menu_services:
                        fragCategory = mServicesFragment;
                        break;
                    case R.id.menu_account:
                        fragCategory = mAccountFragment;
                        break;
                }
                //Set bottom menu selected item text in toolbar
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(item.getTitle());
                }
                switchFragment(mCurFragment, fragCategory);
                mCurFragment = fragCategory;
                return true;
            }
        });
        mBinding.bottomView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, mHomeFragment, mHomeFragment.getClass().getSimpleName());
        transaction.commit();
        showBadgeView(3, 5);
    }

    private void initFragment(){
        mHomeFragment = KuanGeServiceLoader.load(INewsService.class).getHomeFragment();
        mCategoriesFragment = OtherFragment.newInstance(getString(R.string.menu_categories));
        mServicesFragment = OtherFragment.newInstance(getString(R.string.menu_services));
        mAccountFragment = OtherFragment.newInstance(getString(R.string.menu_account));
    }

    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            FragmentManager manger = getSupportFragmentManager();
            FragmentTransaction transaction = manger.beginTransaction();
            if (!to.isAdded()) {
                if (from != null) {
                    transaction.hide(from);
                }
                if (to != null) {
                    transaction.add(R.id.container, to, to.getClass().getName()).commit();
                }

            } else {
                if (from != null) {
                    transaction.hide(from);
                }
                if (to != null) {
                    transaction.show(to).commit();
                }

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("RestrictedApi")
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                // item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
    }
    /**
     * BottomNavigationView????????????
     *
     * @param viewIndex  tab??????
     * @param showNumber ??????????????????????????????0???????????????
     */
    private void showBadgeView(int viewIndex, int showNumber) {
        // ??????child????????????view????????????????????????????????????
        // ???bottomNavigationView?????????BottomNavigationMenuView
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBinding.bottomView.getChildAt(0);
        // ???BottomNavigationMenuView?????????childview, BottomNavigationItemView
        if (viewIndex < menuView.getChildCount()) {
            // ??????viewIndex?????????tab
            View view = menuView.getChildAt(viewIndex);
            // ??????tab??????????????????????????????ImageView
            View icon = view.findViewById(com.google.android.material.R.id.icon);
            // ?????????????????????
            int iconWidth = icon.getWidth();
            // ??????tab?????????/2
            int tabWidth = view.getWidth() / 2;
            // ??????badge????????????????????????
            int spaceWidth = tabWidth - iconWidth;

            // ??????badegeview
            new QBadgeView(this).bindTarget(view).setGravityOffset(spaceWidth + 50, 13, false).setBadgeNumber(showNumber);
        }
    }
}