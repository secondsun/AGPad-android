package org.jboss.aeropad.auth;

import android.util.Log;

import com.google.gson.JsonObject;

import org.jboss.aerogear.android.Provider;
import org.jboss.aerogear.android.authentication.AuthenticationConfig;
import org.jboss.aerogear.android.authentication.impl.AbstractAuthenticationModuleRunner;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.android.http.HttpProvider;
import org.jboss.aerogear.android.impl.core.HttpProviderFactory;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.jboss.aerogear.android.impl.util.UrlUtils;

import static org.jboss.aerogear.android.authentication.AbstractAuthenticationModule.PASSWORD_PARAMETER_NAME;
import static org.jboss.aerogear.android.authentication.AbstractAuthenticationModule.USERNAME_PARAMETER_NAME;

/**
 * Created by summers on 10/8/13.
 */
public class CookieAuthRunner {

    private static final String TAG = AbstractAuthenticationModuleRunner.class.getSimpleName();
    protected final URL baseURL;
    protected final String enrollEndpoint;
    protected final URL enrollURL;
    protected final Provider<HttpProvider> httpProviderFactory = new HttpProviderFactory();
    protected final String loginEndpoint;
    protected final URL loginURL;
    protected final String logoutEndpoint;
    protected final URL logoutURL;
    protected final Integer timeout;

    /**
     * @param baseURL
     * @param config
     * @throws IllegalArgumentException if an endpoint can not be appended to
     * baseURL
     */
    public CookieAuthRunner(URL baseURL, AuthenticationConfig config) {
        this.baseURL = baseURL;
        this.loginEndpoint = config.getLoginEndpoint();
        this.logoutEndpoint = config.getLogoutEndpoint();
        this.enrollEndpoint = config.getEnrollEndpoint();

        this.loginURL = UrlUtils.appendToBaseURL(baseURL, loginEndpoint);
        this.logoutURL = UrlUtils.appendToBaseURL(baseURL, logoutEndpoint);
        this.enrollURL = UrlUtils.appendToBaseURL(baseURL, enrollEndpoint);

        this.timeout = config.getTimeout();

    }

    public URL getBaseURL() {
        return baseURL;
    }

    public URI getBaseURI() {
        try {
            return baseURL.toURI();
        } catch (URISyntaxException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public String getEnrollEndpoint() {
        return enrollEndpoint;
    }

    public String getLoginEndpoint() {
        return loginEndpoint;
    }

    public String getLogoutEndpoint() {
        return logoutEndpoint;
    }

    String buildLoginData(String username, String password) {
        JsonObject response = new JsonObject();
        response.addProperty("loginName", username);
        response.addProperty("password", password);
        return response.toString();
    }

    public HeaderAndBody onEnroll(final Map<String, String> userData) {
        HttpProvider provider = httpProviderFactory.get(enrollURL, timeout);
        String enrollData = new JSONObject(userData).toString();
        return provider.post(enrollData);
    }

    HeaderAndBody onLogin(String username, String password) {
        Map<String, String> loginData = new HashMap<String, String>(2);
        loginData.put(USERNAME_PARAMETER_NAME, username);
        loginData.put(PASSWORD_PARAMETER_NAME, password);
        return onLogin(loginData);
    }




    public HeaderAndBody onLogin(final Map<String, String> loginData) {
        HttpProvider provider = httpProviderFactory.get(loginURL, timeout);
        String loginRequest = new JSONObject(loginData).toString();
        return provider.post(loginRequest);
    }

    public void onLogout() {
        HttpProvider provider = httpProviderFactory.get(logoutURL, timeout);
        provider.post("");
    }


}
