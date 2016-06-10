package com.airtennis.dhisat.fostracking.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.airtennis.dhisat.fostracking.LocationActivity;
import com.airtennis.dhisat.fostracking.MapsActivity;
import com.airtennis.dhisat.fostracking.R;
import com.airtennis.dhisat.fostracking.api.SendToApi;
import com.airtennis.dhisat.fostracking.models.UserSignInData;
import com.airtennis.dhisat.fostracking.presenter.NetworkCommon;
import com.airtennis.dhisat.fostracking.presenter.UICommon;


public class SignUpFragment extends Fragment {


    private View view;
    private EditText name_edt,email_edt,password_edt;
    private TextView create_account;
    private String name,email,password;
    Context context;

    public SignUpFragment() {

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        name_edt	  =	    (EditText)view.findViewById(R.id.new_signup_name);
        email_edt 	  =     (EditText)view.findViewById(R.id.new_signup_email);
        password_edt   =    (EditText)view.findViewById(R.id.new_signup_pass);;
        create_account = (TextView)view.findViewById(R.id.new_register_btn);

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp(getActivity());
            }
        });
        return view;
    }

    public void signUp(Context context)
    {
        name = name_edt.getText().toString();
        email = email_edt.getText().toString();
        password = password_edt.getText().toString();
        String str = "";
        if (name == null || name.trim().compareTo("") == 0) {
            str += context.getString(R.string.name_field_blank) + '\n';
        } else if (name.trim().length() < 3) {
            str += context.getString(R.string.name_should_be_more_than_2_character_long) + '\n';
        } else if (email == null || email.trim().compareTo("") == 0) {
            str += context.getString(R.string.email_id_field_blank) + '\n';
        } else if (!email_edt.getText().toString().matches(("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))) {
            str += context.getString(R.string.email_id_not_valid) + '\n';
        } else if (password == null || password.trim().compareTo("") == 0) {
            str += context.getString(R.string.password_field_blank) + '\n';
        } else if (password.length() < 3) {
            str += context.getString(R.string.password_shld_be_three_characters);
        }

        if (str.equals("")) {
            if (!password.equals("")) {
                // net connection condi...
                if (!NetworkCommon.IsConnected(context)) {
                    UICommon.ShowDialog(context, context.getString(R.string.internet_connection), context.getString(R.string.seems_no_internet));

                } else {
                    // String android_id = Settings.Secure.getString(context).getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    //   new TaskAsync().execute(name, email, password, android_id, mobile, sendexamst, lantype + "", "");
                    new RegistrationAsynTask().execute(name,email,password);
                }
            }
            else {
                UICommon.ShowDialog(context, context.getString(R.string.error), context.getString(R.string.password_confirm_password_do_not_match));
            }
        } else {
            UICommon.ShowDialog(context, context.getString(R.string.error), str);

        }

    }
    public class RegistrationAsynTask extends AsyncTask<String,Void,UserSignInData>
    {

        @Override
        protected UserSignInData doInBackground(String... params) {
            String userName = params[0];
            String userEmail = params[1];
            String userpassword = params[2];
            SendToApi sendToApi = new SendToApi(context);

            return sendToApi.userRegisteration(context,userName,userEmail,userpassword);
        }
        @Override
        protected void onPostExecute(UserSignInData userSignInData)
        {
            if(userSignInData!=null)
            {
                if(userSignInData.success==1)
                {
                    if(userSignInData.isAdmin==1)
                    {
                        Intent intent = new Intent(context,MapsActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else
                    {
                        Intent intent = new Intent(context,LocationActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                }else{
                    UICommon.ShowDialog(context, context.getString(R.string.error), userSignInData.message);
                }
            }else{
                UICommon.ShowDialog(context, context.getString(R.string.error), "error");
            }
        }
    }

}
