package com.floyd.ecigmanagement.services;

import com.floyd.ecigmanagement.models.Arome;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AromeService {

    @GET("arome")
    Call<List<Arome>> getAllAromes();

    @GET("arome/{id}")
    Call<Arome> getArome(@Path("id") int id);

    @Multipart
    @POST("arome")
    Call<Arome> createArome(@Part MultipartBody.Part file, @Part("arome") RequestBody json);

    @PUT("arome/{id}/quantity")
    Call<Arome> updateAromeQuantity(@Body int newQuantity, @Path("id") int id);

    @DELETE("arome/{id}")
    Call<Boolean> deleteArome(@Path("id") int id);
}
