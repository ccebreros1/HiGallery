package com.ccebreros.higallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by cesar on 2017-03-27.
 */

public class ImageAdapter extends BaseAdapter {

    private Context context;
    File folder;

    Bitmap[] images;
    Bitmap image;

    public Bitmap[] getImages()
    {
        folder = new File("/sdcard/Documents/HiGallery/.HiGallery");
        File[] files = folder.listFiles();
        images = new Bitmap[folder.listFiles().length];
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        int i = 0;
        for (File imagePath : files) {
            image = BitmapFactory.decodeFile(imagePath.getAbsolutePath(), bmOptions);
            images[i] = image;
            i++;
        }
        return images;
    }

    public ImageAdapter(Context c)
    {
        context = c;
        images = getImages();
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(images[position].getGenerationId());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(240, 240));
        return imageView;
    }
}
