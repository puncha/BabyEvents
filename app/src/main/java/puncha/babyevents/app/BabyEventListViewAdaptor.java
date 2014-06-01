package puncha.babyevents.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import puncha.babyevents.app.db.BabyEventModel;
import puncha.babyevents.app.db.BabyEventTypes;


public class BabyEventListViewAdaptor extends ArrayAdapter<BabyEventModel> {
    class ViewHolder {
        public TextView timeView;
        public TextView quantityView;
        public TextView typeView;
    }

    public BabyEventListViewAdaptor(Context context, List<BabyEventModel> objects) {
        super(context, R.layout.view_baby_event_item, R.id.time, objects);
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
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BabyEventModel item = getItem(position);
        viewHolder.timeView.setText(new SimpleDateFormat("HH:mm").format(item.date()));
        viewHolder.quantityView.setText(String.valueOf(item.quantity()));
        viewHolder.typeView.setText(BabyEventTypes.getResIdForType(item.type()));

        return convertView;
    }

}
