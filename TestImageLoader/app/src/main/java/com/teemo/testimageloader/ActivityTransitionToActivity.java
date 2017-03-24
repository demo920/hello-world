package com.teemo.testimageloader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.teemo.testimageloader.image.ImageLoadUtils;

/**
 * Activity that gets transitioned to
 */
public class ActivityTransitionToActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_transition_to);

        ImageView imageView = (ImageView) findViewById(R.id.iv_photo);

        Intent intent = getIntent();
        if (intent != null) {
            String url = intent.getStringExtra("bitmap");
            Log.i("TAG","ok---"+url);
            ImageLoadUtils.loadImage(imageView.getContext(), url, imageView);
        }else {

            Log.e("TAG","no---");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
