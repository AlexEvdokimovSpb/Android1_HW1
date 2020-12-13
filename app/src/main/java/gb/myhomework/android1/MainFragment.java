package gb.myhomework.android1;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import gb.myhomework.android1.model.Weather;
import gb.myhomework.android1.model.WeatherRequest;
import gb.myhomework.android1.place.PlaceActivity;

public class MainFragment extends Fragment implements ConnectionForData.WeatherCallback,
        Observer  {

    public static final String TAG = "HW "+ MainFragment.class.getSimpleName();
    private final MainPresenter presenter = MainPresenter.getInstance();
    private final static int ID = 0;

    private TextView place;
    private TextView unitsTemperature;
    private TextView weatherDescription;
    private TextInputEditText numberTemperature;
    private TextInputEditText numberFeelsTemperature;
    private ImageView weatherIcon;

    private MyParcel currentMyParcel;
    private boolean theme = false;
    private boolean languageRu = true;
    private boolean formatMetric = true;
    boolean isExistSetting;
    private WeatherRequest weatherRequest;
    private ConnectionForData connectionForData = new ConnectionForData(this);
    private ConnectionAndSetIcon connectionAndSetIcon = new ConnectionAndSetIcon();
    private Publisher publisher;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        publisher = ((PublisherGetter) context).getPublisher();
        if (Constants.DEBUG) {
            Log.v(TAG, " context " + context);
            Log.v(TAG, " publisher " + publisher);
        }
    }

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
        weatherDescription = (TextView) view.findViewById(R.id.textViewCloudy);
        numberTemperature = (TextInputEditText) view.findViewById(R.id.editTextNumberTemperature2);
        numberFeelsTemperature = (TextInputEditText)
                view.findViewById(R.id.editTextNumberFeelsTemperature2);
        weatherIcon = (ImageView) view.findViewById(R.id.imageViewCloudy);

        setTemperatureSymbol(formatMetric);

        String[] data = getResources().getStringArray(R.array.descriptions);
        int position=presenter.getPlace();

        connectionForData.connection(data[position], languageRu, formatMetric);

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

        Button buttonToday = (Button) view.findViewById(R.id.buttonToday);
        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weatherRequest != null) {
                    Weather weather[] = weatherRequest.getWeather();
                    String foWeatherDescription = weather[ID].getDescription();
                    String weatherIconUrl = weather[ID].getIcon();
                    String foPlace = weatherRequest.getName();
                    String foNumberTemperature = Float.toString(weatherRequest.getMain().getTemp());
                    String foNumberFeelsTemperature = Float.toString(weatherRequest.getMain()
                            .getFeels_like());

                    weatherDescription.setText(foWeatherDescription);
                    place.setText(foPlace);
                    numberTemperature.setText(foNumberTemperature);
                    numberFeelsTemperature.setText(foNumberFeelsTemperature);
                    connectionAndSetIcon.fetchImage(weatherIconUrl, weatherIcon);

                    if (Constants.DEBUG) {
                        Log.v(TAG, "TEST " + foNumberTemperature);
                    }
                } else {
                    if (Constants.DEBUG) {
                        Log.v(TAG, "weatherRequest = null");
                    }
                }
                if (Constants.DEBUG) {
                    Log.v(TAG, "buttonToday");
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
                    currentMyParcel = new MyParcel(theme, languageRu, formatMetric);
                    intent.putExtra("SETTING", currentMyParcel);
                    intent.putExtra("PUBLISHER", publisher);
                    startActivity(intent);
                    if (Constants.DEBUG) {
                        Snackbar.make(v, R.string.toast_button_setting, Snackbar.LENGTH_LONG).show();
                        Log.v(TAG, "select setting");
                        Log.v(TAG, "send publisher " + publisher);
                        Log.v(TAG, "send currentMyParcel " + currentMyParcel);
                        Log.v(TAG, "in setting activity send: "+ currentMyParcel.isTheme()+" "+
                                currentMyParcel.isFormatMetric()+" "+currentMyParcel.isLanguageRu() );
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
            languageRu = currentMyParcel.isFormatMetric();
            setTemperatureSymbol(formatMetric);

            if (Constants.DEBUG) {
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        R.string.toast_restoreInstanceState, Snackbar.LENGTH_SHORT).show();
                Log.v(TAG, "main fragment restoreInstanceState");
            }
        } else {
            currentMyParcel = new MyParcel(theme, languageRu, formatMetric);
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
        connectionForData.connection(data[position], languageRu, formatMetric);
        place.setText(data[position]);
        setTemperatureSymbol(formatMetric);

        if (weatherRequest != null) {
            float temp = weatherRequest.getMain().getTemp();
            if (Constants.DEBUG) {
                Log.v(TAG, "temp TEST " + temp);
            }
        } else {
            if (Constants.DEBUG) {
                Log.v(TAG, "weatherRequest = null");
            }
        }

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
                        currentMyParcel.isFormatMetric()+" "+currentMyParcel.isLanguageRu() );
            }
        }
    }

    @Override
    public void callBackReturn(WeatherRequest weatherRequest) {
        this.weatherRequest = weatherRequest;
    }

    @Override
    public void updateMyParcel(MyParcel myParcel) {
        currentMyParcel = myParcel;
        theme = currentMyParcel.isTheme();
        languageRu = currentMyParcel.isLanguageRu();
        formatMetric = currentMyParcel.isFormatMetric();

        if (Constants.DEBUG) {
            Log.v(TAG, "updateMyParcel " +theme+ " "+ formatMetric + " "+ languageRu);
        }
        onResume();
    }

    private void setTemperatureSymbol(boolean formatMetric){
        if(formatMetric){
            unitsTemperature.setText(R.string.celsius);
        }
        else {
            unitsTemperature.setText(R.string.fahrenheit);
        }
    }
}
