package gb.myhomework.android1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import gb.myhomework.android1.dialog.EnterPlaceFragment;
import gb.myhomework.android1.dialog.OnDialogListener;
import gb.myhomework.android1.model.WeatherRequest;
import gb.myhomework.android1.place.PlaceActivity;

public class MainFragment extends Fragment implements ConnectionForData.WeatherCallback,
        Observer, OnDialogListener {

    public static final String TAG = "HW "+ MainFragment.class.getSimpleName();
    private final MainPresenter presenter = MainPresenter.getInstance();
    private final static int ID = 0;

    private TextView place;
    private TextView unitsTemperature;
    private TextView weatherDescription;
    private TextInputEditText numberTemperature;
    private TextInputEditText numberFeelsTemperature;
    private ImageView weatherIcon;

    private boolean theme;
    private boolean languageRu;
    private boolean formatMetric;
    private MyParcel currentMyParcel;
    private boolean isExistSetting;
    private WeatherRequest weatherRequest;
    private ConnectionForData connectionForData = new ConnectionForData(this);
    private ConnectionAndSetIcon connectionAndSetIcon = new ConnectionAndSetIcon();
    private Publisher publisher;
    private OnFragmentListener onFragmentListener;
    private String newPlace;
    boolean isEnterPlace; // определяет способ ввода места (из списка PlaceActivity или ввода в EnterPlaceFragment)

    static final String BROADCAST_WEATHER = "gb.myhomework.android1.MyIntentService";

    // создал для взаимодействия с EnterPlaceFragment, но похоже этот путь фрагмент - фрагмент не работает
    private OnDialogListener onDialogListener = new OnDialogListener() {
        @Override
        public void onGetString(String string) {
            if (Constants.DEBUG) {
                Log.v(TAG, "from onDialogListener "+ string);
            }
        }
    };

    public static MainFragment create(MyParcel currentMyParcel) {
        MainFragment f = new MainFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.SETTING, currentMyParcel);
        f.setArguments(args);
        if (Constants.DEBUG) {
            Log.v(TAG, "main fragment create");
            Log.v(TAG, "with "+ currentMyParcel);
        }
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PublisherGetter) {
            publisher = ((PublisherGetter) context).getPublisher();
        }
        if (context instanceof MyParcelGetter && ((MyParcelGetter) context).getMyParcel()!=null) {
            currentMyParcel = ((MyParcelGetter) context).getMyParcel();
            theme = currentMyParcel.isTheme();
            formatMetric = currentMyParcel.isFormatMetric();
            languageRu = currentMyParcel.isLanguageRu();
        }
        if (context instanceof OnFragmentListener) {
            this.onFragmentListener = (OnFragmentListener) context;
        }
        if (Constants.DEBUG) {
            Log.v(TAG, "main fragment onAttach");
            Log.v(TAG, "context " + context);
            Log.v(TAG, "publisher " + publisher);
            Log.v(TAG, "with "+currentMyParcel+" "+theme+" "+languageRu+" "+formatMetric);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // канал нотификации
        initNotificationChannel();
        if (Constants.DEBUG) {
            Log.v(TAG, "main fragment onCreateView");
            Log.v(TAG, "with "+ currentMyParcel);
        }
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        place  = (TextView) view.findViewById(R.id.textViewPlace);
        unitsTemperature = (TextView) view.findViewById(R.id.textViewTemperature);
        weatherDescription = (TextView) view.findViewById(R.id.textViewCloudy);
        numberTemperature = (TextInputEditText) view.findViewById(R.id.editTextNumberTemperature2);
        numberFeelsTemperature = (TextInputEditText)
                view.findViewById(R.id.editTextNumberFeelsTemperature2);
        weatherIcon = (ImageView) view.findViewById(R.id.imageViewCloudy);
        setTemperatureSymbol(formatMetric);
        goMyIntentService(); // запрос начальных значений

        Button button_detail = (Button) view.findViewById(R.id.details);
        button_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://m.rp5.ru/";
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);
                if (Constants.DEBUG) {
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
                isEnterPlace = false;
            if (Constants.DEBUG) {
                Log.v(TAG, "select place");
            }
            }
        });

        Button buttonToday = (Button) view.findViewById(R.id.buttonToday);
        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMyIntentService(); // запрос начальных значений
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
                    currentMyParcel = new MyParcel(theme, formatMetric, languageRu);
                    intent.putExtra("SETTING", currentMyParcel);
                    startActivityForResult(intent, Constants.REQUEST_CODE);
                    if (Constants.DEBUG) {
                        Log.v(TAG, "select setting");
                        Log.v(TAG, "send currentMyParcel " + currentMyParcel);
                        Log.v(TAG, "in setting activity send: "+ currentMyParcel.isTheme()+" "+
                                currentMyParcel.isFormatMetric()+" "+currentMyParcel.isLanguageRu() );
                    }
                }
            });
        }

        if (Constants.DEBUG) {
            Log.v(TAG, "main fragment onViewCreated2");
            Log.v(TAG, "with "+currentMyParcel+" "+theme+" "+languageRu+" "+formatMetric);
        }

        // новая кнопка для вызова EnterPlaceFragment
        Button buttonError = (Button) view.findViewById(R.id.button_enter_place);
        buttonError.setOnClickListener(clickButtonEnterPlace);

        // новая кнопка для вызова диалога ошибок
        Button buttonEnterPlace = (Button) view.findViewById(R.id.buttonAlertDialog);
        buttonEnterPlace.setOnClickListener(clickAlertDialog);
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
            if (Constants.DEBUG) { ;
                Log.v(TAG, "main fragment restoreInstanceState");
            }
        } else {
            currentMyParcel = new MyParcel(theme, formatMetric, languageRu);
        }
        showSetting(currentMyParcel);

        if (Constants.DEBUG) {
            Log.v(TAG, "main fragment onActivityCreated");
            Log.v(TAG, "with "+currentMyParcel+" "+theme+" "+languageRu+" "+formatMetric);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        // подписываемся на сервис
        getActivity().registerReceiver(resultConnectionForData, new IntentFilter(BROADCAST_WEATHER));
        if (Constants.DEBUG) {
            Log.v(TAG, "main fragment start");
            Log.v(TAG, "with "+currentMyParcel+" "+theme+" "+languageRu+" "+formatMetric);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        goMyIntentService();
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
            Log.v(TAG, "main fragment resume " + newPlace);
            Log.v(TAG, "with "+currentMyParcel+" "+theme+" "+languageRu+" "+formatMetric);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Constants.DEBUG) {
            Log.v(TAG, "main fragment pause");
            Log.v(TAG, "with "+currentMyParcel+" "+theme+ " "+ formatMetric + " "+ languageRu);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("SETTING", currentMyParcel);
        if (Constants.DEBUG) {
            Log.v(TAG, "main fragment saveInstanceState");
            Log.v(TAG, "with "+currentMyParcel+" "+theme+ " "+ formatMetric + " "+ languageRu);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // отписываемся от сервиса
        getActivity().unregisterReceiver(resultConnectionForData);
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
        if (Constants.DEBUG) {
            Log.v(TAG, "callBackReturn " +weatherRequest);
        }
    }

    @Override
    public void updateMyParcel(MyParcel myParcel) {
        currentMyParcel = myParcel;
        theme = currentMyParcel.isTheme();
        formatMetric = currentMyParcel.isFormatMetric();
        languageRu = currentMyParcel.isLanguageRu();
        onFragmentListener.onGetParcel(currentMyParcel);
        if (Constants.DEBUG) {
            Log.v(TAG, "updateMyParcel " +theme+ " "+ formatMetric + " "+ languageRu);
        }
        onResume();
    }

    private void setTemperatureSymbol (boolean formatMetric){
        if(formatMetric){
            unitsTemperature.setText(R.string.celsius);
        } else {
            unitsTemperature.setText(R.string.fahrenheit);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            currentMyParcel = ((MyParcel)data.getParcelableExtra("SETTING"));
            theme = currentMyParcel.isTheme();
            formatMetric = currentMyParcel.isFormatMetric();
            languageRu = currentMyParcel.isLanguageRu();
            setTemperatureSymbol(formatMetric);
            if (Constants.DEBUG) {
                Log.i(TAG, "start onActivityResult "+currentMyParcel+" "+theme+" "+languageRu+" "+formatMetric);
            }
        }
    }

    // запускаем EnterPlaceFragment
    private final View.OnClickListener clickButtonEnterPlace = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EnterPlaceFragment enterPlaceFragment = EnterPlaceFragment.newInstance();
            enterPlaceFragment.setOnDialogListener(onDialogListener);
            enterPlaceFragment.show(getActivity().getSupportFragmentManager(),  "enterPlaceFragment");
            isEnterPlace = true;
        }
    };

    // Получаем "введеное место" в EnterPlaceFragment через MainActivity
    @Override
    public void onGetString(String resultDialog) {
        newPlace = resultDialog;
        place.setText(newPlace);
        //connectionForData.connection(newPlace, languageRu, formatMetric);
        goMyIntentService();
        if (Constants.DEBUG) {
            Log.v(TAG, "from MainActivity "+ newPlace);
        }
    }

    private View.OnClickListener clickAlertDialog= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final View contentView = getLayoutInflater().inflate(R.layout.alert_dialog, null);
            builder.setTitle(R.string.attention)
                    .setView(contentView)
                    .setPositiveButton(R.string.accepted, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText editText = contentView.findViewById(R.id.alert_dialog);
                            Toast.makeText(getContext(), String.format("Введено: %s", editText
                                    .getText().toString()), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    // Обращение к сервису
    private void goMyIntentService() {
        if (isEnterPlace) {
            place.setText(newPlace);
            MyIntentService.startMyIntentService(getContext(), newPlace);
        } else {
            String[] data = getResources().getStringArray(R.array.descriptions);
            int position = presenter.getPlace();
            place.setText(data[position]);
            MyIntentService.startMyIntentService(getContext(), data[position]);
        }
    }
    // Получатель широковещательного сообщения
    private BroadcastReceiver resultConnectionForData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String foWeatherDescription = intent.getStringExtra(MyIntentService
                    .EXTRA_RESULT_DESCRIPTION);
            final String weatherIconUrl = intent.getStringExtra(MyIntentService.EXTRA_RESULT_ICON);
            final String foPlace = intent.getStringExtra(MyIntentService.EXTRA_RESULT_PLACE);
            final String result = intent.getStringExtra(MyIntentService.EXTRA_RESULT_T);
            final String foNumberFeelsTemperature = intent.getStringExtra(MyIntentService.EXTRA_RESULT_T_FEELS);
            // Потокобезопасный вывод данных
            numberTemperature.post(new Runnable() {
                @Override
                public void run() {
                    weatherDescription.setText(foWeatherDescription);
                    connectionAndSetIcon.fetchImage(weatherIconUrl, weatherIcon);
                    place.setText(foPlace);
                    numberTemperature.setText(result);
                    numberFeelsTemperature.setText(foNumberFeelsTemperature);

                    if (Constants.DEBUG) {
                        Log.v(TAG, "from BroadcastReceiver "+ result);
                    }
                }
            });
        }
    };
    // Канал нотификации
    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel("2", "name", importance);
            notificationManager.createNotificationChannel(mChannel);
        }
    }
}