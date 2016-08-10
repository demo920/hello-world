package com.teemo.ww.ddpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.teemo.ww.ddpay.R;
import com.teemo.ww.ddpay.utils.StringUtil;

public class BookOrderActivity extends Activity {
    private EditText mPayCountEt;
    private Button mPayBtn;
    private TextView mGoodsNameTv;

    private static final int REQUEST_CODE = 1;
    public static final int OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_order);

        initView();
        initData();
    }

    private void initData() {
    }

    private void initView() {
        mPayCountEt = (EditText) findViewById(R.id.et_money_count);
        mPayBtn = (Button) findViewById(R.id.btn_pay);
        mGoodsNameTv = (TextView) findViewById(R.id.tv_goods_name);

        Editable etext = mPayCountEt.getText();
        Selection.setSelection(etext, etext.length());

        mPayBtn.setOnClickListener(payClick);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == OK ){

            finish();
        }
    }

    private View.OnClickListener payClick = new View.OnClickListener() {
        @Override public void onClick(View v) {
            String amount = mPayCountEt.getText().toString();

            Intent intent = new Intent(BookOrderActivity.this,PayOrderActivity.class);
            intent.putExtra(StringUtil.PAY_AMOUNT,amount);
            intent.putExtra(StringUtil.GOODS_NAME, mGoodsNameTv.getText().toString());
            startActivityForResult(intent, REQUEST_CODE);

        }
    };
}
