package gb.myhomework.android1.place;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import gb.myhomework.android1.Constants;
import gb.myhomework.android1.MainPresenter;
import gb.myhomework.android1.R;

public class PlaceActivity extends AppCompatActivity {

    public static final String TAG = "HW "+ PlaceActivity.class.getSimpleName();
    private final MainPresenter presenter = MainPresenter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        initDataSource();

        if (Constants.DEBUG) {
            Log.v(TAG, "place activity create ");
        }
    }

    private void initDataSource() {
        CityDataSource sourceData = new CitySourceBuilder()
                .setResources(getResources())
                .build();
        final PlaceAdapter adapter = initRecyclerView(sourceData);
        if (Constants.DEBUG) {
            Log.v(TAG, "initDataSource ");
        }
    }

    private PlaceAdapter initRecyclerView(CityDataSource data){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        PlaceAdapter adapter = new PlaceAdapter(data);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.setPlace(position);
                Toast.makeText(PlaceActivity.this, String.format("Позиция - %d", position), Toast.LENGTH_SHORT).show();
                if (Constants.DEBUG) {
                    Log.v(TAG, "selected city: "+position);
                }
            }
        });

        if (Constants.DEBUG) {
            Log.v(TAG, "initRecyclerView");
        }
        return adapter;
    }
}