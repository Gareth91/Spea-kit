package com.example.gareth.speakitvisualcommunication;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class SecondScreen extends AppCompatActivity implements AdapterView.OnItemClickListener, TextToSpeech.OnInitListener, AdapterView.OnItemLongClickListener, View.OnClickListener{

    //TTS object
    private TextToSpeech myTTS;

    //status check code
    private int MY_DATA_CHECK_CODE = 0;

    /**
     *
     */
    private List<PecsImages> imageWords;

    /**
     *
     */
    private List<PecsImages> sentenceWords;

    /**
     *
     */
    private List<PecsImages> list;

    /**
     *
     */
    private RecyclerView recyclerView;

    /**
     *
     */
    private SentenceBuilderAdapter mAdapter;

    /**
     *
     */
    private DatabaseOperations ops;

    /**
     *
     */
    private ImageView pecsView;

    /**
     *
     */
    private String category;

    /**
     *
     * @param savedInstanceState
     */
    private ImageAdapter imageAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);

        //Set back button in the bar at the top of screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //
        ops = new DatabaseOperations(getApplicationContext());
        ops.open();

        //
        sentenceWords = new ArrayList<>();

        //
        imageWords = new ArrayList<>();
        imageWords.clear();
        PecsImages image = new PecsImages(getString(R.string.Action_Words),R.drawable.seeyou,1);
        PecsImages addImage = new PecsImages((getString(R.string.Add_Category)), R.drawable.seeyou,1);
        imageWords.add(addImage);
        imageWords.add(image);


        //
        Intent intent = getIntent();
        category = intent.getStringExtra("com.example.gareth.speakitvisualcommunication.Category");
        list = ops.getData(category);
        switch (category){
            case "Favourites":
                break;
            case "At Home":
                break;
            case "About Me":
                break;
            case "Food and Drink":
                break;
            case "Greetings":
                PecsImages image2 = new PecsImages(getString(R.string.Good_Morning),R.drawable.home,1);
                PecsImages image3 = new PecsImages("What's your name?",R.drawable.home,1);
                PecsImages image4 = new PecsImages("See you later",R.drawable.home,1);
                imageWords.add(image2);
                imageWords.add(image3);
                imageWords.add(image4);
                break;
            case "Leisure":
                break;
            case "Today's Activities":
                break;
            default:
                break;

        }
        //
        imageWords.addAll(list);

        //
        final GridView gridView = (GridView)findViewById(R.id.gridviewSecond);
        imageAdapter = new ImageAdapter(this, imageWords);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(this);

        //
        gridView.setOnItemLongClickListener(this);

        //
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        mAdapter = new SentenceBuilderAdapter(sentenceWords);
        RecyclerView.LayoutManager mLayoutManage = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManage);
        recyclerView.setAdapter(mAdapter);

        ImageButton cancelButton = (ImageButton) findViewById(R.id.deleteB2);
        cancelButton.setOnClickListener(this);
        ImageButton playButton = (ImageButton) findViewById(R.id.speakB2);
        playButton.setOnClickListener(this);



        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        //
        List<PecsImages> sentenceList = new ArrayList<>();
        sentenceList = ops.getSentenceData();
        sentenceWords.addAll(sentenceList);
        mAdapter.notifyDataSetChanged();

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
        switch (parent.getId()) {
            case R.id.gridviewSecond:
                PecsImages image = imageWords.get(position);
                speakWords(image.getWord());
                if (image.getWord().equals("Action Words")) {
                    Intent actionWords = new Intent(getApplicationContext(), ActionWords.class);
                    startActivity(actionWords);
                } else if (image.getWord().equals("Add Category")) {
                    Intent upload = new Intent(getApplicationContext(), Uploader.class);
                    startActivity(upload);
                } else {
                    if (image.getNumber() == 1) {
                        Drawable drawable = getResources().getDrawable(image.getImage());
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitMapData = stream.toByteArray();
                        image.setImages(bitMapData);
                    }
                    ops.insertSentenceData(image.getWord(),image.getImages());
                    sentenceWords.add(image);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.recyclerView2:
                PecsImages sentenceImage = sentenceWords.get(position);
                speakWords(sentenceImage.getWord());

        }

    }

    /**
     *
     * @param adapterView
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        boolean status;
        switch (adapterView.getId()) {
            case R.id.gridviewSecond:
                final PecsImages image = imageWords.get(position);
                if (image.getNumber() != 1) {
                    CharSequence[] items = {"Update", "Delete"};
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SecondScreen.this);

                    dialog.setTitle("Choose an action");
                    dialog.setItems(items, new DialogInterface.OnClickListener() {

                        /**
                         *
                         * @param dialog
                         * @param item
                         */
                        @Override public void onClick(DialogInterface dialog, int item) {
                            if (item == 0) {
                                // show dialog update at here
                                showDialogUpdate(SecondScreen.this, image.getId());
                            } else {
                                showDialogDelete(image.getId());
                            }
                        }
                    });
                    dialog.show();
                    status = true;
                } else {
                    status = false;
                }
                break;
            case R.id.recyclerView2:
                PecsImages removeImage = sentenceWords.get(position);
                sentenceWords.remove(removeImage);
                mAdapter.notifyDataSetChanged();
                status = true;
                break;
            default:
                status = false;
                break;
        }
        return status;
    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteB2:
                if (sentenceWords.size() > 0) {
                    ops.deleteSentenceData(sentenceWords.get(sentenceWords.size()-1).getId());
                    sentenceWords.remove(sentenceWords.size()-1);
                    mAdapter.notifyDataSetChanged();

                }
                break;
            case R.id.speakB2:
                StringBuilder finalStringb =new StringBuilder();
                for (PecsImages item : sentenceWords) {
                    finalStringb.append(item.getWord()).append(" ");
                }
                speakWords(finalStringb.toString());

                break;
            default:
                break;
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
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
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
                        SecondScreen.this,
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
                            Uploader.imageViewToByte(pecsView), category,
                            id
                    );
                    Iterator<PecsImages> iterator = imageWords.iterator();
                    while (iterator.hasNext()) {
                        if(iterator.next().getId() == id) {
                            iterator.remove();
                            imageAdapter.notifyDataSetChanged();
                            PecsImages item = ops.getItem(id);
                            imageWords.add(item);
                            imageAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update successfully!!!", Toast.LENGTH_SHORT).show();
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
     *
     * @param idPecs
     */
    private void showDialogDelete(final int idPecs) {
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(SecondScreen.this);

        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure you want to this delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    ops.deleteData(idPecs);
                    Iterator<PecsImages> iterator = imageWords.iterator();
                    while (iterator.hasNext()) {
                        if(iterator.next().getId() == idPecs) {
                            iterator.remove();
                            imageAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Delete successfully!!!", Toast.LENGTH_SHORT).show();

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
     *
     */
    private void updatePecsList() {
        boolean add = true;
        // get all data from sqlite
        list = ops.getData(category);
        for(PecsImages image : imageWords) {
            for(PecsImages image2 : list) {
                if (image.getNumber() != 1 && image.getId() == image2.getId()) {
                    add = false;
                    break;
                }
            }
        }
        if (add == true) {
            imageWords.addAll(list);
            imageAdapter.notifyDataSetChanged();
        }

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
