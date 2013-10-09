package org.jboss.aeropad.auth;

import android.util.Log;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.authentication.AbstractAuthenticationModule;
import org.jboss.aerogear.android.authentication.AuthenticationConfig;
import org.jboss.aerogear.android.authentication.AuthorizationFields;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.android.http.HttpException;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by summers on 10/8/13.
 */
public class CookieAuthModule extends AbstractAuthenticationModule {

    private static final String TAG = CookieAuthModule.class.getSimpleName();

    private boolean isLoggedIn = false;
    private String cookie = "";
    private final CookieAuthRunner runner;

    /**
     *
     * @param baseURL
     * @param config
     * @throws IllegalArgumentException if an endpoint can not be appended to
     * baseURL
     */
    public CookieAuthModule(URL baseURL, AuthenticationConfig config) {
        this.runner = new CookieAuthRunner(baseURL, config);
    }

    @Override
    public URL getBaseURL() {
        return runner.getBaseURL();
    }

    @Override
    public String getLoginEndpoint() {
        return runner.getLoginEndpoint();
    }

    @Override
    public String getLogoutEndpoint() {
        return runner.getLogoutEndpoint();
    }

    @Override
    public String getEnrollEndpoint() {
        return runner.getEnrollEndpoint();
    }

    @Override
    public void enroll(final Map<String, String> userData,
                       final Callback<HeaderAndBody> callback) {
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                HeaderAndBody result = null;
                Exception exception = null;
                try {
                    result = runner.onEnroll(userData);
                    cookie = result.getHeader("Set-Cookie").toString();
                    isLoggedIn = true;
                } catch (Exception e) {
                    Log.e(TAG, "error enrolling", e);
                    exception = e;
                }

                if (exception == null) {
                    callback.onSuccess(result);
                } else {
                    callback.onFailure(exception);
                }

            }
        });

    }

    @Override
    public void login(final String username, final String password,
                      final Callback<HeaderAndBody> callback) {
        Map<String, String> loginData = new HashMap<String, String>(2);
        loginData.put(USERNAME_PARAMETER_NAME, username);
        loginData.put(PASSWORD_PARAMETER_NAME, password);
        login(loginData, callback);
    }

    @Override
    public void login(final Map<String, String> loginData,
                      final Callback<HeaderAndBody> callback) {
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                HeaderAndBody result = null;
                Exception exception = null;
                try {
                    result = runner.onLogin(loginData);
                    cookie = result.getHeader("Set-Cookie").toString();
                    isLoggedIn = true;
                } catch (Exception e) {
                    Log.e(TAG, "error logging in", e);
                    exception = e;
                }

                if (exception == null) {
                    callback.onSuccess(result);
                } else {
                    callback.onFailure(exception);
                }

            }
        });

    }

    @Override
    public void logout(final Callback<Void> callback) {
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                Exception exception = null;
                try {
                    runner.onLogout();

                    CookieStore store = ((CookieManager) CookieManager.getDefault()).getCookieStore();
                    List<HttpCookie> cookies = store.get(getBaseURL().toURI());
                    cookie = "";
                    for (HttpCookie cookie : cookies) {
                        store.remove(getBaseURL().toURI(), cookie);
                    }


                    isLoggedIn = false;
                } catch (Exception e) {
                    Log.e(TAG, "Error with Login", e);
                    exception = e;
                }
                if (exception == null) {
                    callback.onSuccess(null);
                } else {
                    callback.onFailure(exception);
                }
            }
        });

    }

    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @Override
    public AuthorizationFields getAuthorizationFields() {
        AuthorizationFields fields = new AuthorizationFields();
        fields.addHeader("Cookie", cookie);
        return fields;
    }

    @Override
    public AuthorizationFields getAuthorizationFields(URI requestUri, String method, byte[] requestBody) {
        return getAuthorizationFields();
    }



    @Override
    public boolean retryLogin() {
        return false;
    }

}
