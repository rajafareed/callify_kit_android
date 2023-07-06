package com.its.callify_kit;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Permission;

public class CallifyService implements Permission {

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    String senderPhone;
    private Handler.Callback onScoreSavedListener;
    CallifyCallBack callifyCallBack;

    public CallifyService(CallifyCallBack callifyCallBack) {
        this.callifyCallBack = callifyCallBack;
    }

    public void missCallVerification(String apiKey, String campaingID, String phoneNumber, Context ctx) {
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED ) {
           callifyCallBack.callback("PERMISSION_ERROR");
        } else {

            mRequestQueue = Volley.newRequestQueue(ctx);

            mStringRequest = new StringRequest(Request.Method.GET, "https://api.smsits.com/misscall?Apikey=" + apiKey + "&recipient=" + phoneNumber + "&campid=" + campaingID + "&uniqueId=123456",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.e("TAG", "onResponse: "+response );

                                JSONObject obj = new JSONObject(response);

                                if(obj.has("ErrorCode")  && obj.getString("ErrorCode").equals("400"))
                                {
                                    callifyCallBack.callback(obj.getString("ErrorMessage"));
                                }
                                else{
                                 senderPhone = obj.getString("Sender");

                                        callifyCallBack.callback(senderPhone);
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callifyCallBack.callback("REQUEST_FAILED");
                }
            });
            mRequestQueue.add(mStringRequest);

        }

    }

}
