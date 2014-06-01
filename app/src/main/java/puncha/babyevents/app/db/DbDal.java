package puncha.babyevents.app.db;

import android.database.sqlite.SQLiteDatabase;

class DbDalBase {
    private DbConnection mDbConnection;

    public DbDalBase(DbConnection dbConnection) {
        mDbConnection = dbConnection;
    }

    public SQLiteDatabase getDb() {
        return mDbConnection.getWritableDatabase();
    }
}
