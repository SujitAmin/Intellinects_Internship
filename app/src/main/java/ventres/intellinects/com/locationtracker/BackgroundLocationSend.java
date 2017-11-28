package ventres.intellinects.com.locationtracker;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by suman on 13-11-2017.
 */

public class BackgroundLocationSend extends AsyncTask<String, Void, String> {

    String city;
    String state;
    String address;
    String country;
    String postalCode;
    String knownName;
    String dates;
    Context ctx;

    BackgroundLocationSend(Context ctx){
        this.ctx= ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {

        if(result.equals("Location Sent Successfully...."))
        {
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... strings) {
        String locns="http://10.0.2.2/Tracker/inserts.php"  ;
        String method = strings[0];
        if(method.equals("MainActivity")){
//            backgroundLocationSend.execute(method,address,city,state,country,postalCode,knownName,dates);

            address = strings[1];
            city = strings[2];
            state = strings[3];
            country = strings[4];
            postalCode = strings[5];
            knownName = strings[6];
            dates = strings[7];
            //we are using http url
            try {
                URL url = new URL(locns);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS =  httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"+
                        URLEncoder.encode("city","UTF-8")+"="+URLEncoder.encode(city,"UTF-8")+"&"+
                        URLEncoder.encode("state","UTF-8")+"="+URLEncoder.encode(state,"UTF-8")+"&"+
                        URLEncoder.encode("country","UTF-8")+"="+URLEncoder.encode(country,"UTF-8")+"&"+
                        URLEncoder.encode("postalCode","UTF-8")+"="+URLEncoder.encode(postalCode,"UTF-8")+"&"+
                        URLEncoder.encode("knownName","UTF-8")+"="+URLEncoder.encode(knownName,"UTF-8")+"&"+
                        URLEncoder.encode("dates","UTF-8")+"="+URLEncoder.encode(dates,"UTF-8");
                        bufferedWriter.write(data);
                        bufferedWriter.close();
                        OS.close();
                        InputStream IS = httpURLConnection.getInputStream();
                        IS.close();
                        return  "Location Sent Successfully....";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
