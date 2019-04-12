package com.kennedy.hiatus.speak_indictionary.model;

import java.util.List;

public class Word {
    private String word;
    private List<String> shortDefinition;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getShortDefinition() {
        return shortDefinition;
    }

    public void setShortDefinition(List<String> shortDefinition) {
        this.shortDefinition = shortDefinition;
    }
}
