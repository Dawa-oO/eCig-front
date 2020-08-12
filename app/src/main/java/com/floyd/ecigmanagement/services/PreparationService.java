package com.floyd.ecigmanagement.services;

import com.floyd.ecigmanagement.models.Preparation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PreparationService {

    @GET("preparation")
    Call<List<Preparation>> getAllPreparations();

    @GET("preparation/{id}")
    Call<Preparation> getAllPreparations(@Path("id") int id);

    @POST("preparation")
    Call<Preparation> createPreparation(@Body Preparation preparation);

    @PUT("preparation/{id}/quantity")
    Call<Preparation> updatePreparationQuantity(@Body int newQuantity, @Path("id") int id);

    @DELETE("preparation/{id}")
    Call<Boolean> deletePreparation(@Path("id") int id);
}
