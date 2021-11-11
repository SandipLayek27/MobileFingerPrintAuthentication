package com.sandiplayek.auth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler  extends FingerprintManager.AuthenticationCallback {

    private Context context;
    OnResponseListener onResponseListener;
    FingerprintManager fingerprintManager;
    KeyguardManager keyguardManager;

    private KeyStore keyStore;
    private static final String KEY_NAME = "applicationAuthentication";
    private Cipher cipher;

    @SuppressLint("NewApi")
    public FingerprintHandler(Context context, OnResponseListener onResponseListener) {

        this.context = context;
        this.onResponseListener = onResponseListener;

        if(PermissionChecking.checkMarshmallowOrUpper(context)){
            keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);

            if(!PermissionChecking.checkHardwareDetected(fingerprintManager)){
                onResponseListener.onResponse("Your Device does not have a Fingerprint Sensor");
            }else {
                // Checks whether fingerprint permission is set on manifest
                if (!PermissionChecking.checkUseFingerprintSensorEnabledOrNot(context)) {
                    onResponseListener.onResponse("Fingerprint authentication permission not enabled");
                }else{
                    // Check whether at least one fingerprint is registered
                    if (!PermissionChecking.checkHasEnrolledFingerprints(fingerprintManager)) {
                        onResponseListener.onResponse("Register at least one fingerprint in Settings");
                    }else{
                        // Checks whether lock screen security is enabled or not
                        if (!PermissionChecking.checkKeyguardSecure(keyguardManager)) {
                            onResponseListener.onResponse("Lock screen security not enabled in Settings");
                        }else{
                            generateKey();

                            if (cipherInit()) {
                                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);

                                CancellationSignal cancellationSignal = new CancellationSignal();
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
                            }
                        }
                    }
                }
            }
        }else{
            onResponseListener.onResponse("Sorry! Your mobile phone version is lower than Marshmallow");
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        onResponseListener.onResponse("Fingerprint Authentication error");
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        onResponseListener.onResponse("Fingerprint Authentication help");
    }

    @Override
    public void onAuthenticationFailed() {
        onResponseListener.onResponse("Fingerprint Authentication failed");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        onResponseListener.onResponse("Fingerprint Authentication succeeded");
    }

    public interface OnResponseListener {
        void onResponse(String response);
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }
        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}

