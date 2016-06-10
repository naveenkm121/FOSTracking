package com.airtennis.dhisat.fostracking.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.airtennis.dhisat.fostracking.constants.AppConstants;

/**
 * Created by naveen on 28/5/16.
 */
public class SharedCommon {

    public static String GetUserPrevLatitude(Context context)
    {
        SharedPreferences prefs= context.getSharedPreferences(AppConstants.UserSharedPref, 0);
        String prevLatitude=prefs.getString(AppConstants.previousLatitude, 0+"");
        return prevLatitude;
    }
    public static void setUserPrevLatitude(Context context, String name)
    {

        SharedPreferences prefs= context.getSharedPreferences(AppConstants.UserSharedPref, 0);
        SharedPreferences.Editor edit= prefs.edit();
        edit.putString(AppConstants.previousLatitude, name);
        edit.commit();
    }
    public static String GetUserPrevLongitude(Context context)
    {
        SharedPreferences prefs= context.getSharedPreferences(AppConstants.UserSharedPref, 0);
        String prevLatitude=prefs.getString(AppConstants.prevLongitude, 0+"");
        return prevLatitude;
    }
    public static void setUserPrevLongitude(Context context,String name)
    {

        SharedPreferences prefs= context.getSharedPreferences(AppConstants.UserSharedPref, 0);
        SharedPreferences.Editor edit= prefs.edit();
        edit.putString(AppConstants.prevLongitude, name);
        edit.commit();
    }
    public static String getUserName(Context context)
    {
        SharedPreferences prefs= context.getSharedPreferences(AppConstants.UserSharedPref, 0);
        String userName=prefs.getString(AppConstants.userName, 0+"");
        return userName;
    }
    public static void setUserName(Context context,String name)
    {

        SharedPreferences prefs= context.getSharedPreferences(AppConstants.UserSharedPref, 0);
        SharedPreferences.Editor edit= prefs.edit();
        edit.putString(AppConstants.userName, name);
        edit.commit();
    }
    public static String getUserEmail(Context context)
    {
        SharedPreferences prefs= context.getSharedPreferences(AppConstants.UserSharedPref, 0);
        String userEmail=prefs.getString(AppConstants.userEmail, 0+"");
        return userEmail;
    }
    public static void setUserEmail(Context context,String email)
    {

        SharedPreferences prefs= context.getSharedPreferences(AppConstants.UserSharedPref, 0);
        SharedPreferences.Editor edit= prefs.edit();
        edit.putString(AppConstants.userEmail, email);
        edit.commit();
    }
    public static int getUserId(Context context)
    {
        SharedPreferences prefs= context.getSharedPreferences(AppConstants.UserSharedPref, 0);
        int userId=prefs.getInt(AppConstants.userId,0 );
        return userId;
    }
    public static void setUserId(Context context,int userId)
    {

        SharedPreferences prefs= context.getSharedPreferences(AppConstants.UserSharedPref, 0);
        SharedPreferences.Editor edit= prefs.edit();
        edit.putInt(AppConstants.userId, userId);
        edit.commit();
    }
    public static int getUserType(Context context)
    {
        SharedPreferences prefs= context.getSharedPreferences(AppConstants.UserSharedPref, 0);
        int userId=prefs.getInt(AppConstants.userType, 0);
        return userId;
    }
    public static void setUserType(Context context,int userType)
    {

        SharedPreferences prefs= context.getSharedPreferences(AppConstants.UserSharedPref, 0);
        SharedPreferences.Editor edit= prefs.edit();
        edit.putInt(AppConstants.userType, userType);
        edit.commit();
    }

}
