package gb.myhomework.android1;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainFragment extends Fragment implements Constants{

    public static final String TAG = "HW "+ MainFragment.class.getSimpleName();
    private final MainPresenter presenter = MainPresenter.getInstance();
    private final static int REQUEST_CODE = 2;

    private TextView place;
    private TextView unitsTemperature;

    private MyParcel currentMyParcel;
    private boolean theme = true;
    private boolean temperature = false;
    private boolean windSpeed = false;
    private boolean isExistSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (Constants.DEBUG) {
            Log.v(TAG, "main fragment onCreateView");
        }
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        place  = (TextView) view.findViewById(R.id.textViewPlace);
        unitsTemperature = (TextView) view.findViewById(R.id.textViewTemperature);
        unitsTemperature.setText(R.string.celsius);

        Date  currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        TextInputEditText editTextDatePlace = (TextInputEditText) view.findViewById(R.id.editTextDatePlace2);
        TextInputEditText editTextTimePlace = (TextInputEditText) view.findViewById(R.id.editTextTimePlace2);
        editTextDatePlace.setText(dateText);
        editTextTimePlace.setText(timeText);

        Button button_detail = (Button) view.findViewById(R.id.details);
        button_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://m.rp5.ru/";
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);
                if (Constants.DEBUG) {
                    Snackbar.make(v, R.string.toast_button_detail, Snackbar.LENGTH_SHORT).show();
                    Log.v(TAG, "go for getails" + url);
                }
            }
        });

        Button button_select_place = (Button) view.findViewById(R.id.button_select_place);
        button_select_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlaceActivity.class );
                startActivity(intent);
            if (Constants.DEBUG) {
                Snackbar.make(v, R.string.toast_button_place, Snackbar.LENGTH_LONG).show();
                Log.v(TAG, "select place");
            }
            }
        });

        isExistSetting = getResources().getConfiguration().orientation
                != Configuration.ORIENTATION_LANDSCAPE;
        if(isExistSetting) {
            Button button_setting = (Button) view.findViewById(R.id.setting);
            button_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    MyParcel currentMyParcel = new MyParcel(theme, temperature, windSpeed);
                    intent.putExtra("SETTING", currentMyParcel);
                    //startActivityForResult(intent, REQUEST_CODE);
                    startActivity(intent);
                    if (Constants.DEBUG) {
                        Snackbar.make(v, R.string.toast_button_setting, Snackbar.LENGTH_LONG).show();
                        Log.v(TAG, "select setting");
                        Log.v(TAG, "in setting activity send: "+ currentMyParcel.isTheme()+" "+
                                currentMyParcel.isTemperature()+" "+currentMyParcel.isWindSpeed() );
                    }
                }
            });
        }

        super.onViewCreated(view, savedInstanceState);
        if (Constants.DEBUG) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.toast_create,
                    Snackbar.LENGTH_SHORT).show();
            Log.v(TAG, "main fragment onViewCreated");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isExistSetting = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentMyParcel = (MyParcel) savedInstanceState.getParcelable("SETTING");
            temperature = currentMyParcel.isTemperature();
            if(temperature){
                unitsTemperature.setText(R.string.fahrenheit);
            }
            else {
                unitsTemperature.setText(R.string.celsius);
            }

            if (Constants.DEBUG) {
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        R.string.toast_restoreInstanceState, Snackbar.LENGTH_SHORT).show();
                Log.v(TAG, "main fragment restoreInstanceState");
            }
        } else {
            currentMyParcel = new MyParcel(theme,temperature,windSpeed);
        }

        if (isExistSetting) {
            showSetting(currentMyParcel);
        }

        if (Constants.DEBUG) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.toast_restart,
                    Snackbar.LENGTH_SHORT).show();
            Log.v(TAG, "main fragment onActivityCreated");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if (Constants.DEBUG) {
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    R.string.toast_start, Snackbar.LENGTH_SHORT).show();
            Log.v(TAG, "main fragment start");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] data = getResources().getStringArray(R.array.descriptions);
        int position=presenter.getPlace();
        place.setText(data[position]);
        if (Constants.DEBUG) {
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    R.string.toast_resume, Snackbar.LENGTH_SHORT).show();
            Log.v(TAG, "main fragment resume " + position);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Constants.DEBUG) {
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    R.string.toast_pause, Snackbar.LENGTH_SHORT).show();
            Log.v(TAG, "main fragment pause");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("SETTING", currentMyParcel);
        if (Constants.DEBUG) {
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    R.string.toast_saveInstanceState, Snackbar.LENGTH_SHORT).show();
            Log.v(TAG, "main fragment saveInstanceState");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Constants.DEBUG) {
            Log.v(TAG, "main fragment stop");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Constants.DEBUG) {
            Log.v(TAG, "main fragment destroy");
        }
    }

    private void showSetting(MyParcel currentMyParcel) {
        if (isExistSetting) {
            SettingFragment settingFragment = (SettingFragment) getFragmentManager().
                    findFragmentById(R.id.setting_cotainer_main);
            settingFragment = SettingFragment.create(currentMyParcel);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.setting_cotainer_main, settingFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
            if (Constants.DEBUG) {
                Log.v(TAG, "setting fragment commit with "+ currentMyParcel.isTheme()+" "+
                        currentMyParcel.isTemperature()+" "+currentMyParcel.isWindSpeed() );
            }
        }
    }
}
