package com.example.gareth.speakitvisualcommunication;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Uploader extends AppCompatActivity {

    /**
     *
     */
    EditText edtName;

    /**
     *
     */
    Button btnChoose, btnAdd;

    /**
     *
     */
    ImageView imageView;
    //Spinner spinner;

    /**
     *
     */
    private String userChosen;

    /**
     *
     */
    private String categorySelected;

    //private String[] categoryArray = {"Home Page", "Favourites", "At Home", "About Me", "Food and Drink", "Greetings", "Leisure", "Today's Activities"};

    /**
     *
     */
    final int REQUEST_CODE_GALLERY = 1;

    /**
     *
     */
    final int REQUEST_IMAGE_CAPTURE = 0;

    /**
     *
     */
    private DatabaseOperations ops;

    /**
     *
     */
    private String user;

    //private static final int ACTION_TAKE_PHOTO_B = 1;

    //private String mCurrentPhotoPath;

    //private static final String JPEG_FILE_SUFFIX = ".jpg";

    //private static final String JPEG_FILE_PREFIX = "IMG_";

    //private AlbumStorageDirFactory mAlbumStorageDirFactory = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploader);

        //Set back button in the bar at the top of screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        user = intent.getStringExtra("com.example.gareth.speakitvisualcommunication.User");
        categorySelected = intent.getStringExtra("com.example.gareth.speakitvisualcommunication.page");

        if (user == null) {
            Toast.makeText(this, "Please select a User", Toast.LENGTH_SHORT).show();
            finish();
        }

        ops = new DatabaseOperations(getApplicationContext());
        ops.open();

        //final List<String> spinnerList = new ArrayList<>(Arrays.asList(categoryArray));
        //spinner = (Spinner) findViewById(R.id.categorySelection);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(Uploader.this, android.R.layout.simple_spinner_item, spinnerList);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //@Override
           // public void onItemSelected(AdapterView<?> parent, final View view, int position, long id) {
                //Get the selected booking from the spinner
                //categorySelected = spinner.getSelectedItem().toString();
           //}

           // @Override
            //public void onNothingSelected(AdapterView<?> adapterView) {
                //categorySelected = categoryArray[0];
            //}
       // });

        init();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }

        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }

        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        ops.insertData(
                                edtName.getText().toString().trim(),
                                imageViewToByte(imageView), categorySelected, user
                        );
                        Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();
                        if (!categorySelected.equals("Home Page")) {
                            Intent intent = new Intent(Uploader.this, SecondScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("com.example.gareth.speakitvisualcommunication.username2", user);
                            intent.putExtra("com.example.gareth.speakitvisualcommunication.Category", categorySelected);
                            startActivity(intent);
                        } else if (categorySelected.equals("Home Page")){
                            Intent intent = new Intent(Uploader.this, MainScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("com.example.gareth.speakitvisualcommunication.username2", user);
                            intent.putExtra("com.example.gareth.speakitvisualcommunication.Category", categorySelected);
                            startActivity(intent);
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
        });


    }

    /**
     *
     * @param image
     * @return
     */
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChosen.equals("Take Photo"))
                        cameraIntent();
                    else if(userChosen.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     *
     */
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Uploader.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(Uploader.this);

                if (items[item].equals("Take Photo")) {
                    userChosen ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChosen ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     *
     */
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),REQUEST_CODE_GALLERY);
    }

    /**
     *
     */
    private void cameraIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    //@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                onCaptureImageResult(data);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

        /**
         *
         * @param data
         */
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".png");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(thumbnail);
    }

    /**
     *
     * @param data
     */
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri uri = data.getData();
        if (data != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *
     */
    private void init(){
        edtName = (EditText) findViewById(R.id.edtName);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        imageView = (ImageView) findViewById(R.id.imageViewAdd);
    }

    /**
     * Creating options menu and inflating it with menu
     * layout file
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_second, menu);

        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Method for the selection of the home button
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.account2:
                Intent intent = new Intent(Uploader.this, UserSelect.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //private void dispatchTakePictureIntent(int actionCode) {

        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //switch(actionCode) {
            //case ACTION_TAKE_PHOTO_B:
                //File f = null;

                //try {
                   // f = setUpPhotoFile();
                    //mCurrentPhotoPath = f.getAbsolutePath();
                   // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                //} catch (IOException e) {
                  //  e.printStackTrace();
                   // f = null;
                   // mCurrentPhotoPath = null;
                //}
                //break;

            //default:
                //break;
       // }

        //startActivityForResult(takePictureIntent, actionCode);
    //}

    //@Override
    //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // switch (requestCode) {
          //  case ACTION_TAKE_PHOTO_B:
             //   if (resultCode == RESULT_OK) {
                   // handleBigCameraPhoto();
                //}
                //break;
       // } // switch
    //}

    //private void handleBigCameraPhoto() {

        //if (mCurrentPhotoPath != null) {
          //  setPic();
           // galleryAddPic();
           // mCurrentPhotoPath = null;
       // }
    //}


    //private File setUpPhotoFile() throws IOException {

        //File f = createImageFile();
        //mCurrentPhotoPath = f.getAbsolutePath();

        //return f;
    //}

    //private File createImageFile() throws IOException {
        // Create an image file name
       // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
       // String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
       // File albumF = getAlbumDir();
       // File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
       // return imageF;
    //}

    //private File getAlbumDir() {
        //File storageDir = null;

        //if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            //storageDir = mAlbumStorageDirFactory.getAlbumStorageDir("Album");

            //if (storageDir != null) {
                //if (! storageDir.mkdirs()) {
                    //if (! storageDir.exists()){
                        //Log.d("CameraSample", "failed to create directory");
                       // return null;
                   // }
                //}
           // }

       // } else {
          //  Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
       // }

        //return storageDir;
   // }

   // private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        //int targetW = imageView.getWidth();
        //int targetH = imageView.getHeight();

		/* Get the size of the image */
       // BitmapFactory.Options bmOptions = new BitmapFactory.Options();
      //  bmOptions.inJustDecodeBounds = true;
       // BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
       // int photoW = bmOptions.outWidth;
       // int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
       // int scaleFactor = 1;
        //if ((targetW > 0) || (targetH > 0)) {
       //     scaleFactor = Math.min(photoW/targetW, photoH/targetH);
       // }

		/* Set bitmap options to scale the image decode target */
       // bmOptions.inJustDecodeBounds = false;
        //bmOptions.inSampleSize = scaleFactor;
       // bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        //Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        //imageView.setImageBitmap(bitmap);
        //imageView.setVisibility(View.VISIBLE);

    //}

    //private void galleryAddPic() {
        //Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
       // File f = new File(mCurrentPhotoPath);
       // Uri contentUri = Uri.fromFile(f);
       // mediaScanIntent.setData(contentUri);
       // this.sendBroadcast(mediaScanIntent);
    //}



    /**
     * onResume method
     */
    public void onResume() {
        super.onResume();
        //Open database
        ops.open();

    }

    /**
     *onStop method closes the SQLIte database
     */
    @Override
    public void  onStop() {
        super.onStop();
        ops.close();
    }

    /**
     * When the activity is finished the method will close the SQLite database.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //Calling the close method to close the database.
        ops.close();
    }

}

