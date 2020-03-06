package com.karthick.loginnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String KEY_UN = "username";
    private static final String KEY_PW = "password";

    private EditText mUsername, mPassword;
    private Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login);

        mLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login) {
            Bundle bundle = new Bundle();

            bundle.putString(KEY_UN, mUsername.getText().toString());
            bundle.putString(KEY_PW, mPassword.getText().toString());

            Intent intent = new Intent(MainActivity.this, Authentication.class);

            intent.putExtras(bundle);
            startActivityForResult(intent, 201);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String result = data.getStringExtra(getResources().getString(R.string.message));
        String success = getResources().getString(R.string.success);
        String userNameNotFound = getResources().getString(R.string.username_not_found);
        String incorrectPassword = getResources().getString(R.string.incorrect_password);

        if (requestCode == 201) {
            if (result.equalsIgnoreCase(success)) {
                showNotification(result);

            } else if (result.equalsIgnoreCase(userNameNotFound)) {
                showNotification(result);

            } else if (result.equalsIgnoreCase(incorrectPassword)) {
                showNotification(result);
            }
        }

/*        int notificationId = 0;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(result)
                .setContentText(result);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "LN";
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "FIRST NOTIFICATION", notificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(notificationId, builder.build());*/
    }

    public void showNotification(String result) {
        int notificationId = 0;

        //SETTING CUSTOM MESSAGE AND ICON FOR SUCCESS,FAILURE CASES
        int drawable;
        String message;

        if (result.equals("Success")) {
            drawable = R.drawable.ic_smile;
            message = "Login successful";
        } else {
            drawable = R.drawable.ic_sad;
            message = "Login failed";
        }

        //BUILDING THE NOTIFICATION
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notify_001")
                .setSmallIcon(drawable)
                .setContentTitle(message)
                .setContentText(result)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        //DEFINING NOTIFICATION MANAGER
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //SETTING NOTIFICATION CHANNEL FOR ANDROID 0REO AND ABOVE VERSION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "LN";
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "AUTH_NOTIFICATION", notificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId(channelId);
        }

        notificationManager.notify(notificationId, builder.build());
    }
}
