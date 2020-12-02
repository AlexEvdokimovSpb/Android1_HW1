package gb.myhomework.android1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class SettingActivity extends AppCompatActivity implements Constants {

    private static final String TAG = "HW "+ SettingActivity.class.getSimpleName();
    private MyParcel currentMyParcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentMyParcel = getIntent().getExtras().getParcelable("SETTING");
        if (Constants.DEBUG) {
            Log.v(TAG, "setting activity start with "+ currentMyParcel.isTheme()+" "+
                    currentMyParcel.isTemperature()+" "+currentMyParcel.isWindSpeed() );
        }
        setContentView(R.layout.activity_setting);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            if (Constants.DEBUG) {
                Snackbar.make(findViewById(android.R.id.content), R.string.toast_create,
                        Snackbar.LENGTH_LONG).show();
                Log.v(TAG, "setting activity finish ORIENTATION_LANDSCAPE");
            }
            return;
        }

        SettingFragment settingFragment = new SettingFragment();
        settingFragment = SettingFragment.create(currentMyParcel);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.setting_cotainer, settingFragment).commit();

        if (Constants.DEBUG) {
            Snackbar.make(findViewById(android.R.id.content), R.string.toast_create,
                    Snackbar.LENGTH_LONG).show();
            Log.v(TAG, "setting activity create");
        }
    }
}