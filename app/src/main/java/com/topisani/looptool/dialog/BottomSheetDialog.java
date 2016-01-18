package com.topisani.looptool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.topisani.looptool.R;

/**
 * Created by topisani on 11/01/2016.
 */
public class BottomSheetDialog extends Dialog {
    public BottomSheetDialog(Context context, View view) {
        super(context, R.style.MaterialDialogSheet);
        this.setContentView(view);
        this.setCancelable(true);
        this.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        this.getWindow ().setGravity(Gravity.BOTTOM);
    }
}
