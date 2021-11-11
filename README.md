# MobileFingerPrintAuthentication
Mobile Fingerprint Authentication Checking.

## Developed
[![Sandip](https://avatars1.githubusercontent.com/u/31722942?v=4&u=18643bfaaba26114584d27693e9891db26bcb582&s=39) Sandip](https://github.com/SandipLayek27)  
# ★ Gradle Dependency
Add Gradle dependency in the build.gradle file of your application module (app in the most cases) :
First Tab:

```sh
allprojects{
    repositories{
        jcenter()
        maven {
            url 'https://jitpack.io'
        }
    }
}
```

AND

```sh
dependencies {
  implementation 'com.github.SandipLayek27:MobileFingerPrintAuthentication:1.1'
}
```

# ★ Add to manifest.xml
```sh
<uses-permission android:name="android.permission.USE_FINGERPRINT" />
```

# ★ Useful Features
1. Activate fingerprint Sensor
2. Capture the fingerprint
3. Match the fingerprint with device inbuild fingerprint
4. Authentication Handler: Trusted Execution Environment (TEE)

# ★ Uses:
Just call like this from onCreate or any button click.
```sh
new FingerprintHandler(MainActivity.this, new FingerprintHandler.OnResponseListener() {
    @Override
    public void onResponse(String response) {
        tvResponse.setText(response);
    }
});
```

# ★ Error Response:
1. Sorry! Your mobile phone version is lower than Marshmallow.
2. Your Device does not have a Fingerprint Sensor.
3. Fingerprint authentication permission not enabled
4. Register at least one fingerprint in Settings
5. Lock screen security not enabled in Settings
6. Fingerprint Authentication error
7. Fingerprint Authentication help
8. Fingerprint Authentication failed

# ★ Success Response:
1. Fingerprint Authentication succeeded
