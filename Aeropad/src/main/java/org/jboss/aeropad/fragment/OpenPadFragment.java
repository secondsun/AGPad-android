package org.jboss.aeropad.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.jboss.aerogear.android.unifiedpush.PushConfig;
import org.jboss.aeropad.AeropadApplication;
import org.jboss.aeropad.callback.LoginCallback;
import org.jboss.aeropad.R;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by summers on 10/7/13.
 */
public class OpenPadFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.loginlayout, null);
        Button editPadButton = (Button) view.findViewById(R.id.load_pad_button);
        editPadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PadFragment fragment = PadFragment.newInstance(((EditText)view.findViewById(R.id.pad_id)).getText().toString());
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.main, fragment).commit();

            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
