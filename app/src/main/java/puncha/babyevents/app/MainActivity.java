package puncha.babyevents.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener {

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
    }

    private boolean initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.actionBar_viewControls_main,
                android.R.layout.simple_spinner_dropdown_item);

        actionBar.setListNavigationCallbacks(spinnerAdapter, this);

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long id) {
        switch (itemPosition) {
            case 0:
                // Nothing to show. We are in the Events view now!
                break;
            case 1:
                Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, R.string.unknown_options, Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
