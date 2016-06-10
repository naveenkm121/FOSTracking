package com.airtennis.dhisat.fostracking;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airtennis.dhisat.fostracking.api.SendToApi;
import com.airtennis.dhisat.fostracking.constants.IntentConstants;
import com.airtennis.dhisat.fostracking.models.AgentLocationInfo;
import com.airtennis.dhisat.fostracking.presenter.DebugHandler;
import com.airtennis.dhisat.fostracking.presenter.NetworkCommon;
import com.airtennis.dhisat.fostracking.presenter.SharedCommon;
import com.airtennis.dhisat.fostracking.service.GPSTracker;
import com.airtennis.dhisat.fostracking.service.SendUserLocationAlarmReciever;
import com.airtennis.dhisat.fostracking.sync.ReadLocations;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;

public class LocationActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    SendToApi sendToApi;
    private TextView startBtn, stopBtn;
    private double latitude,longitude;
    ArrayList<AgentLocationInfo> agentLocationList;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private GoogleMap googleMap;
    private int userId;
    GPSTracker gps;
    private Toolbar myToolbar;
    private String userName="nav";
    private boolean isThreadRunning=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        try {
            startBtn = (TextView) findViewById(R.id.start);
            stopBtn = (TextView) findViewById(R.id.stop);
            myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            myToolbar.setTitle("My Location");
            setSupportActionBar(myToolbar);
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            agentLocationList = new ArrayList<AgentLocationInfo>();
            userId = SharedCommon.getUserId(LocationActivity.this);
            userName = SharedCommon.getUserName(LocationActivity.this);

            if (Build.VERSION.SDK_INT >= 23) {
                checkLocationPermission();
            }
            initializeCurrentLocation();
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DebugHandler.Log("start tracking");
                   startTracking();

                }
            });

            stopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   stopTracking();

                }
            });
        } catch (Exception e) {
            DebugHandler.LogException(e);
        }
    }

    public void initializeCurrentLocation() {
        gps = new GPSTracker(LocationActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            initilizeMap();
            MarkerOptions agentMarker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(userName).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon));
            googleMap.addMarker(agentMarker);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            gps.showSettingsAlert();
        }

    }

    private void initilizeMap() {
        agentLocationList = ReadLocations.ReadAgentJson(LocationActivity.this);
        if (googleMap != null) {
            for (AgentLocationInfo agentInfo : agentLocationList) {
                double latitude = agentInfo.latitude;
                double longitude = agentInfo.longitude;
                String agentName = agentInfo.agentName;
                MarkerOptions agentMarker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(agentName).icon(BitmapDescriptorFactory.fromResource(R.drawable.agent_icon));
                googleMap.addMarker(agentMarker);
            }
        }
        if (googleMap == null) {
            Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }

    public void startTracking()
    {
        int timeInterval = 5;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Intent myIntent = new Intent(LocationActivity.this, SendUserLocationAlarmReciever.class);
        myIntent.putExtra(IntentConstants.SEND_PERSON_ID, userId);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 234324243, myIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (timeInterval * 1000), (timeInterval * 1000), pendingIntent);
        Toast.makeText(LocationActivity.this, "Start Tracking", Toast.LENGTH_LONG).show();
    }
    public void stopTracking()
    {
        if (alarmManager != null && pendingIntent != null) {
            DebugHandler.Log("cancel tracking");
            alarmManager.cancel(pendingIntent);
            new StopUserTrackingThread(LocationActivity.this,userId).start();
            Toast.makeText(LocationActivity.this, "Stop Tracking", Toast.LENGTH_LONG).show();
        }
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

        } else {
            initializeCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        initializeCurrentLocation();
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.location_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.start_traking:
                startTracking();
                return true;
            case R.id.stop_traking:
                stopTracking();
                return true;
            case R.id.locate_me:
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class StopUserTrackingThread extends Thread{
        Context context;
        private int userId;
        public StopUserTrackingThread()
        {

        }
        public StopUserTrackingThread(Context context,int userId)
        {
            this.context=context;
            this.userId=userId;
        }

        @Override
        public void run() {
            super.run();
            if(isThreadRunning)
            {
                return;
            }
            isThreadRunning = true;
            try {

                if(NetworkCommon.IsConnected(context))
                {
                    SendToApi sendToApi = new SendToApi(context);
                    sendToApi.sendInActivateUserInfoToServer(userId);
                }

            } catch (Exception e) {
                DebugHandler.LogException(e);

            }
            finally {
                isThreadRunning=false;
            }
        }
    }
}
