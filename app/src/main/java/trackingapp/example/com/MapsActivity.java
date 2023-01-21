package trackingapp.example.com;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationListenerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import trackingapp.example.com.databinding.ActivityMapsBinding;

public class MapsActivity<btnSent> extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;

    private LatLng latLng;

    private TextView textView;
    private EditText editTextLong;
    private EditText editTextLat;
    private EditText editTextNumber;
    private Button btnSent;
    private Button btnLoc;
    private String address = "";
    private String city="";
    private String state = "";
    private String country = "";
    private String postalCode = "";
    private String knonName = "";
    private String phoneNumber="10000";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        textView = findViewById(R.id.textView);
//        editTextLong = findViewById(R.id.editTextLong);
//        editTextLat = findViewById(R.id.editTextLat);
        editTextNumber = findViewById(R.id.editTextNumber);

        btnSent = findViewById(R.id.sendmessage);

        btnSent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.SEND_SMS) ==
                        PackageManager.PERMISSION_GRANTED) {
                    Log.v("ababab", editTextNumber.getText().toString());
                    sendSMS(editTextNumber.getText().toString());



                }
            }
        });

        btnLoc = findViewById(R.id.getlocation);

        btnLoc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.SEND_SMS) ==
                        PackageManager.PERMISSION_GRANTED) {
                    getLocationDetails(latLng.latitude,latLng.longitude);

                }
            }
        });

        latLng = new LatLng(-34, 151);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Home"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(latLng).title("My Position"));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    /**
                     String phoneNumber = "99999";
                     String myLatitude = String.valueOf(location.getAltitude());
                     String myLongitude = String.valueOf(location.getLongitude());

                     String message = "Latitude = " + myLatitude + "Longtitude = " + myLongitude;
                     SmsManager smsManager = SmsManager.getDefault();
                     smsManager.sendTextMessage(phoneNumber,null,message, null,null);

                     */
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void getLocationDetails(double latitude, double longitude) {
//        double latitude = latLng.latitude;
//        double longitude = latLng.longitude;
//
//        if (!(editTextLong.getText().toString().isEmpty() || editTextLat.getText().toString().isEmpty())) {
//            latitude = Double.parseDouble(editTextLat.getText().toString());
//            longitude = Double.parseDouble(editTextLong.getText().toString());
//
//            latLng = new LatLng(latitude, longitude);
//
//        }
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());


        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knonName = addresses.get(0).getFeatureName();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in: " + address + city + state + country + postalCode + knonName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        textView.setText(address + city + state + country + postalCode + knonName);

//        btnSent.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.SEND_SMS) ==
//                        PackageManager.PERMISSION_GRANTED) {
//                    sendSMS();
//
//                }
//            }
//        });

    }

    private void sendSMS(String phoneNumber) {

//        Log.v("a","A");
        String message = "Dita's is here " + address + " " +city + " "+ state + " " + country + " " + postalCode + knonName ;
        SmsManager smsManager = SmsManager.getDefault();
        Log.v("a", message);
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);

    }

    public Button getBtnLoc() {
        return btnLoc;
    }

    public void setBtnLoc(Button btnLoc) {
        this.btnLoc = btnLoc;
    }
}