package org.jboss.aeropad.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.jboss.aeropad.AeropadApplication;
import org.jboss.aeropad.MainActivity;
import org.jboss.aeropad.R;
import org.jboss.aeropad.callback.HandlePads;
import org.jboss.aeropad.callback.PadCallback;
import org.jboss.aeropad.vo.Pad;

import java.util.List;

/**
 * Created by summers on 10/15/13.
 */
public class GetPadsListFragment extends DialogFragment implements HandlePads {

    View contentView;
    Activity activity;
    Context applicationContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.pads_list, null);
        ListView list = (ListView) contentView.findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pad pad = ((ArrayAdapter<Pad>)parent.getAdapter()).getItem(position);
                ((EditText)((MainActivity) activity).findViewById(R.id.pad_id)).setText(pad.get_id());
                dismiss();
            }
        });
        return contentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        this.applicationContext = activity.getApplicationContext();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AeropadApplication)activity.getApplication()).getPads(this, new PadCallback("All Pads"));
    }

    @Override
    public void handlePads(List<Pad> pads) {

        ListView list = (ListView) contentView.findViewById(R.id.listView);
        list.setAdapter(new ArrayAdapter<Pad>(applicationContext, android.R.layout.simple_list_item_1, pads));

    }
}
