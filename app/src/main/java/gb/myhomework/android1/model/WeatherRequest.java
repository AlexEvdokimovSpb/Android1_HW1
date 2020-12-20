package gb.myhomework.android1.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// пример ответа
// {"coord":{"lon":103.85,"lat":1.29},
// "weather":[{"id":500,"main":"Rain","description":"небольшой дождь","icon":"10n"}],
// "base":"stations",- не используем
// "main":{"temp":27.21,"feels_like":27.66,"temp_min":26.67,"temp_max":28,"pressure":1008,"humidity":74},
// "visibility":10000,
// "wind":{"speed":6.2,"deg":30},
// "rain":{"1h":0.65},
// "clouds":{"all":75},
// "dt":1608463013,- не используем
// "sys":{"type":1,"id":9470,"country":"SG","sunrise":1608418850,"sunset":1608462216},- не используем
// "timezone":28800,- не используем
// "id":1880252,- не используем
// "name":"Сингапур",
// "cod":200}- не используем

public class WeatherRequest {
    @SerializedName("coord")
    @Expose
    private Coord coord;
    @SerializedName("weather")
    @Expose
    private Weather[] weather;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("visibility")
    @Expose
    private int visibility;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("rain")
    @Expose
    private Rain rain;
    @SerializedName("clouds")
    @Expose
    private Clouds clouds;
    @SerializedName("name")
    @Expose
    private String name;

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}