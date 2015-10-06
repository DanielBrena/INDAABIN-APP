package com.brena.ulsacommunity;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.brena.ulsacommunity.preferences.MyPreference;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Created by DanielBrena on 26/09/15.
 */
public class Aplicacion extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "dn3TwxV0DLPQofTbjxH6dq3NdBVd1YRRdoY4vwyI", "jHIWyxRgKQKDfxetP9Vmer6AqtmjufrkIf5xo3ag");
        ParseInstallation.getCurrentInstallation().saveInBackground();


        ParsePush.subscribeInBackground("INDAABIN");

        TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);


        if(!MyPreference.exist(getApplicationContext(),"deviceId")){


            MyPreference.add(getApplicationContext(), "deviceId", tm.getDeviceId());
        }



    }
}
