package org.jboss.aeropad.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.jboss.aerogear.android.unifiedpush.PushConstants;
import org.jboss.aerogear.android.unifiedpush.Registrations;

public class WebsocketMessageReceiver extends BroadcastReceiver  {
    public static final String NAME = "org.jboss.aerogear.android.unifiedpush.WebsocketMessageReceiver";
    public static final String TOPIC = "org.jboss.aerogear.android.unifiedpush.WebsocketMessageReceiver.TOPIC";
    public static final String PAYLOAD = "org.jboss.aerogear.android.unifiedpush.WebsocketMessageReceiver.PAYLOAD";
    @Override
    public void onReceive(Context context, Intent intent) {
        // notity all attached MessageHandler implementations:

        intent.putExtra(PushConstants.MESSAGE, true);
        Registrations.notifyHandlers(context, intent, null);
    }
}
