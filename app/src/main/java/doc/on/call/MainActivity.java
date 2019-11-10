package doc.on.call;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import doc.on.call.Repository.PatientRepository;
import doc.on.call.Utilities.Checker;
import doc.on.call.Utilities.CheckerDialog;
import doc.on.call.Utilities.Transform;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

//        checker function SUPER RADAR MODE
        Transform check_package = new Transform(this);
        Checker check_root = new Checker(this);
        if (check_package.checkPackageIntegrity()) {
            //Package integrity compromised
            CheckerDialog warning_dialog = new CheckerDialog(this, R.string.alert_checker_message_code_tampering);
            warning_dialog.DisplayDialog();
        } else if (check_package.isUSBDebugging_turned_on()) {
            //Device usb debugging mode enabled
            CheckerDialog warning_dialog = new CheckerDialog(this, R.string.alert_checker_debugging_turned_on);
            warning_dialog.DisplayDialog();
        } else if (check_root.isDeviceRooted()) {
            // Device is rooted or ran in emulator
            CheckerDialog warning_dialog = new CheckerDialog(this, R.string.alert_checker_message);
            warning_dialog.DisplayDialog();
        } else {
            // Allowed to proceed
            PatientRepository mPatient = new PatientRepository(this);

            // Check the session of user
            mPatient.checkLoggedIn();
        }
    }

}