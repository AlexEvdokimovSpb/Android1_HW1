package gb.myhomework.android1;

import androidx.appcompat.app.AppCompatActivity;

//import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements Constants, PublisherGetter {
    public static final String TAG = "HW "+ MainActivity.class.getSimpleName();
    private Publisher publisher = new Publisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mainFragment = new MainFragment();
        publisher.subscribe(mainFragment);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, mainFragment).commit();

        if (Constants.DEBUG) {
            Snackbar.make(findViewById(android.R.id.content),
                    R.string.toast_create, Snackbar.LENGTH_LONG).show();
            Log.v(TAG, "main activity create");
        }
    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }
}