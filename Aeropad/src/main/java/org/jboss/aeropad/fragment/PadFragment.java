package org.jboss.aeropad.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.pipeline.Pipe;
import org.jboss.aerogear.android.unifiedpush.MessageHandler;
import org.jboss.aerogear.android.unifiedpush.PushConfig;
import org.jboss.aerogear.android.unifiedpush.Registrations;
import org.jboss.aeropad.AeropadApplication;
import org.jboss.aeropad.R;
import org.jboss.aeropad.callback.HandlePads;
import org.jboss.aeropad.socket.WebsocketMessageReceiver;
import org.jboss.aeropad.util.PadMergeUtil;
import org.jboss.aeropad.vo.Pad;
import org.jboss.aeropad.vo.PadDiff;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.List;

/**
 * Created by summers on 10/8/13.
 */
public class PadFragment extends Fragment implements MessageHandler, HandlePads {
    private String padId;
    private View contentView;
    private Pipe<PadDiff> diffPipe;
    Pad pad, shadowPad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        this.padId = getArguments().getString("id");
        return contentView = inflater.inflate(R.layout.pad_layout, null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pad_menu, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        Registrations.registerMainThreadHandler(this);
        ((AeropadApplication)getActivity().getApplication()).pad(this, padId);
    }

    @Override
    public void onResume() {
        super.onResume();
        PushConfig pushConfig = new PushConfig();
        pushConfig.setPushServerURI(URI.create("ws://10.0.2.2:8080/pad/" + padId));
        ((AeropadApplication) getActivity().getApplication()).register(pushConfig);
    }

    @Override
    public void onPause() {
        super.onPause();
        Registrations.unregisterMainThreadHandler(this);
        ((AeropadApplication) getActivity().getApplication()).unregister();
    }

    @Override
    public void onDeleteMessage(Context context, Bundle message) {

    }

    @Override
    public void onMessage(Context context, Bundle message) {
        try {
            String json = message.getString(WebsocketMessageReceiver.PAYLOAD);
            JSONObject object = new JSONObject(json);
            if (object.has("diff")) {
                Log.d("DIFF", json);
                PadDiff diff = new PadDiff();
                diff.setDiff(object.getString("diff"));
                diff.setMd5(object.getString("md5"));
                if (!diff.getDiff().trim().isEmpty()) {
                    EditText textView = ((EditText) contentView.findViewById(R.id.pad_text));
                    int cursorStart = textView.getSelectionStart();
                    int cursorEnd = textView.getSelectionStart();
                    textView.removeTextChangedListener(diffSender);
                    PadDiff newDiff = PadMergeUtil.updateAndDiffShadow(pad, shadowPad, diff);
                    if (!newDiff.getDiff().trim().isEmpty()) {
                        diffPipe.save(newDiff, new Callback<PadDiff>() {
                            @Override
                            public void onSuccess(PadDiff data) {

                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.e("ERROR", e.getMessage(), e);
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    int cursorDelta = pad.getContent().length() - textView.getText().length();
                    ((EditText)contentView.findViewById(R.id.pad_text)).setText(pad.getContent());
                    if (cursorStart != cursorEnd) {
                        cursorStart = Math.min(pad.getContent().length(), Math.max(cursorStart + cursorDelta, 0));
                        cursorEnd = Math.min(pad.getContent().length(), Math.max(cursorEnd + cursorDelta, 0));
                        textView.setSelection(cursorStart, cursorEnd);
                    } else {
                        cursorStart = Math.min(pad.getContent().length(), Math.max(cursorStart + cursorDelta, 0));
                        textView.setSelection(cursorStart);
                    }
                    textView.addTextChangedListener(diffSender);

                }
            } else if (object.has("session_id")) {
                diffPipe = ((AeropadApplication)getActivity().getApplication()).padDiff(this, object.getString("session_id"));
            } else if (object.has("error")) {
                Log.e("ERROR", json);
                Toast.makeText(getActivity(), json, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), json, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.e("MESSAGE", e.getMessage(), e);
        }

    }

    @Override
    public void onError() {
        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
    }

    public static PadFragment newInstance(String padId) {
        Bundle bundle = new Bundle();
        bundle.putString("id", padId);
        PadFragment fragment = new PadFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void handlePads(List<Pad> pads) {
        EditText textView = ((EditText) contentView.findViewById(R.id.pad_text));
        textView.setText(pads.get(0).getContent());
        textView.addTextChangedListener(diffSender);
        pad = pads.get(0);
        shadowPad = new Pad();
        shadowPad.setContent(pad.getContent());




    }

    TextWatcher diffSender = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            pad.setContent(s.toString());
            PadDiff diff = PadMergeUtil.updateAndDiffShadow(pad, shadowPad);
            if (!diff.getDiff().isEmpty()) {
                diffPipe.save(diff, new Callback<PadDiff>() {
                    @Override
                    public void onSuccess(PadDiff data) {
                        //ignore
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("ERROR", e.getMessage(), e);
                    }
                });
            }
        }
    };

}
