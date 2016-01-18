package com.topisani.looptool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.topisani.looptool.R;
import com.topisani.looptool.Recording;

import java.util.ArrayList;

/**
 * Created by topisani on 10/01/2016.
 */
public class RecordingListAdapter extends ArrayAdapter<Recording> {

    private final Context context;
    private final ArrayList<Recording> values;

    public RecordingListAdapter(Context context, ArrayList<Recording> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_recording_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(values.get(position).fileName);

        return rowView;
    }
}
