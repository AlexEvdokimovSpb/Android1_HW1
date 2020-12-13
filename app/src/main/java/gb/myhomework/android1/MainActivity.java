package gb.myhomework.android1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import gb.myhomework.android1.place.PlaceActivity;

public class MainActivity extends AppCompatActivity implements PublisherGetter,
        NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "HW "+ MainActivity.class.getSimpleName();
    private Publisher publisher = new Publisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);

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

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_search) {
            String url = "https://m.rp5.ru/";
            Uri uri = Uri.parse(url);
            Intent browser = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(browser);
        } else {
            if(item.getItemId() == R.id.action_settings) {
                Intent intent = new Intent(this, SettingActivity.class);
                boolean theme = false;
                boolean languageRu = false;
                boolean formatMetric = true;
                MyParcel currentMyParcel;
                currentMyParcel = new MyParcel(theme, languageRu, formatMetric);
                intent.putExtra("SETTING", currentMyParcel);
                intent.putExtra("PUBLISHER", publisher);
                startActivity(intent);
            }
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                String url = "https://m.rp5.ru/";
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);
                break;
            case R.id.nav_place:
                Intent intentPlace = new Intent(this, PlaceActivity.class );
                startActivity(intentPlace);
                break;
            case R.id.nav_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                boolean theme = false;
                boolean languageRu = false;
                boolean formatMetric = true;
                MyParcel currentMyParcel;
                currentMyParcel = new MyParcel(theme, languageRu, formatMetric);
                intent.putExtra("SETTING", currentMyParcel);
                intent.putExtra("PUBLISHER", publisher);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

}