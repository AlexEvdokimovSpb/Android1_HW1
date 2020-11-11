package gb.myhomework.android1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SettingActivity extends Activity {

    public static final boolean isDebug = true;
    public static final String TAG = "HW "+ SettingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (isDebug) {
            Log.d(TAG, "place activity create");
        }


    }
}