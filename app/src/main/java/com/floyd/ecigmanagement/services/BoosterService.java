package com.floyd.ecigmanagement.services;

import com.floyd.ecigmanagement.models.Booster;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface BoosterService {

    @GET("booster")
    Call<List<Booster>> getAllBoosters();

    @GET("booster/{id}")
    Call<Booster> getBooster(@Path("id") int id);

    @Multipart
    @POST("booster")
    Call<Booster> createBooster(@Part MultipartBody.Part file, @Part("booster") RequestBody json);

    @PUT("booster/{id}/quantity")
    Call<Booster> updateBoosterQuantity(@Body int newQuantity, @Path("id") int id);
}
