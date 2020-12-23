package gb.myhomework.android1;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import gb.myhomework.android1.connection.ConnectionForData;
import gb.myhomework.android1.database.App;
import gb.myhomework.android1.database.ResponseTheWeather;
import gb.myhomework.android1.database.WeatherDao;
import gb.myhomework.android1.database.WeatherSource;
import gb.myhomework.android1.model.Weather;
import gb.myhomework.android1.model.WeatherRequest;

public class MyIntentService extends IntentService implements ConnectionForData.WeatherCallback {

    public static final String TAG = "HW "+ MyIntentService.class.getSimpleName();

    private static final String EXTRA_SECONDS = "gb.myhomework.android1.MyIntentService.SECONDS";
    private static final String EXTRA_SECONDS_2 = "gb.myhomework.android1.MyIntentService.SECONDS_2";
    private static final String EXTRA_SECONDS_3 = "gb.myhomework.android1.MyIntentService.SECONDS_3";
    static final String EXTRA_RESULT_DESCRIPTION = "RESULT_DESCRIPTION"; // описание погоды
    static final String EXTRA_RESULT_ICON = "RESULT_ICON"; // иконка погоды
    static final String EXTRA_RESULT_PLACE = "RESULT_PLACE"; // место
    static final String EXTRA_RESULT_T = "RESULT_T"; // температура
    static final String EXTRA_RESULT_T_FEELS = "RESULT_T_FEELS"; // температура ощущается как
    private static final String CHANNEL_ID = "IntentService";
    private int messageId=0;
    private ConnectionForData connectionForData  = new ConnectionForData(this); // объект для связи с сервером погоды
    private WeatherRequest weatherRequest; // колбэк объекту для связи с сервером погоды
    private boolean languageRu = true;
    private boolean formatMetric = true;
    private final static int ID = 0;
    private WeatherSource weatherSource;



    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void callBackReturn(WeatherRequest weatherRequest) {
        this.weatherRequest = weatherRequest;
        if (Constants.DEBUG) {
            Log.v(TAG, "callBackReturn weatherRequest=" +weatherRequest);
        }
    }

    public static void startMyIntentService(Context context, String newPlace,
                                            boolean formatMetric, boolean languageRu) {

        Intent intent = new Intent(context, MyIntentService.class);
        intent.putExtra(EXTRA_SECONDS, newPlace);
        intent.putExtra(EXTRA_SECONDS_2, formatMetric);
        intent.putExtra(EXTRA_SECONDS_3, languageRu);
        context.startService(intent);
        if (Constants.DEBUG) {
            Log.v(TAG, "startMyIntentService " + newPlace);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String newPlace = intent.getStringExtra(EXTRA_SECONDS);
        formatMetric = intent.getBooleanExtra(EXTRA_SECONDS_2, formatMetric);
        languageRu = intent.getBooleanExtra(EXTRA_SECONDS_3, languageRu);
        if (Constants.DEBUG) {
            Log.v(TAG, "onHandleIntent newPlace=" + newPlace);
        }
        String[] infoWeather = resultConnectionForData(newPlace);
        sendBrodcast(infoWeather);
        if (Constants.DEBUG) {
            Log.v(TAG, "onHandleIntent numberTemperature=" + infoWeather[3]);
        }
    }

    @Override
    public void onCreate() {
        makeNote("onCreate");
        super.onCreate();
        if (Constants.DEBUG) {
            Log.v(TAG, "onCreate "+connectionForData);
        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (Constants.DEBUG) {
            Log.v(TAG, "onStartCommand ");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean stopService(Intent name) {
        if (Constants.DEBUG) {
            Log.v(TAG, "stopService ");
        }
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        makeNote("onDestroy");
        super.onDestroy();
        if (Constants.DEBUG) {
            Log.v(TAG, "onDestroy ");
        }
    }

    // уведомление в строке состояния
    private void makeNote(String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Main service notification")
                .setContentText(message);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }

    private String[] resultConnectionForData (String newPlace) {
        connectionForData.connection(newPlace, languageRu, formatMetric);
        String[] infoWeather = new String[5];
        while (weatherRequest == null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if (System.in.available() !=0) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (weatherRequest != null) {
            Weather[] weather = weatherRequest.getWeather();
            infoWeather[0] = weather[ID].getDescription();
            infoWeather[1] = weather[ID].getIcon();
            infoWeather[2] = weatherRequest.getName();
            infoWeather[3] = Float.toString(weatherRequest.getMain().getTemp());
            infoWeather[4] = Float.toString(weatherRequest.getMain()
                    .getFeels_like());

            // вставляем данные из запроса в базу данных
            WeatherDao weatherDao = App.getInstance().getWeatherDao();
            weatherSource = new WeatherSource(weatherDao);
            ResponseTheWeather responseTheWeather = new ResponseTheWeather();
            responseTheWeather.place = weatherRequest.getName();
            responseTheWeather.temperature = weatherRequest.getMain().getTemp();
            responseTheWeather.feelsTemperature = weatherRequest.getMain()
                    .getFeels_like();
            // добавляем дату и время запроса
            responseTheWeather.dateAndTime = String.valueOf(LocalDateTime.now());
            weatherSource.addResponseTheWeather(responseTheWeather);

            if (Constants.DEBUG) {
                Log.v(TAG, "resultConnectionForData " + infoWeather[3] + infoWeather[2]);
            }
        } else {
            if (Constants.DEBUG) {
                Log.v(TAG, "weatherRequest = null");
            }
        }
        if (Constants.DEBUG) {
            Log.v(TAG, "resultConnectionForData");
        }
        return infoWeather;
    }

    // Отправка уведомления о завершении сервиса и передача полученных данных
    private void sendBrodcast (String[] infoWeather) {
        Intent broadcastIntent = new Intent(MainFragment.BROADCAST_WEATHER);
        broadcastIntent.putExtra(EXTRA_RESULT_DESCRIPTION, infoWeather[0]);
        broadcastIntent.putExtra(EXTRA_RESULT_ICON, infoWeather[1]);
        broadcastIntent.putExtra(EXTRA_RESULT_PLACE, infoWeather[2]);
        broadcastIntent.putExtra(EXTRA_RESULT_T, infoWeather[3]);
        broadcastIntent.putExtra(EXTRA_RESULT_T_FEELS, infoWeather[4]);
        sendBroadcast(broadcastIntent);
        if (Constants.DEBUG) {
            Log.v(TAG, "sendBrodcast "+ infoWeather[3]);
        }
    }

    private void setParameter(boolean languageRu, boolean formatMetric){
        this.languageRu = languageRu;
        this.formatMetric = formatMetric;
    }
}
