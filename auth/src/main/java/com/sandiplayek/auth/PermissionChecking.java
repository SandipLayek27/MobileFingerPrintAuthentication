package com.sandiplayek.auth;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class PermissionChecking {

    // CHECKING THE APPLICATION MIN SDK VERSION 23 OR UPPER
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean checkMarshmallowOrUpper(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return true;
        } else{
            return false;
        }
    }

    // CHECKING THE DEVICE HAS FINGER PRINT SENSOR
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkHardwareDetected(FingerprintManager fingerprintManager){
        if(fingerprintManager.isHardwareDetected()){
            return true;
        }else{
            return false;
        }
    }

    // CHECKING THE DEVICE HAS ENABLED FINGERPRINT SENSOR
    public static boolean checkUseFingerprintSensorEnabledOrNot(Context context){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    // CHECKING WHETHER AT LEAST ONE FINGERPRINT IS REGISTERED
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkHasEnrolledFingerprints(FingerprintManager fingerprintManager){
        if(fingerprintManager.hasEnrolledFingerprints()){
            return true;
        }else{
            return false;
        }
    }

    // CHECKING WHETHER LOCK SCREEN SECURITY IS ENABLED OR NOT
    public static boolean checkKeyguardSecure(KeyguardManager keyguardManager){
        if(keyguardManager.isKeyguardSecure()){
            return true;
        }else{
            return false;
        }
    }
}
