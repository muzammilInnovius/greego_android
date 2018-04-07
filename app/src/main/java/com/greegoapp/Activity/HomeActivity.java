package com.greegoapp.Activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greegoapp.Adapter.DrawerLayoutAdapter;
import com.greegoapp.Fragment.FreeTripsFragment;
import com.greegoapp.Fragment.HelpFragment;
import com.greegoapp.Fragment.MapHomeFragment;
import com.greegoapp.Fragment.PaymentFragment;
import com.greegoapp.Fragment.SettingFragment;
import com.greegoapp.Fragment.TripHistoryFragment;
import com.greegoapp.R;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    ActivityHomeBinding binding;
    Context context;
    private View snackBarView;


    private RelativeLayout navHeader;
    private ImageView ivPro, ivProPicHome;
    private DrawerLayoutAdapter drawerLayoutAdapter;
    private DrawerLayout drawer_layout;
    private ListView menuList;
    private static RelativeLayout drawerlist;
    android.app.FragmentManager manager;
    private FrameLayout container_body;
    TextView tvDrawUsername, tvDriveGreego;
    private Fragment mContentFragment = null;
    private String[] drawerTitle = {"Payment", "Your Trips", "Free Rides", "Help", "Settings"};
    public static int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerLayoutAdapter = new DrawerLayoutAdapter(HomeActivity.this, drawerTitle);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        context = HomeActivity.this;
        snackBarView = findViewById(android.R.id.content);

        manager = getFragmentManager();

        bindView();
        setListners();

        setHomeValues();
        slideMenu();
        drawerlist.requestDisallowInterceptTouchEvent(true);
        navHeader.requestDisallowInterceptTouchEvent(true);

    }

    private void setHomeValues() {
        try {
            Fragment fragmentPro = null;
            fragmentPro = new MapHomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.replace(R.id.containerBody, fragmentPro);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            mContentFragment = fragmentPro;
            drawer_layout.closeDrawer(drawerlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListners() {
        ivProPicHome.setOnClickListener(this);
    }

    private void bindView() {
        navHeader = binding.navHeader;
        drawer_layout = binding.drawerLayout;
        ivProPicHome = binding.ivProPicHome;
        container_body = binding.containerBody;
        drawerlist = binding.drawerlist;
        ivPro = binding.ivPro;
        tvDrawUsername = binding.tvDrawUsername;
        menuList = binding.menuList;
        tvDriveGreego = binding.tvDriveGreego;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivProPicHome:
                openDrawer();
                break;
        }
    }


    public void openDrawer() {
        try {
//            setvalues();

            container_body.setClickable(false);
            drawerlist.setClickable(true);
            navHeader.setClickable(true);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            menuList.invalidateViews();
            drawer_layout.openDrawer(drawerlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeDrawer() {
        try {
            if (drawer_layout.isDrawerOpen(drawerlist))
                drawer_layout.closeDrawer(drawerlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void slideMenu() {
        try {
            DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) drawerlist.getLayoutParams();
            drawerlist.setLayoutParams(layoutParams);

            menuList.setAdapter(drawerLayoutAdapter);
            if (drawer_layout.isDrawerOpen(drawerlist)) {
                drawer_layout.closeDrawer(drawerlist);
            }

            menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
                    drawer_layout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                        @Override
                        public void onDrawerClosed(View drawerView) {
                            super.onDrawerClosed(drawerView);
                        }
                    });

                    drawer_layout.closeDrawer(drawerlist);
                    index = pos;

                    displayView(pos);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void displayView(final int pos) {
        try {
            closeDrawer();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Fragment fragment = null;
                    switch (pos) {
                        case 0:

                            fragment = new PaymentFragment();
                            break;

                        case 1:
                            fragment = new TripHistoryFragment();
                            break;

                        case 2:
                            fragment = new FreeTripsFragment();
                            break;

                        case 3:
                            fragment = new HelpFragment();
                            break;

                        case 4:
                            fragment = new SettingFragment();
                            break;

//                        default:
//                            break;
                    }

                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerBody, fragment);
                        fragmentTransaction.commit();
                        mContentFragment = fragment;
                    }

                    drawer_layout.closeDrawer(drawerlist);
                    drawerLayoutAdapter.setSelectedIndex(pos);
                }
            }, 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

    }


//    @Override
//    public void onBackStackChanged() {
//
//        int count = manager.getBackStackEntryCount();
//        for (int i = count - 1; i >= 0; i--) {
//            FragmentManager.BackStackEntry entry = (FragmentManager.BackStackEntry) manager.getBackStackEntryAt(i);
//        }
//    }


}
