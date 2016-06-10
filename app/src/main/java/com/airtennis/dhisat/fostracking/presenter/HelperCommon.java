package com.airtennis.dhisat.fostracking.presenter;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by naveen on 29/5/16.
 */
public class HelperCommon {



    public static double calDiffBtwLocations(Location location1,Location location2) {
        double distance = 0;
        try
        {
            distance = location1.distanceTo(location2);
            DebugHandler.Log("distance btw locations:"+distance);
        }
        catch (Exception e)
        {
            DebugHandler.LogException(e);
        }
        return distance;
    }
    public static long GetTimeDifferenceinMinutes(String leavingData, String reachingDate) {


        SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        d.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        long difference = 0;
        try{
            DebugHandler.Log("leavingData "+leavingData);
            DebugHandler.Log("reachingData "+reachingDate);
            Date reachingDateTime = d.parse(reachingDate);
            Date leavingDateTime = d.parse(leavingData);
            difference = leavingDateTime.getTime()-reachingDateTime.getTime();
            DebugHandler.Log("leavingData diff--"+difference);
            difference = difference/(60*1000);
            if(difference < 0)
            {
                difference=0;
            }
            DebugHandler.Log("diff--"+difference);
        }
        catch(Exception e)
        {
            DebugHandler.LogException(e);
        }
        return difference;
    }
}
