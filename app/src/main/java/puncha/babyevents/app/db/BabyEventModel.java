package puncha.babyevents.app.db;

import java.text.SimpleDateFormat;
import java.util.Date;

import puncha.babyevents.app.util.DateUtil;

public class BabyEventModel {
    private long mId;
    private int mType;
    private int mQuantity;
    private Date mDateTime;

    public BabyEventModel() {
        mId = -1;
        mType = -1;
        mQuantity = -1;
        date(new Date());
    }

    public static BabyEventModel createInstanceWithType(int eventType) {
        BabyEventModel event = new BabyEventModel();
        event.id(-1);
        event.type(eventType);
        event.quantity(0);
        return event;
    }

    public long id() {
        return this.mId;
    }

    public void id(long val) {
        this.mId = val;
    }

    public int type() {
        return this.mType;
    }

    public void type(int val) {
        this.mType = val;
    }

    public int quantity() {
        return this.mQuantity;
    }

    public void quantity(int val) {
        this.mQuantity = val;
    }

    public Date date() {
        return DateUtil.utc2Local(mDateTime);
    }

    public void date(Date val) {
        this.mDateTime = DateUtil.local2Utc(val);
    }

    public Date utcDate() {
        return mDateTime;
    }

    public void utcDate(Date val) {
        this.mDateTime = val;
    }

    @Override
    public String toString() {
        String strDate = new SimpleDateFormat("MM-dd HH:mm").format(date());
        return String.format("date: %s;Type: %d;quantity: %d;", strDate, type(), quantity());
    }
}
