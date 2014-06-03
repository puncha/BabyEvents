package puncha.babyevents.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import puncha.babyevents.app.db.BabyEventModel;
import puncha.babyevents.app.db.BabyEventTypes;
import puncha.babyevents.app.util.DateUtil;


public class BabyEventListViewAdapter extends ArrayAdapter<BabyEventModel> {

    final Object mLock;
    List<BabyEventModel> mData;
    Filter mFilter;

    public BabyEventListViewAdapter(Context context, List<BabyEventModel> objects) {
        super(context, R.layout.view_baby_event_item, R.id.time, objects);
        mLock = new Object();
        mData = objects;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null)
            mFilter = new EventTypeFilter();
        return mFilter;
    }

    @Override
    public BabyEventModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public int getPosition(BabyEventModel item) {
        return mData.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = super.getView(position, convertView, parent);
            viewHolder.timeView = (TextView) convertView.findViewById(R.id.time);
            viewHolder.quantityView = (TextView) convertView.findViewById(R.id.quantity);
            viewHolder.typeView = (TextView) convertView.findViewById(R.id.type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BabyEventModel item = getItem(position);
        viewHolder.timeView.setText(new SimpleDateFormat("HH:mm").format(item.date()));
        viewHolder.quantityView.setText(String.valueOf(item.quantity()));
        viewHolder.typeView.setText(BabyEventTypes.getResIdForType(item.type()));

        return convertView;
    }

    static class ViewHolder {
        public TextView timeView;
        public TextView quantityView;
        public TextView typeView;
    }

    private class EventTypeFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            // Keep a copy of original data so that we can release the lock then.
            List<BabyEventModel> events = new ArrayList<BabyEventModel>();
            synchronized (mLock) {
                for(int i = 0; i<BabyEventListViewAdapter.super.getCount(); ++i)
                    events.add(BabyEventListViewAdapter.super.getItem(i));
            }

            // Start filtering
            Date startDate, endDate;
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(charSequence.toString());
            }catch (ParseException exp){
                startDate = new Date();
                DateUtil.setTimeToZeroClock(startDate);
            }
            Calendar cale = Calendar.getInstance();
            cale.setTime(startDate);
            cale.add(Calendar.DATE, 1);
            endDate = cale.getTime();
            List<BabyEventModel> filteredEvents = new ArrayList<BabyEventModel>();
            for (int i = 0; i < events.size(); ++i) {
                BabyEventModel event = events.get(i);
                if (!event.date().before(startDate) && !event.date().after(endDate)) { // after or equal
                    filteredEvents.add(event);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredEvents;
            results.count = filteredEvents.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mData = (List<BabyEventModel>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
