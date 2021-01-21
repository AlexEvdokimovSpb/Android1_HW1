package gb.myhomework.android1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import gb.myhomework.android1.dialog.OnDialogListener;
import gb.myhomework.android1.place.PlaceActivity;
import gb.myhomework.android1.receivers.LowBatteryReceiver;
import gb.myhomework.android1.receivers.NoNetworkReceiver;

public class MainActivity extends AppCompatActivity implements PublisherGetter,
        NavigationView.OnNavigationItemSelectedListener, OnFragmentListener, OnDialogListener, MyParcelGetter {
    public static final String TAG = "HW "+ MainActivity.class.getSimpleName();
    private boolean theme = false;
    private boolean formatMetric= true;
    private boolean languageRu= true;
    private MyParcel currentMyParcel;
    private Publisher publisher = new Publisher();
    private String newPlace;
    private MainFragment mainFragment;
    private BroadcastReceiver noNetworkReceiver = new NoNetworkReceiver();
    private BroadcastReceiver lowBatteryReceiver = new LowBatteryReceiver();
    private String channelId = "2";
    private String name = "name";
    private String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);

        currentMyParcel = new MyParcel(theme, formatMetric, languageRu);
        mainFragment = MainFragment.create(currentMyParcel);
        publisher.subscribe(mainFragment);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, mainFragment).commit();

        if (Constants.DEBUG) {
            Log.v(TAG, "main activity create");
            Log.v(TAG, "with "+currentMyParcel+" "+theme+" "+languageRu+" "+formatMetric);
        }

        // получение токена
        initGetToken();

        // открываем канал нотификации
        // и регистриуем приемник сообщений о низком заряде
        // и приемник изменения доступа к сети
        initNotificationChannel();
        final IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        registerReceiver(lowBatteryReceiver, batteryFilter);
        final IntentFilter netFilters = new IntentFilter();
        netFilters.addAction(CONNECTIVITY_CHANGE);
        registerReceiver(noNetworkReceiver,  netFilters);
    }

    @Override
    public MyParcel getMyParcel() {
        return currentMyParcel;
    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_search) {
            goToWeb();
        } else {
            if(item.getItemId() == R.id.action_settings) {
                //TO DO
            }
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                goToWeb();
                break;
            case R.id.nav_place:
                Intent intentPlace = new Intent(this, PlaceActivity.class );
                startActivity(intentPlace);
                break;
            case R.id.nav_map:
                Intent intentMap = new Intent(this, MapsActivity.class );
                startActivityForResult(intentMap, Constants.MAP_PLACE);
                break;
            case R.id.nav_history:
                Intent intentHistory = new Intent(this, HistoryActivity.class );
                startActivity(intentHistory);
                break;
            case R.id.nav_setting:
                // TO DO
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                currentMyParcel = ((MyParcel) data.getParcelableExtra("SETTING"));
                theme = currentMyParcel.isTheme();
                formatMetric = currentMyParcel.isFormatMetric();
                languageRu = currentMyParcel.isLanguageRu();
                if (Constants.DEBUG) {
                    Log.v(TAG, "start onActivityResult " + currentMyParcel + " "
                            + theme + " " + languageRu + " " + formatMetric);
                }
            }
        }
        if (requestCode == Constants.MAP_PLACE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    if (Constants.DEBUG) {
                        Log.v(TAG, "map onActivityResult NULL");
                    }
                    return;
                }
                newPlace = data.getStringExtra("place");
                mainFragment.onGetString(newPlace);
                if (Constants.DEBUG) {
                    Log.v(TAG, "map onActivityResult " + newPlace);
                }
            }
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("SETTING", currentMyParcel);
        if (Constants.DEBUG) {
            Log.v(TAG, "main fragment saveInstanceState");
            Log.v(TAG, "with "+currentMyParcel+" "+theme+ " "+ formatMetric + " "+ languageRu);
        }
    }

    private void goToWeb(){
        String url = "https://m.rp5.ru/";
        Uri uri = Uri.parse(url);
        Intent browser = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browser);
    }

    // Получаем "введеное место" из EnterPlaceFragment и отправляем в MainFragment
    @Override
    public void onGetString(String resultDialog) {
        newPlace = resultDialog;
        mainFragment.onGetString(newPlace);
        if (Constants.DEBUG) {
            Log.v(TAG, "from EnterPlaceFragment "+ newPlace);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // отменяем регистрации при закрытии активити
        unregisterReceiver(noNetworkReceiver);
        unregisterReceiver(lowBatteryReceiver);
        if (Constants.DEBUG) {
            Log.v(TAG, "onDestroy ");
        }
    }

    // инициализация канала нотификаций
    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            notificationManager.createNotificationChannel(channel);
        }
        if (Constants.DEBUG) {
            Log.v(TAG, "initNotificationChannel ");
        }
    }

    // второй способ получения токена
    private void initGetToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("PushMessage", "getInstanceId failed", task.getException());
                    return;
                }
                String token = task.getResult().getToken();
                Log.d(TAG, "token: "+ token);
            }
        });
        if (Constants.DEBUG) {
            Log.v(TAG, "initGetToken ");
        }
    }
}