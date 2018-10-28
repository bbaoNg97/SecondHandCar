package my.edu.tarc.secondhandcar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Bbao on 28/10/2018.
 */

public class BookingBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

       Toast.makeText(context,"You received a broadcast",Toast.LENGTH_LONG).show();
    }
}
