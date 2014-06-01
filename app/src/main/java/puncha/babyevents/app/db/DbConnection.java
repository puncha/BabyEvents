package puncha.babyevents.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import puncha.babyevents.app.db.BabyEventDal;

public class DbConnection extends SQLiteOpenHelper {

    private static final String LogTag = "BabyEvents";
    private static final String DbName = "BodyEvents.db";
    private static final int DbVersion = 1;

    public DbConnection(Context context) {
        super(context, DbName, null, DbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = BabyEventDal.createTableSql();
        db.execSQL(createTableSql);
        Log.i(LogTag, "Events Table created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int fromVersion, int toVersion) {
    }
}
