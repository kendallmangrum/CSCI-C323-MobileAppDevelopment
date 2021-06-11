package com.c323midtermproject.kmangrum;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_APPEND;

public class CitiesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CitiesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CitiesFragment newInstance(String param1, String param2) {
        CitiesFragment fragment = new CitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

//    Initialize variables for the layout items that I will be getting/modifying/using
    ImageView cameraIcon;
    ImageView capturedImage;

    EditText etCityName;
    EditText etTimeToVisit;
    EditText etTouristSpots;
    EditText etLocation;

    Button add;
    Button cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cities, container, false);

//        Get all of the editTexts and imageviews using findViewById
        etCityName = v.findViewById(R.id.etCityName);
        etTimeToVisit = v.findViewById(R.id.etCityBestTimeToVisit);
        etTouristSpots = v.findViewById(R.id.etCityTouristSpots);
        etLocation = v.findViewById(R.id.etCityLocation);
        capturedImage = v.findViewById(R.id.ivPicture);
        cameraIcon = v.findViewById(R.id.ivCameraIcon);

//        Set onclick listener for camera icon so that the user can take a picture of location
        cameraIcon.setOnClickListener(new View.OnClickListener() {
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

//      Set an onlclick listener for the add button
        add = v.findViewById(R.id.addCityInformationButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Get all of the information from the editTexts and imageview that the user input
                String cityName = etCityName.getText().toString();
                String cityTimetoVisit = etTimeToVisit.getText().toString();
                String cityTouristSpot = etTouristSpots.getText().toString();
                String cityLocation = etLocation.getText().toString();
                BitmapDrawable cityImage = (BitmapDrawable) capturedImage.getDrawable();
                Bitmap bitmapCityImage = cityImage.getBitmap();


//              If the user hasn't input any information about the city and they click add, they are sent to the third activity
                if (cityName.equals("") && cityTimetoVisit.equals("") && cityTouristSpot.equals("") && cityLocation.equals("")) {
                    startActivity(new Intent(getActivity(), ThirdActivity.class));

//              If the user has input all of the required information, proceed with adding the city
                } else if (!cityName.equals("") && !cityTimetoVisit.equals("") && !cityTouristSpot.equals("") && !cityLocation.equals("")){

//                    Check to see if file already exists
                    File check = new File("data/data/com.c323midtermproject.kmangrum/files/cities");
                    JSONArray data;
                    if (check.exists()) {
//                        If it does exist, read the existing file and then add the new city object to the file
                        try {
                            data = new JSONArray(readJSONDataFromFile("cities"));

                            JSONObject city = new JSONObject();
                            city.put("Name", cityName);
                            city.put("Time", cityTimetoVisit);
                            city.put("Tourist", cityTouristSpot);
                            city.put("Location", cityLocation);
                            city.put("Image", getStringFromBitmap(bitmapCityImage));
                            data.put(city);
                            String dataListUpdated = data.toString();

                            File file = new File("data/data/com.c323midtermproject.kmangrum/files/cities");
                            FileWriter myFileWriter = new FileWriter(file);
                            BufferedWriter myBufferedWriter = new BufferedWriter(myFileWriter);
                            myBufferedWriter.write(dataListUpdated);
                            myBufferedWriter.close();

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

//                        If the file doesn't already exist, create the file and store the new city object there
                    } else {
                        data = new JSONArray();
                        JSONObject city  = new JSONObject();
                        try {
                            city.put("Name", cityName);
                            city.put("Time", cityTimetoVisit);
                            city.put("Tourist", cityTouristSpot);
                            city.put("Location", cityLocation);
                            city.put("Image", getStringFromBitmap(bitmapCityImage));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        data.put(city);

                        String text = data.toString();
                        FileOutputStream fos = null;
                        try {
                            fos = getContext().openFileOutput("cities", MODE_APPEND);
                            fos.write(text.getBytes());
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

//                  Provide a toast message that the data has been added and send the user back to the main/first activity
                    Toast.makeText(getActivity(), "Data has been added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            }
        });

//        Set on click Listener for the cancel button
        cancel = v.findViewById(R.id.cancelCityInformationButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Create an intent with the extra of which fragment it is coming from so that the proper file will be read in 3rd activity
                Intent i = new Intent(getActivity(), ThirdActivity.class);
                i.putExtra("FRAGMENT", "cities");
                startActivity(i);
            }
        });

        return v;
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
                    capturedImage = getView().findViewById(R.id.ivPicture);
                    capturedImage.setImageBitmap(bitmap);
                    break;
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

//    Function that reads the JSONArray from file
    public String readJSONDataFromFile(String fileName) throws IOException, JSONException {
        FileInputStream fis = getActivity().openFileInput(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuilder b = new StringBuilder();
        while (bis.available() != 0) {
            b.append((char) bis.read());
        }
        bis.close();
        bis.close();

        return b.toString();
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