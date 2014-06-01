package puncha.babyevents.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import puncha.babyevents.app.db.BabyEventModel;
import puncha.babyevents.app.db.BabyEventTypes;


public class BabyEventListViewAdaptor extends ArrayAdapter<BabyEventModel> {

    Object mLock;
    List<BabyEventModel> mData;
    Filter mFilter;

    public BabyEventListViewAdaptor(Context context, List<BabyEventModel> objects) {
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
    public void addAll(Collection<? extends BabyEventModel> collection) {
        super.addAll(collection);
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
            // Create another copy of original data
            List<BabyEventModel> events = new ArrayList<BabyEventModel>();
            synchronized (mLock) {
                for(int i = 0; i<BabyEventListViewAdaptor.super.getCount(); ++i)
                    events.add(BabyEventListViewAdaptor.super.getItem(i));
            }

            // Start filtering
            Date today = new Date();
            today.setHours(0);
            today.setMinutes(0);
            today.setSeconds(0);
            List<BabyEventModel> filteredEvents = new ArrayList<BabyEventModel>();
            for (int i = 0; i < events.size(); ++i) {
                BabyEventModel event = events.get(i);
                if (!event.date().before(today)) { // after or equal
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
            //if (mData.size() > 0)
                notifyDataSetChanged();
//            else
//                notifyDataSetInvalidated();
        }
    }

}
