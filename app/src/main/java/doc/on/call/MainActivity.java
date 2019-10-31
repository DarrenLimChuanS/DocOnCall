package doc.on.call;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import doc.on.call.Model.Patient;
import doc.on.call.Repository.PatientRepository;
import doc.on.call.Utilities.ObscuredSharedPreference;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private PatientRepository mPatient;
    private ObscuredSharedPreference mSharedPreference;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        mSharedPreference = ObscuredSharedPreference.getPref(this);
        mPatient = new PatientRepository(this);
        Log.d(TAG, "Hello");
        Log.d(TAG, "Token: " + mSharedPreference.readJWTToken());
        Log.d(TAG, "Nonce: " + mSharedPreference.readNonce());

        // Check the session of user
        mPatient.checkLoggedIn();
    }
}