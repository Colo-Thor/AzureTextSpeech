package com.debin.textspeech.bean;

import java.time.Instant;

public class CaptionTiming {
    public Instant begin;
    public Instant end;

    public CaptionTiming(Instant begin, Instant end) {
        this.begin = begin;
        this.end = end;
    }
}
