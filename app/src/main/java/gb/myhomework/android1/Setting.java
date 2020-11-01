package gb.myhomework.android1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Setting extends Activity {

    public static final boolean isDebug = true;
    public static final String TAG = "HWLogger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        if (isDebug) {
            Log.d(TAG, "place activity create");
        }


    }
}