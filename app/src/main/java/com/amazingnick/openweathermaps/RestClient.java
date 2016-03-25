package com.amazingnick.openweathermaps;

import retrofit.RestAdapter;

/**
 * Created by Nick on 3/24/2016.
 */
public class RestClient {
    private static final String URL = "http://api.openweathermap.org/data/2.5/";
    private retrofit.RestAdapter restAdapter;
    private WeatherService weatherService;

    public RestClient()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        weatherService = restAdapter.create(WeatherService.class);
    }

    public WeatherService getService()
    {
        return weatherService;
    }
}
