package gb.myhomework.android1.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

import gb.myhomework.android1.Constants;
import gb.myhomework.android1.R;

public class NoNetworkReceiver extends BroadcastReceiver {
    public static final String TAG = "HW "+ NoNetworkReceiver.class.getSimpleName();
    private int messageId = 0;
    private String netReceiver = "Network Receiver";
    private String channelId = "2";
    private String netAvailable = "Network Available";
    private String netNotAvailable = "Network Not Available";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        // определяем доступность сети
        boolean isConnected = wifi != null && wifi.isConnectedOrConnecting() ||
                mobile != null && mobile.isConnectedOrConnecting();

        if (isConnected) {
            notificationBuilder (context, netAvailable);
            if (Constants.DEBUG) {
                Log.v(TAG, netAvailable);
            }
        } else {
            notificationBuilder (context, netNotAvailable);
            Toast.makeText(context, R.string.make_network_on, Toast.LENGTH_SHORT).show();
            if (Constants.DEBUG) {
                Log.v(TAG, netNotAvailable);
            }
        }
    }

    private  void notificationBuilder (Context context, String contentText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(netReceiver)
                .setContentText(contentText + " " + messageId);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }
}


