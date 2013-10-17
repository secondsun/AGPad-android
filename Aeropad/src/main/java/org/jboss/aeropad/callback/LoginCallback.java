package org.jboss.aeropad.callback;

import android.widget.Toast;

import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.android.pipeline.support.AbstractSupportFragmentCallback;
import org.jboss.aerogear.android.unifiedpush.PushConfig;
import org.jboss.aeropad.AeropadApplication;
import org.jboss.aeropad.MainActivity;

import java.net.URI;

/**
 * Created by summers on 10/7/13.
 */
public class LoginCallback extends AbstractSupportFragmentCallback<HeaderAndBody> {

    @Override
    public void onSuccess(HeaderAndBody headerAndBody) {
        Toast.makeText(getFragment().getActivity(), "Success", Toast.LENGTH_LONG).show();
        ((MainActivity)getFragment().getActivity()).postLogin();
        PushConfig pushConfig = new PushConfig();
        pushConfig.setPushServerURI(URI.create("ws://10.0.2.2:8080/pad/525d661045ce3ede7f31029f"));
        ((AeropadApplication)getFragment().getActivity().getApplication()).register(pushConfig);
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(getFragment().getActivity(), "Failure", Toast.LENGTH_LONG).show();
    }
}
