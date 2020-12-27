package gb.myhomework.android1.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import gb.myhomework.android1.Constants;
import gb.myhomework.android1.R;

public class LowBatteryReceiver extends BroadcastReceiver{
    public static final String TAG = "HW "+ LowBatteryReceiver.class.getSimpleName();
    private int messageId = 0;
    private String problemBattery = "Problem Battery";
    private String lowBattery = "Low battery";
    private String channelId = "2";

    @Override
    public void onReceive(Context context, Intent intent) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(problemBattery)
                    .setContentText(lowBattery + " " + messageId);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(messageId++, builder.build());

        if (Constants.DEBUG) {
            Log.v(TAG, lowBattery);
        }
    }
}
