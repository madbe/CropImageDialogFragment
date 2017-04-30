package edu.ben.cropimagedialogfragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * On load image button click, start pick  image chooser activity.
     */
    public void onLoadImageClick(View view) {
        openMainDialogFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CropImageCallback.mCropImage.mCropListener != null)
            CropImageCallback.mCropImage.mCropListener.onDialogResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (CropImageCallback.mCropImage.mCropListener != null)
            CropImageCallback.mCropImage.mCropListener.onDialogRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openMainDialogFragment() {
        CropDialogFragment dialogFragment = CropDialogFragment.newInstance(true);
        dialogFragment.show(getSupportFragmentManager(), "CropImageFragment");
    }
}
