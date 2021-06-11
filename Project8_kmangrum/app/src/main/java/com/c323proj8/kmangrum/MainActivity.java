package com.c323proj8.kmangrum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

//    Create variables for layout
    TextView tvOPTMessage;
    EditText etOTP;
    ImageView ivImage;
    Button playMusicButton, verifyOTPButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Set the variables to the layout elements
        tvOPTMessage = findViewById(R.id.tvOTPMessage);
        etOTP = findViewById(R.id.etOTP);
        ivImage = findViewById(R.id.ivImage);
        playMusicButton = findViewById(R.id.playMusicButton);
        verifyOTPButton = findViewById(R.id.verifyButton);
        ivImage.setImageResource(R.drawable.verifyicon);

//        Ask the user for permissions for their sms messages
        requestPermissions();

//        Set the editText to the otp Code
        new MySMSReceiver().setEditText_otp(etOTP);
    }

//    Method to ask the user for sms permission
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                    Manifest.permission.RECEIVE_SMS}, 100);
            }
        }

//        Method to start the music in the background
    public void playBackgroundMusic(View view) {
        Intent intent = new Intent(this, MyStarterService.class);
        startService(intent);
    }


//    On button click for verify and continue button
    public void verifyOTP(View view) {
        Intent i = new Intent(this, SecondActivity.class);
        startActivity(i);
    }


//      Method to receive intent from background thread
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("OTP")) {
                final String message = intent.getStringExtra("message");
                etOTP.setText(message);
            }
        }
    };


//    Override method for our receiver
    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("OTP"));
        super.onResume();
    }

//    Override method for our receiver
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

}