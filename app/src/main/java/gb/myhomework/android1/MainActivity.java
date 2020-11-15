package gb.myhomework.android1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Constants{

    public static final boolean isDebug = true;
    public static final String TAG = "HW "+ MainActivity.class.getSimpleName();
    final MainPresenter presenter = MainPresenter.getInstance();
    private final static int REQUEST_CODE = 2;

    private TextView place;
    private TextView unitsTemperature;

    private boolean theme = false;
    private boolean temperature = false;
    private boolean windSpeed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isDebug) {
            Toast.makeText(MainActivity.this, R.string.toast_create, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "main activity create");
        }

        place  = (TextView) findViewById(R.id.textViewPlace);
        place.setText(presenter.getPlace());
        unitsTemperature = (TextView) findViewById(R.id.textViewTemperature);
        unitsTemperature.setText(R.string.celsius);

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        EditText editTextDatePlace = (EditText) findViewById(R.id.editTextDatePlace);
        EditText editTextTimePlace = (EditText) findViewById(R.id.editTextTimePlace);
        editTextDatePlace.setText(dateText);
        editTextTimePlace.setText(timeText);
    }

    public void button_select_place(View view) {
        Intent intent = new Intent(MainActivity.this, PlaceActivity.class);
        startActivity(intent);
        if (isDebug) {
            Toast.makeText(MainActivity.this, R.string.toast_button_place, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "select place");
        }
    }

    public void button_setting(View view) {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
        if (isDebug) {
            Toast.makeText(MainActivity.this, R.string.toast_button_setting, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "select setting");
        }
    }

    public void button_detail(View view) {
        String url = "https://m.rp5.ru/";
        Uri uri = Uri.parse(url);
        Intent browser = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browser);
        if (isDebug) {
            Toast.makeText(MainActivity.this, R.string.toast_button_detail, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "go for getails");
            Log.i(TAG, url);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (isDebug) {
            Toast.makeText(MainActivity.this,R.string.toast_start, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "main activity start");
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        temperature = savedInstanceState.getBoolean("unitsTemperature");
        if(temperature){
            unitsTemperature.setText(R.string.fahrenheit);
        }
        else {
            unitsTemperature.setText(R.string.celsius);
        }
        if (isDebug) {
            Toast.makeText(MainActivity.this,R.string.toast_restoreInstanceState, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "main activity restoreInstanceState");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDebug) {
            Toast.makeText(MainActivity.this,R.string.toast_resume, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "main activity resume");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isDebug) {
            Toast.makeText(MainActivity.this,R.string.toast_pause, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "main activity pause");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("unitsTemperature", temperature);
        if (isDebug) {
            Toast.makeText(MainActivity.this,R.string.toast_saveInstanceState, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "main activity saveInstanceState");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (resultCode == RESULT_OK){
            Parcel parcel = (Parcel)data.getSerializableExtra("SETTING");
            theme=parcel.isTheme();
            temperature=parcel.isTemperature();
            windSpeed=parcel.isWindSpeed();

            if(temperature){
                unitsTemperature.setText(R.string.fahrenheit);
            }
            else {
                unitsTemperature.setText(R.string.celsius);
            }


            if (isDebug) {
                Log.i(TAG, "start onActivityResult " + theme +" "+ temperature + " "+ windSpeed);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        place.setText(presenter.getPlace());
        if (isDebug) {
            Toast.makeText(MainActivity.this,R.string.toast_restart, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "main activity restart");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isDebug) {
            Toast.makeText(MainActivity.this,R.string.toast_stop, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "main activity stop");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isDebug) {
            Toast.makeText(MainActivity.this,R.string.toast_destroy, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "main activity destroy");
        }
    }
}