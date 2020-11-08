package gb.myhomework.android1;

import java.io.Serializable;

public class Parcel implements Serializable {

    private boolean theme;
    private boolean temperature;
    private boolean windSpeed;

    public boolean isTheme() {
        return theme;
    }
    public boolean isTemperature() {
        return temperature;
    }
    public boolean isWindSpeed() {
        return windSpeed;
    }

    public void setTemperature(boolean temperature) {
        this.temperature = temperature;
    }

    public void setTheme(boolean theme) {
        this.theme = theme;
    }

    public void setWindSpeed(boolean windSpeed) {
        this.windSpeed = windSpeed;
    }
}