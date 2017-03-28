package com.ccebreros.higallery;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

/**
 * Created by cesar on 2017-03-27.
 */

public class GalleryActivity extends Activity {


    GridView gridView;

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


}
