package com.airtennis.dhisat.fostracking.database;

import android.content.Context;

import com.airtennis.dhisat.fostracking.presenter.DebugHandler;

/**
 * Created by naveen on 29/5/16.
 */
public class DataBaseCommon {
    private static LocalUserDatabase localUserDatabase = null;

    public synchronized static void setupLocalCustomerDatabase(Context context) {
        try {
            if (localUserDatabase != null) {
                return;
            }
            localUserDatabase = new LocalUserDatabase(context);
        } catch (Exception e) {
            DebugHandler.LogException(e);
        }
    }
    public synchronized static void CloseLocalCustomerDatabase() {
        if (localUserDatabase != null) {
            localUserDatabase = null;
        }
    }
    /* Context can be NULL */
    public static LocalUserDatabase GetLocalCustomerDatabase(Context context) {
        if (localUserDatabase == null) {
            DebugHandler.Log("setting up local customer database");
            setupLocalCustomerDatabase(context);
        }
        return localUserDatabase;
    }

    public static void ResetDatabases(Context context) {
        try {
            CloseLocalCustomerDatabase();
        } catch (Exception e) {
            DebugHandler.LogException(e);
        }
    }



}
