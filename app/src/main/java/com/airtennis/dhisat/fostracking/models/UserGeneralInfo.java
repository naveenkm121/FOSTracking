package com.airtennis.dhisat.fostracking.models;

/**
 * Created by naveen on 29/5/16.
 */
public class UserGeneralInfo {
    public int userId;
    public  int  userType;
    public String userName;
    public  String userEmail;

    public UserGeneralInfo() {

    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
