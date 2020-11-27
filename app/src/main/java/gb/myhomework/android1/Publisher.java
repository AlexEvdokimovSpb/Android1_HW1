package gb.myhomework.android1;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Publisher implements Parcelable {

    private List<Observer> observers;

    public Publisher() {
        observers = new ArrayList<>();
    }

    protected Publisher(Parcel in) {
        observers =in.readArrayList(getClass().getClassLoader());
    }

    public static final Creator<Publisher> CREATOR = new Creator<Publisher>() {
        @Override
        public Publisher createFromParcel(Parcel in) {
            return new Publisher(in);
        }

        @Override
        public Publisher[] newArray(int size) {
            return new Publisher[size];
        }
    };

    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    public void notify(MyParcel myParcel) {
        for (Observer observer : observers) {
            observer.updateMyParcel(myParcel);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
