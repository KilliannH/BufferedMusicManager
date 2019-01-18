package com.killiann;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicBufferManager {

    private static final int BUFFER_SIZE = 128000;
    private static final String path = "src/com/killiann/res/";

    private File soundFile;
    private AudioInputStream audioStream;
    private SourceDataLine sourceLine;

    public MusicBufferManager(String soundFile) {
        File f = new File(path + soundFile);
        this.soundFile = f;
        playSound();
    }

    public void playSound(){

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
        }

        AudioFormat audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();
    }
}