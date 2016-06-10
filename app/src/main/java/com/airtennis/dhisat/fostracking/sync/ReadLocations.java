package com.airtennis.dhisat.fostracking.sync;

import android.content.Context;
import android.content.res.AssetManager;

import com.airtennis.dhisat.fostracking.models.AgentLocationInfo;
import com.airtennis.dhisat.fostracking.models.FosGuyLocationInfo;
import com.airtennis.dhisat.fostracking.models.SyncAgentLocationList;
import com.airtennis.dhisat.fostracking.models.SyncFosGuyLocationList;
import com.airtennis.dhisat.fostracking.presenter.DebugHandler;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by naveen on 27/5/16.
 */
public class ReadLocations {

    public static ArrayList<AgentLocationInfo> ReadAgentJson(Context context) {
        SyncAgentLocationList seld =null;
        ArrayList<AgentLocationInfo> arrayList = new ArrayList<AgentLocationInfo>();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is=assetManager.open("json/agentlocation.json");
            int size=is.available();
            byte[] buffer=new byte[size];
            is.read(buffer);
            is.close();
            String jsonText= new String(buffer);
            DebugHandler.Log("jsonText::"+jsonText);
            seld = new Gson().fromJson(jsonText, SyncAgentLocationList.class);
            if (seld != null) {
                if (seld.success==1) {
                    if(seld.agent!=null && seld.agent.size()>0)
                    {
                        for (int i = 0; i < seld.agent.size(); i++) {
                            DebugHandler.Log("Debugging:" + seld.agent.get(i));
                            AgentLocationInfo agentInfo = new AgentLocationInfo();
                            agentInfo = seld.agent.get(i);
                           arrayList.add(agentInfo);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            DebugHandler.ReportException(context,e);
        }
        return arrayList;
    }
    public static ArrayList<FosGuyLocationInfo> ReadFOSGuysJson(Context context) {
        SyncFosGuyLocationList sfgll =null;
        ArrayList<FosGuyLocationInfo> arrayList = new ArrayList<FosGuyLocationInfo>();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is=assetManager.open("json/fosGuysLocation.json");
            int size=is.available();
            byte[] buffer=new byte[size];
            is.read(buffer);
            is.close();
            String jsonText= new String(buffer);
            DebugHandler.Log("jsonText::"+jsonText);
            sfgll = new Gson().fromJson(jsonText, SyncFosGuyLocationList.class);
            if (sfgll != null) {
                if (sfgll.success==1) {
                    if(sfgll.person!=null && sfgll.person.size()>0)
                    {
                        for (int i = 0; i < sfgll.person.size(); i++) {
                            DebugHandler.Log("Debugging:" + sfgll.person.get(i));
                            FosGuyLocationInfo personInfo = new FosGuyLocationInfo();
                            personInfo = sfgll.person.get(i);
                            arrayList.add(personInfo);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            DebugHandler.ReportException(context,e);
        }
        return arrayList;
    }

}
