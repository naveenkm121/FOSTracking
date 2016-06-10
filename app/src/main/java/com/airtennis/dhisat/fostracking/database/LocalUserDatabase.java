package com.airtennis.dhisat.fostracking.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.airtennis.dhisat.fostracking.database.tables.UserInfoTable;
import com.airtennis.dhisat.fostracking.presenter.DebugHandler;

/**
 * Created by naveen on 29/5/16.
 */
public class LocalUserDatabase extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "localUserDb";

    public LocalUserDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try {
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + UserInfoTable.UserInfo + "( " + UserInfoTable.UserId + " INTEGER PRIMARY KEY," + UserInfoTable.UserName + " TEXT," + UserInfoTable.UserEmail + " TEXT, " + UserInfoTable.UserType + " INTEGER" + ")";
            db.execSQL(CREATE_CONTACTS_TABLE);
        }catch (Exception e)
        {
            DebugHandler.LogException(e);
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + UserInfoTable.UserInfo);

        // Create tables again
        onCreate(db);
    }
}

