package com.example.gareth.speakitvisualcommunication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        List<PecsImages> imageCategories = new ArrayList<>(Arrays.asList(categories));
        final GridView gridView = (GridView)findViewById(R.id.gridview);
        final ImageAdapter imageAdapter = new ImageAdapter(this, imageCategories);
        gridView.setAdapter(imageAdapter);
    }

    private PecsImages[] categories = {
            new PecsImages(R.string.At_Home, R.mipmap.ic_launcher),
            new PecsImages(R.string.About_Me, R.mipmap.ic_launcher),
            new PecsImages(R.string.Food_And_Drink, R.mipmap.ic_launcher),
            new PecsImages(R.string.Greetings, R.mipmap.ic_launcher),
            new PecsImages(R.string.Leisure, R.mipmap.ic_launcher),
            new PecsImages(R.string.Favourites, R.mipmap.ic_launcher),
            new PecsImages(R.string.Todays_Activities, R.mipmap.ic_launcher),
            new PecsImages(R.string.Add_Category, R.mipmap.ic_launcher)
    };
}
