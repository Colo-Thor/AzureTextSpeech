package com.debin.textspeech.bean;


import java.util.List;

public class SpeekItem {
    private String language;
    private List<String> localNameList;
    private List<String> shortNameList;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getLocalNameList() {
        return localNameList;
    }

    public void setLocalNameList(List<String> localNameList) {
        this.localNameList = localNameList;
    }

    public List<String> getShortNameList() {
        return shortNameList;
    }

    public void setShortNameList(List<String> shortNameList) {
        this.shortNameList = shortNameList;
    }
}
