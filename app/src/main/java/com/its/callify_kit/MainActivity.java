package com.its.callify_kit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telecom.Call;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CallifyCallBack{
    CallifyService callifyService;
    String phoneNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


                 callifyService = new CallifyService(this);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_PHONE_STATE }, 001);
        }
        else {
            callifyService.missCallVerification("", "", "", MainActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(MainActivity.this, "Call Log Permission Granted", Toast.LENGTH_SHORT).show();
                callifyService.missCallVerification("", "", "", MainActivity.this);
            }
            else {
                Toast.makeText(MainActivity.this, "Call Log Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void callback(String response) {
        // Responses
        // REQUEST_FAILED
        // PERMISSION_ERROR
        // Sender Phone Number

        //Access In-Coming Call and use Sender Phone Number to validate




    }
}

