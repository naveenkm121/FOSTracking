package com.airtennis.dhisat.fostracking.constants;

/**
 * Created by naveen on 26/5/16.
 */
public class AppConstants {

    public static String UserSharedPref="userdata";
    public static String previousLatitude="prevLatitude";
    public static String prevLongitude="prevLongitude";
    public static String userName="userName";
    public static String userEmail="userEmail";
    public static String userType="userType";
    public static String userId="userId";


    public static final boolean EnableLogging=true;
    private static String LocalMachineIp="192.168.100.90";


    public static String getReadPHPApiUrl()
    {

            return "http://"+LocalMachineIp+"/locationtracker/";

    }
}
