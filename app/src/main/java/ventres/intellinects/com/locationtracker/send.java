package ventres.intellinects.com.locationtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class send extends AppCompatActivity {


    public String userid;
    public String address; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    public String city;
    public String state;
    public String country;
    public String postalCode;
    public String dates;
    public String status;
    private Button sent;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        imports();

        sent =(Button) findViewById(R.id.sent);
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendData();
            }
        });

    }

    public void imports(){
        LoginActivity loginActivity = new LoginActivity();
        userid=loginActivity.userid;
        MainActivity mainActivity = new MainActivity();
        address = mainActivity.address;
        city = mainActivity.city;
        state = mainActivity.state;
        country = mainActivity.country;
        postalCode = mainActivity.postalCode;
        dates = mainActivity.dates;
        status = mainActivity.status;
    }

}
