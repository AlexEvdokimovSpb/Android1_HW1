package gb.myhomework.android1.database;

import android.app.Application;

public class App extends Application {

    private static App instance;
    private WeatherDatabase  db;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        db = WeatherDatabase.createDb();
    }

    public WeatherDao getWeatherDao() {
        return db.getWeatherDao();
    }
}
