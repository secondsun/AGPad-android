package org.jboss.aeropad.callback;

import android.util.Log;
import android.widget.Toast;

import org.jboss.aerogear.android.pipeline.support.AbstractSupportFragmentCallback;
import org.jboss.aeropad.vo.Pad;

import java.util.List;

/**
 * Created by summers on 10/8/13.
 */
public class PadCallback extends AbstractSupportFragmentCallback<List<Pad>> {
    @Override
    public void onSuccess(List<Pad> pads) {
        Toast.makeText(getFragment().getActivity(), pads.get(0).getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(getFragment().getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        Log.e("PAD", e.getMessage(), e);
    }
}
