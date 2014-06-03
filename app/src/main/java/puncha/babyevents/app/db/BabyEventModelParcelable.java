package puncha.babyevents.app.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class BabyEventModelParcelable implements Parcelable {

    public static final Creator<BabyEventModelParcelable> CREATOR =
            new Creator<BabyEventModelParcelable>() {
                @Override
                public BabyEventModelParcelable createFromParcel(Parcel parcel) {
                    return new BabyEventModelParcelable(parcel);
                }

                @Override
                public BabyEventModelParcelable[] newArray(int size) {
                    return new BabyEventModelParcelable[size];
                }
            };
    private BabyEventModel mEvent;

    public BabyEventModelParcelable(BabyEventModel event) {
        mEvent = event;
    }

    public BabyEventModel event() {
        return mEvent;
    }

    public BabyEventModelParcelable(Parcel parcel) {
        mEvent = new BabyEventModel();
        mEvent.id(parcel.readLong());
        mEvent.type(parcel.readInt());
        mEvent.quantity(parcel.readInt());
        mEvent.date(new Date(parcel.readLong()));
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        assert(mEvent != null);
        parcel.writeLong(mEvent.id());
        parcel.writeInt(mEvent.type());
        parcel.writeInt(mEvent.quantity());
        parcel.writeLong(mEvent.date().getTime());
    }

}
