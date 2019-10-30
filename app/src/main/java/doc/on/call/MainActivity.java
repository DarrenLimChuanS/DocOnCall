package doc.on.call;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import doc.on.call.Utilities.ObscuredSharedPreference;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static int SPLASH_TIME_OUT = 3000;
    private ObscuredSharedPreference mSharedPreference;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        this.mSharedPreference = ObscuredSharedPreference.getPref(this);
        Log.d(TAG, "Hello");
        Log.d(TAG, "Token: " + mSharedPreference.readJWTToken());
        Log.d(TAG, "Nonce:" + mSharedPreference.readNonce());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MainActivity.this.startActivity(new Intent(MainActivity.this, SignInActivity.class));
                MainActivity.this.finish();
            }
        }, (long) SPLASH_TIME_OUT);
    }
}