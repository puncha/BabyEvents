package puncha.babyevents.app.fragment;


import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;

import puncha.babyevents.app.R;
import puncha.babyevents.app.db.BabyEventDal;
import puncha.babyevents.app.db.BabyEventReport;
import puncha.babyevents.app.db.BabyEventTypes;
import puncha.babyevents.app.db.DbConnection;

public class ReportFragment extends ListFragment {
    private DbConnection mDbConn;
    private BabyEventDal mEventDal;
    private ArrayAdapter<BabyEventReport> mAdapter;

    public ReportFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDb();
    }

    private void initDb() {
        mDbConn = new DbConnection(getActivity());
        mEventDal = new BabyEventDal(mDbConn);
    }

    @SuppressLint("StringFormatMatches")
    private void refreshData() {
        List<BabyEventReport> reports = mEventDal.getEventReport();
        String[] items = new String[reports.size()];
        for (int i = 0; i < reports.size(); ++i) {
            BabyEventReport report = reports.get(i);
            String message = "";
            switch (report.type()) {
                case BabyEventTypes.BREAST_FEEDING:
                    message = String.format(getString(R.string.event_report_breast_feeding), report.quantity());
                    break;
                case BabyEventTypes.MILK_FEEDING:
                    message = String.format(getString(R.string.event_report_milk_feeding), report.quantity());
                    break;
                case BabyEventTypes.CHANGE_NAPPY:
                    message = String.format(getString(R.string.event_report_change_nappy), report.count());
                    break;
            }

            items[i] = String.format("%s - %s", report.date(), message);
        }

        mAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
        setListAdapter(mAdapter);
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

}
