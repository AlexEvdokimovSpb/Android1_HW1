package gb.myhomework.android1;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class MyParcel implements Parcelable {

    private boolean theme;
    private boolean temperature;
    private boolean windSpeed;

    public MyParcel(boolean theme, boolean temperature, boolean windSpeed) {
        this.theme = theme;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected MyParcel(Parcel in) {
        theme = in.readBoolean();
        temperature = in.readBoolean();
        windSpeed = in.readBoolean();
    }

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

    public static final Creator<MyParcel> CREATOR = new Creator<MyParcel>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public MyParcel createFromParcel(android.os.Parcel in) {
            return new MyParcel(in);
        }

        @Override
        public MyParcel[] newArray(int size) {
            return new MyParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeByte((byte) (theme ? 1 : 0));
        dest.writeByte((byte) (temperature ? 1 : 0));
        dest.writeByte((byte) (windSpeed ? 1 : 0));
    }
}