package com.example.give4friends.models;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.provider.MediaStore;

import androidx.fragment.app.Fragment;

import com.example.give4friends.Cutom_Classes.BitmapScaler;
import com.example.give4friends.ProfileActivity;
import com.example.give4friends.R;
import com.example.give4friends.SignUpActivity;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.BasicHttpParams;

import static android.app.Activity.RESULT_OK;

public final class ProfilePicture {
    public static final String APP_TAG = "SignUpActivity";
    private static final String SERVER_ADDRESS = "https://give4friends.000webhostapp.com/";

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int SELECT_IMAGE_REQUEST_CODE = 1111;
    private static Bitmap photo;
    public static String photoFileName = "photo.jpg";
    public static File photoFile;
    static Activity activity;



    public static void changePhoto(final Context context){
        String[] options = {"Take photo", "Choose from gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Change Profile Picture");
        dialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0) {
                    onLaunchCamera(context); }
                else {
                    onLaunchSelect(context); }
            }
        });
        dialog.show();

    }

    public static void changePhotoFragment(final Fragment fragment){
        String[] options = {"Take photo", "Choose from gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(fragment.getContext());
        dialog.setTitle("Change Profile Picture");
        dialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0) {
                    onLaunchCameraFragment(fragment); }
                else {
                    onLaunchSelect(fragment.getContext());
                }
            }
        });
        dialog.show();

    }




    public static File getPhotoFileUri(String fileName, Context context) {

        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private static void onLaunchCamera(Context context) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName, context);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            activity = (Activity) context;
            activity.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private static void onLaunchCameraFragment(Fragment fragment) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName, fragment.getContext());

        if (intent.resolveActivity(fragment.getContext().getPackageManager()) != null) {

            fragment.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


    public static void onLaunchSelect(Context context) {
        activity = (Activity) context;
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            // Bring up gallery to select a photo
            activity.startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
        }
    }


    public static void onLaunchSelectFragment(Fragment fragment) {
        activity = (Activity) fragment.getContext();
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            // Bring up gallery to select a photo
            fragment.startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
        }
    }

    public static Bitmap RotateBitmapFromBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.setRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    public static Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }


    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }}



    public static void updatePhoto(final ParseUser parseUser, final Bitmap photo) {



        final ParseFile parseFile = conversionBitmapParseFile(photo);
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e==null) {
                    parseUser.put("profileImage", parseFile);
//                    parseFile.cancel();
                    parseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e!=null){
                                e.printStackTrace();
                            }

                        }
                    });
                }else{
                    e.printStackTrace();
                }


            }
        });


    }

    public static void updatePhotoURL(ParseUser parseUser, String url) {

        parseUser.put("profileImageURL", url);

        parseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    e.printStackTrace();
                }

            }
        });


    }

    public static ParseFile conversionBitmapParseFile(Bitmap imageBitmap){
        Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(imageBitmap, 100);

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        ParseFile parseFile = new ParseFile("image_file.png",imageByte);

        return parseFile;
    }



    public static class UploadImage extends AsyncTask<Void,Void,Void>{


        Bitmap image;
        String name;
        Context context;
        ProgressBar progressBarHome;


        public UploadImage(Bitmap image, String name, Context context) {
            this.image = image;
            this.name = name;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {


            progressBarHome = ((Activity)context).findViewById(R.id.progressBarHome);
            showProgressBar();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            dataToSend.add(new BasicNameValuePair("name",name));


            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(SERVER_ADDRESS + "SavePicture.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            hideProgressBar();
            Toast.makeText(context, "Image Uploaded", Toast.LENGTH_SHORT).show();
        }

        public void showProgressBar() {
            // Show progress item
            progressBarHome.setVisibility(View.VISIBLE);
        }

        public void hideProgressBar() {
            // Hide progress item
            progressBarHome.setVisibility(View.INVISIBLE);
        }
    }

    private cz.msebera.android.httpclient.params.HttpParams getHttpRequestParams(){

        cz.msebera.android.httpclient.params.HttpParams httpRequestParams = new BasicHttpParams();


        cz.msebera.android.httpclient.params.HttpConnectionParams.setConnectionTimeout(httpRequestParams,1000*30);
        cz.msebera.android.httpclient.params.HttpConnectionParams.setSoTimeout(httpRequestParams, 100*30);

        return httpRequestParams;
    }





}

