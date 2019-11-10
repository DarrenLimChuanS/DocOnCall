package doc.on.call.Utilities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Transform {
    private Context mcontext;
    private static final String VERIFY_SIGNATURE = "o5uO2nTdkE3LShePRXMHcuouLtp6q3fx9HkE8c/9OC0=";

    public Transform(Context context) {
        this.mcontext = context;
    }

    /**
     * Check for usb_debugging turned on
     *
     * @return true if usb_debugging turned on for user device
     */
    public boolean isUSBDebugging_turned_on() {
        // debugging enabled
        // debugging turned off
        return Settings.Secure.getInt(mcontext.getContentResolver(), Settings.Global.ADB_ENABLED, 0) == 1;
    }


    /**
     * Check for signature of signer packager
     *
     * @return true if file binary "busybox" exist
     */
    public boolean checkPackageIntegrity() {
        try {
            PackageInfo packageInfo = mcontext.getPackageManager().getPackageInfo(mcontext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA-256");
                md.update(signature.toByteArray());
                final String currentSignature = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                Log.d("REMOVE_ME", "Signature to check: " + VERIFY_SIGNATURE);
                Log.d("REMOVE_ME", "Signature of currt: " + currentSignature);
                if (VERIFY_SIGNATURE.compareTo(currentSignature) == 0) {
                    return false;
                } else {
                    Log.d("REMOVE_ME", "Signature to check: " + VERIFY_SIGNATURE);
                    Log.d("REMOVE_ME", "Signature of currt: " + currentSignature);
                    return true;
                }
            }
        } catch (NoSuchAlgorithmException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
