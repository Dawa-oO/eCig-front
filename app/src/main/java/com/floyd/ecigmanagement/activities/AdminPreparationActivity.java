package com.floyd.ecigmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.floyd.ecigmanagement.R;
import com.floyd.ecigmanagement.clients.ClientInstance;
import com.floyd.ecigmanagement.models.Arome;
import com.floyd.ecigmanagement.models.Preparation;
import com.floyd.ecigmanagement.services.AromeService;
import com.floyd.ecigmanagement.services.PreparationService;
import com.floyd.ecigmanagement.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.floyd.ecigmanagement.enums.Level.ERROR;
import static com.floyd.ecigmanagement.enums.Level.SUCCESS;

public class AdminPreparationActivity  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ADMIN_PREPARATION_ACTIVITY";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout_admin_preparation)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    // Form fields
    @BindView(R.id.spinner_preparation_arome)
    Spinner spinner;
    @BindView(R.id.et_preparation_capacity)
    EditText capacityEditText;
    @BindView(R.id.et_preparation_qty)
    EditText quantityEditText;

    AromeService aromeService;
    List<Arome> aromeList = new ArrayList<>();

    PreparationService preparationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_preparation);

        // -- Bind view
        ButterKnife.bind(this);

        // Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.loadSpinnerData(this);
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
            case R.id.nav_booster:
                this.launchActivity(BoosterActivity.class);
                break;
            case R.id.nav_admin_arome:
                this.launchActivity(AdminAromeActivity.class);
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
        navigationView.setCheckedItem(R.id.nav_admin_preparation);
    }

    private void loadSpinnerData(AdminPreparationActivity adminPreparationActivity) {
        List<String> aromeLabels = new ArrayList<>();
        // -- Initialize services
        aromeService = ClientInstance.getAromeService();

        Call<List<Arome>> call = aromeService.getAllAromes();

        call.enqueue(new Callback<List<Arome>>() {
            @Override
            public void onResponse(Call<List<Arome>> call, Response<List<Arome>> response) {
                if(response.isSuccessful()) {
                    aromeList = response.body();
                    for (Arome arome : response.body()) {
                        aromeLabels.add(arome.getTaste());
                    }
                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(adminPreparationActivity,
                            android.R.layout.simple_spinner_item, aromeLabels);

                    // attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter);
                } else {
                Log.d(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Arome>> call, Throwable t) {
                Utils.displayToastyToaster(AdminPreparationActivity.this, ERROR, "Something went wrong...Please try later !");
            }
        });
    }

    /* --------------------------- */
    /* ----- BUTTON LISTENER ----- */
    /* --------------------------- */
    @OnClick(R.id.button_preparation_add)
    public void onAddPreparationClick(View view) {
        Log.d(TAG, "Add preparation clicked");
        Log.d(TAG, "Arome choosed : " + aromeList.get(spinner.getSelectedItemPosition()));
        // Checks
        if (capacityEditText.getText().toString().isEmpty() || quantityEditText.getText().toString().isEmpty() || aromeList.get(spinner.getSelectedItemPosition()) == null) {
            Utils.displayToastyToaster(AdminPreparationActivity.this, ERROR, "Les champs ne sont pas bien remplis !");
        } else {
            Preparation preparation = new Preparation();
            preparation.setArome(aromeList.get(spinner.getSelectedItemPosition()));
            preparation.setCapacity(Integer.parseInt(capacityEditText.getText().toString()));
            preparation.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));

            preparationService = ClientInstance.getPreparationService();

            Call<Preparation> call = preparationService.createPreparation(preparation);

            call.enqueue(new Callback<Preparation>() {
                @Override
                public void onResponse(Call<Preparation> call, Response<Preparation> response) {
                    Utils.displayToastyToaster(AdminPreparationActivity.this, SUCCESS, "La préparation a été créée !");
                }

                @Override
                public void onFailure(Call<Preparation> call, Throwable t) {
                    Utils.displayToastyToaster(AdminPreparationActivity.this, ERROR, "Something went wrong...Please try later !");
                }
            });
        }

    }


}
