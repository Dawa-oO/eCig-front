package com.floyd.ecigmanagement.clients;

import android.util.Log;

import com.floyd.ecigmanagement.services.AromeService;
import com.floyd.ecigmanagement.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientInstance {

    private static final String TAG = "CLIENT_INSTANCE";

    private static AromeService aromeService;

    public static AromeService getAromeService() {
        Log.d(TAG, "getAromeService");
        if (aromeService == null) {
            Log.d(TAG, "aromService is null, instanciate new one");

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


            Retrofit retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Constants.BACK_API_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            aromeService = retrofit.create(AromeService.class);
        }

        return aromeService;
    }
}
