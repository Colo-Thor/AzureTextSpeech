package com.debin.textspeech.util;

import android.util.Log;

import com.debin.textspeech.MyApplication;
import com.microsoft.cognitiveservices.speech.audio.PullAudioInputStreamCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BinaryAudioStreamReader extends PullAudioInputStreamCallback {
    private static final String TAG = BinaryAudioStreamReader.class.getSimpleName();
    private InputStream inputStream;
    private long fileLength;
    private long readLength = 0;

    public BinaryAudioStreamReader(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        fileLength = file.length();
        MyApplication.getInstance().setFileLength(fileLength);
        inputStream = new FileInputStream(file);
    }

    @Override
    public int read(byte[] dataBuffer) {
        try {
            int length = inputStream.read(dataBuffer, 0, dataBuffer.length);
            readLength += length;
            MyApplication.getInstance().setReadLength(readLength);
            return length;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "fileLength: " + fileLength + ", readLength: " + readLength);
            readLength = fileLength;
        }
        return 0;
    }

    /**
     * Closes the audio input stream.
     */
    @Override
    public void close() {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
