package com.floyd.ecigmanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.codekidlabs.storagechooser.StorageChooser;
import com.floyd.ecigmanagement.R;
import com.floyd.ecigmanagement.clients.ClientInstance;
import com.floyd.ecigmanagement.models.Booster;
import com.floyd.ecigmanagement.services.BoosterService;
import com.floyd.ecigmanagement.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.floyd.ecigmanagement.enums.Level.ERROR;
import static com.floyd.ecigmanagement.enums.Level.SUCCESS;

public class AdminBoosterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ADMIN_BOOSTER_ACTIVITY";
    public static final int FILEPICKER_PERMISSIONS = 2;

    // -- FOR DESIGN
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout_admin_booster)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    BoosterService boosterService;

    // -- Form fields
    @BindView(R.id.et_booster_capacity)
    EditText capacityEditText;
    @BindView(R.id.et_booster_quantity)
    EditText quantityEditText;
    @BindView(R.id.et_booster_pgvg)
    EditText pgvgEditText;
    @BindView(R.id.tv_booster_image)
    TextView filePathTextView;

    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_booster);

        // -- Bind view
        ButterKnife.bind(this);

        // Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
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
        navigationView.setCheckedItem(R.id.nav_admin_booster);
    }

    /* --------------------------- */
    /* ----- BUTTON LISTENER ----- */
    /* --------------------------- */
    @OnClick(R.id.upload_booster_image)
    public void onUploadBoosterImageClick(View view) {
        Log.d(TAG, "Upload image click");
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (hasPermissions(AdminBoosterActivity.this, PERMISSIONS)){
            showFilepicker();
        } else {
            ActivityCompat.requestPermissions(AdminBoosterActivity.this, PERMISSIONS, FILEPICKER_PERMISSIONS);
        }
    }

    @OnClick(R.id.button_booster_add)
    public void onAddBoosterClick(View view) {
        Log.d(TAG, "Add arome clicked");

        if (capacityEditText.getText().toString().isEmpty() || quantityEditText.getText().toString().isEmpty() || pgvgEditText.getText().toString().isEmpty()) {
            Utils.displayToastyToaster(AdminBoosterActivity.this, ERROR, "Les champs ne sont pas bien remplis !");
        } else if (filePath.isEmpty()) {
            Utils.displayToastyToaster(AdminBoosterActivity.this, ERROR, "Image manquante !");
        } else {
            Booster booster = new Booster();
            booster.setCapacity(Integer.parseInt(capacityEditText.getText().toString()));
            booster.setPgvg(pgvgEditText.getText().toString());
            booster.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));

            String boosterJSON = new Gson().toJson(booster);
            File file = new File(filePath);

            Log.d(TAG, "boosterJSON:" + boosterJSON);

            if (!file.exists()) {
                Utils.displayToastyToaster(AdminBoosterActivity.this, ERROR, "Le fichier n'existe pas !");
                return;
            }

            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse("image/*"),
                            file
                    );

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            // add another part within the multipart request
            RequestBody description =
                    RequestBody.create(
                            MediaType.parse("application/json"),
                            boosterJSON
                    );

            // -- Initialize services
            boosterService = ClientInstance.getBoosterService();
            Call<Booster> call = boosterService.createBooster(body, description);

            call.enqueue(new Callback<Booster>() {
                @Override
                public void onResponse(Call<Booster> call, Response<Booster> response) {
                    Utils.displayToastyToaster(AdminBoosterActivity.this, SUCCESS, "Le booster a été créé !");
                }

                @Override
                public void onFailure(Call<Booster> call, Throwable t) {
                    Log.e(TAG, "Error while creating boostger : " + t.getMessage());
                    Utils.displayToastyToaster(AdminBoosterActivity.this, ERROR, "Something went wrong...Please try later !");
                }
            });

        }

    }

    public void showFilepicker(){
        // 1. Initialize dialog
        final StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(AdminBoosterActivity.this)
                .withFragmentManager(getFragmentManager())
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.FILE_PICKER)
                .build();

        // 2. Retrieve the selected path by the user and show in a toast !
        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path) {
                Log.d(TAG, "The path is : " + path);
                filePath = path;
                filePathTextView.setText(path);
            }
        });

        // 3. Display File Picker !
        chooser.show();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FILEPICKER_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.displayToastyToaster(AdminBoosterActivity.this, SUCCESS, "Permission granted! Please click on pick a file once again.");
                } else {
                    Utils.displayToastyToaster(AdminBoosterActivity.this, ERROR, "Permission denied to read your External storage :(");
                }
                return;
            }
        }
    }
}
