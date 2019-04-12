package com.kennedy.hiatus.speak_indictionary;


import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kennedy.hiatus.speak_indictionary.model.Word;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BottomResultFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private Word data;
    private TextToSpeech textToSpeech;
    private String dataWord;
    private FrameLayout container;


    public BottomResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_result, container, false);
        TextView wordDefinition = view.findViewById(R.id.word_definition);

        dataWord = data.getShortDefinition().get(0);
        wordDefinition.setText(dataWord);

        textToSpeech = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.getDefault());

                if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA)
                    Log.e(TAG, "onCreate: Language not supported!");

            } else {
                Log.e(TAG, "onCreate: TTS Initialization Failed");
            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        container = view.findViewById(R.id.fragment_container_result);

        container.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(dataWord, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak(dataWord, TextToSpeech.QUEUE_FLUSH, null);
            }

        });




    }

    public void registerData(Word data) {
        this.data = data;
    }
}
