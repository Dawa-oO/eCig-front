package com.floyd.ecigmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.floyd.ecigmanagement.R;
import com.floyd.ecigmanagement.adapters.BoosterAdapter;
import com.floyd.ecigmanagement.uio.BoosterUio;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoosterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BOOSTER_ACTIVITY";

    // -- FOR DESIGN
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout_booster)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.boosterRecyclerView)
    RecyclerView recyclerView;

    List<BoosterUio> boosterUioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_booster);

        // -- Bind view
        ButterKnife.bind(this);

        // Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureRecycleurLayout();
    }

    @Override
    public void onBackPressed() {
        // Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                this.launchActivity(MainActivity.class);
                break;
            case R.id.nav_arome:
                this.launchActivity(AromeActivity.class);
                break;
            case R.id.nav_admin_arome:
                this.launchActivity(AdminAromeActivity.class);
                break;
            case R.id.nav_admin_booster:
                this.launchActivity(AdminBoosterActivity.class);
                break;
            case R.id.nav_admin_preparation:
                this.launchActivity(AdminPreparationActivity.class);
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void launchActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    /* ------------------------- */
    /* ----- CONFIGURATION ----- */
    /* ------------------------- */
    // Configure Toolbar
    private void configureToolBar(){
        setSupportActionBar(toolbar);
    }

    // Configure Drawer Layout
    private void configureDrawerLayout(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void configureNavigationView(){
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_booster);
    }

    private void configureRecycleurLayout() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the boosterlist
        boosterUioList = new ArrayList<>();

        this.mockBoosterList();

        //creating recyclerview adapter
        BoosterAdapter adapter = new BoosterAdapter(this, boosterUioList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    private void mockBoosterList() {
        boosterUioList.add(new BoosterUio(1, 25, 10, "50/50", ""));
    }
}
