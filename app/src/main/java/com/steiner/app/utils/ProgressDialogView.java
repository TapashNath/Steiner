package com.steiner.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.steiner.app.R;


public class ProgressDialogView extends Dialog{

    public ProgressDialogView(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.loding_dialogue_view);
        setCancelable(false);
    }
    public ProgressDialogView(){
        super(MyApplication.context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.loding_dialogue_view);
        setCancelable(false);
    }

}
