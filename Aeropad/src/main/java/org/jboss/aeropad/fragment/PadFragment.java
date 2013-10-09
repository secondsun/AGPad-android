package org.jboss.aeropad.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jboss.aerogear.android.unifiedpush.MessageHandler;
import org.jboss.aerogear.android.unifiedpush.Registrations;
import org.jboss.aeropad.R;
import org.jboss.aeropad.socket.WebsocketMessageReceiver;

/**
 * Created by summers on 10/8/13.
 */
public class PadFragment extends Fragment implements MessageHandler {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.pad_layout, null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pad_menu, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        Registrations.registerMainThreadHandler(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Registrations.unregisterMainThreadHandler(this);
    }

    @Override
    public void onDeleteMessage(Context context, Bundle message) {

    }

    @Override
    public void onMessage(Context context, Bundle message) {
        Toast.makeText(getActivity(), message.getString(WebsocketMessageReceiver.PAYLOAD), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError() {
        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
    }
}
