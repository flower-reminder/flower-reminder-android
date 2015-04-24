package lt.andro.flowr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


/**
 * Example shell activity which simply broadcasts to our receiver and exits.
 */
public class NearByShopBroadcastActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent();
        i.setAction("lt.andro.flowr.SHOW_NOTIFICATION");
        i.putExtra(NearByShopPostNotificationReceiver.CONTENT_KEY, getString(R.string.title));
        sendBroadcast(i);
        finish();
    }
}
