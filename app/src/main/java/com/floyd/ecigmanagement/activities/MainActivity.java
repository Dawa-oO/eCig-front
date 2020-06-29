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
import com.floyd.ecigmanagement.adapters.PreparationAdapter;
import com.floyd.ecigmanagement.clients.ClientInstance;
import com.floyd.ecigmanagement.models.Preparation;
import com.floyd.ecigmanagement.services.PreparationService;
import com.floyd.ecigmanagement.translators.PreparationTranslator;
import com.floyd.ecigmanagement.uio.PreparationUio;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MAIN_ACTIVITY";

    // -- FOR DESIGN
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout_main)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.preparationRecyclerView)
    RecyclerView recyclerView;

    List<PreparationUio> preparationUioList;

    PreparationService preparationService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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
            case R.id.nav_arome:
                this.launchActivity(AromeActivity.class);
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
        navigationView.setCheckedItem(R.id.nav_home);
    }

    private void configureRecycleurLayout() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the preparationlist
        preparationUioList = new ArrayList<>();

        // Call server to get preparation list and populate recycleurLayout
        this.fillPreparationList(this);
    }

    private void fillPreparationList(MainActivity preparationActivity) {
        Log.d(TAG, "Starting to fill preparation uio list");

        // Initialize services
        preparationService = ClientInstance.getPreparationService();

        Call<List<Preparation>> call = preparationService.getAllPreparations();

        call.enqueue(new Callback<List<Preparation>>() {
            @Override
            public void onResponse(Call<List<Preparation>> call, Response<List<Preparation>> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "Starting to add " + response.body().size() + " to list");
                    for (Preparation preparation : response.body()) {
                        Log.d(TAG, preparation.toString());
                        preparationUioList.add(PreparationTranslator.translatePreparationToPreparationUio(preparation));
                    }
                    Log.d(TAG, "Finishing to add " + response.body().size() + " aromes to uio list");
                    //creating recyclerview adapter
                    PreparationAdapter adapter = new PreparationAdapter(preparationActivity, preparationUioList);


                    //setting adapter to recyclerview
                    recyclerView.setAdapter(adapter);
                }  else {
                    Log.d(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Preparation>> call, Throwable t) {
                Utils.displayToastyToaster(MainActivity.this, ERROR, "Something went wrong...Please try later !");
            }
        });
    }
}
