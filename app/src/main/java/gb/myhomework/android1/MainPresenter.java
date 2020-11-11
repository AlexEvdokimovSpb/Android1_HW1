package gb.myhomework.android1;

import android.util.Log;

public final class MainPresenter {

    private static MainPresenter instance = null;
    private static final Object syncObj = new Object();
    private int place;

    public static final boolean isDebug = true;
    public static final String TAG = "HW "+ MainPresenter.class.getSimpleName();

    private MainPresenter(){
        place = R.string.saint_petersburg;
    }

    public synchronized void setPlace(int place) {
        this.place = place;
        if (isDebug) {
            Log.d(TAG, "Place = "+ place);
        }

    }

    public synchronized int getPlace(){
        return place;
    }

    public static MainPresenter getInstance(){
        synchronized (syncObj) {
            if (instance == null) {
                instance = new MainPresenter();
                if (isDebug) {
                    Log.d(TAG, "instance = new MainPresenter");
                }

            }
            return instance;
        }
    }

}
