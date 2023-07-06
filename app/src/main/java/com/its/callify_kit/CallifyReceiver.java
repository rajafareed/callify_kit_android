package com.its.callify_kit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CallifyReceiver extends CallScreeningService {
    @Override
    public void onScreenCall(Call.Details details) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if(details.getCallDirection() == Call.Details.DIRECTION_INCOMING) {
                CallResponse.Builder response = new CallResponse.Builder();
                response.setDisallowCall(false);
                response.setRejectCall(false);
                response.setSilenceCall(false);
                response.setSkipCallLog(false);
                response.setSkipNotification(false);
                details.getHandle();
                respondToCall(details, response.build());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        SharedPreferences sp = getSharedPreferences("callify", MODE_PRIVATE);
                        String incomingNumber = sp.getString("senderNumber","");
                        String extracted = String.valueOf(details.getHandle()).substring(incomingNumber.length()-1);


                        if(extracted.equals(incomingNumber)){
                            Intent i = new Intent(getApplicationContext(),Success.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(i);
                        }


                    }
                },2000);
            }
        }
    }
}