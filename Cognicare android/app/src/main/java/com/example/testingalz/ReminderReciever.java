package com.example.testingalz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.util.Log;
import android.widget.Toast;

public class ReminderReciever extends BroadcastReceiver {

    private static final String TAG = "ReminderReciever";

    @Override
    public void onReceive(Context context, Intent intent) {
        String activity = intent.getStringExtra("activity");


        Log.d(TAG, "Alarm received for: " + activity);


        Ringtone ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        ringtone.play();


        Toast.makeText(context, "Time for: " + activity, Toast.LENGTH_LONG).show();
    }
}
