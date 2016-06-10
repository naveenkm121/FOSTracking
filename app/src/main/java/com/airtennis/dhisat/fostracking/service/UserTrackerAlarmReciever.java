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
public class UserTrackerAlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DebugHandler.Log("GetUserLocationService step 1 ");
        Bundle bundle = intent.getExtras();
        int  userId = bundle.getInt(IntentConstants.PERSON_ID);
        DebugHandler.Log("GetUserLocationService step userId "+userId);
        Intent i = new Intent(context,GetUserLocationService.class);
        i.putExtra(IntentConstants.PERSON_ID, userId);
        context.startService(i);
    }
}
