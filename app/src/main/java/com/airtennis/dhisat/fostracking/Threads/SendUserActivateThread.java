package com.airtennis.dhisat.fostracking.Threads;

import android.content.Context;

import com.airtennis.dhisat.fostracking.api.SendToApi;
import com.airtennis.dhisat.fostracking.models.FosGuyLocationInfo;
import com.airtennis.dhisat.fostracking.presenter.DebugHandler;

/**
 * Created by naveen on 28/5/16.
 */
public class SendUserActivateThread extends Thread {
    public static boolean isThreadRunning=false;
    Context context;
    int threadPurpose=-1;
    public static final int SEND_ACTIVE_USER_INFO = 101;


    public SendUserActivateThread()
    {

    }
    public SendUserActivateThread(Context context,int threadPurpose)
    {
        this.context=context;
        this.threadPurpose=threadPurpose;
    }
    @Override
    public void run() {
        super.run();
        if(isThreadRunning)
        {
            return;
        }
        isThreadRunning = true;
        try {
            if(threadPurpose==SEND_ACTIVE_USER_INFO)
            {

                try {
                    DebugHandler.Log("sync thread in my order history");
                    SendToApi sendToApi = new SendToApi(context);
                    FosGuyLocationInfo fosGuyLocationInfo = new FosGuyLocationInfo();
                    fosGuyLocationInfo.personId=5;
                    fosGuyLocationInfo.latitude=23.13;
                    fosGuyLocationInfo.longitude=45.23;
                    sendToApi.sendCurrentLocationToServer(context,fosGuyLocationInfo);

                } catch (Exception e) {
                    DebugHandler.LogException(e);
                }

            }

        } catch (Exception e) {
            DebugHandler.LogException(e);

        }
        finally {
            isThreadRunning=false;
        }
    }
}
