package com.example.gareth.speakitvisualcommunication;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SecondScreen extends AppCompatActivity implements AdapterView.OnItemClickListener, TextToSpeech.OnInitListener{

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
    private RecyclerView recyclerView;

    /**
     *
     */
    private SentenceBuilderAdapter mAdapter;

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

        sentenceWords = new ArrayList<>();

        imageWords = new ArrayList<>();
        imageWords.clear();
        PecsImages image = new PecsImages(getString(R.string.Action_Words),R.mipmap.ic_launcher,1);
        imageWords.add(image);

        Intent intent = getIntent();
        String category = intent.getStringExtra("com.example.gareth.speakitvisualcommunication.Category");
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
                PecsImages image2 = new PecsImages(getString(R.string.Good_Morning),R.mipmap.ic_launcher,1);
                PecsImages image3 = new PecsImages("What's your name?",R.mipmap.ic_launcher,1);
                PecsImages image4 = new PecsImages("See you later",R.mipmap.ic_launcher,1);
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


        final GridView gridView = (GridView)findViewById(R.id.gridviewSecond);
        imageAdapter = new ImageAdapter(this, imageWords);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(this);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        mAdapter = new SentenceBuilderAdapter(sentenceWords);
        RecyclerView.LayoutManager mLayoutManage = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManage);
        recyclerView.setAdapter(mAdapter);


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
        switch (parent.getId()) {
            case R.id.gridviewSecond:
                PecsImages image = imageWords.get(position);
                speakWords(image.getWord());
                if (image.getWord().equals("Action Words")) {
                    Intent actionWords = new Intent(getApplicationContext(), ActionWords.class);
                    startActivity(actionWords);
                } else {
                    sentenceWords.add(image);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.recyclerView2:


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


}
