package com.airtennis.dhisat.fostracking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.airtennis.dhisat.fostracking.api.SendToApi;
import com.airtennis.dhisat.fostracking.constants.IntentConstants;
import com.airtennis.dhisat.fostracking.models.AgentLocationInfo;
import com.airtennis.dhisat.fostracking.models.FosGuyLocationInfo;
import com.airtennis.dhisat.fostracking.models.SyncFosGuyLocationList;
import com.airtennis.dhisat.fostracking.presenter.DebugHandler;
import com.airtennis.dhisat.fostracking.sync.ReadLocations;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;


public class MapsActivity extends AppCompatActivity {


    ArrayList<AgentLocationInfo> agentLocationList;
    ArrayList<FosGuyLocationInfo> personLocationList;

    // for map
    private GoogleMap googleMap;
    SendToApi sendToApi;
    private ProgressDialog prgDialog;
    private  Toolbar myToolbar;
    public static final int progress_bar_type = 0;
    private HashMap<Marker, FosGuyLocationInfo> userHashMapMarker =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        try {
            myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            myToolbar.setTitle("All FOS Locations");
            setSupportActionBar(myToolbar);
            agentLocationList = new ArrayList<AgentLocationInfo>();
            personLocationList = new ArrayList<FosGuyLocationInfo>();
            userHashMapMarker =  new HashMap<Marker, FosGuyLocationInfo>();
            sendToApi = new SendToApi(MapsActivity.this);

           // initilizeMap();
            new UserLocationTask().execute();

        } catch (Exception e) {
            DebugHandler.LogException(e);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                prgDialog = new ProgressDialog(this);
                prgDialog.setMessage("Getting Locations Please wait...");
                prgDialog.setIndeterminate(false);
                prgDialog.setMax(100);
                prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                prgDialog.setCancelable(false);
                prgDialog.show();
                return prgDialog;
            default:
                return null;
        }
    }


    private void initilizeMap() {
        agentLocationList = ReadLocations.ReadAgentJson(MapsActivity.this);
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (googleMap != null) {
                for (AgentLocationInfo agentInfo : agentLocationList) {
                    double latitude = agentInfo.latitude;
                    double longitude = agentInfo.longitude;
                    String agentName = agentInfo.agentName;
                    DebugHandler.Log("lat agent::" + latitude + "long:" + longitude);
                    MarkerOptions agentMarker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(agentName).icon(BitmapDescriptorFactory.fromResource(R.drawable.agent_icon));
                    googleMap.addMarker(agentMarker);

                }
                for (FosGuyLocationInfo personInfo : personLocationList) {
                    double latitude = personInfo.latitude;
                    double longitude = personInfo.longitude;
                    String userName = personInfo.personName;
                    DebugHandler.Log("lat person::" + userName + "long:" + longitude);
                    MarkerOptions personMarker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(userName).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon));
                    Marker userMarker = googleMap.addMarker(personMarker);
                    userHashMapMarker.put(userMarker, personInfo);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        double latitude = marker.getPosition().latitude;
                        double longitude = marker.getPosition().longitude;
                        Object fgyli = userHashMapMarker.get(marker);
                        if(fgyli  instanceof FosGuyLocationInfo)
                        {
                            DebugHandler.Log("maprker name"+((FosGuyLocationInfo) fgyli).personName);
                            Intent intent = new Intent(MapsActivity.this, TrackerActivity.class);
                            intent.putExtra(IntentConstants.PERSON_LATITUDE, latitude);
                            intent.putExtra(IntentConstants.PERSON_LONGITUDE, longitude);
                            intent.putExtra(IntentConstants.PERSON_ID,((FosGuyLocationInfo) fgyli).personId);
                            intent.putExtra(IntentConstants.PERSON_NAME,((FosGuyLocationInfo) fgyli).personName);
                            startActivity(intent);
                            finish();
                        }
                        return false;
                    }
                });
            }

            if (googleMap == null) {
                Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void drawPath() {
        Polyline line = googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(53.560, 9.939), new LatLng(53.5670, 9.949))
                .width(5)
                .color(Color.RED));
    }




    @Override
    protected void onResume() {
        super.onResume();
      //  initilizeMap();
    }

    public class UserLocationTask extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... params) {
            SyncFosGuyLocationList sfgll=null;
            sfgll=sendToApi.synPersonsData();
            if(sfgll!=null)
            {
                if(sfgll.success==1 && sfgll.person!=null)
                {
                    if(sfgll.person.size()>0)
                    {
                        personLocationList.clear();
                        DebugHandler.Log("sfgll.person:"+sfgll.person.size());
                        for (FosGuyLocationInfo personInfo:sfgll.person) {
                            DebugHandler.Log("sfgll.person::::"+personInfo.personName);
                            personLocationList.add(personInfo);
                        }
                    }
                }
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            dismissDialog(progress_bar_type);
            initilizeMap();
        }
    }



}
