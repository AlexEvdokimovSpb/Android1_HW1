package gb.myhomework.android1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment implements Constants {

    public static final String TAG = "HW " + SettingFragment.class.getSimpleName();
    private boolean theme = false;
    private boolean temperature = false;
    private boolean windSpeed = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Switch switchTemperature = (Switch) view.findViewById(R.id.switch_temperature);
        Switch switchTheme = (Switch) view.findViewById(R.id.switch_theme);
        Switch switchWindSpeed = (Switch) view.findViewById(R.id.switch_wind_speed);

        switchTemperature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                temperature = isChecked;
                if (Constants.DEBUG) {
                    Log.v(TAG, "switch Temperature " + isChecked);
                }
            }
        });

        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                theme = isChecked;
                if (Constants.DEBUG) {
                    Log.v(TAG, "switch Theme " + isChecked);
                }
            }
        });

        switchWindSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                windSpeed = isChecked;
                if (Constants.DEBUG) {
                    Log.v(TAG, "switch WindSpeed " + isChecked);
                }
            }
        });

        Button buttonConfirm = (Button) view.findViewById(R.id.button_confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parcel parcel = new Parcel();
                parcel.setTheme(theme);
                parcel.setTemperature(temperature);
                parcel.setWindSpeed(windSpeed);
                Intent intentResult = new Intent();
                intentResult.putExtra("SETTING", parcel);
                if (Constants.DEBUG) {
                    Log.v(TAG, "confirm");
                }
            }
        });

        if (Constants.DEBUG) {
            Log.v(TAG, "setting fragment create");
        }
    }
    }
