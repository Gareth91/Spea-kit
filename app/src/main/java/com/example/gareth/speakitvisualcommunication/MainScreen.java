package com.example.gareth.speakitvisualcommunication;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainScreen extends AppCompatActivity implements AdapterView.OnItemClickListener, TextToSpeech.OnInitListener{

    //TTS object
    private TextToSpeech myTTS;

    //status check code
    private int MY_DATA_CHECK_CODE = 0;

    //
    private List<PecsImages> imageCategories;

    //
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



    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        imageCategories = new ArrayList<>(Arrays.asList(categories));
        final GridView gridView = (GridView)findViewById(R.id.gridview);
        final ImageAdapter imageAdapter = new ImageAdapter(this, imageCategories);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(this);

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

        Intent intent = new Intent(getApplicationContext(), SecondScreen.class);
        intent.putExtra("com.example.gareth.speakitvisualcommunication.Category", image.getWord());
        startActivity(intent);

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

}

