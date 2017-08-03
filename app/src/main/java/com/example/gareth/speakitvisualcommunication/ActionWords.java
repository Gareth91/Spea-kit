package com.example.gareth.speakitvisualcommunication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ActionWords extends AppCompatActivity implements AdapterView.OnItemClickListener, TextToSpeech.OnInitListener {

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
    private DatabaseOperations ops;


    /**
     *
     */
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_words);

        //
        ops = new DatabaseOperations(getApplicationContext());
        ops.open();

        //Set back button in the bar at the top of screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);

        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        int width = display.widthPixels;
        int height = display.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int)(height*0.9));

        //
        imageWords = new ArrayList<>();
        imageWords.clear();
        PecsImages image = new PecsImages(getString(R.string.I_Want),R.drawable.seeyou,1);
        imageWords.add(image);
        final GridView gridView = (GridView)findViewById(R.id.gridviewThird);
        imageAdapter = new ImageAdapter(this, imageWords);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(this);

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        PecsImages image = imageWords.get(position);
        speakWords(image.getWord());
        //
        if (image.getNumber() == 1) {
            Drawable drawable = getResources().getDrawable(image.getImage());
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitMapData = stream.toByteArray();
            image.setImages(bitMapData);
        }
        ops.insertSentenceData(image.getWord(),image.getImages());
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
