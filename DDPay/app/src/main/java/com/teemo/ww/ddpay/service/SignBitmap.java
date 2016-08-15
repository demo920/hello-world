package com.teemo.ww.ddpay.plug.service;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by George on 15/12/2.
 */
public class SignBitmap implements Parcelable {

    private Bitmap mBitmap;

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public SignBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    protected SignBitmap(Parcel in) {
        this.mBitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<SignBitmap> CREATOR = new Creator<SignBitmap>() {
        @Override
        public SignBitmap createFromParcel(Parcel in) {
            return new SignBitmap(in);
        }

        @Override
        public SignBitmap[] newArray(int size) {
            return new SignBitmap[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(mBitmap, PARCELABLE_WRITE_RETURN_VALUE);
    }
}
