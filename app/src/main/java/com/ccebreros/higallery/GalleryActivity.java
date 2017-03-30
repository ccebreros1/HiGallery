package com.ccebreros.higallery;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cesar on 2017-03-27.
 */

public class GalleryActivity extends Activity {


    GridView gridView;
    Button newImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        //String directory = "/Documents/.HiGallery/";


       /* boolean success = false;
        if (!folder.exists()) {
            folder.mkdirs();
            Toast.makeText(this,"success here", Toast.LENGTH_LONG);
            success = true;
        }
        if (success == true) {
            gridView = (GridView) findViewById(R.id.gallery_grid_view);
            gridView.setAdapter(new ImageAdapter(this));
            Toast.makeText(this,"Loaded", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(this,"Nothing here", Toast.LENGTH_LONG);

        }*/

    }

    //For access to right API use the @TargetApi(Number) annoation
    public void takePicture(View view) {
        //Open camera
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //Create image
        //Create new folder where the images will be saved
        //Path starts with . so it gets hidden to other apps (Except file explorers)
        String filepath = ".HiGallery";
        //This provides the entire path of the directory
        File folder = new File("/sdcard/Documents/HiGallery/" + File.separator + filepath);
        File image = new File(folder, "HiGallery_" + timeStamp + ".jpg");
        Uri uriSavedImage = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider",
                image);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(cameraIntent, 100);
    }
}
