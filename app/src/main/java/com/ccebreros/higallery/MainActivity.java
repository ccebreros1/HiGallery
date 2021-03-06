package com.ccebreros.higallery;

//Many of the needed dependencies were found using the tutorial found at
//http://www.androidauthority.com/how-to-add-fingerprint-authentication-to-your-android-app-747304/

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
public class MainActivity extends AppCompatActivity {

    TextView instructionsText;
    File folder;
    // Declare a string variable for the key we’re going to use in our fingerprint authentication
    private static final String KEY_NAME = "yourKey";
    //Declarate all of the key dependencies used to acces the fingerptint data
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView textView;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    //Permission request codes
    private static final int MY_PERMISSIONS_REQUESTS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Request for permissions

        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
            loadView();
            Toast.makeText(this, "No need to request permissions, but the app won't run", Toast.LENGTH_LONG).show();
        } else {
            if (checkAndRequestPermissions()) {
                loadView();
                //If you have already permitted the permission
            }
        }




    }

    public void loadView()
    {
        //Create new folder where the images will be saved
        //Path starts with . so it gets hidden to other apps (Except file explorers)
        String filepath = ".HiGallery";
        setContentView(R.layout.activity_main);
        instructionsText = (TextView) findViewById(R.id.instructions_textView); ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //This provides the entire path of the directory
        folder = new File("/sdcard/Documents/HiGallery/" + File.separator + filepath);
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        //Log.d("FOLDER", folder.toString());
        //Debug
        //Toast.makeText(this,folder.toString(), Toast.LENGTH_LONG).show();
        // If the device is running Marshmallow and below, the fingerprint authentication will not be available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //Get an instance of KeyguardManager and FingerprintManager to use fingerprint features
            keyguardManager =
                    (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager =
                    (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            //Check whether the device has a fingerprint sensor//
            if (!fingerprintManager.isHardwareDetected())
            {
                // Fingerprint is not available
                instructionsText.setText("Your device doesn't support fingerprint authentication");
            }
            //Check whether the user has granted your app the USE_FINGERPRINT permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            {
                // No permissions granted
                instructionsText.setText("Please enable the fingerprint permission");
            }

            //Check that the user has registered at least one fingerprint
            if (!fingerprintManager.hasEnrolledFingerprints())
            {
                // No fingerprints registered
                instructionsText.setText("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
            }

            //Check if the lockscreen is secured
            if (!keyguardManager.isKeyguardSecure())
            {
                // Lockscreen has no security at all
                instructionsText.setText("Please enable lockscreen security in your device's Settings");
            }
            else {
                try {
                    generateKey();
                } catch (FingerprintException e) {
                    e.printStackTrace();
                }

                if (initCipher()) {
                    //If the cipher is initialized successfully, then create a CryptoObject instance//
                    cryptoObject = new FingerprintManager.CryptoObject(cipher);

                    // Here, I’m referencing the FingerprintHandler class that we’ll create in the
                    // next section. This class will be responsible
                    // for starting the authentication process (via the startAuth method)
                    // and processing the authentication process events
                    FingerprintHandler helper = new FingerprintHandler(this);
                    helper.startAuth(fingerprintManager, cryptoObject);
                }
            }
        }
        else
        {
            instructionsText.setText("The fingerprint is not available on this device. It needs to be android 6.0 and above");
        }
    }

    //Method to ask for permissions
    private boolean checkAndRequestPermissions() {

        //Camera
        int permissionCAMERA = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        //Storage
        int storagePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        //Write Storage
        int writeStoragePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);



        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,


                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUESTS);
            return false;
        }

        return true;
    }

    //Prompt to ask for permissions
    @Override    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUESTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You granted permissions", Toast.LENGTH_LONG).show();
                    //Load the view once the permissions are granted
                    loadView();
                    //Permission Granted Successfully. Write working code here.
                } else {
                    //You did not accept the request can not use the functionality.
                    Toast.makeText(this, "You did not grant permissions", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    //For access to right API use the @TargetApi(Number) annoation
    @TargetApi(Build.VERSION_CODES.M)
    //Create the generateKey method that we’ll use to gain access to the Android keystore and generate the encryption key//
    private void generateKey() throws FingerprintException {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            //Generate the key//
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            //Initialize an empty KeyStore//
            keyStore.load(null);
            //Initialize the KeyGenerator//
            keyGenerator.init(new

                    //Specify the operation(s) this key can be used for//
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            //Generate the key//
            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    //Create a new method that we’ll use to initialize our cipher
    public boolean initCipher() {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Return true if the cipher has been initialized successfully//
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {

            //Return false if cipher initialization failed//
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    //Class for catching the exception if anything fails through the authentication proccess
    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }

}
