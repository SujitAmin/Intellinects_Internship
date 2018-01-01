package ventres.intellinects.com.locationtracker;

import android.app.DialogFragment;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by suman on 03-12-2017.
 */

public class predinedLocation extends DialogFragment implements LocationListener {

    public String address; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    public String city;
    public String state;
    public String country;
    public String postalCode;
    public String dates;
    public double latitude;
    public double longitude;
    Calendar calendar;
    String buttons="predfined";

    Button btn;
    ListView lv;
    SearchView sv;
    ArrayAdapter<String> adapter;
    String[] location = {"Mumbai Corporate office","Nashik Office","Gandhi Nagar Office"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.predefined_location,null);

        lv = (ListView) rootView.findViewById(R.id.listView1);
        sv = (SearchView) rootView.findViewById(R.id.searchView1);
        btn = (Button) rootView.findViewById(R.id.dismiss);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,location);
        lv.setAdapter(adapter);

        sv.setQueryHint("Search...");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);

                return false;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                switch(position) {
                    case 0:
                        latitude=19.057348;
                        longitude =72.847465;
                        address = "2B, Siddhi Vinayak Chambers, " +
                                "Bandra East, Samadhan Cooperative Housing Society, Gandhi Nagar, Bandra East, Mumbai, " +
                                "Maharashtra 400051"; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        city = "Mumbai";
                        state = "Maharashtra";
                        country = "India";
                        postalCode = "400051";
                        calendar = Calendar.getInstance();
                        dates = simpleDateFormat.format(calendar.getTime());

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("address2",address);
                        intent.putExtra("city2",city);
                        intent.putExtra("state2",state);
                        intent.putExtra("country2",country);
                        intent.putExtra("postalcode2",postalCode);
                        intent.putExtra("dates2",dates);
                        intent.putExtra("latitude2",latitude);
                        intent.putExtra("longitude2",longitude);
                        intent.putExtra("Button",buttons);
                        startActivity(intent);

                    case 1:
                        latitude=20.005039;
                        longitude=73.775965;
                        address="Intellinects Ventures Pvt. Ltd. 7 , Chandrakansha Apt , " +
                                "opp Ananda Laundry, Pandit  colony, Lane no 1, Gangapur road, " +
                                "Nashik 422002, India";
                        city ="Nashik";
                        state ="Maharashtra";
                        country="India";
                        postalCode="422002";
                        calendar = Calendar.getInstance();
                        dates = simpleDateFormat.format(calendar.getTime());
                        intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("address2",address);
                        intent.putExtra("city2",city);
                        intent.putExtra("state2",state);
                        intent.putExtra("country2",country);
                        intent.putExtra("postalcode2",postalCode);
                        intent.putExtra("dates2",dates);
                        intent.putExtra("latitude2",latitude);
                        intent.putExtra("longitude2",longitude);
                        intent.putExtra("Button",buttons);
                        startActivity(intent);

                    case 2:
                        latitude=19.058784;
                        longitude=72.849320;
                        address="Shop No. 2-1/1-C, Next to Samaj Mandir Hall, Shastri Nagar, Bandra East, Government Colony, Bandra East, Mumbai, Maharashtra 400051";
                        city ="Mumbai";
                        state ="Maharashtra";
                        country="India";
                        postalCode="400051";
                        calendar = Calendar.getInstance();
                        dates = simpleDateFormat.format(calendar.getTime());
                        intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("address2",address);
                        intent.putExtra("city2",city);
                        intent.putExtra("state2",state);
                        intent.putExtra("country2",country);
                        intent.putExtra("postalcode2",postalCode);
                        intent.putExtra("dates2",dates);
                        intent.putExtra("latitude2",latitude);
                        intent.putExtra("longitude2",longitude);
                        intent.putExtra("Button",buttons);
                        startActivity(intent);




                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return rootView;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
