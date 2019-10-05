package com.gmail.hc.gwnoii.movie.controller;
import android.os.Bundle;

import com.android.volley.toolbox.Volley;
import com.gmail.hc.gwnoii.movie.R;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {

    private ViewPagerFragment viewPagerFragment;
    private DetailViewFragment detailViewFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 데이터베이스를 생성한다.
        AppHelper.openDatabase(this, "movieDatabase");

        // 네트워크연결을 위해 requestQueue를 준비한다.
        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(this);      // static 변수 설정으로 한 번만 초기화 시키면 됨
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPagerFragment = new ViewPagerFragment();

        getSupportActionBar().setTitle("영화 목록");
        getSupportFragmentManager().beginTransaction().add(R.id.containerBox, viewPagerFragment).commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            viewPagerFragment = new ViewPagerFragment();

            if (getSupportActionBar().getTitle() == ("영화 목록")) {
                finish();
            } else if (getSupportActionBar().getTitle() == "영화 상세") {
                getSupportActionBar().setTitle("영화 목록");
                getSupportFragmentManager().beginTransaction().replace(R.id.containerBox, viewPagerFragment).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list) {
            viewPagerFragment = new ViewPagerFragment();
            getSupportActionBar().setTitle("영화 목록");
            getSupportFragmentManager().beginTransaction().replace(R.id.containerBox, viewPagerFragment).commit();
        } else if (id == R.id.nav_api) {

        } else if (id == R.id.nav_reservation) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDetailView(int movieId) {
        getSupportActionBar().setTitle("영화 상세");

        detailViewFragment = new DetailViewFragment(movieId);            //  시작이 id = 1;
        getSupportFragmentManager().beginTransaction().replace(R.id.containerBox, detailViewFragment).commit();
    }


}