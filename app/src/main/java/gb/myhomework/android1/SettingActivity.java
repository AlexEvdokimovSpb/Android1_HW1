package gb.myhomework.android1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends Activity implements Constants {

    public static final boolean isDebug = true;
    public static final String TAG = "HW "+ SettingActivity.class.getSimpleName();
    private boolean theme = false;
    private boolean temperature = false;
    private boolean windSpeed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Switch switchTemperature = (Switch) findViewById(R.id.switch_temperature);
        Switch switchTheme = (Switch) findViewById(R.id.switch_theme);
        Switch switchWindSpeed = (Switch) findViewById(R.id.switch_wind_speed);

        switchTemperature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                temperature = isChecked;
                if (isDebug) {
                    Log.i(TAG, "switch Temperature "+ isChecked);
                }
            }
        });

        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                theme = isChecked;
                if (isDebug) {
                    Log.i(TAG, "switch Theme "+ isChecked);
                }
            }
        });

        switchWindSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                windSpeed = isChecked;
                if (isDebug) {
                    Log.i(TAG, "switch WindSpeed "+ isChecked);
                }
            }
        });

        Button buttonConfirm = (Button) findViewById(R.id.button_confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parcel parcel = new Parcel();
                parcel.setTheme(theme);
                parcel.setTemperature(temperature);
                parcel.setWindSpeed(windSpeed);
                Intent intentResult = new Intent();
                intentResult.putExtra("SETTING", parcel);
                setResult(RESULT_OK, intentResult);
                if (isDebug) {
                    Toast.makeText(SettingActivity.this, R.string.toast_confirm, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "confirm");
                }
                finish();
            }
        });

        if (isDebug) {
            Log.d(TAG, "setting activity create");
        }
    }
}