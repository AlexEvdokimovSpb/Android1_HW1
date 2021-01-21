package gb.myhomework.android1.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"PLACE"})})
public class ResponseTheWeather {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public long id;

    // описание погоды
    @ColumnInfo(name = "DESCRIPTION")
    public String description;

    // иконка
    @ColumnInfo(name = "ICON")
    public String icon;

    // место
    @ColumnInfo(name = "PLACE")
    public String place;

    // температура
    @ColumnInfo(name = "TEMPERATURE")
    public float temperature;

    // ощущается как
    @ColumnInfo(name = "FEEL")
    public float feelsTemperature;

    // дата и время
    @ColumnInfo(name = "DATE_AND_TIME")
    public String dateAndTime;
}
