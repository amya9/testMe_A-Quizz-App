package com.example.testme2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testme2.models.MasterModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private Button startBtn , bookmarksBtn;
    private TextView master_name;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        master_name = findViewById(R.id.main_master_name);
        master_name.setText(getIntent().getStringExtra("master_name") + " Sir");

        bookmarksBtn = findViewById(R.id.bookmarks_btn);
        //Mobile ads initialize
        MobileAds.initialize(this);

        loadAds();
        String master_key =  getIntent().getStringExtra("master_key") ;

// Storing master_key into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("masterKeySH" , MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor editMasterSH = sharedPreferences.edit();
        editMasterSH.putString("masterKey" , master_key);
        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        editMasterSH.apply();

        Log.e(TAG , master_key);


        startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoryIntent = new Intent(MainActivity.this , CategoriesActivity.class);
                startActivity(categoryIntent);
            }
        });
        bookmarksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookmarkIntent = new Intent(MainActivity.this , BookmarksActivity.class);
                startActivity(bookmarkIntent);
            }
        });

    }
    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}