package com.airtennis.dhisat.fostracking.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airtennis.dhisat.fostracking.constants.IntentConstants;
import com.airtennis.dhisat.fostracking.presenter.DebugHandler;

/**
 * Created by naveen on 28/5/16.
 */
public class SendUserLocationAlarmReciever  extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        DebugHandler.Log("SendUserLocationAlarmReciever step 1 ");
        Bundle bundle = intent.getExtras();
        int  userId = bundle.getInt(IntentConstants.SEND_PERSON_ID);
//        double userLatitude = bundle.getDouble(IntentConstants.SEND_PERSON_LATITUDE);
//        double userLongitude = bundle.getDouble(IntentConstants.SEND_PERSON_LONGITUDE);
        Intent i = new Intent(context,SendUserLocationService.class);
//        i.putExtra(IntentConstants.SEND_PERSON_LONGITUDE,userLongitude);
//        i.putExtra(IntentConstants.SEND_PERSON_LATITUDE,userLatitude);
          i.putExtra(IntentConstants.SEND_PERSON_ID,userId);
        context.startService(i);
    }
}
