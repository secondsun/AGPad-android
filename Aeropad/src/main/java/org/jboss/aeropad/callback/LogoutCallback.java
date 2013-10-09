package org.jboss.aeropad.callback;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.pipeline.support.AbstractFragmentActivityCallback;
import org.jboss.aeropad.AeropadApplication;
import org.jboss.aeropad.MainActivity;

/**
 * Created by summers on 10/8/13.
 */
public class LogoutCallback extends AbstractFragmentActivityCallback<Void> {
    @Override
    public void onSuccess(Void data) {
        ((MainActivity)getFragmentActivity()).postLogout();
        ((AeropadApplication)getFragmentActivity().getApplication()).unregister();
    }

    @Override
    public void onFailure(Exception e) {
        ((MainActivity)getFragmentActivity()).postLogout();
        ((AeropadApplication)getFragmentActivity().getApplication()).unregister();
    }
}
