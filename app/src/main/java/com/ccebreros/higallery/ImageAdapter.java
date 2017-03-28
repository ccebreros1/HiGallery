package com.ccebreros.higallery;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by cesar on 2017-03-27.
 */

public class ImageAdapter extends BaseAdapter {

    private Context context;
    File folder;

    ArrayList<String>images;

    public ArrayList<String> getImages()
    {
        folder = new File(Environment.getDataDirectory() +
                File.separator + ".HiGallery");
        images = new ArrayList<String>();
        File[] files = folder.listFiles();
        for (File image : files) {
            images.add(image.toString());
        }
        return images;
    }

    public ImageAdapter(Context c)
    {
        context = c;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.indexOf(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(images.indexOf(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(240, 240));
        return null;
    }
}
