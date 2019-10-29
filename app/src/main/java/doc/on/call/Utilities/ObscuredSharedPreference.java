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
    public String readJWTToken() {
        String string = this.sharedPreferences.getString(Constants.PREF_TOKEN, null);
        return string != null ? decrypt(string) : null;
    }

    public String readNonce() {
        String string = this.sharedPreferences.getString(Constants.PREF_NONCE, null);
        return string != null ? decrypt(string) : null;
    }

    public void writeJWTToken(String str) {
        Editor edit = this.sharedPreferences.edit();
        edit.putString(Constants.PREF_TOKEN, encrypt(str));
        edit.apply();
    }

    public void writeNonce(String str) {
        Editor edit = this.sharedPreferences.edit();
        edit.putString(Constants.PREF_NONCE, encrypt(str));
        edit.apply();
    }

    public void removeSharedPreference(String str) {
        Editor edit = this.sharedPreferences.edit();
        edit.remove(str);
        edit.commit();
    }
    /**
     * =================================== END OF GETTER SETTERS ===================================
     */
}