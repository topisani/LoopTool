package com.topisani.looptool.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.topisani.looptool.R;
import com.topisani.looptool.dialog.BottomSheetDialog;
import com.topisani.looptool.Recording;
import com.topisani.looptool.adapter.RecordingListAdapter;

import java.util.ArrayList;


public class RecordingListActivity extends AppCompatActivity {

    ListView recordingListView;
    ArrayList<Recording> recordingList;
    RecordingListAdapter recordingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_list);

        recordingListView = (ListView) findViewById(R.id.recording_list);

        recordingList = Recording.getAll();
        recordingListAdapter = new RecordingListAdapter(this, recordingList);
        recordingListView.setAdapter(recordingListAdapter);

        recordingListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                View v = getLayoutInflater ().inflate (R.layout.dialog_existing_recording, null);
                final Button play = (Button) v.findViewById( R.id.play );
                final Button delete = (Button) v.findViewById( R.id.delete );
                final TextView name = (TextView) v.findViewById( R.id.name );

                final Recording recording = recordingList.get(position);

                name.setText(recording.fileName);

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog (RecordingListActivity.this, v);

                bottomSheetDialog.show();

                play.setOnClickListener(new Button.OnClickListener() {
                    String buttonState = "play";

                    @Override
                    public void onClick(View v) {

                        switch (buttonState) {
                            case "play":

                                recording.play();
                                buttonState = "stop";
                                play.setText(R.string.stop);

                                Snackbar.make(v, "Playing Recording", Snackbar.LENGTH_SHORT).show();
                                break;

                            case "stop":

                                recording.stop();
                                buttonState = "play";
                                play.setText(R.string.play);

                                Snackbar.make(v, "Stopped Playing", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
                delete.setOnClickListener( new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        recording.delete();
                        recordingListAdapter.remove(recording);
                        bottomSheetDialog.dismiss();
                        Snackbar.make(v, "Deleted", Snackbar.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        });


    }

    public void onFabClick(View v) {
        View view = getLayoutInflater().inflate(R.layout.dialog_new_recording, null);
        TextView txtRecord = (TextView) view.findViewById( R.id.txt_record);

        final Dialog bottomSheetDialog = new BottomSheetDialog(RecordingListActivity.this, view);
        bottomSheetDialog.show();

        txtRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordingListActivity.this, NewRecordingActivity.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        recordingList = Recording.getAll();
        recordingListAdapter.notifyDataSetChanged();
        super.onResume();
    }

}