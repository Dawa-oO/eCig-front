package com.floyd.ecigmanagement.services;

import com.floyd.ecigmanagement.models.Arome;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AromeService {

    @GET("arome")
    Call<List<Arome>> getAllAromes();

    @GET("arome/{id}")
    Call<Arome> getArome(@Path("id") int id);

    @POST("arome")
    Call<Arome> createArome(@Body Arome arome);

    @PUT("arome/{id}/quantity")
    Call<Arome> updateAromeQuantity(@Body int newQuantity, @Path("id") int id);
}
