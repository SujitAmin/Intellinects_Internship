package ventres.intellinects.com.locationtracker;

import ventres.intellinects.com.locationtracker.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.app.ProgressDialog;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements TextWatcher,CompoundButton.OnCheckedChangeListener{

    public static final String LOGIN_URL="http://essl.intellinects.org/login.php";
    public static final String LOGIN_SUCCESS="success";
    private EditText  etUserid,etPass;
    private CheckBox rem_userpass;
    private Button login;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREF_NAME ="prefs";
    public static final String KEY_USERID ="userid";
    public static final String KEY_REMEMBER ="remember";
    public static final String KEY_PASS= "password";
    public  String userid;
    public String password;
    ConnectionDetector cd;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        cd = new ConnectionDetector(this);
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        etUserid =  (EditText) findViewById(R.id.userId);
        etPass =(EditText) findViewById(R.id.pass);
        rem_userpass = (CheckBox) findViewById(R.id.checkboxs);
        login = (Button) findViewById(R.id.login);
        if(sharedPreferences.getBoolean(KEY_REMEMBER,false)){
            rem_userpass.setChecked(true);
        }else{
            rem_userpass.setChecked(false);
        }
        etUserid.setText(sharedPreferences.getString(KEY_USERID,""));
        etPass.setText(sharedPreferences.getString(KEY_PASS,""));

        etUserid.addTextChangedListener(this); //here
        etPass.addTextChangedListener(this);
        rem_userpass.setOnCheckedChangeListener(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cd.isConnected()) {
                    String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +

                            "\\@" +

                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                            "(" +

                            "\\." +

                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                            ")+";

                    String userid = etUserid.getText().toString();
                    Matcher matcher = Pattern.compile(validemail).matcher(userid);

                    if (matcher.matches()) {
                        login();
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter Valid Email-Id", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Snackbar.make(view,"No internet connection",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                }
            }
        });
    }

    private void login() {

         userid = etUserid.getText().toString().trim(); //here
         password = etPass.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if(response.trim().equals("success")){
                            Intent intent = new Intent(LoginActivity.this,Main2Activity.class);
                            intent.putExtra("useridsss",userid); //here
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_USERID,userid); //here
                map.put(KEY_PASS,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Checking Credentials....");
        progressDialog.setTitle("Please Wait!!!");
        progressDialog.show();
    }





    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            managePrefs();
    }

     @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        managePrefs();
    }
    private void managePrefs(){
        if(rem_userpass.isChecked()){
            editor.putString(KEY_USERID,etUserid.getText().toString().trim()); //here
            editor.putString(KEY_PASS,etPass.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER,true);
            editor.apply();
        }else{
            editor.putBoolean(KEY_REMEMBER,false);
            editor.remove(KEY_PASS);
            editor.remove(KEY_USERID);  //here
            editor.apply();
        }
    }

}
