package gb.myhomework.android1.place;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import gb.myhomework.android1.Constants;
import gb.myhomework.android1.R;

public class CitySource implements CityDataSource {
    private List<City> dataSource;
    private Resources resources;
    public static final String TAG = "HW "+ CitySource.class.getSimpleName();

    public CitySource(Resources resources) {
        dataSource = new ArrayList<>();
        this.resources = resources;
    }

    public CitySource init(){
        String[] descriptions = resources.getStringArray(R.array.descriptions);
        int[] pictures = getImageArray();
        for (int i = 0; i < descriptions.length; i++) {
            dataSource.add(new City(descriptions[i], pictures[i]));
            if (Constants.DEBUG) {
                Log.v(TAG, "city " + descriptions[i]+" pictures "+ pictures[i]);
            }
        }
        return this;
    }

    @Override
    public City getCity(int position) {
        return dataSource.get(position);
    }
    @Override
    public int size(){
        if (Constants.DEBUG) {
            Log.v(TAG, "dataSource.size=" + dataSource.size());
        }
        return dataSource.size();

    }

    private int[] getImageArray(){
        @SuppressLint("Recycle") TypedArray pictures = resources.obtainTypedArray(R.array.pictures);
        int length = pictures.length();
        int[] answer = new int[length];
        for(int i = 0; i < length; i++){
            answer[i] = pictures.getResourceId(i, 0);
        }
        return answer;
    }
}
