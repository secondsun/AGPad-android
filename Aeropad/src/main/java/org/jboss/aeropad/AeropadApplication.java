package org.jboss.aeropad;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.Pipeline;
import org.jboss.aerogear.android.authentication.AuthenticationConfig;
import org.jboss.aerogear.android.authentication.impl.Authenticator;
import org.jboss.aerogear.android.impl.pipeline.PipeConfig;
import org.jboss.aerogear.android.unifiedpush.PushConfig;
import org.jboss.aerogear.android.unifiedpush.PushRegistrar;
import org.jboss.aerogear.android.unifiedpush.PushRegistrarFactory;
import org.jboss.aerogear.android.unifiedpush.Registrations;
import org.jboss.aeropad.callback.LoginCallback;
import org.jboss.aeropad.callback.LogoutCallback;
import org.jboss.aeropad.callback.PadCallback;
import org.jboss.aeropad.auth.CookieAuthModule;
import org.jboss.aeropad.fragment.AddPadFragment;
import org.jboss.aeropad.socket.WebsocketPushRegistrar;
import org.jboss.aeropad.vo.Pad;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by summers on 10/7/13.
 */
public class AeropadApplication extends Application {

    private static final java.net.URL URL;

    static {
        try {
            URL = new URL("http://10.0.2.2:8080");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    Authenticator auth = new Authenticator(URL);
    Pipeline pipeline = new Pipeline(URL);
    Registrations r = new Registrations(new PushRegistrarFactory() {
        @Override
        public PushRegistrar createPushRegistrar(PushConfig config) {
            Map<String, String> cookieMap = new HashMap<String, String>();
            cookieMap.put("cookie", config.getSecret());
            return new WebsocketPushRegistrar(config, getApplicationContext(), cookieMap);
        }
    });
    public void onCreate() {
        auth.add("auth", new CookieAuthModule(URL,new AuthenticationConfig()));
        PipeConfig padConfig = new PipeConfig(URL, Pad.class);
        padConfig.setAuthModule(auth.get("auth"));
        pipeline.pipe(Pad.class, padConfig);


    }

    public void login(Fragment fragment, LoginCallback callback, Map<String, String> loginParams) {
        auth.get("auth", fragment, this).login(loginParams, callback);
    }

    public void enroll(Fragment fragment, LoginCallback callback, Map<String, String> loginParams) {
        auth.get("auth", fragment, this).enroll(loginParams, callback);
    }

    public void pad(Fragment fragment) {
        pipeline.get("pad", fragment, this).read(new PadCallback());
    }

    public void logout(FragmentActivity activity) {
        auth.get("auth", activity).logout(new LogoutCallback());
    }

    public void register(PushConfig pushConfig) {

        if (r.get("socket") != null) {
            r.get("socket").unregister(this, new Callback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    Log.d("PadApp", "unregister success");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("PadApp", e.getMessage(), e);
                }
            });
        }
        r.push("socket", pushConfig);
        r.get("socket").register(getApplicationContext(), new Callback<Void>() {
            @Override
            public void onSuccess(Void data) {
                Log.d("PadApp", "register success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("PadApp", e.getMessage(), e);
            }
        });
    }


    public void unregister() {
        r.get("socket").unregister(getApplicationContext(), new Callback<Void>() {
            @Override
            public void onSuccess(Void data) {
                Log.d("PadApp", "unregister success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("PadApp", e.getMessage(), e);
            }
        });

    }

    public void addPad(AddPadFragment addPadFragment, String padName, AddPadFragment.AddPadFragmentCallback callback) {
        Pad pad = new Pad();
        pad.setName(padName);
        pipeline.get("pad", addPadFragment, this).save(pad, callback);
    }
}
