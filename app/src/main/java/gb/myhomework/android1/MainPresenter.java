package gb.myhomework.android1;

public final class MainPresenter {

    private static MainPresenter instance = null;
    private static final Object syncObj = new Object();
    private int place;

    private MainPresenter(){
        place = R.string.saint_petersburg;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getPlace(){
        return place;
    }

    public static MainPresenter getInstance(){
        synchronized (syncObj) {
            if (instance == null) {
                instance = new MainPresenter();
            }
            return instance;
        }
    }

}
