package ventres.intellinects.com.locationtracker;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity  implements LocationListener{

    private static final int REQUEST_PERMISSION_FINE_LOCATION_RESULT = 0;
    public Button preLocation;
    public Button getLocationBtns;
    private LocationManager locationManager;
    public String address; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    public String state;
    public String city;
    public String country;
    public String postalCode;
    public String knownName;
    public String dates;
    public String status;
    public String userid;
    public double latitude;
    public double longitude;
    public String buttons ="getAddress";
    Button location2;
    Calendar calendar;
    private boolean FOUND= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();
        location2 = (Button) findViewById(R.id.Location2);
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        final FragmentManager fm = getFragmentManager();
        final predinedLocation p = new predinedLocation();
        preLocation = (Button) findViewById(R.id.prelocation);
        preLocation.startAnimation(animation);
        preLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.clearAnimation();
                p.show(fm, "Locations");
            }
        });
        location2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        getLocation();

                    }else{
                        if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            Toast.makeText(getApplicationContext(), "App requires access location", Toast.LENGTH_SHORT).show();
                        }else {
                            ActivityCompat.requestPermissions(Main2Activity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FINE_LOCATION_RESULT);
                        }
                    }
                }else{
                    getLocation();
                }
            }
        });


    }

    public void getLocation(){
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 2, this);
        }catch(SecurityException e){
            Toast.makeText(getApplicationContext(),"Not Desired Location",Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }


    }

    @Override
    public void onLocationChanged(Location location) {
            try{
                Geocoder geocoder = new Geocoder(this,Locale.getDefault());
                List<Address> addresss = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                address = addresss.get(0).getAddressLine(0);
                city = addresss.get(0).getLocality();
                state = addresss.get(0).getAdminArea();
                country = addresss.get(0).getCountryName();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                postalCode = addresss.get(0).getPostalCode();
                calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                dates = simpleDateFormat.format(calendar.getTime());
                Toast.makeText(getApplicationContext(),"LOCATION FOUND",Toast.LENGTH_LONG).show();;
                FOUND=true;
                emptynessDialog();
            }catch(Exception e){
                Toast.makeText(getApplicationContext(),"PLEASE RESTART YOUR PHONE",Toast.LENGTH_LONG).show();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION_FINE_LOCATION_RESULT){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"Appplication cannot run without location permission",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Failed to get ACCESS", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(getApplicationContext(),"GPS ENABLED",Toast.LENGTH_LONG).show();;

    }

    @Override
    public void onProviderDisabled(String s) {
        dialo();
    }
     void dialo(){
        AlertDialog.Builder altdial = new AlertDialog.Builder(Main2Activity.this) ;
        altdial.setMessage("Please enable gps, cant access your location!").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                    }
                });

        AlertDialog alert = altdial.create();
        alert.setTitle("ENABLE GPS");
        alert.show();

    }
    public void emptynessDialog(){
         if(address.isEmpty() || address ==null){
             Toast.makeText(getApplicationContext(),"PLease wait till location accessed",Toast.LENGTH_LONG).show();
         }else{
             AlertDialog.Builder altdials = new AlertDialog.Builder(Main2Activity.this) ;
             altdials.setMessage("Your address is:   "+address).setCancelable(true)
                     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                             intent.putExtra("address2",address);
                             intent.putExtra("city2",city);
                             intent.putExtra("state2",state);
                             intent.putExtra("country2",country);
                             intent.putExtra("latitude2",latitude);
                             intent.putExtra("longitude2",longitude);
                             intent.putExtra("postalcode2",postalCode);
                             intent.putExtra("dates2",dates);
                             intent.putExtra("Button",buttons);
                             startActivity(intent);
                         }
                     })
                     .setNegativeButton("Not this one", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             dialogInterface.cancel();
                         }
                     });

             AlertDialog alert = altdials.create();
             alert.setTitle("YOUR FETCHED LOCATION");
             alert.show();

         }
    }



}
