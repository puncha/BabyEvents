package puncha.babyevents.app.provider;

import android.content.Context;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import puncha.babyevents.app.R;

public class DateSelectionProvider extends ActionProvider {
    private Context mContext;

    public DateSelectionProvider(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateActionView() {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.view_date_selection_widget, null);
        Button button = (Button) view.findViewById(R.id.button);
        DateFormat dateFormat = new SimpleDateFormat("MM月dd日");
        button.setText(dateFormat.format(new Date()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
