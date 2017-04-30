package edu.ben.cropimagedialogfragment;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CropDialogFragment extends DialogFragment implements
        CropImageView.OnGetCroppedImageCompleteListener,
        CropImageView.OnSetImageUriCompleteListener,
        CropImageContract.CropImageResult,
        View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IS_DIALOG = "IsDialog";
    private CropImageView mCropImageView;
    private Uri mCropImageUri;
    private View mProgressView;
    private TextView mProgressViewText;
    private Button mCropImageBtn;
    private OnFragmentInteractionListener mListener;
    private AlertDialog.Builder mDialogBuilder;
    private ViewGroup mContainer;
    private boolean mIsDialog;

    public CropDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param isDialog set the fragment as a dialog.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CropDialogFragment newInstance(boolean isDialog) {
        CropDialogFragment fragment = new CropDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_DIALOG, isDialog);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsDialog = getArguments().getBoolean(ARG_IS_DIALOG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mIsDialog) {
            if (!getShowsDialog()) // AVOID REQUEST FEATURE CRASH
            {
                mContainer = container;
                return super.onCreateView(inflater, container, savedInstanceState);
            } else {
                // Inflate the layout for this Dialog fragment
                View view = inflater.inflate(R.layout.fragment_crop_dialog, container, false);
                intCropImageView(view);
                return view;
            }
        } else {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_crop_dialog, container, false);
            intCropImageView(view);
            return view;
        }

    }

    private void intCropImageView(View view) {
        if (mIsDialog) {
            WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
            wmlp.gravity = Gravity.CENTER;
        }
        mCropImageView = (CropImageView) view.findViewById(R.id.CropImageView);
        mProgressView = view.findViewById(R.id.ProgressView);
        mProgressViewText = (TextView) view.findViewById(R.id.ProgressViewText);
        mCropImageBtn = (Button) view.findViewById(R.id.btn_crop_image_fragment);
        mCropImageBtn.setOnClickListener(this);
        setInitialCropRect();
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnGetCroppedImageCompleteListener(this);
    }

    /**
     * Set the initial rectangle to use.
     */
    public void setInitialCropRect() {
        mCropImageView.setCropRect(new Rect(100, 300, 500, 1200));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CropImageCallback.getInstance().setOnCropImageCompletedListener(this);
        CropImage.startPickImageActivity(getActivity());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_crop_dialog, mContainer);

        mDialogBuilder.setView(view);
        mDialogBuilder.setTitle("crop image");

        return mDialogBuilder.create();
    }
    /*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onStart() {
        super.onStart();
        int dialogWidth;
        int dialogHeight;
        // safety check
        if (getDialog() != null) {
            dialogWidth = 1100; // specify a value here
            dialogHeight = 1200; // specify a value here
            getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mListener != null)
            mListener = null;
        mCropImageView.setOnSetImageUriCompleteListener(null);
        mCropImageView.setOnGetCroppedImageCompleteListener(null);
    }


    @Override
    public void onGetCroppedImageComplete(CropImageView view, Bitmap bitmap, Exception error) {
        mProgressView.setVisibility(View.INVISIBLE);
        if (error == null) {
            if (bitmap != null) {
                mCropImageView.setImageBitmap(bitmap);
            }
        } else {
            Log.e("Crop", "Failed to crop image", error);
            Toast.makeText(getContext(), "Something went wrong, try again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        mProgressView.setVisibility(View.INVISIBLE);
        if (error != null) {
            Log.e("Crop", "Failed to load image for cropping", error);
            Toast.makeText(getContext(), "Something went wrong, try again", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Crop the image and set it back to the  cropping view.
     */
    public void onCropTheImageClick(View view) {
        mCropImageView.getCroppedImageAsync();
        mProgressViewText.setText("Cropping...");
        mProgressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        onCropTheImageClick(v);
    }

    @Override
    public void onDialogResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                mCropImageUri = imageUri;
                mCropImageView.setImageUriAsync(mCropImageUri);
                mProgressViewText.setText("Loading...");
                mProgressView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDialogRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mCropImageView.setImageUriAsync(mCropImageUri);
            mProgressViewText.setText("Loading...");
            mProgressView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getContext(), "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
