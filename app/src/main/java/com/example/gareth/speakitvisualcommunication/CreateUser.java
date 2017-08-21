package com.example.gareth.speakitvisualcommunication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gareth.speakitvisualcommunication.volley.ErrorResponse;
import com.example.gareth.speakitvisualcommunication.volley.VolleyCallBack;
import com.example.gareth.speakitvisualcommunication.volley.VolleyHelp;
import com.example.gareth.speakitvisualcommunication.volley.VolleyRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class CreateUser extends AppCompatActivity {

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

    String login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        //Set back button in the bar at the top of screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Intent intent = getIntent();
        login = intent.getStringExtra("com.example.gareth.speakitvisualcommunication.login");

        ops = new DatabaseOperations(getApplicationContext());
        ops.open();

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
                final String username = ops.getUserName(edtName.getText().toString());

                if (edtName.getText().toString().equals("")) {
                    Toast.makeText(CreateUser.this, "Please enter a UserName", Toast.LENGTH_SHORT).show();
                } else {
                    //String BASE_URL = "http://awsandroid.eu-west-1.elasticbeanstalk.com/project/getUsername";
                    String BASE_URL = "http://10.0.2.2:5000/project/getUsername";
                    String url = BASE_URL;

                    HashMap<String, String> headers  = new HashMap<>();
                    HashMap<String, String> body  = new HashMap<>();

                    body.put("username", edtName.getText().toString());

                    String contentType =  "application/json";
                    VolleyRequest request =   new VolleyRequest(CreateUser.this, VolleyHelp.methodDescription.POST, contentType, url, headers, body);

                    request.serviceJsonCall(new VolleyCallBack(){
                        @Override
                        public void onSuccess(String result){
                            System.out.print("CALLBACK SUCCESS: " + result);
                            if(result != null) {
                                String BASE_URL = "http://10.0.2.2:5000/project/insertUser";
                                String url = BASE_URL;

                                HashMap<String, String> headers  = new HashMap<>();
                                HashMap<String, String> body  = new HashMap<>();

                                body.put("username", edtName.getText().toString());
                                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                                body.put("image", BitMapToString(bitmap));
                                body.put("accountusername", login);

                                String contentType =  "application/json";
                                VolleyRequest request =   new VolleyRequest(CreateUser.this, VolleyHelp.methodDescription.POST, contentType, url, headers, body);

                                request.serviceJsonCall(new VolleyCallBack(){
                                    @Override
                                    public void onSuccess(String result){
                                        System.out.print("CALLBACK SUCCESS: " + result);
                                        Toast.makeText(CreateUser.this, "Success ", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(CreateUser.this, UserSelect.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                    @Override
                                    public void onError(ErrorResponse errorResponse){
                                        System.out.print("CALLBACK ERROR: " + errorResponse.getMessage());
                                    }
                                });
                            } else {
                                Toast.makeText(CreateUser.this, "UserName "+result+ " already exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onError(ErrorResponse errorResponse){
                            System.out.print("CALLBACK ERROR: " + errorResponse.getMessage());

                        }
                    });
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

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateUser.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(CreateUser.this);

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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_IMAGE_CAPTURE)
                onCaptureImageResult(data);
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
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

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
        edtName = (EditText) findViewById(R.id.username);
        btnChoose = (Button) findViewById(R.id.btnChooseImage);
        btnAdd = (Button) findViewById(R.id.btnAddImage);
        imageView = (ImageView) findViewById(R.id.userImageAdd);
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param bitmap
     * @return
     */
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

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


