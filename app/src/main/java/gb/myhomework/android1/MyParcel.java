package gb.myhomework.android1;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class MyParcel implements Parcelable {

    private boolean theme;
    private boolean formatMetric;
    private boolean languageRu;

    public MyParcel(boolean theme, boolean formatMetric, boolean languageRu) {
        this.theme = theme;
        this.formatMetric = formatMetric;
        this.languageRu = languageRu;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected MyParcel(Parcel in) {
        theme = in.readBoolean();
        formatMetric = in.readBoolean();
        languageRu = in.readBoolean();
    }

    public boolean isTheme() {
        return theme;
    }
    public boolean isFormatMetric() {
        return formatMetric;
    }
    public boolean isLanguageRu() {
        return languageRu;
    }

    public void setTheme(boolean theme) {
        this.theme = theme;
    }
    public void setFormatMetric(boolean formatMetric) {
        this.formatMetric = formatMetric;
    }
    public void setLanguageRu(boolean languageRu) {
        this.languageRu = languageRu;
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
        dest.writeByte((byte) (formatMetric ? 1 : 0));
        dest.writeByte((byte) (languageRu ? 1 : 0));
    }
}