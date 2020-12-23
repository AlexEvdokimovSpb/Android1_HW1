package gb.myhomework.android1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import gb.myhomework.android1.database.App;
import gb.myhomework.android1.database.HistoryRecyclerAdapter;
import gb.myhomework.android1.database.WeatherDao;
import gb.myhomework.android1.database.WeatherSource;


public class HistoryActivity extends AppCompatActivity {

    private HistoryRecyclerAdapter adapter;
    private WeatherSource weatherSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initRecyclerView();
    }

    // Инициализация списка
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.history_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        WeatherDao weatherDao = App.getInstance().getWeatherDao();
        weatherSource = new WeatherSource(weatherDao);

        adapter = new HistoryRecyclerAdapter(weatherSource,this);
        recyclerView.setAdapter(adapter);
    }
}
