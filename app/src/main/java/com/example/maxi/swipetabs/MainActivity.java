package com.example.maxi.swipetabs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener{

    private DrawerLayout drawerLayout;
    private final String[] tabs = { "Days", "Weeks", "Months" };
    private View lateralLayout;

    private ViewPager viewPager;
    private MyAdapter mAdapter;
    private ActionBar actionBar;
    private Switch alarm;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lateralLayout = findViewById(R.id.lateral_layout);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("VIVZ", "onPageScrolled at position" + position);
            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                Log.d("VIVZ","onPageSelected at position"+ position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state==ViewPager.SCROLL_STATE_IDLE){
                    Log.d("VIVZ","onPageScrollStateChanged idle");
                }
                if (state==ViewPager.SCROLL_STATE_DRAGGING){
                    Log.d("VIVZ","onPageScrollStateChanged dragging");
                }
                if (state==ViewPager.SCROLL_STATE_SETTLING){
                    Log.d("VIVZ","onPageScrollStateChanged settling");
                }
            }
        });
        actionBar = getSupportActionBar();
        mAdapter = new MyAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_drawer);

        alarm = (Switch) findViewById(R.id.switch3);

        loadSettings();

        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSettings("alarm", isChecked);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                if (drawerLayout.isDrawerOpen(lateralLayout)) {
                    drawerLayout.closeDrawer(lateralLayout);
                }else {
                    MainActivity.this.finish();
                }
                return true;
            case KeyEvent.KEYCODE_MENU:
                if (drawerLayout.isDrawerOpen(lateralLayout)) {
                    drawerLayout.closeDrawer(lateralLayout);
                }else {
                    drawerLayout.openDrawer(lateralLayout);
                }
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(lateralLayout)) {
                    drawerLayout.closeDrawer(lateralLayout);
                }else {
                    drawerLayout.openDrawer(lateralLayout);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        //Log.d("VIVZ","onTabSelected at position"+ tab.getPosition()+" name "+tab.getText());
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
       // Log.d("VIVZ","onTabUnselected at position"+ tab.getPosition()+" name "+tab.getText());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
      //  Log.d("VIVZ","onTabReselected at position"+ tab.getPosition()+" name "+tab.getText());
    }

    public void exit(View v){
        MainActivity.this.finish();
    }

    public void saveSettings(String switchName, Boolean status){
        preferences = getSharedPreferences("my_settings", MODE_PRIVATE);
        editor = preferences.edit();

        editor.putBoolean(switchName, status);
        editor.commit();
    }

    public void loadSettings(){
        preferences = getSharedPreferences("my_settings", MODE_PRIVATE);

        alarm.setChecked(preferences.getBoolean("alarm", false));
    }
}


