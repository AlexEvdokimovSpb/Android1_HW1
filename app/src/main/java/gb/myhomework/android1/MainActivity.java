package gb.myhomework.android1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        EditText editTextDatePlace = (EditText) findViewById(R.id.editTextDatePlace);
        EditText editTextTimePlace = (EditText) findViewById(R.id.editTextTimePlace);
        editTextDatePlace.setText(dateText);
        editTextTimePlace.setText(timeText);


    }





}