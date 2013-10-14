package org.jboss.aeropad.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jboss.aerogear.android.pipeline.support.AbstractSupportFragmentCallback;
import org.jboss.aeropad.AeropadApplication;
import org.jboss.aeropad.R;
import org.jboss.aeropad.vo.Pad;

/**
 * Created by summers on 10/11/13.
 */
public class AddPadFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.add_pad, null);
        Button addButton = (Button) v.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String padName = ((EditText)v.findViewById(R.id.pad_name)).getText().toString();
                ((AeropadApplication)getActivity().getApplication()).addPad(AddPadFragment.this, padName, new AddPadFragmentCallback());
            }
        });
        return v;
    }

    public static class AddPadFragmentCallback extends AbstractSupportFragmentCallback<Pad> {

        @Override
        public void onSuccess(Pad pad) {
            ((DialogFragment)getFragment()).dismiss();
        }

        @Override
        public void onFailure(Exception e) {
            Toast.makeText(getFragment().getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
