package com.its.callify_kit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.role.RoleManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telecom.Call;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CallifyCallBack{
    CallifyService callifyService;

    Button btnVerification;
    String senderPhone;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callifyService = new CallifyService(this);
        btnVerification = findViewById(R.id.btnVerification);


        btnVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

    }

    void checkPermission(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.READ_CALL_LOG }, 001);
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
//                callifyService.missCallVerification("", "", "", MainActivity.this);

            }
            else {
                Toast.makeText(MainActivity.this, "Call Log Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private static final int REQUEST_ID = 1;

    @Override
    public void callback(String response) {
        // Responses

        //- REQUEST_FAILED
        //- PERMISSION_ERROR
        //- Sender Phone Number


        // Access In-Coming Call and use Sender Phone Number to validate


        senderPhone = response;
        sharedPreferences = getSharedPreferences("callify", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("senderNumber", senderPhone);
        editor.apply();


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            RoleManager roleManager = null;
            roleManager = (RoleManager) getSystemService(ROLE_SERVICE);
            Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING);
            startActivityForResult(intent, REQUEST_ID);


        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID) {
            if (resultCode == android.app.Activity.RESULT_OK) {


            } else {

            }
        }
    }
}

