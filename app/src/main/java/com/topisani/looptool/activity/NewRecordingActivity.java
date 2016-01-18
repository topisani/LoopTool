package com.topisani.looptool.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;

import com.topisani.looptool.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewRecordingActivity extends Activity {
    FloatingActionButton fab;
    private String fabState;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String outputFile = null;
    private String outputDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recording);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabState = "record";
        outputDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LoopTool/";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (fabState) {
                    case "record":
                        outputFile = fileName();
                        mediaRecorder.setOutputFile(outputFile);
                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        changeFabState("stop_record");
                        Snackbar.make(fab, "Recording started", Snackbar.LENGTH_LONG).show();

                        break;
                    case "stop_record":
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;

                        changeFabState("play");

                        Snackbar.make(fab, "Audio successfully recorded", Snackbar.LENGTH_LONG).show();
                        break;
                    case "play":

                        mediaPlayer = new MediaPlayer();

                        try {
                            mediaPlayer.setDataSource(outputFile);
                        } catch (IOException e) {
                            Snackbar.make(fab, "No File found", Snackbar.LENGTH_SHORT).show();
                            changeFabState("record");
                            break;
                        }

                        try {
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                        mediaPlayer.start();

                        changeFabState("stop_play");

                        Snackbar.make(fab, "Playing Recording", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "stop_play":
                        mediaPlayer.stop();

                        changeFabState("play");

                        Snackbar.make(fab, "Stopped Playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changeFabState(String newState) {
        fabState = newState;
        Drawable icon;
        switch (newState) {
            case "play" :
                icon = getDrawable(R.drawable.ic_play_arrow_white_48dp);
                break;
            case "stop_record":
                icon = getDrawable(R.drawable.ic_stop_white_48dp);
                break;
            case "stop_play":
                icon = getDrawable(R.drawable.ic_stop_white_48dp);
                break;
            default:
                icon = getDrawable(R.drawable.ic_mic_white_48dp);
                break;
        }
        fab.setImageDrawable(icon);
    }

    private String fileName() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm_");
        String fileName = dateFormat.format(new Date());
        for (int i = 0; true; i++) {
            File file = new File(outputDir + fileName + i + ".mp3");
            if (!file.exists()) {
                return outputDir + fileName + i + ".mp3";
            }
        }
    }
}
