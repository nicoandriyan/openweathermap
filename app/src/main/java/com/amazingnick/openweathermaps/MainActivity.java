package com.amazingnick.openweathermaps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.Example;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements LocationListener, View.OnClickListener {

    LocationManager locationManager;
    String provider;
    Location location;

    final Context context = this;

    RestClient restClient;
    private Button buttonInput;
    private Button buttonGPS;

    private static final String API_KEY = "532d313d6a9ec4ea93eb89696983e369";
    private static final String UNITS = "metric";
    private static final String gambar = "http://openweathermap.org/img/w/10n.png";

    private String waktu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        restClient = new RestClient();
        buttonInput = (Button) findViewById(R.id.buttonInput);
        buttonInput.setOnClickListener(this);
        buttonGPS = (Button) findViewById(R.id.buttonGPS);
        buttonGPS.setOnClickListener(this);

        //---------------------------------current date-------------------------------------//
        Calendar today = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        waktu = "get at " + df.format(today.getTime());
        //---------------------------------current date-------------------------------------//
    }

    public void findCity(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View promptsView = inflater.inflate(R.layout.dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                final String param = userInput.getText().toString().trim();
                                showCityWeather(param);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showCityWeather(String kota){
        restClient.getService().getHasil(kota, UNITS, API_KEY, new Callback<Example>() {
            @Override
            public void success(Example result, Response response) {
                String city = result.getName() + ", ";
                String country = result.getSys().getCountry();
                String suhu = "" + result.getMain().getTemp();
                String cuaca = "";
                String description = "";
                String icon = "";
                if(result.getWeather()!=null && result.getWeather().size()>0){
                    for (int i = 0; i <result.getWeather().size(); i++){
                        cuaca = result.getWeather().get(i).getMain();
                        description = result.getWeather().get(i).getDescription();
                        icon = "http://openweathermap.org/img/w/" +result.getWeather().get(i).getIcon() +".png";
                    }
                }
                String wind = "" + result.getWind().getSpeed() + " m/s, (";
                if(result.getWind().getDeg() != null){
                    wind += result.getWind().getDeg() + ")";
                }
                String pressure = "" + result.getMain().getPressure() + " hpa";
                String humidity = "" + result.getMain().getHumidity() + " %";
                Integer sunriseunix = result.getSys().getSunrise();
                Integer sunsetunix = result.getSys().getSunset();
                //----------------------------convert sunrise timestamp-------------------------------//
                java.util.Date time=new java.util.Date((long)sunriseunix*1000);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String sunrise = sdf.format(time);
                //----------------------------convert sunset timestamp-------------------------------//
                java.util.Date time2=new java.util.Date((long)sunsetunix*1000);
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                String sunset = sdf2.format(time2);

                String coords = "[" + result.getCoord().getLon() + "," + result.getCoord().getLat();

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("waktu", waktu);
                intent.putExtra("city", city);
                intent.putExtra("icon", icon);
                intent.putExtra("country", country);
                intent.putExtra("suhu", suhu);
                intent.putExtra("cuaca", cuaca);
                intent.putExtra("description", description);
                intent.putExtra("wind", wind);
                intent.putExtra("pressure", pressure);
                intent.putExtra("humidity", humidity);
                intent.putExtra("sunrise", sunrise);
                intent.putExtra("sunset", sunset);
                intent.putExtra("coords", coords);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void findLocation(){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        provider = locationManager.getBestProvider(c, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(provider);
        if(location !=null)
        {
            //get latitude and longitude of the location
            double lng= location.getLongitude();
            double lat= location.getLatitude();
            String LONGITUDE = String.valueOf(lng);
            String LATITUDE = String.valueOf(lat);
            currentWeather(LATITUDE, LONGITUDE);
            //ln.setText(""+longitut);
            //lt.setText(""+latitut);
        }
        else
        {
            //ln.setText("No Provider");
            //lt.setText("No Provider");
        }
    }

    public void currentWeather(String latitude, String longitute){
        restClient.getService().getYours(latitude, longitute, UNITS, API_KEY, new Callback<Example>() {
            @Override
            public void success(Example result, Response response) {
                String city = result.getName() + ", ";
                String country = result.getSys().getCountry();
                String suhu = "" + result.getMain().getTemp();
                String cuaca = "";
                String description = "";
                String icon = "";
                if(result.getWeather()!=null && result.getWeather().size()>0){
                    for (int i = 0; i <result.getWeather().size(); i++){
                        cuaca = result.getWeather().get(i).getMain();
                        description = result.getWeather().get(i).getDescription();
                        icon = "http://openweathermap.org/img/w/" +result.getWeather().get(i).getIcon() +".png";
                    }
                }
                String wind = "" + result.getWind().getSpeed() + " m/s, (";
                if(result.getWind().getDeg() != null){
                    wind += result.getWind().getDeg() + ")";
                }
                String pressure = "" + result.getMain().getPressure() + " hpa";
                String humidity = "" + result.getMain().getHumidity() + " %";
                Integer sunriseunix = result.getSys().getSunrise();
                Integer sunsetunix = result.getSys().getSunset();
                //----------------------------convert sunrise timestamp-------------------------------//
                java.util.Date time=new java.util.Date((long)sunriseunix*1000);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String sunrise = sdf.format(time);
                //----------------------------convert sunset timestamp-------------------------------//
                java.util.Date time2=new java.util.Date((long)sunsetunix*1000);
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                String sunset = sdf2.format(time2);

                String coords = "[" + result.getCoord().getLon() + "," + result.getCoord().getLat();

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("waktu", waktu);
                intent.putExtra("city", city);
                intent.putExtra("icon", icon);
                intent.putExtra("country", country);
                intent.putExtra("suhu", suhu);
                intent.putExtra("cuaca", cuaca);
                intent.putExtra("description", description);
                intent.putExtra("wind", wind);
                intent.putExtra("pressure", pressure);
                intent.putExtra("humidity", humidity);
                intent.putExtra("sunrise", sunrise);
                intent.putExtra("sunset", sunset);
                intent.putExtra("coords", coords);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == buttonInput){
            findCity();
        }
        else if(v == buttonGPS){
            findLocation();
        }
    }

    @Override
    public void onLocationChanged(Location arg0)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
