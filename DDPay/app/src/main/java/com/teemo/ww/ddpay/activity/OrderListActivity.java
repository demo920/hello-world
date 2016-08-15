package com.teemo.ww.ddpay.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.teemo.ww.ddpay.R;
import com.teemo.ww.ddpay.adapter.ListViewAdapter;
import com.teemo.ww.ddpay.bean.Order;
import com.teemo.ww.ddpay.db.DbHelper;
import com.teemo.ww.ddpay.utils.ObjToFile;
import com.teemo.ww.ddpay.utils.StringUtil;
import com.teemo.ww.ddpay.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class OrderListActivity extends Activity {

    private ListView mListView;
    private ObjToFile objToFile;
    private List<Order> mDatas;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_order_list);

        initView();
        initData();
    }

    private void initData() {
        mDatas = new ArrayList<>();

        objToFile = new ObjToFile(this);
//        mDatas = objToFile.getObjList();
        mDatas = DbHelper.getInstance().readDbList(Order.class);
        Log.d("数据大小---" + mDatas.size(), "");
        //对日期进行升序排列
        Collections.sort(mDatas, new Comparator<Order>() {
                    @Override
                    public int compare(Order lhs, Order rhs) {
                        Date date1 = StringUtil.stringToDate(lhs.getmDateTime());
                        Date  date2= StringUtil.stringToDate(rhs.getmDateTime());
                        //使用after则进行降序排列
                        if(date1.before(date2)){
                            return 1;
                        }
                        return -1;
                    }
                });

        mListView.setAdapter(new ListViewAdapter(this,mDatas));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order) ((ListViewAdapter) parent.getAdapter()).getItem(position);
                Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                intent.putExtra(StringUtil.ORDER_DETAIL, order);
                startActivity(intent);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Order order = (Order) adapterView.getAdapter().getItem(position);
                if (order.getmCbTradeNo() == null) {
                    DbHelper.getInstance().deleteObj(Order.class, order.getId());
                    ToastUtils.show(mContext, "删除" + order.getId() + "成功");
                    mDatas = DbHelper.getInstance().readDbList(Order.class);
                    ((ListViewAdapter) mListView.getAdapter()).setDatas(mDatas);
                }
                return true;
            }
        });
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listView);
    }

}
