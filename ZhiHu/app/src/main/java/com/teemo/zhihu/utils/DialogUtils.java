//package com.teemo.zhihu.utils;
//
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.app.TimePickerDialog;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.support.v7.app.AlertDialog;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.Window;
//import android.widget.CalendarView;
//import android.widget.DatePicker;
//import android.widget.TextView;
//import android.widget.TimePicker;
//
//import com.afollestad.materialdialogs.GravityEnum;
//import com.afollestad.materialdialogs.MaterialDialog;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.List;
//
//import ddtech.ddtechcmsandroid.R;
//import ddtech.ddtechcmsandroid.model.MonitorMenuOrderDetail;
//import ddtech.ddtechcmsandroid.widget.FeedBackProDialog;
//import ddtech.ddtechcmsandroid.widget.InHandleDialog;
//import ddtech.ddtechcmsandroid.widget.OrderDetailDialog;
//import ddtech.ddtechcmsandroid.widget.TwoInputDialog;
//
///**
// * 对话框管理类
// * Created by admin on 2016/8/24.
// */
//
//public class DialogUtils {
//
//    private static final String TAG = "DialogUtils";
//
//    public static void showSelectDialog(Context mContext, String title, String content, MaterialDialog.SingleButtonCallback positiveCallback, MaterialDialog.SingleButtonCallback negativeCallback) {
//        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
//                .title(title)
//                .titleColorRes(R.color.colorPrimary)
//                .titleGravity(GravityEnum.CENTER)
//                .content(content)
//                .titleColorRes(R.color.colorPrimary)
//                .positiveColorRes(R.color.colorPrimary)
//                .contentColorRes(R.color.colorGray3)
//                .negativeColorRes(R.color.colorGray3)
//                .positiveText(R.string.sure)
//                .negativeText(R.string.disagree)
//                .onNegative(negativeCallback)
//                .onPositive(positiveCallback)
//                .build();
//        Window window = dialog.getWindow();
//        window.setWindowAnimations(R.style.dialog_exit);
//        dialog.show();
//    }
//
//    public static void showSelectDialog(Context mContext, int titleId, int contentId, MaterialDialog.SingleButtonCallback positiveCallback, MaterialDialog.SingleButtonCallback negativeCallback) {
//        showSelectDialog(mContext, mContext.getString(titleId), mContext.getString(contentId), positiveCallback, negativeCallback);
//    }
//
//    /**
//     * @param mContext s
//     * @param titleId  标题
//     * @param itemsRes 资源文件
//     * @param tv       数据赋值给TextView控件
//     */
//    public static void showListDialog(Context mContext, int titleId, int itemsRes, final TextView tv) {
//        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
//                .title(mContext.getString(titleId))
//                .titleGravity(GravityEnum.CENTER)
//                .titleColorRes(R.color.colorPrimary)
//                .positiveColorRes(R.color.colorPrimary)
//                .dividerColorRes(R.color.colorGray6)
//                .items(itemsRes)
//                .itemsCallback(new MaterialDialog.ListCallback() {
//                    @Override
//                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                        if (tv != null) {
//                            tv.setText(text);
//                        }
//                    }
//                })
//                .build();
//
//        Window window = dialog.getWindow();
//        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
//        window.setWindowAnimations(R.style.dialog_exit);  //添加动画
//        dialog.show();
//    }
//
//    /**
//     * @param mContext s
//     * @param titleId  标题
//     * @param data     dialog数据
//     * @param tv       数据赋值给TextView控件
//     */
//    public static void showListDialog(Context mContext, int titleId, String[] data, final TextView tv) {
//        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
//                .title(mContext.getString(titleId))
//                .titleGravity(GravityEnum.CENTER)
//                .titleColorRes(R.color.colorPrimary)
//                .dividerColorRes(R.color.colorGray6)
//                .items(data)
//                .itemsCallback(new MaterialDialog.ListCallback() {
//                    @Override
//                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                        if (tv != null) {
//                            tv.setText(text);
//                        }
//                    }
//                })
//                .show();
//
//        Window window = dialog.getWindow();
//        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
//        window.setWindowAnimations(R.style.dialog_exit);  //添加动画
//        dialog.show();
//    }
//
//    /**
//     * @param mContext     s
//     * @param titleId      标题
//     * @param itemsRes     资源文件
//     * @param listCallback 回调
//     */
//    public static void showListDialogWithCallback(Context mContext, int titleId, int itemsRes, MaterialDialog.ListCallback listCallback) {
//        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
//                .title(mContext.getString(titleId))
//                .titleGravity(GravityEnum.CENTER)
//                .titleColorRes(R.color.colorPrimary)
//                .dividerColorRes(R.color.colorGray6)
//                .items(itemsRes)
//                .itemsCallback(listCallback)
//                .show();
//        Window window = dialog.getWindow();
//        window.setWindowAnimations(R.style.dialog_exit);
//        dialog.show();
//    }
//
//    public static void showDialog(Context mContext, int titleId, int itemsRes, MaterialDialog.ListCallback listCallback) {
//        new MaterialDialog.Builder(mContext)
//                .title(mContext.getString(titleId))
//                .titleGravity(GravityEnum.CENTER)
//                .titleColorRes(R.color.colorPrimary)
//                .dividerColorRes(R.color.colorGray6)
//                .items(itemsRes)
//                .itemsCallback(listCallback)
//                .show();
//    }
//
//    /**
//     * @param mContext     s
//     * @param titleId      标题
//     * @param data         数据
//     * @param listCallback 回调
//     */
//    public static void showListDialogWithCallback(Context mContext, int titleId, String[] data, MaterialDialog.ListCallback listCallback) {
//        new MaterialDialog.Builder(mContext)
//                .title(mContext.getString(titleId))
//                .titleGravity(GravityEnum.CENTER)
//                .titleColorRes(R.color.colorPrimary)
//                .dividerColorRes(R.color.colorGray6)
//                .items(data)
//                .itemsCallback(listCallback)
//                .show();
//    }
//
//    public static MaterialDialog showLoading(Context mContext, int titleId) {
//        return showLoading(mContext, mContext.getString(titleId), mContext.getString(R.string.loading));
//    }
//
//    public static MaterialDialog showLoading(Context mContext, String title, String content) {
//        return new MaterialDialog.Builder(mContext)
//                .content(content)
//                .progress(true, 100)
//                .show();
//    }
//
//    public static MaterialDialog showLoading(Context mContext) {
//        return new MaterialDialog.Builder(mContext)
//                .content(mContext.getString(R.string.loading))
//                .progress(true, 100)
//                .cancelable(false)
//                .show();
//    }
//
//    public static MaterialDialog showDialog(Context mContext, int titleId, int contentId, MaterialDialog.SingleButtonCallback callback) {
//        return showDialog(mContext, mContext.getString(titleId), mContext.getString(contentId),
//                mContext.getString(R.string.sure), mContext.getString(R.string.no), callback);
//    }
//
//    public static MaterialDialog showDialog(Context mContext, String titleId, String contentId,
//                                            String positiveText, String negativeText, MaterialDialog.SingleButtonCallback callback) {
//        return new MaterialDialog.Builder(mContext)
//                .title(titleId)
//                .content(contentId)
//                .contentColorRes(R.color.colorGray6)
//                .titleColorRes(R.color.colorPrimary)
//                .positiveColorRes(R.color.colorPrimary)
//                .positiveText(positiveText)
//                .negativeText(negativeText)
//                .negativeColorRes(R.color.colorGray6)
//                .onPositive(callback)
//                .show();
//    }
//
//    /**
//     * @param mContext
//     * @param title
//     * @param content
//     * @param itemRes
//     * @param inputCallback
//     * @return
//     */
//    public static Dialog showInputDialog(final Context mContext, String title, String content, int itemRes,
//                                         MaterialDialog.InputCallback inputCallback) {
//        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
//                .title(title)
//                .titleColorRes(R.color.colorPrimary)
//                .titleGravity(GravityEnum.CENTER)
//                .content(content)
//                .input(mContext.getString(R.string.remark_here), null, false, inputCallback)
//                .titleColorRes(R.color.colorPrimary)
//                .positiveColorRes(R.color.colorPrimary)
//                .contentColorRes(R.color.colorGray3)
//                .negativeColorRes(R.color.colorGray3)
//                .positiveText(R.string.sure)
//                .negativeText(R.string.disagree)
//                .build();
//        Window window = dialog.getWindow();
//        window.setWindowAnimations(R.style.dialog_exit);
//        dialog.show();
//        return dialog;
//    }
//
//    public static InHandleDialog showInHandleDialog(Context mContext, String titleStr, View.OnClickListener sureListener) {
//        return new InHandleDialog(mContext, titleStr, sureListener);
//    }
//
//    public static TwoInputDialog showTwoInputDialog(Context mContext, String titleStr, TwoInputDialog.InputClickListener sureListener) {
//        TwoInputDialog twoInputDialog = new TwoInputDialog(mContext, titleStr, sureListener);
//        twoInputDialog.mDialog.show();
//        return twoInputDialog;
//    }
//
//    public static OrderDetailDialog showMenuDetailDialog(Context mContext, List<MonitorMenuOrderDetail> datas, View.OnClickListener sureListener) {
//        return new OrderDetailDialog(mContext, datas);
//    }
//
//    public static FeedBackProDialog showRemarkDialog(Context mContext, FeedBackProDialog.InputClickListener sureListener) {
//        return new FeedBackProDialog(mContext,sureListener);
//    }
//
//    /**
//     * 选择日期
//     *
//     * @param mContext s
//     * @param tv       设置控件
//     */
//    public static void calendarPick(Context mContext, final TextView tv) {
//        final Dialog dialog = new AlertDialog.Builder(mContext).setView(R.layout.calender).show();
//        CalendarView cv = (CalendarView) dialog.findViewById(R.id.calenddarView);
//        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
//                tv.setText(i + "." + (i1 + 1) + "." + i2);
//                dialog.dismiss();
//            }
//        });
//    }
//
//    /**
//     * 选择日期
//     *
//     * @param mContext s
//     * @param tv       设置控件
//     */
//    public static void datePicker(Context mContext, final TextView tv) {
//        Calendar c = Calendar.getInstance();
//        //取得系统日期:
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        final Dialog dialog = new AlertDialog.Builder(mContext).setView(R.layout.dialog_date_picker).show();
//        DatePicker dp = (DatePicker) dialog.findViewById(R.id.dp_view);
//        dp.init(year, month, day, new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                // 获取一个日历对象，并初始化为当前选中的时间
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year, monthOfYear, dayOfMonth);
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                String time = format.format(calendar.getTime());
//                tv.setText(time);
//                dialog.dismiss();
//            }
//        });
//    }
//
//    /**
//     * 选择日期
//     *
//     * @param mContext s
//     * @param tv       设置控件
//     */
//    public static void datePickerDialog(Context mContext, String title, final TextView tv) {
//        Calendar c = Calendar.getInstance();
//        //取得系统日期:
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                // 获取一个日历对象，并初始化为当前选中的时间
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year, month, dayOfMonth);
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                String time = format.format(calendar.getTime());
//                tv.setText(time);
//            }
//        }, year, month, day);
//        dialog.setTitle(title);
//
//        dialog.show();
//    }
//
//    public static void datePickerDialogWithCallback(Context mContext, DatePickerDialog.OnDateSetListener callback) {
//        Calendar c = Calendar.getInstance();
//        //取得系统日期:
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        DatePickerDialog dialog = new DatePickerDialog(mContext, callback, year, month, day);
//        dialog.show();
//    }
//
//    /**
//     * 选择日期
//     *
//     * @param mContext s
//     */
//    public static Dialog datePickerDialog(Context mContext, DatePickerDialog.OnDateSetListener listener) {
//        ContentResolver cv = mContext.getContentResolver();
//        String strTimeFormat = android.provider.Settings.System.getString(cv,
//                android.provider.Settings.System.TIME_12_24);
//        if (strTimeFormat.equals("24")) {
//            Log.i("activity", "24");
//        }
//        Calendar c = Calendar.getInstance();
//        //取得系统日期:
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        return new DatePickerDialog(mContext, listener, year, month, day);
//    }
//
//    /**
//     * 设置小时分钟
//     *
//     * @param mContext s
//     * @param tv       设置控件
//     */
//    public static String clockPick(Context mContext, final TextView tv) {
//        final String time = "";
//        Calendar c = Calendar.getInstance();
//        //取得系统日期:
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        //取得系统时间：
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//
//        new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//                String time = hour + "：" + minute;
//                tv.setText(time);
//            }
//        }, hour, minute, true).show();
//        return time;
//    }
//
//}
