package puncha.babyevents.app;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.List;

import puncha.babyevents.app.db.BabyEventDal;
import puncha.babyevents.app.db.BabyEventModel;
import puncha.babyevents.app.db.DbConnection;


public class MainActivity extends ListActivity {

    private DbConnection mDbConn;
    private BabyEventDal mEventDal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();
        initDb();
    }

    private boolean initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.actionBar_viewControls_main,
                android.R.layout.simple_spinner_dropdown_item);

        final MainActivity that = this;
        actionBar.setListNavigationCallbacks(spinnerAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long id) {
                switch (itemPosition) {
                    case 0:
                        // Nothing to show. We are in the Events view now!
                        break;
                    case 1:
                        Toast.makeText(that, R.string.not_implemented, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(that, R.string.unknown_options, Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        return true;
    }

    private void initDb() {
        mDbConn = new DbConnection(this);
        mEventDal = new BabyEventDal(mDbConn);
    }

    private boolean initData() {
        List<BabyEventModel> events = mEventDal.findAll();
        BabyEventListViewAdaptor adapter = new BabyEventListViewAdaptor(this, events);
        setListAdapter(adapter);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.breast_feeding:
            case R.id.milk_feeding:
                Intent intent = new Intent(this, FeedMilkDetailActivity.class);
                startActivity(intent);
                break;

            case R.id.change_nappy:
            case R.id.date_selection:
            case R.id.search:
            case R.id.action_settings:
                Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, R.string.unknown_options, Toast.LENGTH_SHORT);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDbConn.close();
    }

}
