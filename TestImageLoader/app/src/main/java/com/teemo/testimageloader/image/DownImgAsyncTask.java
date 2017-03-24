package com.teemo.testimageloader.image;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.teemo.testimageloader.image.ImageLoadUtils;

/**
 * Created by Asus on 2017/3/14.
 */


class DownImgAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView showImageView;

    DownImgAsyncTask(ImageView showImageView) {
        this.showImageView = showImageView;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        showImageView.setImageBitmap(null);

    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // TODO Auto-generated method stub
        Bitmap b = ImageLoadUtils.getImageBitmap(params[0]);
        return b;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        if (result != null) {
            showImageView.setImageBitmap(result);
        }
    }


}
