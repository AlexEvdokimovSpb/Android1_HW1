package gb.myhomework.android1;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private int messageId = 0;
    public static final String TAG = "HW "+ FirebaseMessagingService.class.getSimpleName();
    private String tokenIs = "Token ";
    private String pushMessage = "Push Message";
    private String channelId = "2";

    public MyFirebaseMessagingService() {
    }

    //для обработки получаемого сообщения с сервера
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, remoteMessage.getNotification().getBody());
        String title = remoteMessage.getNotification().getTitle();
        if (title == null){
            title = pushMessage;
        }
        String text = remoteMessage.getNotification().getBody();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }

    //для получения нового токена
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, tokenIs + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
    }
}
