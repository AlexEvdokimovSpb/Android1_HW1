package gb.myhomework.android1;

import android.os.Handler;
import android.util.Log;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;
import javax.net.ssl.HttpsURLConnection;
import gb.myhomework.android1.model.WeatherRequest;

public class ConnectionForData {

    private static final String TAG = "HW WebBrowser" + ConnectionForData.class.getSimpleName();

    private BufferedReader in;
    private HttpsURLConnection urlConnection = null;

    private boolean languageRu = true;
    private boolean formatMetric = true;

    WeatherCallback callback;
    public interface WeatherCallback{
        void callBackReturn(WeatherRequest weatherRequest);
    }
    public ConnectionForData (WeatherCallback callback) {
        this.callback = callback;
    }

    public void connection (String place, boolean languageRu, boolean formatMetric) {
        this.languageRu = languageRu;
        this.formatMetric = formatMetric;

        try {
            String url = generateRequestToServer(place, languageRu, formatMetric);
            final URL uri = new URL(url);
            final Handler handler = new Handler();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        urlConnection = (HttpsURLConnection) uri.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setReadTimeout(10000);
                        BufferedReader in = new BufferedReader
                                (new InputStreamReader(urlConnection.getInputStream()));
                        String result = getLines(in);

                        Gson gson = new Gson();
                        final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);

                        if (callback instanceof WeatherCallback) {
                            callback.callBackReturn(weatherRequest);
                        } else {
                            throw new IllegalArgumentException("callback must implement  WeatherCallback");
                        }
                        if (Constants.DEBUG) {
                            Log.v(TAG, "result= " + result);
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "Fail connection", e);
                        e.printStackTrace();
                    } finally {
                        if (null != urlConnection) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
    }

    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    private String generateRequestToServer (String place, boolean languageRu, boolean formatMetric){
        this.languageRu = languageRu;
        this.formatMetric = formatMetric;
        String request;

        String startRequest = "https://api.openweathermap.org/data/2.5/weather?q=";

        String unitFormat;
        if(formatMetric){
            unitFormat = "&units=metric";
        }else {
            unitFormat = "&units=imperial";
        }

        String langFormat;
        if (languageRu) {
            langFormat = "&lang=ru";
        } else {
            langFormat = "&lang=en";
        }

        String endRequest = "&APPID=";

        request = startRequest + place + langFormat + unitFormat + endRequest + Constants.API_KEY_TEST;
        if (Constants.DEBUG) {
            Log.v(TAG, "request= " + request);
        }
        return request;
    }
}
