package doc.on.call;

import androidx.lifecycle.LiveData;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import doc.on.call.Repository.PatientRepository;
import doc.on.call.Utilities.ObscuredSharedPreference;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private PatientRepository mPatient;
    private ObscuredSharedPreference mSharedPreference;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        mSharedPreference = new ObscuredSharedPreference(this);
        mPatient = new PatientRepository(this);
        Log.d(TAG, "Hello");
        Log.d(TAG, "Token: " + mSharedPreference.readJWTToken());
        Log.d(TAG, "Nonce: " + mSharedPreference.readNonce());

        // Check the session of user
        mPatient.checkLoggedIn();
    }
}