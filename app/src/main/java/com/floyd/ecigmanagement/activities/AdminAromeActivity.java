package com.floyd.ecigmanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.floyd.ecigmanagement.models.Arome;
import com.floyd.ecigmanagement.services.AromeService;
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

public class AdminAromeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ADMIN_AROME_ACTIVITY";
    public static final int FILEPICKER_PERMISSIONS = 2;

    // -- FOR DESIGN
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout_admin_arome)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    AromeService aromeService;

    // -- Form fields
    @BindView(R.id.et_arome_taste)
    EditText tasteEditText;
    @BindView(R.id.et_arome_brand)
    EditText brandEditText;
    @BindView(R.id.et_arome_capacity)
    EditText capacityEditText;
    @BindView(R.id.et_arome_quantity)
    EditText quantityEditText;
    @BindView(R.id.et_arome_note)
    EditText noteEditText;
    @BindView(R.id.tv_arome_image)
    TextView filePathTextView;

    private Uri fileUri;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_arome);

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
        navigationView.setCheckedItem(R.id.nav_admin_arome);
    }

    /* --------------------------- */
    /* ----- BUTTON LISTENER ----- */
    /* --------------------------- */
    @OnClick(R.id.upload_arome_image)
    public void onUploadAromeImageClick(View view) {
        Log.d(TAG, "Upload image click");

        String[] PERMISSIONS = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (hasPermissions(AdminAromeActivity.this, PERMISSIONS)){
            showFilepicker();
        } else {
            ActivityCompat.requestPermissions(AdminAromeActivity.this, PERMISSIONS, FILEPICKER_PERMISSIONS);
        }
    }


    @OnClick(R.id.button_arome_add)
    public void onAddPreparationClick(View view) {
        Log.d(TAG, "Add arome clicked");

        if (brandEditText.getText().toString().isEmpty() || capacityEditText.getText().toString().isEmpty() || noteEditText.getText().toString().isEmpty() || quantityEditText.getText().toString().isEmpty() || tasteEditText.getText().toString().isEmpty()) {
            Utils.displayToastyToaster(AdminAromeActivity.this, ERROR, "Les champs ne sont pas bien remplis !");
        } else if (filePath.isEmpty()) {
            Utils.displayToastyToaster(AdminAromeActivity.this, ERROR, "Image manquante !");
        } else {
            Arome arome = new Arome();
            arome.setBrand(brandEditText.getText().toString());
            arome.setTaste(tasteEditText.getText().toString());
            arome.setCapacity(Integer.parseInt(capacityEditText.getText().toString()));
            arome.setNote(Double.parseDouble(noteEditText.getText().toString()));
            arome.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));

            String aromeJSON = new Gson().toJson(arome);
            File file = new File(filePath);

            Log.d(TAG, "aromeJSON:" + aromeJSON);

            if (!file.exists()) {
                Utils.displayToastyToaster(AdminAromeActivity.this, ERROR, "Le fichier n'existe pas !");
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
                            aromeJSON
                    );


            // -- Initialize services
            aromeService = ClientInstance.getAromeService();
            Call<Arome> call = aromeService.createArome(body, description);

            call.enqueue(new Callback<Arome>() {

                @Override
                public void onResponse(Call<Arome> call, Response<Arome> response) {
                    Utils.displayToastyToaster(AdminAromeActivity.this, SUCCESS, "La préparation a été créée !");
                }

                @Override
                public void onFailure(Call<Arome> call, Throwable t) {
                    Log.e(TAG, "Error while creating arome : " + t.getMessage());
                    Utils.displayToastyToaster(AdminAromeActivity.this, ERROR, "Something went wrong...Please try later !");
                }
            });
        }
    }

    public void showFilepicker(){
        // 1. Initialize dialog
        final StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(AdminAromeActivity.this)
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
                    Utils.displayToastyToaster(AdminAromeActivity.this, SUCCESS, "Permission granted! Please click on pick a file once again.");
                } else {
                    Utils.displayToastyToaster(AdminAromeActivity.this, ERROR, "Permission denied to read your External storage :(");
                }
                return;
            }
        }
    }
}
