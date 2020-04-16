package com.example.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.test.ui.dashboard.DashboardFragment;
import com.example.test.ui.notifications.NotificationsFragment;
import com.example.test.ui.systeminfo.SystemInfoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Method;
import java.util.List;

import static android.widget.LinearLayout.*;
import static com.example.test.ApkTool.REQUEST_READ_PHONE_STATE;

public class BottomNavigation extends FragmentActivity{
    private static final String TAG = "MainActivity";
    public static CustomPopWindow homePopWindow;
    public static SpeedPopWindow speedPopWindow;
    private Fragment currentFragment=new Fragment();
    private FragmentManager manager;
    private ImageView menu;
    private SharedPreferences sp;

    //为弹出窗口实现监听类
    private View.OnClickListener homeItemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            homePopWindow.dismiss();
            switch (v.getId()) {

            }
        }

    };
    private View.OnClickListener speedItemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            speedPopWindow.dismiss();
            switch (v.getId()) {

            }
        }

    };
    private OnClickListener homeMenuOnClick=new OnClickListener() {
        @Override
        public void onClick(View view) {
            final CheckBox positiveOrderItem = homePopWindow.view.findViewById(R.id.positiveOrder);
            final CheckBox reverseOrderItem = homePopWindow.view.findViewById(R.id.reverseOrder);
            final CheckBox dayOrderItem = homePopWindow.view.findViewById(R.id.dayOrder);
            final CheckBox weekOrderItem = homePopWindow.view.findViewById(R.id.weekOrder);
            final CheckBox monthOrderItem=homePopWindow.view.findViewById(R.id.monothOrder);
            positiveOrderItem.setChecked(false);
            reverseOrderItem.setChecked(false);
            dayOrderItem.setChecked(false);
            weekOrderItem.setChecked(false);
            monthOrderItem.setChecked(false);
            //获取toolbar的高度
            Toolbar toolbar=findViewById(R.id.toolbar);
            int toolbarheight=toolbar.getHeight();
            int menuheight=menu.getHeight();
            int menuwidth=menu.getWidth();

            homePopWindow.showAtLocation(view, Gravity.RIGHT|Gravity.TOP, menuwidth/2, toolbarheight+(menuheight*3/4));

            //弹出菜单监听
            positiveOrderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //positiveOrderItem.setChecked(!positiveOrderItem.isChecked());
                    if (positiveOrderItem.isChecked()){
                        reverseOrderItem.setClickable(false);
                    }else {
                        reverseOrderItem.setClickable(true);
                    }
                }
            });
            reverseOrderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (reverseOrderItem.isChecked()){
                        positiveOrderItem.setClickable(false);
                    }else {
                        positiveOrderItem.setClickable(true);
                    }
                }
            });
            dayOrderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dayOrderItem.isChecked()){
                        weekOrderItem.setClickable(false);
                        monthOrderItem.setClickable(false);
                    }else {
                        weekOrderItem.setClickable(true);
                        monthOrderItem.setClickable(true);
                    }
                }
            });
            weekOrderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (weekOrderItem.isChecked()){
                        dayOrderItem.setClickable(false);
                        monthOrderItem.setClickable(false);
                    }else {
                        dayOrderItem.setClickable(true);
                        monthOrderItem.setClickable(true);
                    }
                }
            });
            monthOrderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (monthOrderItem.isChecked()){
                        dayOrderItem.setClickable(false);
                        weekOrderItem.setClickable(false);
                    }else {
                        dayOrderItem.setClickable(true);
                        weekOrderItem.setClickable(true);
                    }
                }
            });
        }
    };
    private OnClickListener speedMenuOnClick=new OnClickListener() {
        @Override
        public void onClick(View view) {
            final CheckBox positiveOrderItem = speedPopWindow.view.findViewById(R.id.positiveOrder);
            final CheckBox reverseOrderItem = speedPopWindow.view.findViewById(R.id.reverseOrder);
            final CheckBox nameOrderItem = speedPopWindow.view.findViewById(R.id.nameOrder);
            positiveOrderItem.setChecked(false);
            reverseOrderItem.setChecked(false);
            nameOrderItem.setChecked(false);
            //获取toolbar的高度
            Toolbar toolbar=findViewById(R.id.toolbar);
            int toolbarheight=toolbar.getHeight();
            int menuheight=menu.getHeight();
            int menuwidth=menu.getWidth();
            speedPopWindow.showAtLocation(view, Gravity.RIGHT|Gravity.TOP, menuwidth/2, toolbarheight+(menuheight*3/4));
            //弹出菜单监听
            positiveOrderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //positiveOrderItem.setChecked(!positiveOrderItem.isChecked());
                    if (positiveOrderItem.isChecked()){
                        reverseOrderItem.setClickable(false);
                        nameOrderItem.setClickable(false);
                    }else {
                        reverseOrderItem.setClickable(true);
                        nameOrderItem.setClickable(true);
                    }
                }
            });
            reverseOrderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (reverseOrderItem.isChecked()){
                        positiveOrderItem.setClickable(false);
                        nameOrderItem.setClickable(false);
                    }else {
                        positiveOrderItem.setClickable(true);
                        nameOrderItem.setClickable(true);
                    }
                }
            });
            nameOrderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nameOrderItem.isChecked()){
                        positiveOrderItem.setClickable(false);
                        reverseOrderItem.setClickable(false);
                    }else {
                        positiveOrderItem.setClickable(true);
                        reverseOrderItem.setClickable(true);
                    }
                }
            });
        }
    };
    private void showFragment(Fragment fragment) {
        if (currentFragment!=fragment) {
            FragmentTransaction transaction = manager.beginTransaction();
            Log.e("Fragment数量", String.valueOf(manager.getFragments().size()));
            if (!currentFragment.isAdded()){
                transaction.add(R.id.nav_host_fragment, currentFragment);
            }
            transaction.hide(currentFragment);
            currentFragment = fragment;
            if (!fragment.isAdded()) {
                transaction.add(R.id.nav_host_fragment, fragment).show(fragment).commit();
            } else {
                transaction.show(fragment).commit();
            }
        }
     }
    @SuppressLint("RestrictedApi")
    public Fragment getCurrentFragment() {
        manager = getSupportFragmentManager();
        List<Fragment> fragments = manager.getFragments();
        for(int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if(fragment!=null && fragment.isAdded()&&fragment.isMenuVisible()) {
                return fragment;
            }
        }
        return null;
    }
    //为了避免崩溃重启后fragment重叠问题
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //权限
        //动态申请权限
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }
        if (!hasEnablePermission(this)) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bottom_navigation);
        setStatusBarColor(BottomNavigation.this);
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        homePopWindow = new CustomPopWindow(BottomNavigation.this, homeItemsOnClick);
        speedPopWindow = new SpeedPopWindow(BottomNavigation.this, speedItemsOnClick);
        menu=findViewById(R.id.menu);
        Fragment homeFragment=new Fragment();
        final DashboardFragment dashboardFragment=new DashboardFragment();
        final NotificationsFragment notificationsFragment=new NotificationsFragment();
        final SystemInfoFragment systemInfoFragment=new SystemInfoFragment();
        //获取当前Fragment
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (manager.getFragments().size()==0){
            Fragment current=manager.findFragmentById(R.id.nav_host_fragment);
            if (current != null ){
                currentFragment=current;
                homeFragment=current;
            }
        }
        final Fragment finalHomeFragment = homeFragment;
        final TextView title=findViewById(R.id.title);
        menu.setOnClickListener(homeMenuOnClick);
        //导航栏点击监听事件
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getTitle().toString().equals(getResources().getString(R.string.title_home))){
                    title.setText(getResources().getString(R.string.title_home));
                    showFragment(finalHomeFragment);
                    menu.setOnClickListener(homeMenuOnClick);
                }
                else if (menuItem.getTitle().toString().equals(getResources().getString(R.string.title_dashboard))){
                    title.setText(getResources().getString(R.string.title_dashboard));
                    showFragment(dashboardFragment);
                    menu.setOnClickListener(speedMenuOnClick);
                }
                else if (menuItem.getTitle().toString().equals(getResources().getString(R.string.title_notifications))){
                    title.setText(getResources().getString(R.string.title_notifications));
                    showFragment(notificationsFragment);
                    menu.setOnClickListener(null);
                }
                else if (menuItem.getTitle().toString().equals(getResources().getString(R.string.title_system_information))){
                    title.setText(getResources().getString(R.string.title_system_information));
                    showFragment(systemInfoFragment);//点击后跳转到对应的fragment
                    menu.setOnClickListener(null);
                }
                else if (menuItem.getTitle().toString().equals(getResources().getString(R.string.title_sensor_information))){
//                    title.setText(getResources().getString(R.string.title_sensor_information));
//                    showFragment(sensorInfoFragment);//点击后跳转到对应的fragment
                    Intent intent=new Intent(BottomNavigation.this,GetSensorInfo.class);
                    startActivity(intent);
                    menu.setOnClickListener(null);
                }

                return true;
            }
        });

    }


    /**
     * 解决Toolbar中Menu无法同时显示图标和文字的问题
     * */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.M)
    public void setStatusBarColor(Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏为蓝色
            window.setStatusBarColor(Color.TRANSPARENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    DrawerLayout drawerLayout=findViewById(R.id.drawer_left);
                    //将侧边栏顶部延伸至status bar
                    drawerLayout.setFitsSystemWindows(true);
                    //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
                    drawerLayout.setClipToPadding(false);
                }
            }

            if(Build.VERSION.SDK_INT >= 21) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    /**
     *  判断当前应用是否有查看应用使用情况的权限（针对于android5.0以上的系统）
     * @return
     */
    @SuppressLint("NewApi")
    public static  boolean hasEnablePermission(Context context){

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){   // 如果大于等于5.0 再做判断
            long ts = System.currentTimeMillis();
            UsageStatsManager usageStatsManager=(UsageStatsManager)context.getSystemService(Service.USAGE_STATS_SERVICE);
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
            if (queryUsageStats == null || queryUsageStats.isEmpty()) {
                return false;
            }
        }
        return true;
    }


}
