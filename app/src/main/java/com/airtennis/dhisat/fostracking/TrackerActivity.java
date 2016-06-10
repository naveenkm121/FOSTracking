package com.airtennis.dhisat.fostracking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.airtennis.dhisat.fostracking.constants.IntentConstants;
import com.airtennis.dhisat.fostracking.models.AgentLocationInfo;
import com.airtennis.dhisat.fostracking.presenter.DebugHandler;
import com.airtennis.dhisat.fostracking.presenter.HelperCommon;
import com.airtennis.dhisat.fostracking.service.UserTrackerAlarmReciever;
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

public class TrackerActivity extends AppCompatActivity {


    private int MIN_DISTANCE = 10;
    private GoogleMap googleMap;
    private LatLng userLocationPoint;
    ArrayList<AgentLocationInfo> agentLocationList;
    double userCurrentLatitude;
    double userCurrentLongitude;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    private Toolbar myToolbar;
    private String userName;
    private int userId;
    private boolean reachLocationflag=false;
    private boolean isThreadRunning=false;
    private Location targetLocation;
    private String userCurrentTime;
    private String targetReachedTime;
    private String targetLeavingTime;
    private TextView showMessageTv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        try {
            myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            Intent intent = getIntent();

            showMessageTv = (TextView)findViewById(R.id.showMessage);
            userCurrentLatitude = intent.getDoubleExtra(IntentConstants.PERSON_LATITUDE, 28.4593); //Huda city center
            userCurrentLongitude = intent.getDoubleExtra(IntentConstants.PERSON_LONGITUDE, 77.0724);
            userId = intent.getIntExtra(IntentConstants.PERSON_ID, -1);
            userName = intent.getStringExtra(IntentConstants.PERSON_NAME);
            if (userName != null && !userName.equals(""))
            {
                myToolbar.setTitle(userName +" Tracking");
            }
            else
            {
                myToolbar.setTitle("FOS Tracking");
            }

            setSupportActionBar(myToolbar);

            if (getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            DebugHandler.Log("userLat :" + userId + "::" + userName);
            userLocationPoint = new LatLng(userCurrentLatitude, userCurrentLongitude);
            agentLocationList = new ArrayList<AgentLocationInfo>();
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            initiliseMap();

            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(IntentConstants.SEND_LOCAL_BROADCAST_LOCATION));
        }catch (Exception e)
        {
            DebugHandler.LogException(e);
        }

    }

    public void initiliseMap()
    {
        agentLocationList = ReadLocations.ReadAgentJson(TrackerActivity.this);
        if (googleMap != null)
        {
            for ( AgentLocationInfo agentInfo:agentLocationList) {
                double latitude = agentInfo.latitude;
                double longitude = agentInfo.longitude;
                String agentName = agentInfo.agentName;
                DebugHandler.Log("lat agent::" + latitude + "long:" + longitude);
                MarkerOptions agentMarker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(agentName).icon(BitmapDescriptorFactory.fromResource(R.drawable.agent_icon));
                googleMap.addMarker(agentMarker);

            }
            MarkerOptions personMarker = new MarkerOptions().position(userLocationPoint).title(userName).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon));
            googleMap.addMarker(personMarker);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(userLocationPoint).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

           try {
               String action = intent.getAction();
               userCurrentLatitude = intent.getDoubleExtra("latitude", 28.4593);
               userCurrentLongitude = intent.getDoubleExtra("longitude", 77.0724);
               userCurrentTime = intent.getStringExtra("userCurrentTime");
               userId = intent.getIntExtra(IntentConstants.PERSON_ID, -1);
              // userName = intent.getStringExtra(IntentConstants.PERSON_NAME);
               userLocationPoint = new LatLng(userCurrentLatitude, userCurrentLongitude);
               MarkerOptions personMarker = new MarkerOptions().position(userLocationPoint).title(userName).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon));
               googleMap.addMarker(personMarker);
               CameraPosition cameraPosition = new CameraPosition.Builder().target(userLocationPoint).zoom(14).build();
               googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
               DebugHandler.Log("userCurrentTime ::" + userCurrentTime +"userCurrentLatitude:"+userCurrentLatitude +"userCurrentLongitude:"+userCurrentLongitude);
               Location userLocation = new Location(userName);
               userLocation.setLongitude(userCurrentLongitude);
               userLocation.setLatitude(userCurrentLatitude);
               if (!reachLocationflag)
               {
                   reachLocationflag = true;
                   targetReachedTime = userCurrentTime;
                   targetLocation = getTargetLocation(userLocation);
                   DebugHandler.Log("reached true  target lat"+targetReachedTime);

               }
               else
               {
                   DebugHandler.Log("reached false ");
                   if(targetLocation!=null)
                   {
                      double distancebtwTargetUser = HelperCommon.calDiffBtwLocations(targetLocation,userLocation);
                       DebugHandler.Log("reached distance between  :"+distancebtwTargetUser);
                       if(distancebtwTargetUser > MIN_DISTANCE) {
                           targetLeavingTime = userCurrentTime;
                           if(targetReachedTime!=null && targetLeavingTime!=null)
                           {
                               if(!(targetReachedTime.equals(targetLeavingTime))) {
                                   DebugHandler.Log("reached true  target lat" + targetLeavingTime + "reached time:" + targetReachedTime);
                                   long timeDiff = HelperCommon.GetTimeDifferenceinMinutes(targetLeavingTime, targetReachedTime);
                                   showMessageTv.setText(userName + " reached Agent and Spend " + timeDiff + " minutes ");
                                   showMessageTv.setVisibility(View.VISIBLE);
                                   reachLocationflag = false;
                                   targetReachedTime = null;
                                   targetLeavingTime = null;
                                   targetLocation = null;
                               }
                           }
                       }
                   }

               }
           }catch (Exception e)
           {
               DebugHandler.LogException(e);
           }

        }
    };


    public void startAlarmManager()
    {
        int timeInterval = 5;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Intent myIntent = new Intent(TrackerActivity.this, UserTrackerAlarmReciever.class);
        myIntent.putExtra(IntentConstants.PERSON_ID, userId);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 234324243, myIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (timeInterval * 1000), (timeInterval * 1000), pendingIntent);
    }
    public void stopAlarmManager()
    {
        if(alarmManager!=null)
        {
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startAlarmManager();
    }
    @Override
    protected void onStop() {
        super.onStop();
        stopAlarmManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tracking_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.stop_traking:
                return true;
            case R.id.locate_me:
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(userCurrentLatitude, userCurrentLongitude)).zoom(16).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TrackerActivity.this,MapsActivity.class);
        startActivity(intent);
        finish();
    }

    public Location getTargetLocation(Location userLocation)
    {
        Location targetLocation=null;
        for ( AgentLocationInfo agentInfo:agentLocationList) {
            double latitude = agentInfo.latitude;
            double longitude = agentInfo.longitude;
            String agentName = agentInfo.agentName;
            targetLocation = new Location(agentName);
            targetLocation.setLatitude(latitude);
            targetLocation.setLongitude(longitude);
            double distance = HelperCommon.calDiffBtwLocations(userLocation, targetLocation);
            if(distance < MIN_DISTANCE)
            {
             return targetLocation;
            }
        }
        return  targetLocation;
    }


}
