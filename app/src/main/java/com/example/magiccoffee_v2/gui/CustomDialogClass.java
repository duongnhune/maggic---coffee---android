package com.example.magiccoffee_v2.gui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.magiccoffee_v2.R;
import com.example.magiccoffee_v2.gui.my_interface.IClickDialog;

public class CustomDialogClass extends Dialog{

    public Button ok, cancel;
    private IClickDialog iClickDialog;

    public CustomDialogClass(@NonNull Context context, IClickDialog iClickDialog) {
        super(context);
        this.iClickDialog = iClickDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        ok = (Button) findViewById(R.id.btn_ok);
        cancel = (Button) findViewById(R.id.btn_cancel);

        ok.setOnClickListener(view -> {
            iClickDialog.onClickOk();
        });
        cancel.setOnClickListener(vew->{
            iClickDialog.onClickCancel();
        });
    }
}
