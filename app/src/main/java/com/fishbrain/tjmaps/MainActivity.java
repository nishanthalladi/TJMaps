package com.fishbrain.tjmaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;
    private Button mGetButton;
    private TextView mTextView;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Task<LocationSettingsResponse> task;
    private ImageView banana;
    private ImageView gaborRoom;
    private ImageView lightnerRoom;
//    private double xMax = 38.85, xMin = 38.7, yMax = -77, yMin = -77.2, height = 1000, width = 500;
//    private double baseLocationX = 38.78435, baseLocationY = -77.1584, xConst = 0.00005, yConst = 0.001; // house
    private double baseLocationX = 38.8177579, baseLocationY = -77.169024, xConst = 0.001, yConst = 0.005; // school
    private double xMax = baseLocationX+xConst, xMin = baseLocationX-xConst, yMax = baseLocationY+yConst, yMin = baseLocationY-yConst, height, width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        banana = findViewById(R.id.imageView);
        gaborRoom = findViewById(R.id.imageView2);
        lightnerRoom = findViewById(R.id.imageView3);


        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        Log.d("dimensions",height + " " + width);

        mTextView = findViewById(R.id.textView);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(MainActivity.this, "null update", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    banana.setX((float) (((location.getLongitude() - yMin) / (yMax - yMin)) * height));
                    banana.setY((float) (((location.getLatitude() - xMin) / (xMax - xMin)) * width));
                    mTextView.setText("\n" + location.getLatitude() + " " + location.getLongitude());
                }
            };
        };
        
        gaborRoom.setX((float) (((-77.1687349 - yMin) / (yMax - yMin)) * height));
        gaborRoom.setY((float) (((38.8177195 - xMin) / (xMax - xMin)) * width));

//        lightnerRoom.setX((float) (((-77.168734 - yMin) / (yMax - yMin)) * height));
//        lightnerRoom.setY((float) (((38.8177195 - xMin) / (xMax - xMin)) * width));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    banana.setX((float) (((location.getLongitude() - yMin) / (yMax - yMin)) * width));
                    Log.d("longitude",location.getLongitude() + "");
                    banana.setY((float) (((location.getLatitude() - xMin) / (xMax - xMin)) * height));
                    mTextView.setText(location.getLatitude() + " " + location.getLongitude());
//                    Toast.makeText(getApplicationContext(), banana.getX() + " " + banana.getY(), Toast.LENGTH_SHORT).show();
                }
            });

            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            mTextView.setText("No permission");
        }




    }

    public void clicked(View view) {

    }
}
