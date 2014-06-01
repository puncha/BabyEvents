package puncha.babyevents.app.db;

import java.text.SimpleDateFormat;
import java.util.Date;

import puncha.babyevents.app.R;
import puncha.babyevents.app.util.DateUtil;

public class BabyEventModel {
    private long mId;
    private int mType;
    private int mQuantity;
    private Date mDateTime;

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

    public void utcDate(Date val){
        this.mDateTime = val;
    }

    @Override
    public String toString() {
        String strDate = new SimpleDateFormat("MM-dd HH:mm").format(date());
        String strType = getStringforEventResId(BabyEventTypes.getResIdForType(type()));
        return String.format("%s\t %s: %dml", strDate, strType, quantity());
    }

    // TODO: Remove me once we show event without calling Model's toString()
    public static String getStringforEventResId(int resId){
        switch (resId) {
            case R.string.breast_feeding:
                return "喂母乳";
            case R.string.milk_feeding:
            return "喂配方奶";
            case R.string.change_nappy:
                return "换尿布";
        }
        return "";
    }
}
