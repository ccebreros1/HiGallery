package com.ccebreros.higallery;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by cesar on 2017-03-21.
 */
@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    // You should use the CancellationSignal method whenever your app can no longer process user input (Background, different activity, etc)
    // If you don’t use this method, then other apps will be unable to access the touch sensor, including the lockscreen

    private CancellationSignal cancellationSignal;
    private Context context;
//Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
    }
//start user authentication

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    //onAuthenticationError is called when a fatal error has occurred.

    public void onAuthenticationError(int errMsgId, CharSequence errString) {

        Toast.makeText(context, "Authentication error\n" + errString, Toast.LENGTH_LONG).show();
    }

    @Override

    //onAuthenticationFailed is called when the fingerprint doesn’t match with any of the fingerprints registered on the device//

    public void onAuthenticationFailed() {
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
    }

    @Override

    //onAuthenticationHelp is called when a non-fatal error has occurred.
    // This method provides additional information about the error

    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Toast.makeText(context, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();
    }@Override

    //onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints
    // stored on the user’s device
    // Here you will do any activity start or change, or do any call to logic that will happen
    //when the authentication is good
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {
        Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show();
        //Finish with this activity
        ((Activity)context).finish();
        //New intent for gallery
        Intent intent = new Intent(context, GalleryActivity.class);
        //Jump to activity
        context.startActivity(intent);
    }
}
