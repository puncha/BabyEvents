package puncha.babyevents.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import puncha.babyevents.app.fragment.EventFragment;
import puncha.babyevents.app.fragment.ReportFragment;


public class MainActivity extends Activity implements ActionBar.OnNavigationListener {

    FrameLayout mFragmentContainer;
    Fragment mEventFragment;
    Fragment mReportFragment;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initControls();
        initActionBar();
    }

    private void initControls() {
        mFragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
    }

    private Fragment getEventFragment() {
        if (mEventFragment == null)
            mEventFragment = new EventFragment();
        return mEventFragment;
    }

    private Fragment getReportFragment() {
        if (mReportFragment == null)
            mReportFragment = new ReportFragment();
        return mReportFragment;
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
        FragmentTransaction trans;
        switch (itemPosition) {
            case 0:
                String fragmentName = getEventFragment().getClass().toString();
                Log.i("TAG", fragmentName);
                trans = getFragmentManager().beginTransaction();
                trans.replace(mFragmentContainer.getId(), getEventFragment(), fragmentName);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                Fragment found = getFragmentManager().findFragmentByTag(fragmentName);
                trans.commit();
                break;
            case 1:
                Log.i("TAG", String.valueOf(getReportFragment().getId()));
                trans = getFragmentManager().beginTransaction();
                trans.replace(mFragmentContainer.getId(), getReportFragment());
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.commit();
                break;
            default:
                Toast.makeText(this, R.string.unknown_options, Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
