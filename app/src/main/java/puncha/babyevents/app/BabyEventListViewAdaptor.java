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
    public BabyEventListViewAdaptor(Context context, List<BabyEventModel> objects) {
        super(context, R.layout.view_baby_event_item, R.id.time, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView timeEdit = (TextView) view.findViewById(R.id.time);
        TextView quantityEdit = (TextView) view.findViewById(R.id.quantity);
        TextView typeEdit = (TextView) view.findViewById(R.id.type);

        BabyEventModel item = getItem(position);
        timeEdit.setText(new SimpleDateFormat("HH:mm").format(item.date()));
        quantityEdit.setText(String.valueOf(item.quantity()));
        typeEdit.setText(BabyEventTypes.getResIdForType(item.type()));

        return view;
    }
}
