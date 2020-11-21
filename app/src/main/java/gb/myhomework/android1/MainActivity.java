package gb.myhomework.android1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements Constants{
    public static final String TAG = "HW "+ MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Constants.DEBUG) {
            Snackbar.make(findViewById(android.R.id.content),
                    R.string.toast_create, Snackbar.LENGTH_LONG).show();
            Log.v(TAG, "main activity create");
        }
    }
}