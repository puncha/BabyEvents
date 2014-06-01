package puncha.babyevents.app;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Date;

public class DateTimePickerDialog extends DialogFragment {

    public interface Callback {
        public void onDateTimeSet(Date date);
    }

    private int mTitleResId;
    private Date mDate;
    private Callback mCallback;

    DateTimePickerDialog(int titleResId, Date defaultDate, Callback callback) {
        mTitleResId = titleResId;
        mDate = defaultDate;
        mCallback = callback;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_date_time_picker, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(getString(mTitleResId));
        return dialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        InitDatePicker(view);
        InitTimePicker(view);
        InitActionButton(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void InitActionButton(View view) {
        Button button = (Button) view.findViewById(R.id.button_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onDateTimeSet(mDate);
                dismiss();
            }
        });
    }

    private void InitDatePicker(View view) {
        int year = mDate.getYear() + 1900;
        int month = mDate.getMonth();
        int day = mDate.getDate();

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                mDate.setYear(year - 1900);
                mDate.setMonth(month);
                mDate.setDate(day);
            }
        });
    }

    private void InitTimePicker(View view) {
        int hour = mDate.getHours();
        int minute = mDate.getMinutes();

        TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                mDate.setHours(hour);
                mDate.setMinutes(minute);
            }
        });
    }

}
