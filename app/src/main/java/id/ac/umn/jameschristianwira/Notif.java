package id.ac.umn.jameschristianwira;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notif extends Application {
    public static final String CHANNEL_1_ID = "Data";
    public static final String CHANNEL_2_ID = "Detail";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Data",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notification from DataActivity");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Detail",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notification from DetailActivity");

            NotificationManager manager2 = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel2);

        }
    }
}
