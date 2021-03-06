package com.alimuzaffar.animator.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    Button mBtnListScrollAnimator;
    Button mBtnListScrollMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnListScrollAnimator = (Button) findViewById(R.id.btnListScrollAnimator);
        mBtnListScrollMove = (Button) findViewById(R.id.btnListScrollMove);

        mBtnListScrollAnimator.setOnClickListener(this);
        mBtnListScrollMove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnListScrollAnimator) {
            Intent intent = new Intent(this, ListScrollAnimatorActivity.class);
            startActivity(intent);
        } else if (v == mBtnListScrollMove) {
            Intent intent = new Intent(this, ListScrollMoveViewActivity.class);
            startActivity(intent);
        }
    }
}
