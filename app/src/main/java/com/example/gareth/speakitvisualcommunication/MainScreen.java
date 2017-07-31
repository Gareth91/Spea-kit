package com.example.gareth.speakitvisualcommunication;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.example.gareth.speakitvisualcommunication.Uploader.sqLiteHelper;

public class MainScreen extends AppCompatActivity implements AdapterView.OnItemClickListener, TextToSpeech.OnInitListener{

    //TTS object
    private TextToSpeech myTTS;

    //status check code
    private int MY_DATA_CHECK_CODE = 0;

    /**
     *
     */
    private List<PecsImages> imageCategories;

    /**
     *
     */
    private List<PecsImages> list;

    /**
     *
     */
    private ImageAdapter imageAdapter;

    /**
     *
     */
    private GridView gridView;

    /**
     *
     */
    private PecsImages[] categories = {
            new PecsImages("Add Category", R.mipmap.ic_launcher),
            new PecsImages("Favourites", R.mipmap.ic_launcher),
            new PecsImages("At Home", R.drawable.home),
            new PecsImages("About Me", R.mipmap.ic_launcher),
            new PecsImages("Food And Drink", R.mipmap.ic_launcher),
            new PecsImages("Greetings", R.mipmap.ic_launcher),
            new PecsImages("Leisure", R.mipmap.ic_launcher),
            new PecsImages("Today's Activities", R.mipmap.ic_launcher)
    };

    ImageView pecsView;


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        imageCategories = new ArrayList<>(Arrays.asList(categories));
        list = new ArrayList<>();
        gridView = (GridView)findViewById(R.id.gridview);
        imageAdapter = new ImageAdapter(this, imageCategories);
        gridView.setAdapter(imageAdapter);

        // get all data from sqlite
        //Cursor cursor = sqLiteHelper.getData("SELECT * FROM PECS");
        //if there are images present
        //if(cursor.getCount() > 0) {
            //Move to the first row
           // cursor.moveToFirst();
           // do {
               // int id = cursor.getInt(0);
               // String word = cursor.getString(1);
               // byte[] images = cursor.getBlob(2);
               // list.add(new PecsImages(word, images, id));
          //  }while(cursor.moveToNext());
          //  imageCategories.addAll(list);
           // imageAdapter.notifyDataSetChanged();
      //  }

        gridView.setOnItemClickListener(this);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainScreen.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // update
                            Cursor c = sqLiteHelper.getData("SELECT id FROM PECS");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            // show dialog update at here
                            showDialogUpdate(MainScreen.this, arrID.get(position));

                        } else {
                            // delete
                            Cursor c = sqLiteHelper.getData("SELECT id FROM PECS");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });


        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

    }


    /**
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PecsImages image = imageCategories.get(position);
        speakWords(image.getWord());

        if (image.getWord().equals("Add Category")) {

        } else {
            Intent intent = new Intent(getApplicationContext(), SecondScreen.class);
            intent.putExtra("com.example.gareth.speakitvisualcommunication.Category", image.getWord());
            startActivity(intent);
        }

    }

    /**
     *
     * @param speech
     */
    private void speakWords(String speech) {

        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    /**
     * act on result of TTS data check
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(this, this);
            }
            else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }

        if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                pecsView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     *
     * @param initStatus
     */
    @Override
    public void onInit(int initStatus) {

        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);

        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * @param activity
     * @param position
     */
    private void showDialogUpdate(Activity activity, final int position) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_pecs_images);
        dialog.setTitle("Update");

        pecsView = (ImageView) dialog.findViewById(R.id.pecsImage);
        final EditText edtName = (EditText) dialog.findViewById(R.id.edtName);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        pecsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        MainScreen.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uploader.sqLiteHelper.updateData(
                            edtName.getText().toString().trim(),
                            Uploader.imageViewToByte(pecsView),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                updatePecsList();
            }
        });
    }

    /**
     *
     */
    private void updatePecsList() {
        // get all data from sqlite
        Cursor cursor = Uploader.sqLiteHelper.getData("SELECT * FROM PECS");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String word = cursor.getString(1);
            byte[] images = cursor.getBlob(2);

            list.add(new PecsImages(word, images, id));
        }
        imageCategories.addAll(list);
        imageAdapter.notifyDataSetChanged();
    }

    /**
     *
     * @param idPecs
     */
    private void showDialogDelete(final int idPecs) {
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainScreen.this);

        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure you want to this delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Uploader.sqLiteHelper.deleteData(idPecs);
                    Toast.makeText(getApplicationContext(), "Delete successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
                updatePecsList();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}

