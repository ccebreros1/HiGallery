package com.ccebreros.higallery;

import android.app.Activity;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.GridView;

import java.io.File;

/**
 * Created by cesar on 2017-03-27.
 */

public class GalleryActivity extends Activity {

    File folder;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String filepath = ".HiGallery";
        setContentView(R.layout.gallery);
        //String directory = "/Documents/.HiGallery/";
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        folder = new File(Environment.getExternalStorageDirectory() + File.separator + filepath);
        Log.d("FOLDER", folder.toString());
        folder.mkdirs();
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


}
