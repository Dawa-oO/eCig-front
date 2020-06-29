package com.floyd.ecigmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.floyd.ecigmanagement.R;
import com.floyd.ecigmanagement.adapters.AromeAdapter;
import com.floyd.ecigmanagement.clients.ClientInstance;
import com.floyd.ecigmanagement.models.Arome;
import com.floyd.ecigmanagement.services.AromeService;
import com.floyd.ecigmanagement.translators.AromeTranslator;
import com.floyd.ecigmanagement.uio.AromeUio;
import com.floyd.ecigmanagement.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.floyd.ecigmanagement.enums.Level.ERROR;

public class AromeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "AROME_ACTIVITY";

    // -- FOR DESIGN
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout_arome)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.aromeRecyclerView)
    RecyclerView recyclerView;

    List<AromeUio> aromeUioList;

    AromeService aromeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_arome);

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
            case R.id.nav_booster:
                this.launchActivity(BoosterActivity.class);
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
        navigationView.setCheckedItem(R.id.nav_arome);
    }

    private void configureRecycleurLayout() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the aromelist
        aromeUioList = new ArrayList<>();

        // mock the list
        //this.mockAromeList();

        // Call server to get arome list and populate recycleurLayout
        this.fillAromeList(this);

    }

    private void mockAromeList() {
        aromeUioList.add(new AromeUio(1, 1, 30, "Arlequin", "DO IT", 4.5, ""));
        aromeUioList.add(new AromeUio(2, 5, 10, "Crème brûlée", "Supervape", 4.8, ""));
        aromeUioList.add(new AromeUio(3, 1, 30, "Framboise", "DO IT", 5.0, ""));
    }

    private void fillAromeList(AromeActivity aromeActivity) {
        Log.d(TAG, "Starting to fill arome uio list");

        // -- Initialize services
        aromeService = ClientInstance.getAromeService();

        Call<List<Arome>> call = aromeService.getAllAromes();

        call.enqueue(new Callback<List<Arome>>() {
            @Override
            public void onResponse(Call<List<Arome>> call, Response<List<Arome>> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "Starting to add " + response.body().size() + " to list");
                    for (Arome arome : response.body()) {
                        Log.d(TAG, arome.toString());
                        aromeUioList.add(AromeTranslator.translateAromeToAromeUio(arome));
                    }
                    Log.d(TAG, "Finishing to add " + response.body().size() + " aromes to uio list");
                    //creating recyclerview adapter
                    AromeAdapter adapter = new AromeAdapter(aromeActivity, aromeUioList);


                    //setting adapter to recyclerview
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Arome>> call, Throwable t) {
                Utils.displayToastyToaster(AromeActivity.this, ERROR, "Something went wrong...Please try later !");
            }
        });
    }

}
