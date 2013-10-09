package org.jboss.aeropad.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.jboss.aeropad.AeropadApplication;
import org.jboss.aeropad.callback.LoginCallback;
import org.jboss.aeropad.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by summers on 10/7/13.
 */
public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loginlayout, null);

        final EditText username = (EditText) view.findViewById(R.id.username);
        final EditText password = (EditText) view.findViewById(R.id.password);

        Button login = (Button) view.findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> loginParams = new HashMap<String, String>();
                loginParams.put("username", username.getText().toString());
                loginParams.put("password", password.getText().toString());
                ((AeropadApplication) getActivity().getApplication()).login(LoginFragment.this, new LoginCallback(), loginParams);
            }
        });

        Button enroll = (Button) view.findViewById(R.id.enroll_button);
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> loginParams = new HashMap<String, String>();
                loginParams.put("username", username.getText().toString());
                loginParams.put("password", password.getText().toString());
                ((AeropadApplication) getActivity().getApplication()).login(LoginFragment.this, new LoginCallback(), loginParams);
            }
        });


        return view;
    }

}
