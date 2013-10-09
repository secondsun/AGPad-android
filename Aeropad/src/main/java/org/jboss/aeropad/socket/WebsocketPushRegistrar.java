package org.jboss.aeropad.socket;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.unifiedpush.PushConfig;
import org.jboss.aerogear.android.unifiedpush.PushConstants;
import org.jboss.aerogear.android.unifiedpush.PushRegistrar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by summers on 10/8/13.
 */
public class WebsocketPushRegistrar extends WebSocketClient implements PushRegistrar  {

    private static final String TAG = WebsocketPushRegistrar.class.getSimpleName();
    final PushConfig config;
    final Context appContext;
    Callback<Void> connectCallback, disconnectCallback;

    public WebsocketPushRegistrar(PushConfig config, Context context, Map<String, String> cookieMap) {
        super(config.getPushServerURI(), new Draft_10(), cookieMap, 0);
        this.config = config;
        this.appContext = context.getApplicationContext();
    }


    @Override
    public void register(Context context, Callback<Void> callback) {
        connectCallback = callback;

        connect();
    }

    @Override
    public void unregister(Context context, Callback<Void> callback) {
        disconnectCallback = callback;
        close();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        signalSuccess();
    }

    @Override
    public void onMessage(String s) {
        Log.d("WS Reg", s);
        Intent message = new Intent(WebsocketMessageReceiver.NAME);
        message.putExtra(WebsocketMessageReceiver.TOPIC, config.getCategory());
        message.putExtra(WebsocketMessageReceiver.PAYLOAD, s);
        appContext.sendBroadcast(message);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        signalSuccess();
    }

    @Override
    public void onError(Exception e) {
        signalError(e);
    }

    private void signalError(Exception e) {
        if (disconnectCallback == null && connectCallback == null ) {
            Log.e(TAG, e.getMessage(), e);
            Intent message = new Intent(WebsocketMessageReceiver.NAME);
            message.putExtra(PushConstants.ERROR, e);
            appContext.sendBroadcast(message);
        } else {
            if (connectCallback != null) {
                connectCallback.onFailure(e);
                connectCallback = null;
            }

            if (disconnectCallback != null) {
                disconnectCallback.onFailure(e);
                disconnectCallback = null;
            }
        }

    }


    private void signalSuccess() {
        if (connectCallback != null) {
            connectCallback.onSuccess(null);
            connectCallback = null;
        }
    }

}
