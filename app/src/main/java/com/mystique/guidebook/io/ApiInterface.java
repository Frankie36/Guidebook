package com.mystique.guidebook.io;

import com.mystique.guidebook.model.GuideResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("upcomingGuides")
    Call<GuideResponse> getData();
}
