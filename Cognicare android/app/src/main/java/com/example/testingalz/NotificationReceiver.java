package com.example.testingalz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String patientName = intent.getStringExtra("patientName");
        String date = intent.getStringExtra("appointmentDate");
        String time = intent.getStringExtra("appointmentTime");

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ScheduleAppointment.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_reminder) // Ensure this matches your drawable resource
                .setContentTitle("Appointment Reminder")
                .setContentText("You have an appointment scheduled with " + patientName + " on " + date + " at " + time)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Notify the user
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        } else {
            // Handle the case where the permission is not granted
            // For example, you can log an error or prompt the user to grant the permission
        }
    }
}
