package com.airtennis.dhisat.fostracking.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.airtennis.dhisat.fostracking.api.SendToApi;
import com.airtennis.dhisat.fostracking.constants.IntentConstants;
import com.airtennis.dhisat.fostracking.models.FosGuyLocationInfo;
import com.airtennis.dhisat.fostracking.presenter.DebugHandler;
import com.airtennis.dhisat.fostracking.presenter.HelperCommon;
import com.airtennis.dhisat.fostracking.presenter.NetworkCommon;
import com.airtennis.dhisat.fostracking.presenter.SharedCommon;

/**
 * Created by naveen on 28/5/16.
 */
public class SendUserLocationService extends IntentService {
    GPSTracker gps;

    public SendUserLocationService() {
        super("SendUserLocationService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            DebugHandler.Log("SendUserLocationAlarmReciever step 2 ");
            if (NetworkCommon.IsConnected(SendUserLocationService.this)) {
                Bundle bundle = intent.getExtras();
                gps = new GPSTracker(SendUserLocationService.this);
                if (gps.canGetLocation())
                {
                    try {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        String prevLatitude = SharedCommon.GetUserPrevLatitude(SendUserLocationService.this);
                        String prevLongitude = SharedCommon.GetUserPrevLongitude(SendUserLocationService.this);
                        double prev_lat = Double.parseDouble(prevLatitude);
                        double prev_long = Double.parseDouble(prevLongitude);
                        Location newlocation = new Location("New Location");
                        Location prevlocation = new Location("Prev Location");
                        newlocation.setLatitude(latitude);
                        newlocation.setLongitude(longitude);
                        prevlocation.setLongitude(prev_long);
                        prevlocation.setLatitude(prev_lat);
                        double distance = HelperCommon.calDiffBtwLocations(newlocation,prevlocation);

                        if (distance >= 3)
                        {
                            DebugHandler.Log("location in diff " +distance);
                            SendToApi sendToApi = new SendToApi(SendUserLocationService.this);
                            FosGuyLocationInfo fosGuyLocationInfo = new FosGuyLocationInfo();
                            fosGuyLocationInfo.personId = bundle.getInt(IntentConstants.SEND_PERSON_ID);
                            fosGuyLocationInfo.latitude = gps.getLatitude();
                            fosGuyLocationInfo.longitude = gps.getLongitude();
                            sendToApi.sendCurrentLocationToServer(SendUserLocationService.this, fosGuyLocationInfo);
                        }
                    }catch (Exception e)
                    {
                        DebugHandler.LogException(e);
                    }
                } else {
                    gps.showSettingsAlert();
                }
            }
        }catch (Exception e)
        {
            DebugHandler.LogException(e);
        }
    }
}
