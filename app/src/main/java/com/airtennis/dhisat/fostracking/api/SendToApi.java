package com.airtennis.dhisat.fostracking.api;

import android.content.Context;

import com.airtennis.dhisat.fostracking.constants.AppConstants;
import com.airtennis.dhisat.fostracking.constants.SyncFunctions;
import com.airtennis.dhisat.fostracking.models.FosGuyLocationInfo;
import com.airtennis.dhisat.fostracking.models.SuccessFromServer;
import com.airtennis.dhisat.fostracking.models.SyncFosGuyLocationList;
import com.airtennis.dhisat.fostracking.models.UserGeneralInfo;
import com.airtennis.dhisat.fostracking.models.UserSignInData;
import com.airtennis.dhisat.fostracking.presenter.DebugHandler;
import com.airtennis.dhisat.fostracking.presenter.NetworkCommon;
import com.airtennis.dhisat.fostracking.presenter.SharedCommon;
import com.airtennis.dhisat.fostracking.presenter.UserInfoCommon;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 28/5/16.
 */
public class SendToApi {

    List<NameValuePair> data;
    Context context;
    public SendToApi(Context context)
    {
        this.context=context;
        data = new ArrayList<NameValuePair>();
    }
    public SyncFosGuyLocationList synPersonsData()
    {
        SyncFosGuyLocationList sfgll=null;
        try
        {
            String callingUrl = AppConstants.getReadPHPApiUrl() + SyncFunctions.GetAllUserInfoFunction;
            DebugHandler.Log("synPersonsData callingurl::"+callingUrl);
            String resultjson = NetworkCommon.HttpGetRequest(callingUrl);
            DebugHandler.Log("synPersonsData resultjson::"+resultjson);
            sfgll = new Gson().fromJson(resultjson, SyncFosGuyLocationList.class);
            if(sfgll.success==1)
            {
                DebugHandler.Log("result::"+sfgll.person.get(0).latitude);
            }
        }catch (Exception e)
        {
            DebugHandler.LogException(e);
        }
        return sfgll;
    }
    public SyncFosGuyLocationList synUserLocationData(int userId)
    {
        SyncFosGuyLocationList sfgll=null;
        try
        {
            String callingUrl = AppConstants.getReadPHPApiUrl() + SyncFunctions.GetUserLocationFunction;
            callingUrl = callingUrl+"?userId="+userId;
            DebugHandler.Log("syncUserData callingurl::"+callingUrl);
            String resultjson = NetworkCommon.HttpGetRequest(callingUrl);
            DebugHandler.Log("syncUserData resultjson::"+resultjson);
            sfgll = new Gson().fromJson(resultjson, SyncFosGuyLocationList.class);
        }catch (Exception e)
        {
            DebugHandler.LogException(e);
        }
        return sfgll;
    }
    public void sendActivateInfoToServer(FosGuyLocationInfo userInfo)
    {
        try
        {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair("userId", userInfo.personId+""));
            data.add(new BasicNameValuePair("latitude", userInfo.latitude + ""));
            data.add(new BasicNameValuePair("longitude", userInfo.longitude + ""));
            String callingUrl = AppConstants.getReadPHPApiUrl() + SyncFunctions.InsertUserActiveLocation;
            DebugHandler.Log("SendUserData callingurl::"+callingUrl);
            DebugHandler.Log("SendUserData data::"+data);
            String resultJson = NetworkCommon.HttpPostRequest(callingUrl,data);;
            DebugHandler.Log("SendUserData : resultjson "+resultJson);
        }
        catch(Exception e)
        {
            DebugHandler.LogException(e);
        }
    }
    public void sendInActivateUserInfoToServer(int userId)
    {
        try
        {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair("userId", userId+""));
            String callingUrl = AppConstants.getReadPHPApiUrl() + SyncFunctions.DeleteCurrentLocation;
            DebugHandler.Log("SendUserData Inactive callingurl::"+callingUrl);
            DebugHandler.Log("SendUserData Inactive::"+data);
            String resultJson = NetworkCommon.HttpPostRequest(callingUrl,data);;
            DebugHandler.Log("SendUserData Inactive : resultjson "+resultJson);
        }
        catch(Exception e)
        {
            DebugHandler.LogException(e);
        }
    }

    public UserSignInData userRegisteration(Context context,String name,String email,String password)
    {
        UserSignInData sos=null;
        try
        {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair("name", name));
            data.add(new BasicNameValuePair("email", email));
            data.add(new BasicNameValuePair("password", password));
            String callingUrl = AppConstants.getReadPHPApiUrl() + SyncFunctions.InsertUserRegistarionInfo;
            DebugHandler.Log("userRegistarion callingurl::"+callingUrl);
            DebugHandler.Log("userRegistarion data::"+data);
            String resultjson = NetworkCommon.HttpPostRequest(callingUrl,data);
            DebugHandler.Log("userRegistarion resultjson::"+resultjson);
            sos = new Gson().fromJson(resultjson, UserSignInData.class);
            if(sos!=null)
            {
                if(sos.success==1)
                {

                    UserGeneralInfo userGeneralInfo = new UserGeneralInfo();
                    userGeneralInfo.userName = sos.name;
                    userGeneralInfo.userType=sos.isAdmin;
                    userGeneralInfo.userEmail=sos.email;
                    userGeneralInfo.userId = sos.userId;
                    SharedCommon.setUserEmail(context, sos.email);
                    SharedCommon.setUserName(context,sos.name);
                    SharedCommon.setUserId(context,sos.userId);
                    SharedCommon.setUserType(context,sos.isAdmin);
                    UserInfoCommon.deleteUserInfo(context);
                    UserInfoCommon.InsertUserInfo(context,userGeneralInfo);
                }
            }

        }
        catch(Exception e)
        {
            DebugHandler.LogException(e);
        }
        return sos;
    }
    public UserSignInData userSignIn(Context context,String email,String password)
    {
        UserSignInData sos=null;
        try
        {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair("email", email));
            data.add(new BasicNameValuePair("password", password));
            String callingUrl = AppConstants.getReadPHPApiUrl() + SyncFunctions.GetUserLogInDetails;
            DebugHandler.Log("userSignIn callingurl::"+callingUrl);
            DebugHandler.Log("userSignIn data::"+data);
            String resultjson = NetworkCommon.HttpPostRequest(callingUrl,data);
            DebugHandler.Log("userSignIn resultjson::"+resultjson);
            sos = new Gson().fromJson(resultjson, UserSignInData.class);
            if(sos!=null)
            {
                if(sos.success==1)
                {
                    UserGeneralInfo userGeneralInfo = new UserGeneralInfo();
                    userGeneralInfo.userName = sos.name;
                    userGeneralInfo.userType=sos.isAdmin;
                    userGeneralInfo.userEmail=sos.email;
                    userGeneralInfo.userId = sos.userId;
                    UserInfoCommon.deleteUserInfo(context);
                    SharedCommon.setUserEmail(context, sos.email);
                    SharedCommon.setUserName(context, sos.name);
                    SharedCommon.setUserId(context, sos.userId);
                    SharedCommon.setUserType(context, sos.isAdmin);
                    UserInfoCommon.InsertUserInfo(context, userGeneralInfo);
                    userGeneralInfo = UserInfoCommon.getUserInfo(context,sos.userId);
                    DebugHandler.Log("userGeneralInfo name:"+userGeneralInfo.userEmail);
                }
            }

        }
        catch(Exception e)
        {
            DebugHandler.LogException(e);
        }
        return sos;
    }

    public void sendCurrentLocationToServer(Context context,FosGuyLocationInfo userInfo)
    {
        try
        {
            SuccessFromServer sos=null;
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair("userId", userInfo.personId + ""));
            data.add(new BasicNameValuePair("latitude", userInfo.latitude + ""));
            data.add(new BasicNameValuePair("longitude", userInfo.longitude + ""));
            String callingUrl = AppConstants.getReadPHPApiUrl() + SyncFunctions.InsertCurrentLocationLocation;
            DebugHandler.Log("currentLocation callingurl::"+callingUrl);
            DebugHandler.Log("currentLocation data::"+data);
            String resultjson = NetworkCommon.HttpPostRequest(callingUrl,data);
            DebugHandler.Log("currentLocation : resultjson "+resultjson);
            sos = new Gson().fromJson(resultjson, SuccessFromServer.class);
            if(sos!= null)
            {
                if(sos.success==1) {

                    SharedCommon.setUserPrevLatitude(context, userInfo.latitude + "");
                    SharedCommon.setUserPrevLongitude(context, userInfo.longitude + "");
                }
            }

        }
        catch(Exception e)
        {
            DebugHandler.LogException(e);
        }
    }


}
