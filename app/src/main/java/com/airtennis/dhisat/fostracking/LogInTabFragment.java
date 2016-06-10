package com.airtennis.dhisat.fostracking;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.airtennis.dhisat.fostracking.fragment.SignInFragment;
import com.airtennis.dhisat.fostracking.fragment.SignUpFragment;

/**
 * Created by naveen on 29/5/16.
 */
public class LogInTabFragment extends FragmentStatePagerAdapter {

    int mNumOfTabs=2;
    private Context context;

    public LogInTabFragment(Context context,FragmentManager fm) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0)
        {

            SignInFragment signInFragment = new SignInFragment();
            return signInFragment;
        }
        else {
            SignUpFragment signUpFragment = new SignUpFragment();
            return signUpFragment;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.SignIn);
            case 1:
                return context.getResources().getString(R.string.SignUp);
            default:
                return context.getResources().getString(R.string.SignIn);
        }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
