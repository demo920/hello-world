package com.teemo.zhihu.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.teemo.zhihu.R;
import com.teemo.zhihu.http.HttpUtils;
import com.teemo.zhihu.model.StartImage;
import com.teemo.zhihu.utils.LogUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();
    private Context mContext;

    private TextView mTvText;
    private ImageView mIvImg;
    private TextView mTvSplashContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mContext = this;
        setContentView(R.layout.activity_splash);

        initView();
        countDownNet();
    }

    private void initView() {
        mTvText = (TextView) findViewById(R.id.tv_text);
        mTvSplashContent = (TextView) findViewById(R.id.tv_splash_content);
        mIvImg = (ImageView) findViewById(R.id.iv_img);

        mIvImg.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        mIvImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvImg.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        });
    }

    private void requestNet() {
        HttpUtils.startImage(new Callback<StartImage>() {
            @Override
            public void onResponse(Call<StartImage> call, Response<StartImage> response) {
                startAnimator(mTvSplashContent, "alpha", 1.0f, 0.0f);
                startAnimator(mTvText, "alpha", 0.0f, 1.0f);
                StartImage sImg = response.body();
                LogUtils.e(TAG, "--" + sImg.toString());
                mTvText.setText(sImg.getText());
                getImg(sImg.getImg());
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void getImg(String url) {
        HttpUtils.getImage(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                LogUtils.e(TAG, "--OK--");
                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                mIvImg.setImageBitmap(bitmap);
                startAnimator(mIvImg, "alpha", 0.3f, 1.0f);
                countDownEnter();
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void startAnimator(View view, String propertyName, float start, float end) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, propertyName, start, end);
        alpha.setDuration(1500);//设置动画时间
        alpha.setInterpolator(new DecelerateInterpolator());//设置动画插入器，减速
        alpha.setRepeatCount(0);//设置动画重复次数，这里-1代表无限
        alpha.start();//启动动画。
    }

    private void countDownNet() {
        CountDownTimer countDownTimer = new CountDownTimer(500, 1000) {

            public void onTick(long millisUntilFinished) {
                LogUtils.i(TAG, "seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                LogUtils.e(TAG, "done!");
                requestNet();
            }
        }.start();
    }

    private void countDownEnter() {
        CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
                LogUtils.i(TAG, "seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                LogUtils.e(TAG, "done!");
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

}
