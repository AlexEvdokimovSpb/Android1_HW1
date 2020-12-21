package gb.myhomework.android1;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity implements OnFragmentListener{

    private static final String TAG = "HW "+ SettingActivity.class.getSimpleName();
    private MyParcel currentMyParcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentMyParcel = getIntent().getExtras().getParcelable("SETTING");
        if (Constants.DEBUG) {
            Log.v(TAG, "setting activity start currentMyParcel " + currentMyParcel);
            Log.v(TAG, "setting activity start with "+ currentMyParcel.isTheme()+" "+
                    currentMyParcel.isFormatMetric()+" "+currentMyParcel.isLanguageRu() );
        }
        setContentView(R.layout.activity_setting);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            if (Constants.DEBUG) {
                Log.v(TAG, "setting activity finish ORIENTATION_LANDSCAPE");
            }
            return;
        }

        SettingFragment settingFragment;
        settingFragment = SettingFragment.create(currentMyParcel);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.setting_cotainer, settingFragment).commit();
        if (Constants.DEBUG) {
            Log.v(TAG, "setting activity create");
        }
    }

    @Override
    public void onGetParcel(MyParcel myParcel) {
        currentMyParcel = myParcel;
        Intent intentResult = new Intent();
        intentResult.putExtra("SETTING", currentMyParcel);
        setResult(RESULT_OK, intentResult);
        if (Constants.DEBUG) {
                Log.v(TAG, "putExtra " +currentMyParcel);
        }
    }
}