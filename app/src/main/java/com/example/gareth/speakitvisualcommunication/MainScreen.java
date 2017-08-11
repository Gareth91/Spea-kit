package com.example.gareth.speakitvisualcommunication;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.PersistableBundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Class used to create the main screen of the app
 */
public class MainScreen extends AppCompatActivity implements AdapterView.OnItemClickListener, TextToSpeech.OnInitListener, View.OnClickListener, View.OnLongClickListener{

    /**
     * TTS object
     */
    private TextToSpeech myTTS;

    /**
     * status check code
     */
    private int MY_DATA_CHECK_CODE = 0;

    /**
     * A list containing PecsImages objects
     */
    private List<PecsImages> imageCategories;

    /**
     * A list containing PecsImages objects
     */
    private List<PecsImages> list;

    /**
     * ImageAdapter object which is used to create the grid view
     */
    private ImageAdapter imageAdapter;

    /**
     * GridView object
     */
    private GridView gridView;

    /**
     * Array containing PecsImages objects which take an image from
     * drawable resource
     */
    private PecsImages[] categories = {

            new PecsImages("Add Category", R.drawable.addcategory,1),
            new PecsImages("At Home", R.drawable.home,1),
            new PecsImages("Favourites", R.drawable.favourites,1),
            new PecsImages("About Me", R.drawable.aboutme,1),
            new PecsImages("Greetings", R.drawable.seeyou,1),
            new PecsImages("Food And Drink", R.drawable.foodanddrink,1),
            new PecsImages("Leisure", R.drawable.leisure,1),
            new PecsImages("Today's Activities", R.drawable.schedule,1)
    };

    /**
     * DatabaseOperations object
     */
    private DatabaseOperations ops;

    /**
     * ImageView object
     */
    private ImageView pecsView;

    /**
     * RecyclerView object used to create the sentence builder
     * at the top of the page
     */
    private RecyclerView recyclerView;

    /**
     * SentenceBuilderAdapter used to create the sentence builder at the top
     * of the page
     */
    private SentenceBuilderAdapter mAdapter;

    /**
     * A List containing PecsImages objects which will be placed in
     * the RecyclerView
     */
    private List<PecsImages> sentenceWords;

    /**
     * User
     */
    String user = null;
    String user2 = null;


    /**
     * onCreate method called when screen is first created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Open a connection with the SQLite database using
        // instance of the DatabaseOperations class
        ops = new DatabaseOperations(getApplicationContext());
        ops.open();

        Intent intent = getIntent();
        user = intent.getStringExtra("com.example.gareth.speakitvisualcommunication.username");
        user2 = intent.getStringExtra("com.example.gareth.speakitvisualcommunication.username2");

        // Add the array containing PecsImages objects to the
        // imageCategories list which is then added to the GridView
        // using the ImageAdapter class
        imageCategories = new ArrayList<>(Arrays.asList(categories));
        if (user != null && user == null) {
            List<PecsImages> list2 = ops.getData("Home Page", user);
            imageCategories.addAll(list2);
        } else if (user == null && user2 != null) {
            List<PecsImages> list2 = ops.getData("Home Page", user2);
            imageCategories.addAll(list2);
        }
        if (user == null) {
            user = user2;
        }
        gridView = (GridView)findViewById(R.id.gridview);
        imageAdapter = new ImageAdapter(this, imageCategories);
        gridView.setAdapter(imageAdapter);

        List<PecsImages> list = new ArrayList<>();
        list = ops.getSentenceData();
        for (PecsImages image: list) {
            ops.deleteSentenceData(image.getId());
        }

        // List sentence words is created and then PecsImages objects
        // in the sentence table ar added to the list using method from DatabaseOperations class.
        sentenceWords = new ArrayList<>();
        //List<PecsImages> list = new ArrayList<>();
        //list = ops.getSentenceData();
        //sentenceWords.addAll(list);
        // sentenceWords List is then added to the RecyclerView using
        // SentenceBuilderAdapter class.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new SentenceBuilderAdapter(sentenceWords);
        RecyclerView.LayoutManager mLayoutManage = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManage);
        recyclerView.setAdapter(mAdapter);


        // onItemClickListener setup on GridView to allow images to be selected
        gridView.setOnItemClickListener(this);


        // onItemLongClickListener setup on Gridview. This long click allows images within the GridView
        // to be deleted or updated.
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final PecsImages image = imageCategories.get(position);
                if (image.getNumber() != 1) {
                    CharSequence[] items = {"Update", "Delete"};
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainScreen.this);

                    dialog.setTitle("Choose an action");
                    dialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (item == 0) {
                                 //show dialog update at here
                                showDialogUpdate(MainScreen.this, image.getId());
                            } else {
                                showDialogDelete(image.getId());
                            }
                        }
                    });
                    dialog.show();
                    return true;
                } else {
                    return false;
                }
            }
        });

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        //onClickListener setup with play and delete button. This allows either the sentence
        //in the RecyclerView to be spoken out allowed or for an image to be deleted from
        //the sentence builder.
        ImageButton cancelButton = (ImageButton) findViewById(R.id.deleteB);
        cancelButton.setOnClickListener(this);
        cancelButton.setOnLongClickListener(this);
        //ImageButton playButton = (ImageButton) findViewById(R.id.speakB);
        //playButton.setOnClickListener(this);

    }


    /**
     * Method adds functionality to the item clicks within the GridView.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //PecsImages object selected
        PecsImages image = imageCategories.get(position);
        //The word in the object is spoken.
        speakWords(image.getWord());

        //if the item clicked has the word Add Category it takes you to the upload page
        if (image.getWord().equals("Add Category")) {
            Intent intent2  = new Intent(getApplicationContext(), Uploader.class);
            intent2.putExtra("com.example.gareth.speakitvisualcommunication.User", user);
            intent2.putExtra("com.example.gareth.speakitvisualcommunication.page", "Home Page");
            startActivity(intent2);
        } else {
            //if any other item is selected it takes you to the second screen.
            Intent intent = new Intent(getApplicationContext(), SecondScreen.class);
            intent.putExtra("com.example.gareth.speakitvisualcommunication.Category", image.getWord());
            intent.putExtra("com.example.gareth.speakitvisualcommunication.username2", user);
            startActivity(intent);
        }

    }

    /**
     *Method adds functionality to click events on the add or delete button.
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteB:
                //if the list sentenceWords is greater than zero the item at the end of the list
                //will be deleted
                if (sentenceWords.size() > 0) {
                    ops.deleteSentenceData(sentenceWords.get(sentenceWords.size()-1).getId());
                    sentenceWords.remove(sentenceWords.size()-1);
                    mAdapter.notifyDataSetChanged();

                }
                break;
            //case R.id.speakB:
                //The words from the different items in the view are added together and then spoken aloud
                //StringBuilder finalStringb =new StringBuilder();
                //for (PecsImages item : sentenceWords) {
                    //finalStringb.append(item.getWord()).append(" ");
                //}
                //speakWords(finalStringb.toString());
                //break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.deleteB:
                List<PecsImages> list = new ArrayList<>();
                list = ops.getSentenceData();
                for (PecsImages image: list) {
                    ops.deleteSentenceData(image.getId());
                }
                sentenceWords.clear();
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return false;
        }

    }

    /**
     * Method called to speak words aloud
     * @param speech
     */
    private void speakWords(String speech) {

        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    /**
     * Act on result of TTS data check
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
     * Method called when update is selected when user has clicked and held on an item within GridView.
     * This method will delete a PecsImages object from the list and remove it from the screen. It will also then update
     * the database.
     * @param activity
     * @param id
     */
    private void showDialogUpdate(Activity activity, final int id) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_pecs_images);
        dialog.setTitle("Update");

        pecsView = (ImageView) dialog.findViewById(R.id.pecsImage);
        final EditText edtName = (EditText) dialog.findViewById(R.id.pecsName);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
        ImageButton back = (ImageButton)dialog.findViewById(R.id.dialogClose);

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
                    ops.updateData(
                            edtName.getText().toString().trim(),
                            Uploader.imageViewToByte(pecsView), "Home Page",
                            id
                    );
                    Iterator<PecsImages> iterator = imageCategories.iterator();
                        while (iterator.hasNext()) {
                            if(iterator.next().getId() == id) {
                                iterator.remove();
                                imageAdapter.notifyDataSetChanged();
                                PecsImages item = ops.getItem(id);
                                imageCategories.add(item);
                                imageAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Updated successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }



    /**
     * Method deletes an image from the list used to populate the GridView
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
                    ops.deleteData(idPecs);
                    Iterator<PecsImages> iterator = imageCategories.iterator();
                    while (iterator.hasNext()) {
                        if(iterator.next().getId() == idPecs) {
                            iterator.remove();
                            imageAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Deleted successfully!!!", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
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
     *Permission to use image
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

    /**
     *Method will update the List used to populate the GridView.
     */
    private void updatePecsList() {



    }

    /**
     * onResume method
     */
    public void onResume() {
        super.onResume();
        //Open database
        ops.open();
        updatePecsList();
        //
        sentenceWords.clear();
        List<PecsImages> list = new ArrayList<>();
        list = ops.getSentenceData();
        sentenceWords.addAll(list);
        mAdapter.notifyDataSetChanged();


    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.account:
                Intent intent = new Intent(MainScreen.this, UserSelect.class);
                startActivity(intent);
                return true;
            case R.id.action_play:
                //The words from the different items in the view are added together and then spoken aloud
                StringBuilder finalStringb =new StringBuilder();
                for (PecsImages image : sentenceWords) {
                    finalStringb.append(image.getWord()).append(" ");
                }
                speakWords(finalStringb.toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     *onStop method closes database
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

        if(myTTS != null) {
            myTTS.stop();
            myTTS.shutdown();
        }
    }



}

