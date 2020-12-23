package gb.myhomework.android1.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ResponseTheWeather.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {
    private static final String DB_NAME = "weather_database.db";
    public abstract WeatherDao getWeatherDao();
    public static WeatherDatabase createDb(){
        return Room.databaseBuilder(App.getInstance(), WeatherDatabase.class, DB_NAME)
                .allowMainThreadQueries() //Только для примеров и тестирования.
                .build();
    }
}
