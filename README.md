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
<uses-permission android:name="android.permission.USE_FINGERPRINT" />

# ★ Useful Features
Activate fingerprint Sensor
Capture the fingerprint
Match the fingerprint with device inbuild fingerprint
Authentication Handler: Trusted Execution Environment (TEE)

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
Sorry! Your mobile phone version is lower than Marshmallow.
Your Device does not have a Fingerprint Sensor.
Fingerprint authentication permission not enabled
Register at least one fingerprint in Settings
Lock screen security not enabled in Settings
Fingerprint Authentication error
Fingerprint Authentication help
Fingerprint Authentication failed

# ★ Success Response:
Fingerprint Authentication succeeded
