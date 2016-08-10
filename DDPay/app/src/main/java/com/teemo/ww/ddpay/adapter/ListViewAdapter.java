package com.teemo.ww.ddpay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teemo.ww.ddpay.R;
import com.teemo.ww.ddpay.bean.Order;

import java.util.List;

/**
 * Created by admin on 2016/8/10.
 */

public class ListViewAdapter extends BaseAdapter {
    private List<Order> mDatas;
    private Context mContext;

    public ListViewAdapter(Context context,List<Order> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    public void setDatas(List<Order> datas){
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override public int getCount() {
        return mDatas.size();
    }

    @Override public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    public String getOrderNo(int position) {
        return mDatas.get(position).getmOutTradeNo();
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {

            convertView =
                    LayoutInflater.from(mContext).inflate(R.layout.layout_order_item, null);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Order order = mDatas.get(position);

        viewHolder.mGoodsNameTv.setText(order.getmGoodsName());
        viewHolder.mAmountTv.setText(order.getmAmount());
        viewHolder.mDateTv.setText(order.getmDateTime());

        if (!TextUtils.isEmpty(order.getmTradeStatus()) && order.getmTradeStatus()
                .contains("成功")) {
            viewHolder.mTradeStatusTv.setTextColor(Color.parseColor("#27C861"));
        } else {
            viewHolder.mTradeStatusTv.setTextColor(Color.parseColor("#ADADAD"));
        }
        viewHolder.mTradeStatusTv.setText(order.getmTradeStatus());

        return convertView;
    }

    static class ViewHolder {
        TextView mGoodsNameTv, mAmountTv, mDateTv, mTradeStatusTv;

        public ViewHolder(View convertView) {
            mGoodsNameTv = (TextView) convertView.findViewById(R.id.tv_goods_name);
            mAmountTv = (TextView) convertView.findViewById(R.id.tv_amount);
            mDateTv = (TextView) convertView.findViewById(R.id.tv_date);
            mTradeStatusTv =
                    (TextView) convertView.findViewById(R.id.tv_trade_status);
        }
}


}
