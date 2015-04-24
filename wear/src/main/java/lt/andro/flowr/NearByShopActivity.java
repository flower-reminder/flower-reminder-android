package lt.andro.flowr;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class NearByShopActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_shop);
        mTextView = (TextView) findViewById(R.id.text);
    }
}