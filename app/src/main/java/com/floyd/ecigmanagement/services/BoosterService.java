package com.floyd.ecigmanagement.services;

import com.floyd.ecigmanagement.models.Booster;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BoosterService {

    @GET("booster")
    Call<List<Booster>> getAllBoosters();

    @GET("booster/{id}")
    Call<Booster> getBooster(@Path("id") int id);

    @POST("booster")
    Call<Booster> createBooster(@Body Booster booster);

    @PUT("booster/{id}/quantity")
    Call<Booster> updateBoosterQuantity(@Body int newQuantity, @Path("id") int id);
}
