package com.ccebreros.higallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cesar on 2017-03-27.
 */

public class GalleryActivity extends Activity {


    GridView gridView;
    Button newImageButton;

    //For image handling
    File folder;

    Bitmap[] images;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        ImageAdapter imageAdapter1 = new ImageAdapter(this);
        images = imageAdapter1.getImages();
        getImages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getImages();
    }

    public void getImages()
    {
        File folder = new File("/sdcard/Documents/HiGallery/.HiGallery");
        File files[] = folder.listFiles();

        gridView = (GridView) findViewById(R.id.gallery_grid_view);
        gridView.setAdapter(new ImageAdapter(this));
        //Toast.makeText(this,"Loaded", Toast.LENGTH_LONG);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap image = (Bitmap) parent.getItemAtPosition(position);

                //Get bitmap location
                Uri imageUri = getImageUri(getApplicationContext(), image, position);

                //Launch other view
                Intent intent = new Intent(GalleryActivity.this, SingleImageView.class);
                //Pass in the data
                Bundle bundle = new Bundle();
                bundle.putString("IMAGEURL", imageUri.toString());
                intent.putExtras(bundle);
                //Start the activity
                GalleryActivity.this.startActivity(intent);

            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap image = (Bitmap) parent.getItemAtPosition(position);
                //Get bitmap location
                Uri imageUri = getImageUri(getApplicationContext(), image, position);

                openDialog(view, imageUri.toString());
                return true;
            }
        });
    }

    public void deleteImage(String imagePath)
    {
        folder = new File(imagePath);
        try {
            folder.delete();
            getImages();
        }
        catch (Exception e)
        {
            Log.d("DELETEFAIL", e.toString());
        }
    }

    public void openDialog(View view, final String imageToDelete){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sureou want to delete the image?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteImage(imageToDelete);
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getImages();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage, int index)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        folder = new File("/sdcard/Documents/HiGallery/.HiGallery");
        File[] files = folder.listFiles();
        images = new Bitmap[folder.listFiles().length];
        int i = index;
        String path = files[i].getAbsolutePath();
        return Uri.parse(path);
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
