package com.topisani.looptool;

import android.media.MediaPlayer;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by topisani on 11/01/2016.
 */
public class Recording {

    public static final String recordingsDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LoopTool/";
    public String fileName;
    public String path;
    public File file;
    private MediaPlayer mediaPlayer;

    public Recording( String fileName ) {
        this.fileName = fileName;
        this.path = recordingsDir + fileName;
        this.file = new File(this.path);
        this.mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this.path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Play recording
     */
    public void play() {
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    /**
     * Stop playing recording
     */
    public void stop() {
        mediaPlayer.stop();
    }

    /**
     * Deletes the recording file
     */
    public void delete() {
        this.file.delete();
    }
    /**
     * returns a list of all recordings
     * @return Arraylist of all recordings
     */
    public static ArrayList<Recording> getAll() {
        File dir = new  File(recordingsDir);
        if (! dir.exists() && ! dir.isDirectory()) {
            dir.mkdirs();
        }
        File[] files = dir.listFiles();
        ArrayList<Recording> list = new ArrayList<>();
        for (int i = 0; i < files.length; ++i) {
            list.add(new Recording(files[i].getName()));
        }
        return list;
    }
}
