package com.example.rojsa.weatherfromaccu.ui.base;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.rojsa.weatherfromaccu.R;

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void showProgressBar(){
        if (mProgressBar == null){
            mProgressBar = new ProgressDialog(this);
            mProgressBar.setMessage(getString(R.string.text_progressbar));
            mProgressBar.show();
        }

    }

    protected void dismissProgressBar(){
        if (mProgressBar != null && mProgressBar.isShowing()){
            mProgressBar.dismiss();
        }
        mProgressBar = null;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }
}
