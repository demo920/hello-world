package com.teemo.testimageloader;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.teemo.testimageloader.image.ImageLoadUtils;

import java.util.List;

/**
 * 适配器
 * Created by Asus on 2017/3/13.
 */

class TestAdapter extends RecyclerView.Adapter {

    private ScrollingActivity scrollingActivity;
    private final List<String> mData;

    public interface Listener {
        void onImageClicked(View view, String drawable);
    }

    Listener mListener;

    public TestAdapter(ScrollingActivity scrollingActivity, List<String> data, Listener listener) {
        this.scrollingActivity = scrollingActivity;
        this.mData = data;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(scrollingActivity).inflate(R.layout.adapter_image, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ImageView imageView = ((ImageHolder) holder).image;

//        new DownImgAsyncTask(imageView).execute(mData.get(position));

//        SimpleTarget viewTarget = new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                imageView.setImageBitmap(resource);
//            }
//        };

        ((ImageHolder) holder).image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onImageClicked(view, mData.get(position));
            }
        });
        ImageLoadUtils.loadImage(imageView.getContext(), mData.get(position), imageView);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ImageHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
}
