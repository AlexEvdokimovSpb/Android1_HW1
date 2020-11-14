package gb.myhomework.android1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity implements Constants {

    private static final String TAG = "HW "+ SettingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            if (Constants.DEBUG) {
                Toast.makeText(SettingActivity.this, R.string.toast_create, Toast.LENGTH_SHORT).show();
                Log.v(TAG, "setting activity finish ORIENTATION_LANDSCAPE");
            }
            return;
        }

        if (savedInstanceState == null) {
            SettingFragment settingFragment = new SettingFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.setting_cotainer, settingFragment).commit();
        }

        if (Constants.DEBUG) {
            Toast.makeText(SettingActivity.this, R.string.toast_create, Toast.LENGTH_SHORT).show();
            Log.v(TAG, "setting activity create");
        }
    }
}