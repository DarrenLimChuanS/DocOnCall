package doc.on.call.Utilities;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Checker {
    private static final String[] NonWritablePath = {
            "/system",
            "/system/bin",
            "/system/sbin",
            "/system/xbin",
            "/vendor/bin",
            "/sbin",
            "/etc",
            //"/sys",
            //"/proc",
            //"/dev"
    };
    private Context mcontext;
    private String[] binaryPaths = {
            "/data/local/",
            "/data/local/bin/",
            "/data/local/xbin/",
            "/sbin/",
            "/su/bin/",
            "/system/bin/",
            "/system/bin/.ext/",
            "/system/bin/failsafe/",
            "/system/sd/xbin/",
            "/system/usr/we-need-root/",
            "/system/xbin/",
            "/cache",
            "/data",
            "/dev"
    };
    // contains list of knownRootCloakingPackages, knownDangerousAppsPackages & knownRootAppsPackages
    private String[] Root_Related_Packages = {
            "com.noshufou.android.su",
            "com.noshufou.android.su.elite",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.yellowes.su",
            "com.topjohnwu.magisk",
            "com.koushikdutta.rommanager",
            "com.koushikdutta.rommanager.license",
            "com.dimonvideo.luckypatcher",
            "com.chelpus.lackypatch",
            "com.ramdroid.appquarantine",
            "com.ramdroid.appquarantinepro",
            "com.android.vending.billing.InAppBillingService.COIN",
            "com.chelpus.luckypatcher",
            "com.devadvance.rootcloak",
            "com.devadvance.rootcloakplus",
            "de.robv.android.xposed.installer",
            "com.saurik.substrate",
            "com.zachspong.temprootremovejb",
            "com.amphoras.hidemyroot",
            "com.amphoras.hidemyrootadfree",
            "com.formyhm.hiderootPremium",
            "com.formyhm.hideroot"
    };

    public Checker(Context context) {
        this.mcontext = context;
    }

    /**
     * Check if device is rooted
     *
     * @return true if one or more of the method called returned true
     */
    public boolean isDeviceRooted() {
        return checkForBusyBoxBinary() || checkForSuBinary() || checkForRWPaths() ||
                checkSuExists() || root_su_package_exist() || checkForMagiskBinary() ||
                checkDebuggable() || isEmulator_M2() || isEmulator_M1();
    }

    /**
     * Check for "su" binary in binary_paths
     *
     * @return true if file binary "su" exist
     */
    private boolean checkForSuBinary() {
        return checkForBinary("su");
    }

    /**
     * Check for "busybox" binary in binary_paths
     *
     * @return true if file binary "busybox" exist
     */
    private boolean checkForBusyBoxBinary() {
        return checkForBinary("busybox");
    }

    /**
     * Check for "magisk" binary in binary_paths
     *
     * @return true if file binary "magisk" exist
     */
    private boolean checkForMagiskBinary() {
        return checkForBinary("magisk");
    }

    /**
     * Check for file binary in each binary_path
     *
     * @return true if file binary specified exists
     */
    private boolean checkForBinary(String filename) {
        for (String path : binaryPaths) {
            File f = new File(path, filename);
            boolean fileExists = f.exists();
            if (fileExists) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check for if "su" exist
     *
     * @return true if "su" exists
     */
    private boolean checkSuExists() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]
                    {"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line = in.readLine();
            process.destroy();
            return line != null;
        } catch (Exception e) {
            if (process != null) {
                process.destroy();
            }
            return false;
        }
    }

    /**
     * Check if any root or su related package is installed
     *
     * @return true if any of the package is installed
     */
    // Check if root or su related packages is installed
    private boolean root_su_package_exist() {
        PackageManager pm = mcontext.getPackageManager();
        for (String target_package : Root_Related_Packages) {
            if (isPackageInstalled(target_package, pm)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if package is installed in the android device
     *
     * @return true if the package name is installed
     */
    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * When you're root you can change the permissions on common system directories, this method checks if any of these patha Const.pathsThatShouldNotBeWrtiable are writable.
     *
     * @return true if one of the dir is writable
     */
    private boolean checkForRWPaths() {

        boolean result = false;

        String[] lines = mountReader();

        if (lines == null) {
            // Could not read, assume false;
            return false;
        }

        for (String line : lines) {

            // Split lines into parts
            String[] args = line.split(" ");

            if (args.length < 4) {
                // If we don't have enough options per line, skip this
                continue;
            }

            String mountPoint = args[1];
            String mountOptions = args[3];

            for (String pathToCheck : NonWritablePath) {
                if (mountPoint.equalsIgnoreCase(pathToCheck)) {

                    // Split options out and compare against "rw" to avoid false positives
                    for (String option : mountOptions.split(",")) {

                        if (option.equalsIgnoreCase("rw")) {
                            // path is mounted with rw permissions!
                            result = true;
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }

    private String[] mountReader() {
        try {
            InputStream inputstream = Runtime.getRuntime().exec("mount").getInputStream();
            if (inputstream == null) return null;
            String propVal = new Scanner(inputstream).useDelimiter("\\A").next();
            return propVal.split("\n");
        } catch (IOException | NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method 1: Check if application is running in emulator
     *
     * @return true if show signs of emulator
     */
    private boolean isEmulator_M1() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator");
    }

    /**
     * Method 2: Check if application is running in emulator
     *
     * @return true if show signs of emulator
     */
    private boolean isEmulator_M2() {
        TelephonyManager tm = (TelephonyManager) mcontext.getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tm.getNetworkOperatorName();
        return "Android".equals(networkOperator);
    }

    /**
     * Method 1: Check if application is running in debuggable mode
     *
     * @return true if show signs of debuggable mode
     */
    private boolean checkDebuggable() {
        return (mcontext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

}