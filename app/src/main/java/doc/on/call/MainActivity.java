package doc.on.call;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import doc.on.call.Repository.PatientRepository;
import doc.on.call.Utilities.Checker;
import doc.on.call.Utilities.CheckerDialog;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        // Check if Device is rooted or emulator
        Checker check_root = new Checker(this);
        if(check_root.isDeviceRooted()) {
            CheckerDialog warning_dialog = new CheckerDialog(this);
            warning_dialog.DisplayDialog();
        } else {
            // Allowed to proceed
            PatientRepository mPatient = new PatientRepository(this);

            // Check the session of user
            mPatient.checkLoggedIn();
        }
    }
}