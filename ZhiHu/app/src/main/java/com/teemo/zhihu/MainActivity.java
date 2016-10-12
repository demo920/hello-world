package com.teemo.zhihu;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teemo.zhihu.http.HttpUtils;
import com.teemo.zhihu.model.StartImage;
import com.teemo.zhihu.utils.LogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();
    private Context mContext;

    TextView mTvText;
    ImageView mIvImg;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mContext = this;
        setContentView(R.layout.activity_main);

        initView();
        countDown();
    }

    private void initView() {
        mTvText = (TextView) findViewById(R.id.tv_text);
        mIvImg = (ImageView) findViewById(R.id.iv_img);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mIvImg.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        mIvImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvImg.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        });
        countDownTimer = new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                LogUtils.i(TAG, "seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                LogUtils.e(TAG, "done!-gone");
                mIvImg.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                countDownTimer.start();
            }
        }.start();
    }

    private void requestNet() {
        HttpUtils.start_image(new Callback<StartImage>() {
            @Override
            public void onResponse(Call<StartImage> call, Response<StartImage> response) {
                StartImage sImg = response.body();
                LogUtils.e(TAG, "--" + sImg.toString());
                mTvText.setText(sImg.getText());
                Glide.with(mContext)
                        .load(sImg.getImg())
                        .into(mIvImg);
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void countDown() {
        CountDownTimer countDownTimer = new CountDownTimer(1500, 1000) {

            public void onTick(long millisUntilFinished) {
                LogUtils.i(TAG, "seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                LogUtils.e(TAG, "done!");
                requestNet();
            }
        }.start();
    }

}
