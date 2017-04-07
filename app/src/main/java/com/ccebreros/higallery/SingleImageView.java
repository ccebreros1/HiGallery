package com.ccebreros.higallery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;

/**
 * Created by cesar on 2017-04-07.
 */

public class SingleImageView extends Activity {

    File imagePath;
    Bitmap image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view);
        //Get passed data from last view
        Bundle bundle = getIntent().getExtras();
        String filePath = bundle.getString("IMAGEURL");

        TouchImageView imageView = (TouchImageView) findViewById(R.id.single_image_view);
        try {
            image = getImage(filePath);
            imageView.setImageBitmap(image);
        }
        catch (Exception e)
        {
            Log.d("FAILED", e.toString());
        }
    }

    public Bitmap getImage(String path)
    {
        imagePath = new File(path);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;
        bmOptions.inScaled = true;

        image = BitmapFactory.decodeFile(imagePath.getAbsolutePath(), bmOptions);

        return image;
    }
}
