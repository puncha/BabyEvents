package puncha.babyevents.app.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import puncha.babyevents.app.util.DateUtil;

public class BabyEventDal extends DbDalBase {

    public static final String TableName = "Events";
    public static final String ColumnId = "id";
    public static final String ColumnType = "type";
    public static final String ColumnQuantity = "quantity";
    public static final String ColumnDateTime = "datetime";
    public static final String[] AllColumns = {ColumnId, ColumnType, ColumnQuantity, ColumnDateTime};

    public static final String WhereClause = "id=?";

    public BabyEventDal(DbConnection dbConnection) {
        super(dbConnection);
    }

    public static String createTableSql() {
        return
            "CREATE TABLE " + TableName + " (" +
                ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ColumnType + " INTEGER, " +
                ColumnQuantity + " INTEGER, " +
                ColumnDateTime + " DATETIME" +
            ")";
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

    public boolean create(BabyEventModel event) {
        assert (event != null);
        ContentValues values = createContentValuesForEvent(event);
        long id = getDb().insert(TableName, null, values);
        event.id(id);
        return true;
    }

    public boolean update(BabyEventModel event) {
        assert (event != null);
        ContentValues values = createContentValuesForEvent(event);
        getDb().update(TableName, values, WhereClause, constructWhereClauseById(event));
        return true;
    }

    public boolean delete(BabyEventModel event) {
        getDb().delete(TableName, WhereClause, constructWhereClauseById(event));
        return true;
    }

    private ContentValues createContentValuesForEvent(BabyEventModel event) {
        ContentValues values = new ContentValues();
        values.put(ColumnType, event.type());
        values.put(ColumnQuantity, event.quantity());
        values.put(ColumnDateTime, DateUtil.ToSqliteAwareString(event.utcDate()));
        return values;
    }

    private String[] constructWhereClauseById(BabyEventModel event) {
        return new String[]{String.valueOf(event.id())};
    }
}
