package ventres.intellinects.com.locationtracker;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import static java.lang.Math.abs;
import static ventres.intellinects.com.locationtracker.LoginActivity.PREF_NAME;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private  static final String TAG= MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION_FINE_LOCATION_RESULT = 0;
    private TextView locationText;
    private Button sendLocation;
    private LocationManager locationManager;
    public String address;
    public String city;
    public String state;
    public String country;
    public String postalCode;
    public String knownName;
    public String dates;
    public String status;
    public String userid;
    public double latitude;
    public double longitude;
    private double defaultValue=0.00;
    private double myLatitude =0.00;
    private double myLongitude = 0.00;
    public String typedaddress;
    String whichButton;
    Calendar calendar;
    RadioButton rb;
    private ProgressDialog progressDialog;
    RadioGroup radioGroup;
    private static final String SEND_URL ="http://essl.intellinects.org/location.php";
    public static final String KEY_USERID="userid";
    public static final String KEY_ADDRESS="useraddress";
    public static final String KEY_CITY="city";
    public static final String KEY_STATE="state";
    public static final String KEY_COUNTRY="country";
    public static final String KEY_DATES="dates";
    public static final String KEY_TYPEDADDRESS ="typedaddress";
    public static final String KEY_POSTALCODE="postalcode";
    public static final String KEY_STATUS="status";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        typedaddress="None";
        locationText =(TextView) findViewById(R.id.locationText);
        sendLocation =(Button) findViewById(R.id.send_location);
        getuserdata();
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        getLocation();

                    }else{
                            if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                Toast.makeText(getApplicationContext(), "App requires access location", Toast.LENGTH_SHORT).show();
                            }else {
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FINE_LOCATION_RESULT);
                            }
                    }
                }else{
                        getLocation();
                }

                Intent intent = getIntent();
                whichButton =intent.getStringExtra("Button") ;
                    displaygetintent();
                    displayLocationText();
                    sendLocations();


    }


    void getLocation(){
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 2, this);
        }catch(SecurityException e){
            Toast.makeText(getApplicationContext(),"Security Exception in Location",Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }

    }

    @Override
    public void onLocationChanged(Location location) {
      try{
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Security Exception",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION_FINE_LOCATION_RESULT){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"Application cannot run without location permission",Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getApplicationContext(),"GPS ENABLED",Toast.LENGTH_LONG).show();
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

                        if(whichButton.equals("predfined")) {
                            if (distance(latitude, longitude, myLatitude, myLongitude) < 0.05) {
                                if(myLatitude !=0) {
                                    sendData();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Please Wait while verifying your Location"+"\n"+"Depends on INTERNET CONNECTIVITY SPEED",Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Outside Locations", Toast.LENGTH_LONG).show();
                            }
                        }
                        if(whichButton.equals("getAddress")){
                            //mumbai office
                            if(distance(19.057348,72.847465,latitude,longitude)<0.05){
                                       if(latitude !=0){
                                           sendData();
                                       }else{
                                           Toast.makeText(getApplicationContext(),"Please Wait while Location Loads"+"\n"+"Depends on INTERNET CONNECTIVITY SPEED",Toast.LENGTH_LONG).show();
                                       }
                            }else if(distance(20.005039,73.775965,latitude,longitude)<0.05){//nashik office
                                if(latitude !=0){
                                    sendData();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Please Wait while Location Loads"+"\n"+"Depends on INTERNET CONNECTIVITY SPEED",Toast.LENGTH_LONG).show();
                                }
                            }else{
          //                      Toast.makeText(getApplicationContext(),"OUTSIDE OFFICE LOCATION",Toast.LENGTH_LONG).show();
                                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                                        final View mView = getLayoutInflater().inflate(R.layout.takenameoflocation,null);
                                        final EditText ueAddress =(EditText) mView.findViewById(R.id.uaddress);
                                        Button ok = (Button) mView.findViewById(R.id.ok);
                                        mBuilder.setView(mView);
                                        AlertDialog dialog = mBuilder.create();
                                        dialog.show();
                                        ok.setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View view) {
                                                                      typedaddress= ueAddress.getText().toString();
                                                                      sendData();

                                                                  }
                                        });


                            }

                        }

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
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "NOT IN OFFICE LOCATION "+"\n Please ENTER OFFICE"/*error.toString()*/, Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String>params = new HashMap<String, String>();
                params.put(KEY_USERID,userid);
                params.put(KEY_ADDRESS,address);
                params.put(KEY_TYPEDADDRESS,typedaddress);
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("While data is sent....... ");
        progressDialog.setTitle("Please Wait!!!");
        progressDialog.show();
    }

    public void displaygetintent(){
        Intent intent = getIntent();
        address = intent.getStringExtra("address2");
        city = intent.getStringExtra("city2");
        state = intent.getStringExtra("state2");
        country = intent.getStringExtra("country2");
        latitude = intent.getDoubleExtra("latitude2",defaultValue);
        longitude = intent.getDoubleExtra("longitude2",defaultValue);
        postalCode = intent.getStringExtra("postalcode2");
        dates = intent.getStringExtra("dates2");
      //  Toast.makeText(getApplicationContext(),""+longitude,Toast.LENGTH_LONG).show();
    }
    public void getuserdata(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        userid=sharedPreferences.getString("userid","");

    }
    public void displayLocationText(){
        locationText.setText(address);
    }

    public double distance(double deflat,double deflon,double mylat,double mylong){

        double dist=0;
        double earthRadius = 3958.75;

        double dLat = Math.toRadians(abs(deflat-mylat));
        double dLng = Math.toRadians(abs(deflon-mylong));

        double sindLat = Math.sin(dLat/2);
        double sindLng = Math.sin(dLng/2);

        double a = Math.pow(sindLat, 2) + (Math.pow(sindLng, 2) * Math.cos(Math.toRadians(mylat)) * Math.cos(Math.toRadians(deflat)));
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        dist = earthRadius*c;
        return dist;
    }



}
