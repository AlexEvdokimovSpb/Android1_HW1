package gb.myhomework.android1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Intent intent = new Intent(MainActivity.this, Place.class);
        startActivity(intent);
    }

    public void button_setting(View view) {
        Intent intent = new Intent(MainActivity.this, Setting.class);
        startActivity(intent);
    }
}