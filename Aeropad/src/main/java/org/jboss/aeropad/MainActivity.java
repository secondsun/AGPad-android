package org.jboss.aeropad;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import org.jboss.aeropad.fragment.LoginFragment;
import org.jboss.aeropad.fragment.PadFragment;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.main);
        getSupportFragmentManager().beginTransaction().add(R.id.main, new LoginFragment(), "login").commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    public void postLogin() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main, new PadFragment()).commit();
    }

    public void postLogout() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main, new LoginFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                ((AeropadApplication)getApplication()).logout(this);
                return true;
        }
        return false;
    }
}
