package gb.myhomework.android1.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertResponse(ResponseTheWeather responseTheWeather);

    @Update
    void updateResponse(ResponseTheWeather responseTheWeather);

    @Delete
    void deleteResponse(ResponseTheWeather responseTheWeather);

    @Query("DELETE FROM responseTheWeather WHERE id = :id")
    void deleteResponseById(long id);

    @Query("SELECT * FROM responseTheWeather")
    List<ResponseTheWeather> getAllResponseTheWeather();

    @Query("SELECT * FROM responseTheWeather WHERE id = :id")
    ResponseTheWeather getResponseById(long id);

    @Query("SELECT COUNT() FROM responseTheWeather")
    long getCountResponseTheWeather();

}
