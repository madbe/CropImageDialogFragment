package edu.ben.cropimagedialogfragment;

import android.content.Intent;

interface CropImageContract {

    interface CropImageResult {

        void onDialogResult(int requestCode, int resultCode, Intent data);

        void onDialogRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults);
    }

    /*
         interface that will pass the saved Crop image to the ProfileDialog
    */
    interface OnCropImageCompleted {
        void onCropImageCompleted(String cropImagePath);
    }
}
