package com.kennedy.hiatus.speak_indictionary;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.kennedy.hiatus.speak_indictionary.controller.ViewpagerAdapter;
import com.kennedy.hiatus.speak_indictionary.model.Word;
import com.kennedy.hiatus.speak_indictionary.thread.WordLoader;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String wordString;

    private ImageButton micButton;
    private ViewPager horizontalViewPager;
    private TabLayout tabLayout;
    private ViewpagerAdapter adapter;
    private FrameLayout initialState, noInternet;
    private ProgressBar loadingScreen;

    private static final String MD_PUBLIC_API_ELEMENTARY = "https://dictionaryapi.com/api/v3/references/sd2/json";
    private final int WORD_TASK_LOADER = 1;
    private final String ELEMENTARY_DICTIONARY_KEY = "f5b63ed7-c8df-498b-bcaa-1f8982c053a9";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Fetch ID for views.
        micButton = findViewById(R.id.mic_button);
        horizontalViewPager = findViewById(R.id.bottom_horizontal_viewpager);
        tabLayout = findViewById(R.id.tab_layout);
        initialState = findViewById(R.id.initial_state_fragment_container);
        loadingScreen = findViewById(R.id.progressbar);
        noInternet = findViewById(R.id.no_internet_fragment_container);

        adapter = new ViewpagerAdapter(getSupportFragmentManager());

        micButton.setOnClickListener(v -> promptSpeechInput());




    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> wordInput = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    wordString = wordInput.get(0);
//                    Check if there is a internet connection
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo info = cm.getActiveNetworkInfo();
                    boolean isConnected = info != null &&
                            info.isConnected();

                    if (isConnected && wordString != null) {
                        getSupportLoaderManager().restartLoader(WORD_TASK_LOADER, null, asynctaskLoader);
                    } else
                        Toast.makeText(getApplicationContext(), "No internet connect", Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    private LoaderManager.LoaderCallbacks<Word> asynctaskLoader = new LoaderManager.LoaderCallbacks<Word>() {
        @NonNull
        @Override
        public Loader<Word> onCreateLoader(int id, @Nullable Bundle args) {

            Uri uriBase = Uri.parse(MD_PUBLIC_API_ELEMENTARY);
            Uri.Builder builder = uriBase.buildUpon();

            builder.appendEncodedPath(wordString);
            builder.appendQueryParameter("key", ELEMENTARY_DICTIONARY_KEY);

            initialState.setVisibility(View.GONE);
            loadingScreen.setVisibility(View.VISIBLE);
            horizontalViewPager.setVisibility(View.GONE);

            return new WordLoader(MainActivity.this, builder.toString());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Word> loader, Word data) {
            if (data != null) {

                loadingScreen.setVisibility(View.GONE);
                horizontalViewPager.setVisibility(View.VISIBLE);

                BottomResultFragment fragment = new BottomResultFragment();
                fragment.registerData(data);
                adapter.addFragment(fragment, wordString);
                horizontalViewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(horizontalViewPager);

                //set the tab to the last tab
                tabLayout.getTabAt(tabLayout.getTabCount() - 1).select();

                loader.abandon();
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Word> loader) {

        }
    };
}
