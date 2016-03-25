package com.amazingnick.openweathermaps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    final Context context = this;

    private TextView textViewCity;
    private TextView textViewCountry;
    private TextView textViewSuhu;
    private TextView textViewCuaca;
    private TextView textViewWaktu;
    private TextView textViewWind;
    private TextView textViewDescription;
    private TextView textViewPressure;
    private TextView textViewHumidity;
    private TextView textViewSunrise;
    private TextView textViewSunset;
    private TextView textViewCoords;

    private ImageView imageViewIkon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewCity = (TextView) findViewById(R.id.city);
        textViewCountry = (TextView) findViewById(R.id.country);
        textViewSuhu = (TextView) findViewById(R.id.suhu);
        textViewCuaca = (TextView) findViewById(R.id.cuaca);
        textViewWaktu = (TextView) findViewById(R.id.waktu);
        textViewWind = (TextView) findViewById(R.id.wind);
        textViewDescription = (TextView) findViewById(R.id.description);
        textViewPressure = (TextView) findViewById(R.id.pressure);
        textViewHumidity = (TextView) findViewById(R.id.humidity);
        textViewSunrise = (TextView) findViewById(R.id.sunrise);
        textViewSunset = (TextView) findViewById(R.id.sunset);
        textViewCoords = (TextView) findViewById(R.id.coords);
        imageViewIkon = (ImageView) findViewById(R.id.ikon);

        Intent intent = getIntent();

        String waktu = intent.getStringExtra("waktu");
        String city = intent.getStringExtra("city");
        String icon = intent.getStringExtra("icon");
        String country = intent.getStringExtra("country");
        String suhu = intent.getStringExtra("suhu");
        String cuaca = intent.getStringExtra("cuaca");
        String description = intent.getStringExtra("description");
        String wind = intent.getStringExtra("wind");
        String pressure = intent.getStringExtra("pressure");
        String humidity = intent.getStringExtra("humidity");
        String sunrise = intent.getStringExtra("sunrise");
        String sunset = intent.getStringExtra("sunset");
        String coords = intent.getStringExtra("coords");

        Glide.with(context)
                .load(icon)
                .override(200, 200)
                .into(imageViewIkon);

        textViewCity.setText(city);
        textViewCountry.setText(country);
        textViewSuhu.setText(suhu);
        textViewCuaca.setText(cuaca);
        textViewWaktu.setText(waktu);
        textViewWind.setText(wind);
        textViewDescription.setText(description);
        textViewPressure.setText(pressure);
        textViewHumidity.setText(humidity);
        textViewSunrise.setText(sunrise);
        textViewSunset.setText(sunset);
        textViewCoords.setText(coords);
    }

}
