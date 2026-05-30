package com.arraiyan.sosguard;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

public class SOSBackgroundService extends Service {
    private static final String CHANNEL_ID = "SOS_Guard_Channel";
    private BluetoothAdapter bluetoothAdapter;
    // এখানে অফিশিয়াল ইমার্জেন্সি নম্বর ৯৯৯ সেট করা হলো
    private String emergencyNumber = "999";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            bluetoothAdapter = bluetoothManager.getAdapter();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("emergency_number")) {
            emergencyNumber = intent.getStringExtra("emergency_number");
        }

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Ar Raiyan SOS Guard")
                    .setContentText("Monitoring Smart Bracelet in background...")
                    .setSmallIcon(android.R.drawable.ic_menu_compass)
                    .build();
        }
        
        startForeground(1, notification);
        startBraceletMonitoring();

        return START_STICKY;
    }

    private void startBraceletMonitoring() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "SOS Guardian: Scanning for Bracelet...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Ar Raiyan SOS Guard Monitoring",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
