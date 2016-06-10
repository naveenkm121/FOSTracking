package com.airtennis.dhisat.fostracking;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.airtennis.dhisat.fostracking.models.UserGeneralInfo;
import com.airtennis.dhisat.fostracking.presenter.DebugHandler;
import com.airtennis.dhisat.fostracking.presenter.UserInfoCommon;

public class LaunchAcitivity extends AppCompatActivity {


    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_acitivity);
        try
        {
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar_configure);
            new CheckUserLoginSession().execute();
        }catch (Exception e)
        {
            DebugHandler.LogException(e);
        }
    }

    public class CheckUserLoginSession extends AsyncTask<Void,Void,UserGeneralInfo> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected UserGeneralInfo doInBackground(Void... params) {
            UserGeneralInfo userGeneralInfo = null;
            userGeneralInfo = UserInfoCommon.checkUserInfo(LaunchAcitivity.this);
            return userGeneralInfo;
        }

        @Override
        protected void onPostExecute(UserGeneralInfo userSignInData) {
            mProgressBar.setVisibility(View.GONE);
            if (userSignInData != null)
            {
                if(userSignInData.userType ==1)
                {
                    DebugHandler.Log("userSignInData.userType ::1");
                    Intent intent = new Intent(LaunchAcitivity.this,MapsActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    DebugHandler.Log("userSignInData.userType ::2");
                    Intent intent = new Intent(LaunchAcitivity.this,LocationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            else
            {
                DebugHandler.Log("userSignInData.userType ::3");
                Intent intent = new Intent(LaunchAcitivity.this,LoginMainActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }
}
