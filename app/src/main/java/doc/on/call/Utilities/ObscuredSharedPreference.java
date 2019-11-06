package doc.on.call.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import static doc.on.call.Utilities.Constants.PREF_NONCE;
import static doc.on.call.Utilities.Constants.PREF_RESEND;
import static doc.on.call.Utilities.Constants.PREF_TOKEN;

public class ObscuredSharedPreference {

    // Fetch NDK
    static {
        System.loadLibrary("native-lib");
    }

    public static native String getPrefFile();

    protected Context context;
    private SharedPreferences sharedPreferences;
    private static String alias;
    private static KeyStore keystore;
    private static SecretKey secret_key;


    public ObscuredSharedPreference(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(getPrefFile(), 0);
        keystore = create_AndroidKeyStore();
        ObscuredSharedPreference.set_alias(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        Log.d("DocOnCall", "keystore_alias: " + alias);
        ObscuredSharedPreference.get_SymmetricKeyinKeyStore();
    }

    public static void get_SymmetricKeyinKeyStore() {
        try {
            Log.d("Token", "keystore alias exist: " + keystore.containsAlias(alias));
            if (!keystore.containsAlias(alias)){
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(alias,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setRandomizedEncryptionRequired(true)
                        .build();
                keyGenerator.init(keyGenParameterSpec);
                secret_key = keyGenerator.generateKey();
            }else {
                KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keystore.getEntry(alias, null);
                secret_key = secretKeyEntry.getSecretKey();
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException | KeyStoreException | InvalidAlgorithmParameterException | UnrecoverableEntryException e) {
            e.printStackTrace();
        }
    }

    public static void set_alias(String name) {
        alias = name;
    }

    public static KeyStore create_AndroidKeyStore() {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyStore;
    }

    protected String decrypt(HashMap<String, String> outputMap) {
        byte[] encryption_data = (outputMap.get("encrypted_data") != null) ? Base64.decode(outputMap.get("encrypted_data"), Base64.NO_WRAP) : new byte[0];
        byte[] encryption_IV = (outputMap.get("encrypted_iv") != null) ? Base64.decode(outputMap.get("encrypted_iv"), Base64.NO_WRAP) : new byte[0];

        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, encryption_IV);

            ByteBuffer byteBuffer = ByteBuffer.wrap(encryption_data);
            byte[] encrypted = new byte[byteBuffer.remaining()];
            byteBuffer.get(encrypted);

            cipher.init(Cipher.DECRYPT_MODE, secret_key, spec);
            String decrypted_string = new String(cipher.doFinal(encrypted));
            return decrypted_string;

        } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
            return new String(encryption_data);
        }

    }

    protected void encrypt(String value, String action_path) {
        final byte[] bytes = value != null ? value.getBytes() : new byte[0];
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secret_key);
            HashMap<String, String> EncryptedInMap = new HashMap<>();
            String iv_storage = new String(Base64.encode(cipher.getIV(), Base64.NO_WRAP));
            Log.d ("Token", "encode IV used: " + iv_storage);
            Log.d ("Token", "encode_to_string IV used: " + Base64.encodeToString(cipher.getIV(), Base64.NO_WRAP));
            Log.d("Token", "plaintext: " + value);
            String encrypted = new String(Base64.encode(cipher.doFinal(bytes), Base64.NO_WRAP));
            Log.d("Token", "encode ciphertext: " + encrypted);
            EncryptedInMap.put("encrypted_iv", iv_storage);
            EncryptedInMap.put("encrypted_data", encrypted);
            saveMap(EncryptedInMap, action_path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * =================================== START OF HASH MAP ===================================
     */

    private void saveMap(HashMap<String, String> inputMap, String path) {
        SharedPreferences pSharedPref = this.sharedPreferences;
        if (pSharedPref != null) {
            if (path.equals(PREF_NONCE)) {
                JSONObject jsonObject = new JSONObject(inputMap);
                String jsonString = jsonObject.toString();
                SharedPreferences.Editor editor = pSharedPref.edit();
                editor.remove(PREF_NONCE).apply();
                editor.putString(PREF_NONCE, jsonString);
                editor.commit();
            } else if (path.equals(PREF_TOKEN)) {
                JSONObject jsonObject = new JSONObject(inputMap);
                String jsonString = jsonObject.toString();
                SharedPreferences.Editor editor = pSharedPref.edit();
                editor.remove(PREF_TOKEN).apply();
                editor.putString(PREF_TOKEN, jsonString);
                editor.commit();
            } else {
                JSONObject jsonObject = new JSONObject(inputMap);
                String jsonString = jsonObject.toString();
                SharedPreferences.Editor editor = pSharedPref.edit();
                editor.remove(PREF_RESEND).apply();
                editor.putString(PREF_RESEND, jsonString);
                editor.commit();
            }
        }
    }

    private HashMap<String, String> loadMap(String token_value) {
        HashMap<String, String> outputMap = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(token_value);
            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                outputMap.put(key, jsonObject.get(key).toString());
            }
            return outputMap;
        } catch (Exception e) {
            return outputMap;
        }
    }

    /**
     * =================================== END OF HASH MAP ===================================
     */

    /**
     * =================================== START OF GETTER SETTERS ===================================
     */
    public void writeNonce(String nonce) {
        encrypt(nonce, PREF_NONCE);
    }

    public String readNonce() {
        String token = this.sharedPreferences.getString(PREF_NONCE, null);
        if (token != null) {
            HashMap<String, String> outputMap = loadMap(token);
            if (!outputMap.isEmpty()) {
                return decrypt(outputMap);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void writeJWTToken(String token) {
        encrypt(token, PREF_TOKEN);
    }

    public String readJWTToken() {
        String token = this.sharedPreferences.getString(PREF_TOKEN, null);
        if (token != null) {
            HashMap<String, String> outputMap = loadMap(token);
            if (!outputMap.isEmpty()) {
                return decrypt(outputMap);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void writeRegisterationResendToken(String token) {
        encrypt(token, PREF_RESEND);
    }

    public String readRegisterationResendToken() {
        String token = this.sharedPreferences.getString(PREF_RESEND, null);
        if (token != null) {
            HashMap<String, String> outputMap = loadMap(token);
            if (!outputMap.isEmpty()) {
                return decrypt(outputMap);
            } else {
                return null;
            }
        } else {
            return null;
        }
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