package org.jboss.aeropad;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.jboss.aerogear.android.unifiedpush.MessageHandler;
import org.jboss.aerogear.android.unifiedpush.Registrations;
import org.jboss.aeropad.fragment.AddPadFragment;
import org.jboss.aeropad.fragment.GetPadsListFragment;
import org.jboss.aeropad.fragment.OpenPadFragment;
import org.jboss.aeropad.fragment.PadFragment;
import org.jboss.aeropad.socket.WebsocketMessageReceiver;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.main, new OpenPadFragment(), "login").commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    public void postLogin() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main, new PadFragment()).commit();
    }

    public void postLogout() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main, new OpenPadFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_pad: {
                // DialogFragment.show() will take care of adding the fragment
                // in a transaction.  We also want to remove any currently showing
                // dialog, so make our own transaction and take care of that here.
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                AddPadFragment newFragment = new AddPadFragment();
                newFragment.show(ft, "dialog");
            }
                return true;
            case R.id.view_pads: {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                GetPadsListFragment newFragment = new GetPadsListFragment();
                newFragment.show(ft, "dialog");
            }
                return true;
        }
        return false;
    }





}
