package com.c323proj8.kmangrum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.EditText;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

//  Create broadcast receiver
public class MySMSReceiver extends BroadcastReceiver {

//    Instance of editText
    private static EditText editText_otp;

//  Method to set the edittext
    public void setEditText_otp(EditText editText) {
        MySMSReceiver.editText_otp = editText;
    }

//  Override onReceive method
    @Override
    public void onReceive(Context context, Intent intent) {
//        Get the messages
        SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
//        Create string that will be each listview item
        String smsDisplayString = "";
//        Loop through messages, parsing each one for the information we want to display
        for (SmsMessage smsMessage : smsMessages) {
            String message_body = smsMessage.getMessageBody();

//            When getting otp code, the format should be: Message Code:####
            String word_message = message_body.split("Code:")[0];

            String address = smsMessage.getOriginatingAddress();
            smsDisplayString = "SMS From: " + address + "\n" + " Message: " + word_message + "\n";
            String getOTP = message_body.split("Code:")[1];
            editText_otp.setText(getOTP);
        }

//        Update the listview in the SecondActivity
        SecondActivity instance = SecondActivity.instance();
        instance.updateList(smsDisplayString);
    }


}
