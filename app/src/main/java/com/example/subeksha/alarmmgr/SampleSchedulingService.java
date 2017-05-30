package com.example.subeksha.alarmmgr;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Timer;

import io.realm.Realm;

/**
 * Created by Subeksha on 5/22/17.
 */

public class SampleSchedulingService extends Service implements LocationListener {

    public static String TAG = "SampleSchedulingService";
    public static String str_receiver = "GHT_LOCATION";
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    long notify_interval = 1000;
    Intent intent;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private Realm realm;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        realm = Realm.getDefaultInstance();

//        mTimer = new Timer();
//        mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);
        intent = new Intent(str_receiver);
        fn_getlocation();
    }

    @Override
    public void onLocationChanged(final Location location) {
        this.location =location;
        Log.e("latitude", location.getLatitude() + "");
        Log.e("longitude", location.getLongitude() + "");

        realm.beginTransaction();
        final LocationRealm locationRealm = new LocationRealm();

        Number number = realm.where(LocationRealm.class).max("id");
        final int nextNumber;
        if (number == null) {
            nextNumber = 1;
        } else {
            nextNumber = number.intValue() + 1;
        }
        locationRealm.setId(nextNumber);
        locationRealm.setLatitude(String.valueOf(location.getLatitude()));
        locationRealm.setLongitude(String.valueOf(location.getLongitude()));
        realm.copyToRealm(locationRealm);
        realm.commitTransaction();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e(TAG, "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e(TAG, "onProviderDisabled");
    }

    private void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {
            if (isNetworkEnable) {
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
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }

            }


            if (isGPSEnable) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);



                /*if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }*/
            }


        }

    }

    private void fn_update(Location location) {

        intent.putExtra("latitude", location.getLatitude() + "");
        intent.putExtra("longitude", location.getLongitude() + "");
        sendBroadcast(intent);
        locationManager.removeUpdates(this);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
