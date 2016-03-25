package com.amazingnick.openweathermaps;


import model.Example;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Nick on 3/25/2016.
 */
public interface WeatherService {
    @GET("/weather")
    public void getHasil(@Query("q") String city, @Query("units") String unit,
                         @Query("appid") String api,Callback<Example> exampleCallback);

    @GET("/weather")
    public void getYours(@Query("lat") String lat, @Query("lon") String lon,
                         @Query("units") String unit, @Query("appid") String api,Callback<Example> exampleCallback);
}
