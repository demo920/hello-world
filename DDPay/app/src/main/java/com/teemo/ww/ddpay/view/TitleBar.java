package com.teemo.ww.ddpay.view;/*
 * Copyright (c) 2011-2015. ShenZhen iBOXPAY Information Technology Co.,Ltd.
 *
 * All right reserved.
 *
 * This software is the confidential and proprietary
 * information of iBoxPay Company of China.
 * ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only
 * in accordance with the terms of the contract agreement
 * you entered into with iBoxpay inc.
 * TitleBar.java ,Created by: wangxiunian ,2015-06-10 20:17:15 ,lastModified:2015-06-10 20:17:06
 */


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teemo.ww.ddpay.R;
import com.teemo.ww.ddpay.utils.StringUtil;

/**
 * it is the common titlebar
 * usage in xml :
 * <pre>    &lt;com.iboxpay.minicashbox.ui.widget.TitleBar
 *             android:id="@+id/titlebar"
 *             xmlns:app="http://schemas.android.com/apk/res-auto"
 *             android:layout_width="match_parent"
 *             android:layout_height="wrap_content"
 *             android:title="@string/title_suggest_feedback"
 *             app:rightBtnText="@string/next"
 *             app:contentInsetEnd="0dp"
 *             app:contentInsetStart="0dp"/&gt;
 * </pre>
 */
public class TitleBar extends RelativeLayout {

    private String mTitle;
    private TextView mTitleTv;
    private ImageView mBackIv;
    private TextView mNextBtn;
    private TextView mLeftTextTv;
    private View mBackLayout;
    private TextView mPrintReceiptBtn;

    public TitleBar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        initView(context, attrs);
        if (context instanceof Activity) {
            mBackLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) context).onBackPressed();
                }
            });
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mNextBtn != null && mBackLayout != null) {
            int maxLenth = mNextBtn.getWidth() > mBackLayout.getWidth() ? mNextBtn.getWidth() : mBackLayout
                    .getWidth();
            mTitleTv.setPadding(maxLenth, mTitleTv.getPaddingTop(),
                    maxLenth, mTitleTv.getPaddingBottom());
        }
      /*
        Paint textPaint = mTitleTv.getPaint();
        String text = mTitleTv.getText().toString();
        textPaint.getTextBounds(text, 0, text.length(), mBounds);
        int textWidth = mBounds.width();
        if (mTitleTv.getWidth() != 0 && textWidth + mBackLayout.getWidth() + 10
                > mTitleTv.getWidth()) {
            mTitleTv.setGravity(Gravity.START);
        } else {
            mTitleTv.setGravity(Gravity.CENTER);
            mTitleTv.setPadding(mTitleTv.getPaddingLeft() + leftOffsetPadding, mTitleTv.getPaddingTop(),
                    mTitleTv.getPaddingRight(), mTitleTv.getPaddingBottom());
        }*/
    }

    public void setTitle(CharSequence title) {
        mTitle = title == null ? null : title.toString();
        mTitleTv.setText(title);
    }

    public void setTitle(int resId) {
        setTitle(getContext().getString(resId));
    }

    public String getTitle() {
        return mTitle;
    }

    public void setVisible(boolean visible) {
        this.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void initView(Context context, AttributeSet attrs) {

        View layout = LayoutInflater.from(getContext()).inflate(R.layout.layout_titlebar, this);
        mBackIv = (ImageView) layout.findViewById(R.id.iv_titlebar_left);
        mNextBtn = (TextView) layout.findViewById(R.id.btn_titlebar_right);
        mTitleTv = (TextView) layout.findViewById(R.id.tv_titlebar_name);
        mLeftTextTv = (TextView) layout.findViewById(R.id.tv_left_text);
        mBackLayout = layout.findViewById(R.id.layout_titlebar_left);
        mPrintReceiptBtn = (TextView) layout.findViewById(R.id.btn_titlebar_print_receipt);

//        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        addView(layout, layoutParams);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        mTitle = typedArray.getString(R.styleable.TitleBar_android_title);
        if (StringUtil.checkString(mTitle)) {
            setTitle(mTitle);
        } else {
            setTitle("");
        }

        boolean showLeftArrow = typedArray.getBoolean(R.styleable.TitleBar_showLeftArrow, true);
        mBackIv.setVisibility(showLeftArrow ? View.VISIBLE : View.GONE);
        String rigntBtnText = typedArray.getString(R.styleable.TitleBar_rightBtnText);
        if (StringUtil.checkString(rigntBtnText)) {
            mNextBtn.setText(rigntBtnText);
        } else {
            mNextBtn.setVisibility(View.GONE);
        }
        String leftBtnText = typedArray.getString(R.styleable.TitleBar_leftBtnText);
        if (StringUtil.checkString(leftBtnText)) {
            mLeftTextTv.setVisibility(VISIBLE);
            mLeftTextTv.setText(leftBtnText);
        } else {
            mLeftTextTv.setVisibility(View.GONE);
        }
        //int rightBtnShape = typedArray.getInt(R.styleable.TitleBar_rightBtnShape, -1);
        typedArray.recycle();
    }


    public void setRightBtn(String title, OnClickListener listener) {
        mNextBtn.setVisibility(View.VISIBLE);
        mNextBtn.setText(title);
        mNextBtn.setOnClickListener(listener);
    }

    public void setPrintReceiptBtn(String titleString, OnClickListener listener) {
        mPrintReceiptBtn.setVisibility(View.VISIBLE);
        mPrintReceiptBtn.setText(titleString);
        mPrintReceiptBtn.setOnClickListener(listener);
    }

    public void setRightBtnEnable(boolean enable) {
        mNextBtn.setEnabled(enable);
    }

    public void setRightBtnClickListener(OnClickListener listener) {
        mNextBtn.setOnClickListener(listener);
    }

    public void setRightBtnVisible(int visible) {
        mNextBtn.setVisibility(visible);
    }

    public void setBackBtnClickListener(OnClickListener listener) {
        mBackLayout.setOnClickListener(listener);
    }

    public void setBackBtnVisibility(int visibility) {
        mBackLayout.setVisibility(visibility);
    }

    public View getBackView() {
        return mBackLayout;
    }

    public void hideBackView() {
        mBackLayout.setClickable(false);
        mBackLayout.setVisibility(View.INVISIBLE);
    }

    public TextView getRightBtnView() {
        return mNextBtn;
    }

    public TextView getTitleView() {
        return mTitleTv;
    }
}
