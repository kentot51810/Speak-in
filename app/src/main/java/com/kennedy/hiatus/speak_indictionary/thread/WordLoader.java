package com.kennedy.hiatus.speak_indictionary.thread;

import android.content.Context;

import com.kennedy.hiatus.speak_indictionary.model.Word;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class WordLoader extends AsyncTaskLoader<Word> {

    private String URL;

    public WordLoader(@NonNull Context context, String URL) {
        super(context);
        this.URL = URL;
    }

    @Nullable
    @Override
    public Word loadInBackground() {
        if (URL == null){
            return null;
        }

        Word word = QueryUtils.fetchWordData(URL);
        return word;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
