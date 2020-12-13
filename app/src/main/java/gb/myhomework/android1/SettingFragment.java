package gb.myhomework.android1;

import android.content.Context;
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
    private boolean theme;
    private boolean formatMetric;
    private boolean languageRu;
    private MyParcel currentMyParcel;
    private boolean isExistSetting;
    private Publisher publisher;
    private OnFragmentListener onFragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            this.onFragmentListener = (OnFragmentListener) context;
        }
        if (context instanceof PublisherGetter) {
            publisher = ((PublisherGetter) context).getPublisher();
        }
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

        SwitchMaterial switchTheme = (SwitchMaterial) view.findViewById(R.id.switch_theme);
        SwitchMaterial switchFormat = (SwitchMaterial) view.findViewById(R.id.switch_format);
        SwitchMaterial switchLanguage = (SwitchMaterial) view.findViewById(R.id.switch_language);

        switchTheme.setChecked(theme);
        switchFormat.setChecked(formatMetric);
        switchLanguage.setChecked(languageRu);

        switchFormat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                formatMetric = isChecked;
                currentMyParcel.setFormatMetric(formatMetric);
                if (Constants.DEBUG) {
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
                    Log.v(TAG, "switch Theme " + isChecked);
                }
            }
        });

        switchLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                languageRu = isChecked;
                currentMyParcel.setLanguageRu(languageRu);
                if (Constants.DEBUG) {
                    Log.v(TAG, "switch WindSpeed " + isChecked);
                }
            }
        });

        Button buttonConfirm = (Button) view.findViewById(R.id.button_confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExistSetting = getResources().getConfiguration().orientation
                        != Configuration.ORIENTATION_LANDSCAPE;
                if(isExistSetting){
                    onFragmentListener.onGetParcel(currentMyParcel);
                    if (Constants.DEBUG) {
                        Log.v(TAG, "onGetParcel OK " + currentMyParcel);
                        Log.v(TAG, "confirm and send "+theme+" "+formatMetric+" "+languageRu);
                    }
                    getActivity().finish();
                } else {
                    publisher.notify(currentMyParcel);
                    if (Constants.DEBUG) {
                        Log.v(TAG, "notify OK "+publisher);
                        Log.v(TAG, "confirm and send "+theme+" "+formatMetric+" "+languageRu);
                    }
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
            formatMetric = savedInstanceState.getBoolean("format");
            languageRu = savedInstanceState.getBoolean("language");

            if (Constants.DEBUG) {
                Log.v(TAG, "setting fragment restoreInstanceState");
                Log.v(TAG, "with "+theme+" "+formatMetric+" "+languageRu);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("theme", theme);
        outState.putBoolean("format", formatMetric);
        outState.putBoolean("language", languageRu);
        if (Constants.DEBUG) {
            Log.v(TAG, "setting fragment saveInstanceState");
            Log.v(TAG, "with "+theme+" "+formatMetric+" "+languageRu);
        }
    }
}
