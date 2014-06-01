package puncha.babyevents.app.db;

import puncha.babyevents.app.R;


public class BabyEventTypes {
    public static final int MILK_FEEDING = 1;
    public static final int BREAST_FEEDING = 2;
    public static final int CHANGE_NAPPY = 3;
    public static final int[] ALL_EVENTS = {
        MILK_FEEDING, BREAST_FEEDING, CHANGE_NAPPY
    };


    public static int getResIdForType(int eventType) {

        int resId = 0;

        switch (eventType) {
            case MILK_FEEDING:
                return R.string.milk_feeding;
            case BREAST_FEEDING:
                return R.string.breast_feeding;
            case CHANGE_NAPPY:
                return R.string.change_nappy;
            default:
                assert (false);
        }

        return resId;
    }
}
