package gb.myhomework.android1.connection;
import gb.myhomework.android1.model.WeatherRequest;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {
        @GET("data/2.5/weather")
        Call<WeatherRequest> loadWeather(@Query("q") String cityCountry,
                                         @Query("units") String units,
                                         @Query("lang") String language,
                                         @Query("appid") String keyApi);
}
