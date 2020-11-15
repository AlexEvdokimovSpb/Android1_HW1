package gb.myhomework.android1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlaceActivity extends AppCompatActivity implements Constants {

    public static final String TAG = "HW "+ PlaceActivity.class.getSimpleName();
    final MainPresenter presenter = MainPresenter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        String[] data = getResources().getStringArray(R.array.descriptions);
        initRecyclerView(data);

        if (Constants.DEBUG) {
            Log.v(TAG, "place activity create ");
        }
    }

    private PlaceAdapter initRecyclerView(String[] data){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        PlaceAdapter adapter = new PlaceAdapter(data);
        recyclerView.setAdapter(adapter);

        adapter.SetOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
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