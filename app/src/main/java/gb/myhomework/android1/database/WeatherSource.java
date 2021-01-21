package gb.myhomework.android1.database;

import java.util.List;

public class WeatherSource {

    private final WeatherDao weatherDao;

    private List<ResponseTheWeather> responseTheWeathers;

    public WeatherSource(WeatherDao weatherDao){
        this.weatherDao = weatherDao;
    }

    public List<ResponseTheWeather> getResponseTheWeathers(){
        if (responseTheWeathers == null){
            LoadResponseTheWeathers();
        }
        return responseTheWeathers;
    }

    public void LoadResponseTheWeathers(){
        responseTheWeathers = weatherDao.getAllResponseTheWeather();
    }

    public long getCountResponseTheWeathers(){
        return weatherDao.getCountResponseTheWeather();
    }

    public void addResponseTheWeather(ResponseTheWeather responseTheWeather){
        weatherDao.insertResponse(responseTheWeather);
        LoadResponseTheWeathers();
    }

    public void updateResponseTheWeather(ResponseTheWeather responseTheWeather){
        weatherDao.updateResponse(responseTheWeather);
        LoadResponseTheWeathers();
    }

    public void removeResponseTheWeather(long id){
        weatherDao.deleteResponseById(id);
        LoadResponseTheWeathers();
    }
}
