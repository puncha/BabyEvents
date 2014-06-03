package puncha.babyevents.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import puncha.babyevents.app.db.BabyEventDal;
import puncha.babyevents.app.db.BabyEventModel;
import puncha.babyevents.app.db.BabyEventModelParcelable;
import puncha.babyevents.app.db.BabyEventTypes;
import puncha.babyevents.app.db.DbConnection;
import puncha.babyevents.app.dialog.DateTimePickerDialog;

public class BabyEventDetailActivity extends Activity {
    // Controls
    private Spinner mSpinner;
    private EditText mEditQuantity;
    private EditText mEditDateTime;

    // DB Stuff
    private DbConnection mDbConn;
    private BabyEventDal mEventDal;

    // Data
    private Date mSelectedDate;
    private BabyEventModel mEvent;

    public BabyEventDetailActivity(){
        mSelectedDate = new Date();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_event_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        initDb();
        initControls();
        refreshData();
    }

    private void initDb() {
        mDbConn = new DbConnection(this);
        mEventDal = new BabyEventDal(mDbConn);
    }

    private void initControls() {
        setupControlRefs();
        initControlData();
        hookupControlEvents();
    }

    private void setupControlRefs() {
        mSpinner = (Spinner) findViewById(R.id.spinner_type_selection);
        mEditDateTime = (EditText) findViewById(R.id.edit_date_time);
        mEditQuantity = (EditText) findViewById(R.id.edit_quantity);
    }

    private void initControlData() {
        assert (getIntent() != null);
        assert (getIntent().getExtras() != null);

        BabyEventModelParcelable parcelable =
                (BabyEventModelParcelable) getIntent().getExtras().get(BabyEventModel.class.toString());
        mEvent = parcelable.event();

        // Event Type Spinner
        // TODO: Improve me!
        Spinner spinner = (Spinner) findViewById(R.id.spinner_type_selection);
        ArrayList<String> eventTypes = new ArrayList();
        for(int eventType: BabyEventTypes.ALL_EVENTS) {
            String strEventType = getString(BabyEventTypes.getResIdForType(eventType));
            eventTypes.add(strEventType);
        }
        ArrayAdapter<CharSequence> adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, eventTypes);
        spinner.setAdapter(adaptor);
        int position = -1;
        for(int eventType: BabyEventTypes.ALL_EVENTS) {
            ++position;
            if (eventType == mEvent.type()) {
                spinner.setSelection(position, true);
            }
        }

        // Quantity EditBox
        mEditQuantity.setText(String.valueOf(mEvent.quantity()));

        // DateTimePicker
        mSelectedDate = mEvent.date();

        // Predefined Quantity Spinner
        final Context that = this;
        ImageButton button = (ImageButton) findViewById(R.id.button_predefined_quantity);
        final String[] quantities = getResources().getStringArray(R.array.predefined_quantities);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(that);
                builder.setItems(quantities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index) {
                        mEditQuantity.setText(quantities[index]);
                    }
                });
                builder.create().show();
            }
        });
    }

    private void hookupControlEvents() {
        mEditDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimePickerDialog dialog = new DateTimePickerDialog(
                        R.string.select_event_datetime, mSelectedDate, new DateTimePickerDialog.Callback() {
                    @Override
                    public void onDateTimeSet(Date date) {
                        mSelectedDate = date;
                        refreshData();
                    }
                });
                dialog.show(getFragmentManager(), getString(R.string.select_event_datetime));
            }
        });
    }

    private void refreshData() {
        mEditDateTime.setText(new SimpleDateFormat("MM-dd HH:mm").format(mSelectedDate));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.ok:
                boolean eventCreated = saveEvent();
                if (!eventCreated) {
                    // TODO: Report error!
                }
            case android.R.id.home:
                finish();
                break;

            default:
                Toast.makeText(this, R.string.unknown_options, Toast.LENGTH_SHORT);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean saveEvent() {
        getDataFromUI();
        return pushEventToDB();
    }

    private boolean pushEventToDB() {
        if (mEvent.id() != -1)
            mEventDal.update(mEvent);
        else
            mEventDal.create(mEvent);
        return true;
    }

    private void getDataFromUI() {
        // TODO: should we update the event when user makes changes in the UI immediately?
        mEvent.type(getSelectedEventType());
        mEvent.date(mSelectedDate);
        mEvent.quantity(Integer.valueOf(mEditQuantity.getText().toString()));
    }

    private int getSelectedEventType() {
        int position = mSpinner.getSelectedItemPosition();
        return BabyEventTypes.ALL_EVENTS[position];
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDbConn.close();
    }

}
