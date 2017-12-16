package ventres.intellinects.com.locationtracker;

import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by suman on 12-12-2017.
 */

public class ConnectionDetector {

    public Context context;
    public ConnectionDetector(Context context) {
            this.context = context;
    }
    public boolean isConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if (connectivityManager !=null){
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if(info!=null){
                if(info.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }
}
