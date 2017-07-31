package com.example.gareth.speakitvisualcommunication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Uploader extends AppCompatActivity {

    EditText edtName;
    Button btnChoose, btnAdd, btnList;
    ImageView imageView;

    final int REQUEST_CODE_GALLERY = 999;

    private DatabaseOperations ops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploader);

        ops = new DatabaseOperations(getApplicationContext());
        ops.open();

        init();

        btnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(
                            Uploader.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_GALLERY
                    );
                }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        ops.insertData(
                                edtName.getText().toString().trim(),
                                imageViewToByte(imageView)
                        );
                        Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();
                        edtName.setText("");
                        imageView.setImageResource(R.mipmap.ic_launcher);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
        });


    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                    Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                    e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void init(){
        edtName = (EditText) findViewById(R.id.edtName);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnList = (Button) findViewById(R.id.btnList);
        imageView = (ImageView) findViewById(R.id.imageViewAdd);
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
     *onStop method closes the event listener
     */
    @Override
    public void  onStop() {
        super.onStop();
        ops.close();
    }

    /**
     * When the activity is finished the method will close the  SQLite database.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //Calling the close method to close the database.
        ops.close();
    }

}

