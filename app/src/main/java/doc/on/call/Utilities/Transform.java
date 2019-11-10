package doc.on.call.Utilities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.provider.Settings;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Transform {

    // Fetch NDK
    static {
        System.loadLibrary("native-lib");
    }

    public static native String getVerifySignature();

    private Context context;

    public Transform(Context context) {
        this.context = context;
    }

    /**
     * Check for usb_debugging turned on
     *
     * @return true if usb_debugging turned on for user device
     */
    public boolean isUSBDebugging_turned_on() {
        // debugging enabled
        // debugging turned off
        return Settings.Secure.getInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 0) == 1;
    }


    /**
     * Check for signature of signer packager
     *
     * @return true if file binary "busybox" exist
     */
    public boolean checkPackageIntegrity() {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA-256");
                md.update(signature.toByteArray());
                final String currentSignature = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                if (getVerifySignature().compareTo(currentSignature) == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (NoSuchAlgorithmException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
