package edu.ben.cropimagedialogfragment;

import android.support.annotation.Nullable;

public class CropImageCallback {

    CropImageContract.CropImageResult mCropListener;
    static CropImageCallback mCropImage;

    public static CropImageCallback getInstance() {
        mCropImage = new CropImageCallback();
        return mCropImage;
    }

    /**
     * Register a callback to be invoke when the Crop image is saved.
     * @param listener The callback that will run
     */
    public void setOnCropImageCompletedListener(@Nullable CropImageContract.CropImageResult listener) {
        mCropListener = listener;
    }

}
