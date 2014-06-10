package puncha.babyevents.app.fragment;

import android.app.DatePickerDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import puncha.babyevents.app.BabyEventDetailActivity;
import puncha.babyevents.app.BabyEventListViewAdapter;
import puncha.babyevents.app.R;
import puncha.babyevents.app.db.BabyEventDal;
import puncha.babyevents.app.db.BabyEventModel;
import puncha.babyevents.app.db.BabyEventModelParcelable;
import puncha.babyevents.app.db.BabyEventTypes;
import puncha.babyevents.app.db.DbConnection;
import puncha.babyevents.app.util.DateUtil;

public class EventFragment extends ListFragment {
    private DbConnection mDbConn;
    private BabyEventDal mEventDal;
    private Date mDateFilter;
    private BabyEventListViewAdapter mAdapter;
    private MenuItem mDateFilterMenuItem;

    public EventFragment() {
        mDateFilter = new Date();
        DateUtil.setTimeToZeroClock(mDateFilter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initContextMenu();
        initDb();
    }


    private void initContextMenu() {
        setHasOptionsMenu(true);
        assert (getListView() != null);
        getListView().setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.setHeaderTitle(getString(R.string.event_operation));
                getActivity().getMenuInflater().inflate(R.menu.ctx_main, contextMenu);
            }
        });
    }

    private void initDb() {
        mDbConn = new DbConnection(getActivity());
        mEventDal = new BabyEventDal(mDbConn);
    }

    private void refreshFilterText() {
        mAdapter.getFilter().filter(new SimpleDateFormat("yyyy-MM-dd").format(mDateFilter));
    }

    private void refreshData() {
        List<BabyEventModel> events = mEventDal.findAll();
        mAdapter = new BabyEventListViewAdapter(getActivity(), events);
        setListAdapter(mAdapter);
        refreshFilterText();
    }

    private void refreshMenuText() {
        if (mDateFilterMenuItem != null)
            mDateFilterMenuItem.setTitle(new SimpleDateFormat("MM-dd").format(mDateFilter));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        mDateFilterMenuItem = menu.findItem(R.id.date_selection);
        refreshMenuText();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        int iBabyEventType = OptionItemIdToEventType.getType(id);
        boolean isBabyEvent = (iBabyEventType != -1);
        if (isBabyEvent)
            onBabyEventOptionItemsClicked(iBabyEventType);
        else
            onNonEventOptionsItemsClicked(id);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_event:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                assert (info != null);
                BabyEventModel event = getEventByPosition(info.position);
                assert (event != null);
                mEventDal.delete(event);
                refreshData();
                return true;
        }


        return super.onContextItemSelected(item);
    }

    private BabyEventModel getEventByPosition(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        BabyEventModel event = getEventByPosition(position);
        assert (event != null);
        startEventItemDetailedActivity(event);
        super.onListItemClick(listView, view, position, id);
    }

    private void onBabyEventOptionItemsClicked(int eventType) {
        BabyEventModel event = BabyEventModel.createInstanceWithType(eventType);
        startEventItemDetailedActivity(event);
    }

    void startEventItemDetailedActivity(BabyEventModel event) {
        Intent intent = new Intent(getActivity(), BabyEventDetailActivity.class);
        intent.putExtra(BabyEventModel.class.toString(), new BabyEventModelParcelable(event));
        startActivity(intent);
    }

    private void onNonEventOptionsItemsClicked(int id) {
        Context context = getActivity();

        switch (id) {
            case R.id.date_selection:
                onDataSelectionOptionItemClicked(context);
                break;

            case R.id.action_settings:
                Toast.makeText(context, R.string.not_implemented, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, R.string.unknown_options, Toast.LENGTH_SHORT);
                break;
        }
    }

    private void onDataSelectionOptionItemClicked(Context that) {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mDateFilter.setYear(year - 1900);
                mDateFilter.setMonth(month);
                mDateFilter.setDate(day);
                refreshMenuText();
                refreshFilterText();
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(
                that, listener, mDateFilter.getYear() + 1900, mDateFilter.getMonth(), mDateFilter.getDate());
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDbConn.close();
    }


    // Helper class to map option item to event type
    private static class OptionItemIdToEventType {
        final static int[] ACTION_BUTTON_IDS = {
                R.id.breast_feeding,
                R.id.milk_feeding,
                R.id.change_nappy,
        };
        final static int[] EVENT_TYPE = {
                BabyEventTypes.BREAST_FEEDING,
                BabyEventTypes.MILK_FEEDING,
                BabyEventTypes.CHANGE_NAPPY
        };

        public static int getType(int actionBtnId) {
            int index = 0;
            for (int btnId : ACTION_BUTTON_IDS) {
                if (btnId == actionBtnId)
                    return EVENT_TYPE[index];
                ++index;
            }
            return -1;
        }
    }

}
