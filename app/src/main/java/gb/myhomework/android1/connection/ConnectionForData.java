package gb.myhomework.android1.connection;

import android.util.Log;

import gb.myhomework.android1.Constants;
import gb.myhomework.android1.model.WeatherRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionForData {
    private static final String API_KEY_TEST = "18d59917d1f12ece27eea1a690b3f920";
    //private static final String API_KEY_TEST = BuildConfig.WEATHER_API_KEY;

    private static final String TAG = "HW WebBrowser" + ConnectionForData.class.getSimpleName();
    //private final String keyApi = Constants.API_KEY_TEST;
    private String langFormat;
    private String unitFormat;
    private OpenWeather openWeather;
    private float lat; // широта
    private float lon; // долгота

    WeatherCallback callback;
    public interface WeatherCallback{
        void callBackReturn(WeatherRequest weatherRequest);
    }

    public ConnectionForData (WeatherCallback callback) {
        this.callback = callback;
    }

    public void connection (String place, boolean languageRu, boolean formatMetric) {
        initRetorfit();
        setParameter(languageRu, formatMetric);
        requestRetrofit(place, unitFormat, langFormat, API_KEY_TEST);
        if (Constants.DEBUG) {
            Log.v(TAG, "connection with place " + place);
        }
    }

    private void initRetorfit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
        if (Constants.DEBUG) {
            Log.v(TAG, "initRetorfit");
        }
    }

    private void requestRetrofit(String place, String unitFormat, String langFormat, String API_KEY_TEST) {
        openWeather.loadWeather(place, unitFormat, langFormat, API_KEY_TEST)
                .enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                if (response.body() != null) {
                    // если результат есть
                    callback.callBackReturn(response.body());
                    if (Constants.DEBUG) {
                        Log.v(TAG, "response.body OK");
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherRequest> call, Throwable t) {
                // если результата нет
                if (Constants.DEBUG) {
                    Log.v(TAG, "Error");
                }
            }
        });
        if (Constants.DEBUG) {
            Log.v(TAG, "requestRetrofit");
        }
    }

    private void setParameter(boolean languageRu, boolean formatMetric){

        if(formatMetric){
            unitFormat = "metric";
        }else {
            unitFormat = "imperial";
        }

        if (languageRu) {
            langFormat = "ru";
        } else {
            langFormat = "en";
        }
    }
}
