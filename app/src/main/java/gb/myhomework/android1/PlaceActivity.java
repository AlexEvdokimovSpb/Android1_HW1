package gb.myhomework.android1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PlaceActivity extends Activity implements View.OnClickListener {

    public static final boolean isDebug = true;
    public static final String TAG = "HW "+ PlaceActivity.class.getSimpleName();
    final MainPresenter presenter = MainPresenter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);


        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);

        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);

        if (isDebug) {
            Log.d(TAG, "place activity create ");
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                presenter.setPlace(R.string.saint_petersburg);
                Toast.makeText(PlaceActivity.this, R.string.saint_petersburg, Toast.LENGTH_SHORT).show();
                if (isDebug) {
                    Log.i(TAG, "selected saint_petersburg");
                }
                break;
            case R.id.button2:
                presenter.setPlace(R.string.moscow);
                Toast.makeText(PlaceActivity.this, R.string.moscow, Toast.LENGTH_SHORT).show();
                if (isDebug) {
                    Log.i(TAG, "selected moscow");
                }
                break;
            case R.id.button3:
                presenter.setPlace(R.string.london);
                Toast.makeText(PlaceActivity.this, R.string.london, Toast.LENGTH_SHORT).show();
                if (isDebug) {
                    Log.i(TAG, "selected london");
                }
                break;
            case R.id.button4:
                presenter.setPlace(R.string.paris);
                Toast.makeText(PlaceActivity.this, R.string.paris, Toast.LENGTH_SHORT).show();
                if (isDebug) {
                    Log.i(TAG, "selected paris");
                }
                break;
            case R.id.button5:
                presenter.setPlace(R.string.rom);
                Toast.makeText(PlaceActivity.this, R.string.rom, Toast.LENGTH_SHORT).show();
                if (isDebug) {
                    Log.i(TAG, "selected rom");
                }
                break;
        }
    }
}