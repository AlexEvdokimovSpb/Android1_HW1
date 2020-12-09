package gb.myhomework.android1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingFragment extends Fragment{

    public static final String TAG = "HW " + SettingFragment.class.getSimpleName();
    private boolean theme = false;
    private boolean formatMetric = false;
    private boolean languageRu = false;
    private MyParcel currentMyParcel;
    private boolean isExistSetting;
    private Publisher publisher;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        publisher = ((PublisherGetter) context).getPublisher();
        if (Constants.DEBUG) {
            Log.v(TAG, "porteret context " + context);
            Log.v(TAG, "publisher " + publisher);
        }
    }

    public static SettingFragment create(MyParcel currentMyParcel) {
        SettingFragment f = new SettingFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.SETTING, currentMyParcel);
        f.setArguments(args);
        return f;
    }

    public MyParcel getParcel() {
        currentMyParcel = (MyParcel) getArguments().getParcelable(Constants.SETTING);
        return currentMyParcel;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        currentMyParcel = getParcel();
        theme = currentMyParcel.isTheme();
        formatMetric = currentMyParcel.isFormatMetric();
        languageRu = currentMyParcel.isLanguageRu();

        SwitchMaterial switchTemperature = (SwitchMaterial) view.findViewById(R.id.switch_temperature);
        SwitchMaterial switchTheme = (SwitchMaterial) view.findViewById(R.id.switch_theme);
        SwitchMaterial switchWindSpeed = (SwitchMaterial) view.findViewById(R.id.switch_wind_speed);
        switchTheme.setChecked(theme);
        switchTemperature.setChecked(formatMetric);
        switchWindSpeed.setChecked(languageRu);

        switchTemperature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                formatMetric = isChecked;
                currentMyParcel.setFormatMetric(formatMetric);
                if (Constants.DEBUG) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            R.string.metric_system, Snackbar.LENGTH_LONG).show();
                    Log.v(TAG, "switch Temperature " + isChecked);
                }
            }
        });

        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                theme = isChecked;
                currentMyParcel.setTheme(theme);
                if (Constants.DEBUG) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            R.string.theme, Snackbar.LENGTH_LONG).show();
                    Log.v(TAG, "switch Theme " + isChecked);
                }
            }
        });

        switchWindSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                languageRu = isChecked;
                currentMyParcel.setLanguageRu(languageRu);
                if (Constants.DEBUG) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            R.string.russian, Snackbar.LENGTH_LONG).show();
                    Log.v(TAG, "switch WindSpeed " + isChecked);
                }
            }
        });

        Button buttonConfirm = (Button) view.findViewById(R.id.button_confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.DEBUG) {
                    Log.v(TAG, "notify GO " + publisher);
                }
                publisher.notify(currentMyParcel);
                if (Constants.DEBUG) {
                    Log.v(TAG, "notify OK " + publisher);
                }

                if (Constants.DEBUG) {
                    Log.v(TAG, "confirm and send " +theme+ " "+ formatMetric + " "+ languageRu);
                }

                isExistSetting = getResources().getConfiguration().orientation
                        != Configuration.ORIENTATION_LANDSCAPE;
                if(isExistSetting){
                    getActivity().finish();
                }
            }
        });

        if (Constants.DEBUG) {
            Log.v(TAG, "setting fragment create");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            theme = savedInstanceState.getBoolean("theme");
            formatMetric = savedInstanceState.getBoolean("unitsTemperature");
            languageRu = savedInstanceState.getBoolean("windSpeed");

            if (Constants.DEBUG) {
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        R.string.toast_restoreInstanceState, Snackbar.LENGTH_SHORT).show();
                Log.v(TAG, "setting fragment restoreInstanceState");
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("theme", theme);
        outState.putBoolean("unitsTemperature", formatMetric);
        outState.putBoolean("windSpeed", languageRu);
        outState.putParcelable("SETTING", currentMyParcel);

        if (Constants.DEBUG) {
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    R.string.toast_saveInstanceState, Snackbar.LENGTH_SHORT).show();
            Log.v(TAG, "setting fragment saveInstanceState");
        }
    }
    }
