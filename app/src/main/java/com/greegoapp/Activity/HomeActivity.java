package com.greegoapp.Activity;

import android.content.Context;
import android.content.Intent;
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
import com.greegoapp.Interface.BackPressedFragment;
import com.greegoapp.Interface.CallFragmentInterface;
import com.greegoapp.R;
import com.greegoapp.Utils.HeaderBar;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.databinding.ActivityHomeBinding;

import java.util.HashMap;
import java.util.Stack;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, CallFragmentInterface, BackPressedFragment {


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
    private Stack<Fragment> fragmentStack;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerLayoutAdapter = new DrawerLayoutAdapter(HomeActivity.this, drawerTitle);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        context = HomeActivity.this;
        fragmentStack = new Stack<Fragment>();
        fragmentManager = this.getSupportFragmentManager();
        snackBarView = findViewById(android.R.id.content);

        manager = getFragmentManager();

        bindView();
//        setHeaderbar();
        setListners();
        setHomeValues();
        slideMenu();
        drawerlist.requestDisallowInterceptTouchEvent(true);
        navHeader.requestDisallowInterceptTouchEvent(true);

    }


//    private HeaderBar headerBar;
//
//    private void setHeaderbar() {
//        try {
//            headerBar = binding.headerBar;
//            headerBar.ivLeft.setImageResource(R.mipmap.ic_profile);
//            headerBar.ivLeft.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    openDrawer();
//                }
//            });
//            headerBar.rrHomeBtn.setVisibility(View.GONE);
//
//            headerBar.ivRight.setVisibility(View.GONE);
//            headerBar.ivRightOfLeft.setVisibility(View.GONE);
//
//            headerBar.tvTitle.setVisibility(View.VISIBLE);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
//        headerBar = binding.headerBar;
        container_body = binding.containerBody;
        drawerlist = binding.drawerlist;
        ivPro = binding.ivPro;
        tvDrawUsername = binding.tvDrawUsername;
        menuList = binding.menuList;
        tvDriveGreego = binding.tvDriveGreego;

        ivProPicHome.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                    Intent in;
                    switch (pos) {
                        case 0:
//                            ivProPicHome.setVisibility(View.GONE);
                            in = new Intent(HomeActivity.this, PaymentActivity.class);
                            startActivity(in);
                            break;

                        case 1:
//                            ivProPicHome.setVisibility(View.GONE);
                            in = new Intent(HomeActivity.this, TripHistoryDetailActivity.class);
                            startActivity(in);
//                            fragment = new TripHistoryFragment().newInstance("", "");
                            break;

                        case 2:
//                            ivProPicHome.setVisibility(View.GONE);
                            in = new Intent(HomeActivity.this, FreeTripsActivity.class);
                            startActivity(in);
//                            fragment = new FreeTripsFragment().newInstance("", "");
                            break;

                        case 3:
//                            ivProPicHome.setVisibility(View.GONE);
                            in = new Intent(HomeActivity.this, HelpActivity.class);
                            startActivity(in);
//                            fragment = new HelpFragment().newInstance("", "");
                            break;

                        case 4:
//                            ivProPicHome.setVisibility(View.GONE);
                            in = new Intent(HomeActivity.this, SettingActivity.class);
                            startActivity(in);
//                            fragment = new SettingFragment().newInstance("", "");
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


    @Override
    public void onBackPressed(Context context) {
        try {
            onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();

        if (manager.getBackStackEntryCount() > 2) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();

        } else {
            finish();
        }


//        if (container_body.getChildCount() == 1) {
//            super.onBackPressed();
//            if (container_body.getChildCount() == 0) {
//               finish();
//                // load your first Fragment here
//            }
//        } else if (container_body.getChildCount() == 0) {
//            setHomeValues();
//            // load your first Fragment here
//        } else {
//            super.onBackPressed();
//        }
    }


    @Override
    public void onFragmentCall(Context mContext, Fragment myFragment, String Tag) {
        if (container_body.getVisibility() == View.GONE) {
            container_body.setVisibility(View.VISIBLE);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerBody, myFragment, Tag);
        fragmentStack.push(myFragment);
        fragmentTransaction.commit();
    }


}



