package gb.myhomework.android1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final boolean isDebug = true;
    public static final String TAG = "HW "+ MainActivity.class.getSimpleName();
    final MainPresenter presenter = MainPresenter.getInstance();
    private TextView place;

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
        startActivity(intent);
        if (isDebug) {
            Toast.makeText(MainActivity.this, R.string.toast_button_setting, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "select setting");
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
        if (isDebug) {
            Toast.makeText(MainActivity.this,R.string.toast_saveInstanceState, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "main activity saveInstanceState");
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