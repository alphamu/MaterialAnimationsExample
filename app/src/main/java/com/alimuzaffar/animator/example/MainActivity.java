package com.alimuzaffar.animator.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by alimuzzaffar on 11/05/2015.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    Button mBtnListScrollAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnListScrollAnimator = (Button) findViewById(R.id.btnListScrollAnimator);

        mBtnListScrollAnimator.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnListScrollAnimator) {
            Intent intent = new Intent(this, ListScrollAnimatorActivity.class);
            startActivity(intent);
        }
    }
}
