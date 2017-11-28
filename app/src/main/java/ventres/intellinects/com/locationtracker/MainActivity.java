package ventres.intellinects.com.locationtracker;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationListener{
    private  static final String TAG= MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION_FINE_LOCATION_RESULT = 0;
    private TextView locationText;
    private Button sendLocation;
    private Button getLocationBtn;
    private LocationManager locationManager;
    public String address; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    public String city;
    public String state;
    public String country;
    public String postalCode;
    public String knownName;
    public String dates;
    public String status;
    public String userid;
    Calendar calendar;
    RadioButton rb;
    public ProgressDialog progressDialog;
    // Only if available else return NULL
    RadioGroup radioGroup;

    private static final String SEND_URL ="http://essl.intellinects.org/location.php";
    public static final String KEY_USERID="userid";
    public static final String KEY_ADDRESS="useraddress";
    public static final String KEY_CITY="city";
    public static final String KEY_STATE="state";
    public static final String KEY_COUNTRY="country";
    public static final String KEY_DATES="dates";
    public static final String KEY_POSTALCODE="postalcode";
    public static final String KEY_STATUS="status";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        userid = getIntent().getStringExtra("useridsss");
        locationText =(TextView) findViewById(R.id.locationText);
        getLocationBtn =(Button)findViewById(R.id.getLocationBtn);
        sendLocation =(Button) findViewById(R.id.send_location);

        getLocationBtn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("LOADING");
                progressDialog.setMessage("Please wait for some time......");
                progressDialog.show();
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                               getLocation();

                        }
                        else{
                            if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                Toast.makeText(getApplicationContext(), "App requires access location", Toast.LENGTH_SHORT).show();
                            }
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSION_FINE_LOCATION_RESULT);
                        }
                }else{
                        getLocation();
                    }
                progressDialog.cancel();
            }
        });

                sendLocations();


    }


    void getLocation(){

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }catch(SecurityException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode == REQUEST_PERMISSION_FINE_LOCATION_RESULT){
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"Application cannot run without location permission",Toast.LENGTH_SHORT).show();
                }
            }
    }

    @Override
    public void onLocationChanged(Location location) {
        locationText.setText("Latitude:"+location.getLatitude()+"\n Longitude:"+location.getLongitude());
        try{
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            calendar = Calendar.getInstance();
       //      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd ");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            dates = simpleDateFormat.format(calendar.getTime());
            knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            /* locationText.setText(locationText.getText()+"\n"+address+"\n"+city+"\n"+state+"\n"
                    +country+"\n"+postalCode+"\n"+knownName);
*/
           locationText.setText(""+address);
         /*  locationText.setText(locationText.getText()+"\n"+addresses.get(0).getAddressLine(0)
           +", "+addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));*/
         //finish();
            Toast.makeText(getApplicationContext(),"LOCATION FOUND",Toast.LENGTH_LONG).show();

        }catch(Exception e){}

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
            Toast.makeText(MainActivity.this,"Please Enable GPS and INTERNET",Toast.LENGTH_LONG).show();
    }

    public void sendLocations(){
        radioGroup =  (RadioGroup)findViewById(R.id.radioGroup);
        sendLocation =(Button) findViewById(R.id.send_location);
        sendLocation.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = radioGroup.getCheckedRadioButtonId();
            rb =(RadioButton) radioGroup.findViewById(i);

            if(R.id.in == i){
                status ="in";
            }
            if(R.id.out == i){
                status ="out";
            }
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("You have clicked '"+rb.getText()+"'");
                alertDialogBuilder.setMessage("Are you sure you are "+rb.getText().toString().toLowerCase()+" ?");
                alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            sendData();
                    }
                });
                alertDialogBuilder.setNegativeButton("NO", null);


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


        }
    });







    }
    private void sendData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String>params = new HashMap<String, String>();
                params.put(KEY_USERID,userid);
                params.put(KEY_ADDRESS,address);
                params.put(KEY_CITY,city);
                params.put(KEY_STATE,state);
                params.put(KEY_COUNTRY,country);
                params.put(KEY_POSTALCODE,postalCode);
                params.put(KEY_DATES,dates);
                params.put(KEY_STATUS,status);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




}
