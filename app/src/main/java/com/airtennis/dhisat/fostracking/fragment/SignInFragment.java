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


public class SignInFragment extends Fragment {
    private View view;
    private EditText email_edt,password_edt;
    private TextView login;
    private String email,password;
    Context context;

    public SignInFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_sign_in, container, false);
        email_edt 	  =     (EditText)view.findViewById(R.id.new_signup_email);
        password_edt   =    (EditText)view.findViewById(R.id.new_signup_pass);;
        login = (TextView)view.findViewById(R.id.new_register_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(context);
            }
        });
        return view;
    }

    public void signIn(Context context)
    {
        email = email_edt.getText().toString();
        password = password_edt.getText().toString();
        String str = "";
        if (email == null || email.trim().compareTo("") == 0) {
            str += context.getString(R.string.email_id_field_blank) + '\n';
        } else if (!email_edt.getText().toString().matches(("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))) {
            str += context.getString(R.string.email_id_not_valid) + '\n';
        }

        if (str.equals("")) {
            if (!password.equals("")) {
                // net connection condi...
                if (!NetworkCommon.IsConnected(context)) {
                    UICommon.ShowDialog(context, context.getString(R.string.internet_connection), context.getString(R.string.seems_no_internet));

                }
                else
                {
                    new SignInAsynTask().execute(email, password);
                }
            }
            else {
                UICommon.ShowDialog(context, context.getString(R.string.error), context.getString(R.string.password_confirm_password_do_not_match));
            }
        } else {
            UICommon.ShowDialog(context, context.getString(R.string.error), str);

        }

    }
    public class SignInAsynTask extends AsyncTask<String,Void,UserSignInData>
    {

        @Override
        protected UserSignInData doInBackground(String... params) {
            String userEmail = params[0];
            String userpassword = params[1];
            SendToApi sendToApi = new SendToApi(context);

            return sendToApi.userSignIn(context, userEmail, userpassword);
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
                       Intent intent = new Intent(context, MapsActivity.class);
                       context.startActivity(intent);
                       getActivity().finish();
                   }else
                   {
                       Intent intent = new Intent(context, LocationActivity.class);
                       context.startActivity(intent);
                       context.startActivity(intent);
                       getActivity().finish();
                   }
                }
                else
                {
                    UICommon.ShowDialog(context, context.getString(R.string.error), userSignInData.message);
                }
            }else{
                UICommon.ShowDialog(context, context.getString(R.string.error), "error");
            }
        }
    }


}
