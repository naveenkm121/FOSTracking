package com.airtennis.dhisat.fostracking.presenter;

import android.content.Context;
import android.util.Log;

import com.airtennis.dhisat.fostracking.constants.AppConstants;

/**
 * Created by naveen on 26/5/16.
 */
public class DebugHandler {
    private static final String LOG_TAG = "foslog::";
    public static final String SnapShotName="error_snapshot.jpg";

    public static void LogException(Exception e) {
        if(AppConstants.EnableLogging)
        {
            Log.e(LOG_TAG, "Exception- ", e);
        }

    }

    public static void Log(String message)
    {
        if(AppConstants.EnableLogging && message !=null)
        {
            Log.i(LOG_TAG,message);
        }
    }

    public static void ReportException(Context context,Throwable e)
    {
        if(null==context)
        {
            return;
        }
        if(AppConstants.EnableLogging)
        {
            Log.e(LOG_TAG,"Exception- ",e);
        }
    }

}
