package gb.myhomework.android1;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String TAG = "HW "+ MapsActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 10;
    private List<Marker> markers = new ArrayList<Marker>();
    private EditText textLatitude;
    private EditText textLongitude;
    private TextView textAddress;
    private TextView textPlace;
    private String newPlace;
    private int minTimeMs = 10000;
    private int minDistanceM = 10;
    private int zoom = 12;

    private GoogleMap mMap;
    private Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initViews();
        requestPemissions();
        initSearchByAddress();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng spb = new LatLng(59.93, 30.25);
        currentMarker = mMap.addMarker(new MarkerOptions().position(spb).title("Текущая позиция"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(spb));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                getAddress(latLng);
                addMarker(latLng);
            }
        });
    }

    private void getAddress(final LatLng location){
        final Geocoder geocoder = new Geocoder(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Address> addresses = geocoder.getFromLocation(location.latitude,
                            location.longitude, 1);
                    textAddress.post(new Runnable() {
                        @Override
                        public void run() {
                            textPlace.setText(addresses.get(0).getLocality());
                            newPlace=addresses.get(0).getLocality();
                            textAddress.setText(newPlace);
                            Intent intentResult = new Intent();
                            intentResult.putExtra("place", newPlace);
                            setResult(RESULT_OK, intentResult);
                            if (Constants.DEBUG) {
                                Log.v(TAG, "map onActivityResult " + newPlace);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void addMarker(LatLng location){
        String title = Integer.toString(markers.size());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
        markers.add(marker);
    }

    private void initViews() {
        textLatitude = findViewById(R.id.editLat);
        textLongitude = findViewById(R.id.editLng);
        textAddress = findViewById(R.id.textAddress);
        textPlace = findViewById(R.id.textPlace);
        initSearchByAddress();
    }

    private void requestPemissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider,
                    minTimeMs, minDistanceM, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude(); // Широта
                    String latitude = Double.toString(lat);
                    textLatitude.setText(latitude);

                    double lng = location.getLongitude(); // Долгота
                    String longitude = Double.toString(lng);
                    textLongitude.setText(longitude);

                    String accuracy = Float.toString(location.getAccuracy());   // Точность

                    LatLng currentPosition = new LatLng(lat, lng);
                    currentMarker.setPosition(currentPosition);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, (float)zoom));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1]
                            == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
            }
        }
    }

    private void initSearchByAddress() {
        final EditText textSearch = findViewById(R.id.searchAddress);
        findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Geocoder geocoder = new Geocoder(MapsActivity.this);
                final String searchText = textSearch.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(searchText, 1);
                            if (addresses.size() > 0){
                                final LatLng location = new LatLng(addresses.get(0).getLatitude(),
                                        addresses.get(0).getLongitude());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMap.addMarker(new MarkerOptions()
                                                .position(location)
                                                .title(searchText)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable
                                                        .ic_search_marker)));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,
                                                (float)zoom));
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}





