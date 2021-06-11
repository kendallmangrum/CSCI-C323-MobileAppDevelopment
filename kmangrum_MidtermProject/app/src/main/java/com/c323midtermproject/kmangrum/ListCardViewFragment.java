package com.c323midtermproject.kmangrum;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_APPEND;


public class ListCardViewFragment extends Fragment implements GestureDetector.OnGestureListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListCardViewFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ListCardViewFragment newInstance(String param1, String param2) {
        ListCardViewFragment fragment = new ListCardViewFragment();
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

//    Create variables for ArrayList of destinations, arrayAdapter, and the Listview
    private ArrayList<String> destinations;
    private ArrayAdapter<String> myAdapter;
    ListView destinationlv;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_card_view, container, false);

//        Get the string of which fragment will have its information read/displayed
        final String selectedFragment = getArguments().getString("FRAGMENT");

//      Get the listview
        destinationlv = v.findViewById(R.id.defaultListView);

//        If destinations arraylist is null, create new Arraylist
        if (destinations == null) {
            destinations = new ArrayList<String>();
        }

//        Create the adapter using the default simple list, and provide destinations arraylist to populate it
        myAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, destinations);

//      Call method createListView that reads in the appropriate file and gets the name of each destination to display
        createListView(selectedFragment);

//      Get the names of the objects in destinations and add them into a string array
        final String[] names = new String[destinations.size()];
        for (int i = 0; i < destinations.size(); i++) {
            names[i] = destinations.get(i);
        }

//        Make the listview clickable so that we can have popup windown on click
        destinationlv.setClickable(true);

//      Set an itemOnClickListener so that we can click on listview item and have corresponding popup window appear
        destinationlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
//                    call popup window method
                    onShowPopupWindowClick(view, selectedFragment, position);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }



//  Function that gets the name of each destination from jsonObject in file, sets adapter, and then displays
    public void createListView(String selectedFragment) {
        try {
            String names = readJSONDataFromFileForListView(selectedFragment);
            System.out.println(names);
            String[] eachName = names.split("\n");
            for (String name : eachName) {
                destinations.add(name);
            }
            destinationlv.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


// Function for creating the popup window
    public void onShowPopupWindowClick(View view, String selectedFragment, int position) throws IOException, JSONException {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.city_cardview_layout, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

//        Read in the json file
        JSONArray array = readInJSONArray(selectedFragment);
        final JSONObject object = array.getJSONObject(position);

//      Gets each of the elements of popup window
        TextView tvName = popupView.findViewById(R.id.tv1);
        TextView tvVisit = popupView.findViewById(R.id.tv3);
        TextView tvTourist = popupView.findViewById(R.id.tv2);
        TextView tvPrice = popupView.findViewById(R.id.tv4);
        TextView tvLocation = popupView.findViewById(R.id.tv5);
        ImageView ivCapturedImage = popupView.findViewById(R.id.ivPicture);

//      Determine which file was read and set the various elements to the corresponding object fields
        if (selectedFragment.equals("cities")) {
            tvName.setText("Name: " + object.getString("Name"));
            tvVisit.setText("Best Time to Visit: " + object.getString("Time"));
            tvTourist.setText("Tourist Spot: " + object.getString("Tourist"));
            tvLocation.setText("Location: " + object.getString("Location"));
            ivCapturedImage.setImageBitmap(getBitmapFromString(object.getString("Image")));
        } else if (selectedFragment.equals("monuments")) {
            tvName.setText("Name: " + object.getString("Name"));
            tvVisit.setText("Best Time to Visit: " + object.getString("Time"));
            tvTourist.setText("History: " + object.getString("History"));
            tvPrice.setText("Ticket Price: " + object.getString("Price"));
            tvLocation.setText("Location: " + object.getString("Location"));
            ivCapturedImage.setImageBitmap(getBitmapFromString(object.getString("Image")));
        } else {
            tvName.setText("Name: " + object.getString("Name"));
            tvVisit.setText("Best Time to Visit: " + object.getString("Time"));
            tvTourist.setText("Nearest Metropolitan Location: " + object.getString("Metro"));
            tvLocation.setText("Location: " + object.getString("Location"));
            ivCapturedImage.setImageBitmap(getBitmapFromString(object.getString("Image")));
        }


        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


//        Get the addFavorites button from the popupview and add an onclickListener to it
        Button addFavoritesButton = popupView.findViewById(R.id.favoriteButton);
        addFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Check to see if the favorites file already exists
                File check = new File("data/data/com.c323midtermproject.kmangrum/files/favorites");
                JSONArray data;
                if (check.exists()) {
//                    If it does, read in the file
                    try {
                        data = new JSONArray(readJSONDataFromFile("favorites"));

//                        Add the newly favorited object to the jsonArray that was read in and convert array to string
                        data.put(object);
                        String dataListUpdated = data.toString();

//                        Write the updated array to the favorites file
                        File file = new File("data/data/com.c323midtermproject.kmangrum/files/favorites");
                        FileWriter myFileWriter = new FileWriter(file);
                        BufferedWriter myBufferedWriter = new BufferedWriter(myFileWriter);
                        myBufferedWriter.write(dataListUpdated);
                        myBufferedWriter.close();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

//                    If the favorites file doesn't exist yet, create a new JsonArray to store the obect and write it to favorites file
                } else {
                    data = new JSONArray();
                    data.put(object);
                    String text = data.toString();
                    FileOutputStream fos = null;
                    try {
                        fos = getContext().openFileOutput("favorites", MODE_APPEND);
                        fos.write(text.getBytes());
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                Display a toast to notify that the destination was added to favorites
            Toast.makeText(getContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
            }
        });

//        Get maps button and add an onclickListener for it
        Button showMapsButton = popupView.findViewById(R.id.displayMapButton);
        showMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Create instances of locationManager and LocationListener to get user's current location
                LocationManager myLocationManager;
                LocationListener myLocationListener;

                //        Start the location manager and make sure that location is enabled
                myLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    myLocationManager.isLocationEnabled();
                } else {

                }
                myLocationListener = new LocationListener() {

                    @Override
                    public void onProviderDisabled(@NonNull String provider) {
                        startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

//            Function to get the location
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
//                Get the latitude and longitude of device
                        double myLat = location.getLatitude();
                        double myLong = location.getLongitude();
                        String coords = "" + myLat + ","+ myLong;

                        try {
                            Intent i = new Intent(getContext(), MapsActivity.class);
//                            Store the name and location of the destination as well as the user's current location to pass to Maps activity
                            i.putExtra("NAME_LOCATION_COORDS", new String[] {object.getString("Name"), object.getString("Location"), coords});
                            getContext().startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

//        Check to see if we have permission to use the devices location, if no ask for permission. Otherwise request the current location of the user
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 13);
                    return;
                } else {
                    myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, myLocationListener);
                }

            }
        });

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

    }

//  Function to read in the jsonArray from file
    public JSONArray readInJSONArray(String fileName) throws IOException, JSONException {
        FileInputStream fis = getActivity().openFileInput(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuilder b = new StringBuilder();
        while (bis.available() != 0) {
            b.append((char) bis.read());
        }
        bis.close();
        bis.close();

        JSONArray data = new JSONArray(b.toString());
        return data;
    }

//  Function that reads in data from file and gets the name of each object
    public String readJSONDataFromFileForListView(String fileName) throws IOException, JSONException {
        FileInputStream fis = getActivity().openFileInput(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuilder b = new StringBuilder();
        while (bis.available() != 0) {
            b.append((char) bis.read());
        }
        bis.close();
        bis.close();

        JSONArray data = new JSONArray(b.toString());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            String stdName = data.getJSONObject(i).getString("Name");
            sb.append(stdName + "\n");
        }
        return sb.toString();
    }

//  Reads in whole file and converts JsonArray to a string
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


//    This Function converts the String back to Bitmap
    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }


    /*
    Here I would have a method that takes the boolean of isListview and determine what to do when it is true/false
    If it is true, I would inflate the default layout that I am using because it is already the desired listview.

    If it was false, I would instead inflate a layout of a listview consisting of cardviews. This would require me to
    use to use a custom adapter to properly display the layout.

    For either layout I would set a gesture detecture on each item and override its onfling method.
    For onFling, if the user swiped to the left, that item would be deleted from the listview as well as from
    the file that it was read from
     */




    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}