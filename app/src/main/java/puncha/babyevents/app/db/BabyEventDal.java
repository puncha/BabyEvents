package puncha.babyevents.app.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import puncha.babyevents.app.util.DateUtil;

public class BabyEventDal extends DbDalBase {

    public static final String TableName = "Events";
    public static final String ColumnId = "id";
    public static final String ColumnType = "type";
    public static final String ColumnQuantity = "quantity";
    public static final String ColumnDateTime = "datetime";

    public static final String[] AllColumns = {ColumnId, ColumnType, ColumnQuantity, ColumnDateTime};

    public BabyEventDal(DbConnection dbConnection) {
        super(dbConnection);
    }

    public static String createTableSql(){
        return
            "CREATE TABLE " + TableName + " (" +
                ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ColumnType + " INTEGER, " +
                ColumnQuantity + " INTEGER, " +
                ColumnDateTime + " DATETIME" +
            ")";
    }

    public BabyEventModel create(BabyEventModel babyEventModel) {
        ContentValues values = new ContentValues();
        values.put(ColumnType, babyEventModel.type());
        values.put(ColumnQuantity, babyEventModel.quantity());
        values.put(ColumnDateTime, DateUtil.ToSqliteAwareString(babyEventModel.utcDate()));
        long id = getDb().insert(TableName, null, values);
        babyEventModel.id(id);
        return babyEventModel;
    }

    public List<BabyEventModel> findAll() {
        List<BabyEventModel> babyEventModels = new ArrayList<BabyEventModel>();

        Cursor cursor = getDb().query(TableName, AllColumns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            BabyEventModel babyEventModel = new BabyEventModel();

            babyEventModel.id(cursor.getLong(cursor.getColumnIndex(ColumnId)));
            babyEventModel.quantity(cursor.getInt(cursor.getColumnIndex(ColumnQuantity)));
            babyEventModel.type(cursor.getInt(cursor.getColumnIndex(ColumnType)));
            // datetime
            String dateFromDb = cursor.getString(cursor.getColumnIndex(ColumnDateTime));
            babyEventModel.utcDate(DateUtil.FromSqliteAwareString(dateFromDb));

            babyEventModels.add(babyEventModel);
            Log.i("BabyEvents", babyEventModel.toString());
        }

        return babyEventModels;
    }
}
