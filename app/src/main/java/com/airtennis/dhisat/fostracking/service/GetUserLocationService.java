package com.airtennis.dhisat.fostracking.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.airtennis.dhisat.fostracking.api.SendToApi;
import com.airtennis.dhisat.fostracking.constants.IntentConstants;
import com.airtennis.dhisat.fostracking.models.SyncFosGuyLocationList;
import com.airtennis.dhisat.fostracking.presenter.DebugHandler;
import com.airtennis.dhisat.fostracking.presenter.NetworkCommon;

/**
 * Created by naveen on 28/5/16.
 */
public class GetUserLocationService extends IntentService {
    public  final static String MY_ACTION = "MY_ACTION";
    private double latitude;
    private double longitude;
    private String userCurrentTime ;


    public GetUserLocationService() {
        super("GetUserLocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DebugHandler.Log("GetUserLocationService step 2 ");
        try{
            if(NetworkCommon.IsConnected(GetUserLocationService.this))
            {
                Bundle bundle = intent.getExtras();
                int userId = bundle.getInt(IntentConstants.PERSON_ID);
                DebugHandler.Log("GetUserLocationService step 2 user:"+userId);
                SyncFosGuyLocationList sfgll=new SyncFosGuyLocationList();
                SendToApi sendToApi = new SendToApi(GetUserLocationService.this);
                sfgll=sendToApi.synUserLocationData(userId);
                if(sfgll!=null)
                {
                    if(sfgll.person!=null && sfgll.person.size()>0)
                    {
                        latitude = sfgll.person.get(0).latitude;
                        longitude = sfgll.person.get(0).longitude;
                        userCurrentTime = sfgll.person.get(0).currentTime;
                        sendLocationToActivity();
                    }
                }

            }

        }catch (Exception e)
        {
            DebugHandler.LogException(e);
        }
        stopSelf();

    }
    private void sendLocationToActivity()
    {
        Intent intent = new Intent(IntentConstants.SEND_LOCAL_BROADCAST_LOCATION);
        sendLocationBroadcast(intent);
    }
    private void sendLocationBroadcast(Intent intent){
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("userCurrentTime",userCurrentTime);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
