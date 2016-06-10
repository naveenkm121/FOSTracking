package com.airtennis.dhisat.fostracking.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.airtennis.dhisat.fostracking.database.DataBaseCommon;
import com.airtennis.dhisat.fostracking.database.LocalUserDatabase;
import com.airtennis.dhisat.fostracking.database.tables.UserInfoTable;
import com.airtennis.dhisat.fostracking.models.UserGeneralInfo;

/**
 * Created by naveen on 29/5/16.
 */
public class UserInfoCommon {

    public static void InsertUserInfo(Context context,UserGeneralInfo userGeneralInfo)
    {
        Cursor c = null;
        try
        {
            LocalUserDatabase lud = DataBaseCommon.GetLocalCustomerDatabase(context);
            SQLiteDatabase db = lud.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(UserInfoTable.UserName,userGeneralInfo.userName);
            cv.put(UserInfoTable.UserEmail, userGeneralInfo.userEmail);
            cv.put(UserInfoTable.UserType, userGeneralInfo.userType);

            DebugHandler.Log("UserInfoCommon name" + userGeneralInfo.userName );
            String sql = "select "+UserInfoTable.UserId+" from "+UserInfoTable.UserInfo+" where "+UserInfoTable.UserId+"="+userGeneralInfo.userId;
            c = db.rawQuery(sql,null);
            if(c!=null && c.getCount()>0)
            {
                DebugHandler.Log("UserInfoCommon update ");
                db.update(UserInfoTable.UserInfo,cv,UserInfoTable.UserId+"="+userGeneralInfo.userId,null);
            }
            else
            {
                DebugHandler.Log("UserInfoCommon insert ");
                cv.put(UserInfoTable.UserId,userGeneralInfo.userId);
                db.insert(UserInfoTable.UserInfo, "save", cv);
            }
        }
        catch(Exception e)
        {
            DebugHandler.LogException(e);
        }
        finally
        {
            if(c !=null)
            {
                c.close();
            }
        }
    }
    public static UserGeneralInfo checkUserInfo(Context context) {
        LocalUserDatabase lud = DataBaseCommon.GetLocalCustomerDatabase(context);
        SQLiteDatabase db = lud.getWritableDatabase();
        UserGeneralInfo userGeneralInfo = null;
        Cursor cursor = null;
        try {
            String sql = "select "+
                    UserInfoTable.UserId+ " ,"+
                    UserInfoTable.UserEmail+ " ,"+
                    UserInfoTable.UserName+","+
                    UserInfoTable.UserEmail+","+
                    UserInfoTable.UserType+""+
                    " from "+UserInfoTable.UserInfo;
            cursor = db.rawQuery(sql,null);
            if (cursor!=null && cursor.getCount()>0)
            {
                cursor.moveToFirst();
                userGeneralInfo = new UserGeneralInfo();
                userGeneralInfo.userId = cursor.getInt(cursor.getColumnIndex(UserInfoTable.UserId));
                userGeneralInfo.userName = cursor.getString(cursor.getColumnIndex(UserInfoTable.UserName));
                userGeneralInfo.userEmail = cursor.getString(cursor.getColumnIndex(UserInfoTable.UserEmail));
                userGeneralInfo.userType = cursor.getInt(cursor.getColumnIndex(UserInfoTable.UserType));
            }
        }
        catch (Exception e)
        {
            DebugHandler.LogException(e);
        }
        finally {
            if(cursor!=null)
            {
                cursor.close();
            }
        }
        return userGeneralInfo;
    }
    public static UserGeneralInfo getUserInfo(Context context, int useId) {
        LocalUserDatabase lud = DataBaseCommon.GetLocalCustomerDatabase(context);
        SQLiteDatabase db = lud.getWritableDatabase();
        UserGeneralInfo userGeneralInfo = new UserGeneralInfo();
        Cursor cursor = null;
        try {
            String sql = "select "+
                    UserInfoTable.UserEmail+ " ,"+
                    UserInfoTable.UserName+","+
                    UserInfoTable.UserEmail+","+
                    UserInfoTable.UserType+""+
                    " from "+UserInfoTable.UserInfo+
                    " where "+UserInfoTable.UserId+
                    "="+userGeneralInfo.userId;
            cursor = db.rawQuery(sql,null);
            if (cursor!=null && cursor.getCount()>0)
            {
                cursor.moveToFirst();
                userGeneralInfo.userId = useId;
                userGeneralInfo.userName = cursor.getString(cursor.getColumnIndex(UserInfoTable.UserName));
                userGeneralInfo.userEmail = cursor.getString(cursor.getColumnIndex(UserInfoTable.UserEmail));
                userGeneralInfo.userType = cursor.getInt(cursor.getColumnIndex(UserInfoTable.UserType));
            }
        }
        catch (Exception e)
        {
            DebugHandler.LogException(e);
        }
        finally {
            if(cursor!=null) {
                cursor.close();
            }
        }
        return userGeneralInfo;
    }
    public static void deleteUserInfo(Context context) {
        LocalUserDatabase lud = DataBaseCommon.GetLocalCustomerDatabase(context);
        SQLiteDatabase db = lud.getWritableDatabase();
        Cursor cursor = null;
        try
        {
            String sql = "delete  from "+UserInfoTable.UserInfo;
            cursor = db.rawQuery(sql,null);
            DebugHandler.Log("Delete user info succesfully");
        }
        catch (Exception e)
        {
            DebugHandler.LogException(e);
        }
        finally
        {
            if(cursor!=null) {
                cursor.close();
            }
        }
    }
}
