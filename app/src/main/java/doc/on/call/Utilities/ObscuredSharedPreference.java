package doc.on.call.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import static doc.on.call.Utilities.Constants.PREF_NONCE;
import static doc.on.call.Utilities.Constants.PREF_RESEND;
import static doc.on.call.Utilities.Constants.PREF_TOKEN;

public class ObscuredSharedPreference {
    // Fetch NDK
    static {
        System.loadLibrary("native-lib");
    }
    public static native String getPrefFile();

    // Variables
    private static ObscuredSharedPreference prefs;
    protected Context context;
    protected SharedPreferences sharedPreferences;
    private static byte[] SALT = null;
    private static char[] SEKRIT = null;
    protected static final String UTF8 = "UTF-8";

    public ObscuredSharedPreference(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(getPrefFile(), 0);
        ObscuredSharedPreference.setNewKey(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        ObscuredSharedPreference.setNewSalt(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
    }

    public static ObscuredSharedPreference getPref(Context context) {
        synchronized (ObscuredSharedPreference.class) {
            if (prefs == null) {
                prefs = new ObscuredSharedPreference(context);
            }
        }
        return prefs;
    }

    public static void setNewKey(String key) {
        SEKRIT = key.toCharArray();
    }

    public static void setNewSalt(String salt) {
        try {
            SALT = salt.getBytes(UTF8);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected String decrypt(String value){
        try {
            final byte[] bytes = value!=null ? Base64Support.decode(value,Base64Support.DEFAULT) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            return new String(pbeCipher.doFinal(bytes),UTF8);
        } catch( Exception e) {
            Log.e(this.getClass().getName(), "Warning, could not decrypt the value.  It may be stored in plaintext.  "+e.getMessage());
            return value;
        }
    }

    protected String encrypt( String value ) {

        try {
            final byte[] bytes = value!=null ? value.getBytes(UTF8) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            return new String(Base64Support.encode(pbeCipher.doFinal(bytes), Base64Support.NO_WRAP),UTF8);
        } catch( Exception e ) {
            throw new RuntimeException(e);
        }

    }

    /**
     * =================================== START OF GETTER SETTERS ===================================
     */
    public void writeNonce(String nonce) {
        Editor edit = this.sharedPreferences.edit();
        edit.putString(PREF_NONCE, encrypt(nonce));
        edit.apply();
    }

    public String readNonce() {
        String nonce = this.sharedPreferences.getString(PREF_NONCE, null);
        return nonce != null ? decrypt(nonce) : null;
    }

    public void writeJWTToken(String token) {
        Editor edit = this.sharedPreferences.edit();
        edit.putString(PREF_TOKEN, encrypt(token));
        edit.apply();
    }

    public String readJWTToken() {
        String token = this.sharedPreferences.getString(PREF_TOKEN, null);
        return token != null ? decrypt(token) : null;
    }

    public void writeRegisterationResendToken(String token) {
        Editor edit = this.sharedPreferences.edit();
        edit.putString(PREF_RESEND, encrypt(token));
        edit.apply();
    }

    public String readRegisterationResendToken() {
        String token = this.sharedPreferences.getString(PREF_RESEND, null);
        return token != null ? decrypt(token) : null;
    }

    public void removeSharedPreference(String key) {
        Editor edit = this.sharedPreferences.edit();
        edit.remove(key);
        edit.commit();
    }
    /**
     * =================================== END OF GETTER SETTERS ===================================
     */
}