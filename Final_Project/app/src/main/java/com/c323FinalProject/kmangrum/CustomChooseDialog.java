package com.c323FinalProject.kmangrum;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.c323FinalProject.kmangrum.daos.CategoryDAO;
import com.c323FinalProject.kmangrum.entities.Category;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class CustomChooseDialog extends DialogFragment {

//    Initialize variables that will be used later
    private EditText edCategoryName;
    private ImageView ivCategoryImage;
    private Button bttnChooseImage;
    CategoryDAO categoryDAO;
    AppDatabase database;
    Uri imageUri;

//    Interface to handle when dialog is closed
    public interface CustomChooseDialogListener {
        void onDialogComplete();
    }

//    notify when dialog is closed
    public void sendResults() {
        CustomChooseDialogListener listener = (CustomChooseDialogListener) getTargetFragment();
        listener.onDialogComplete();
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlerts);
//      inflate layout for adding a category
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_category_layout, null);
//      Get database instance
        database = Room.databaseBuilder(getContext(), AppDatabase.class, "ReviewDatabase").allowMainThreadQueries().build();
        categoryDAO = database.getCategoryDAO();

//        Set up actions for when cancel and save are pressed
        builder.setView(view)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        sendResults();
                    }
                })
//                Update the database to include the new category
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Category newCategory = new Category();
                        newCategory.setCategoryName(edCategoryName.getText().toString());

                        BitmapDrawable categoryImage = (BitmapDrawable) ivCategoryImage.getDrawable();
                        Bitmap bitmapCategoryImage = categoryImage.getBitmap();

                        newCategory.setImage(getStringFromBitmap(bitmapCategoryImage));
//                      Insert the category into the database
                        categoryDAO.insert(newCategory);
                        Toast.makeText(getContext(), "Category Added!", Toast.LENGTH_SHORT).show();
                        sendResults();
                    }
                });

//        get the layout views
        edCategoryName = view.findViewById(R.id.edCategoryName);
        ivCategoryImage = view.findViewById(R.id.ivChooseCategory);
        bttnChooseImage = view.findViewById(R.id.bttnChooseImage);

//        Onclicklistener so the user can choose an image from their gallery
        bttnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery= new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Select Image"), 1);
            }
        });

        return builder.create();
    }

//    Override the method so that the user can choose an image from their gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                ivCategoryImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//  Function that converts image bitmap into a base64 string that can be stored in JSONobject and saved to file
    private String getStringFromBitmap(Bitmap bitmapPicture) {
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }
}
