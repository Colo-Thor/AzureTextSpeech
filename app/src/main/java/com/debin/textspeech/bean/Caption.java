package com.debin.textspeech.bean;


import java.time.Instant;
import java.util.Optional;

public class Caption {
    public Optional<String> language;
    public int sequence;
    public Instant begin;
    public Instant end;
    public String text;

    public Caption(Optional<String> language, int sequence, Instant begin, Instant end, String text) {
        this.language = language;
        this.sequence = sequence;
        this.begin = begin;
        this.end = end;
        this.text = text;
    }
}
