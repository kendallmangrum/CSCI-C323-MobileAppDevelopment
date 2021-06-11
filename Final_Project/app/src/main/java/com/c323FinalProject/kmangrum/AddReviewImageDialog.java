package com.c323FinalProject.kmangrum;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddReviewImageDialog extends AppCompatDialogFragment{

//    Initialize variables that will be used later
    private Button bttnImageCamera;
    private Button bttnImageGallery;
    private ImageView ivReviewImage;
    Uri imageUri;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        Inflate layout for choosing an image dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.choose_image_input, null);

//        Get bttns and imageview
        bttnImageCamera = view.findViewById(R.id.bttnImageCamera);
        bttnImageGallery = view.findViewById(R.id.bttnImageGallery);
        ivReviewImage = view.findViewById(R.id.ivReviewlistItem);

//        Set an onclicklistener that lets the user take a picture with their camera
        bttnImageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasCamera()) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, 17);
                } else {
                    v.setEnabled(false);
                }
            }
        });

//        Set an onclicklistener that lets the user choose an image from their gallery
        bttnImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Select Image"), 1);
            }
        });

//        Determine actions for when the cancel and save button are pressed
        builder.setView(view).setTitle("Choose Image Upload Method")
//                if cancelled, give a default image
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ivReviewImage = getActivity().findViewById(R.id.ivReviewlistItem);
                        ivReviewImage.setImageResource(R.drawable.placeholder_image);
                    }
                })
//                If saved, set imageview to the selected image
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ivReviewImage = getActivity().findViewById(R.id.ivReviewlistItem);
                    }
                });

        return builder.create();
    }


//    Function that gets the image that the user took and sets the imageview to display that image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 17:
                    Bundle extra = data.getExtras();
                    Bitmap bitmap = (Bitmap) extra.get("data");
                    ivReviewImage = getActivity().findViewById(R.id.ivReviewlistItem);
                    ivReviewImage.setImageBitmap(bitmap);
                    break;

                case 1:
                    imageUri = data.getData();
                    try {
                        Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        ivReviewImage = getActivity().findViewById(R.id.ivReviewlistItem);
                        ivReviewImage.setImageBitmap(bitmap2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }


//    Function to see if the user's device has a camera
    private boolean hasCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
            return true;
        else
            return false;
    }

}
