package com.floyd.ecigmanagement.clients;

import android.util.Log;

import com.floyd.ecigmanagement.services.AromeService;
import com.floyd.ecigmanagement.services.BoosterService;
import com.floyd.ecigmanagement.services.PreparationService;
import com.floyd.ecigmanagement.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientInstance {

    private static final String TAG = "CLIENT_INSTANCE";

    private static AromeService aromeService;
    private static BoosterService boosterService;
    private static PreparationService preparationService;

    public static AromeService getAromeService() {
        Log.d(TAG, "getAromeService");
        if (aromeService == null) {

            Retrofit retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Constants.BACK_API_URL)
                    .client(getClientLoggingInterceptor())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            aromeService = retrofit.create(AromeService.class);
        }

        return aromeService;
    }

    public static BoosterService getBoosterService() {
        Log.d(TAG, "getBoosterService");

        if (boosterService == null) {
            Retrofit retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Constants.BACK_API_URL)
                    .client(getClientLoggingInterceptor())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            boosterService = retrofit.create(BoosterService.class);
        }
        return boosterService;
    }

    public static PreparationService getPreparationService() {
        Log.d(TAG, "getPreparationService");

        if (preparationService == null) {
            Retrofit retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Constants.BACK_API_URL)
                    .client(getClientLoggingInterceptor())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            preparationService = retrofit.create(PreparationService.class);
        }
        return preparationService;
    }

    private static OkHttpClient getClientLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }
}
