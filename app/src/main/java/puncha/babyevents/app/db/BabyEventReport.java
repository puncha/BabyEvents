package puncha.babyevents.app.db;


public class BabyEventReport {
    private int mType;
    private long mQuantity;
    private String mDate;
    private long mCount;

    public BabyEventReport(int type, long quantity, long count, String date) {
        mType = type;
        mQuantity = quantity;
        mCount = count;
        mDate = date;
    }

    public int type() {
        return this.mType;
    }

    public long quantity() {
        return this.mQuantity;
    }

    public long count() { return mCount; }

    public String date() {
        return this.mDate;
    }

    @Override
    public String toString() {
        return String.format("%s\tType: %d;quantity: %d;count: %d", date(), type(), quantity(), count());
    }

}
