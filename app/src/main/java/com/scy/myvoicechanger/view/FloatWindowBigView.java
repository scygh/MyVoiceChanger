package com.scy.myvoicechanger.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.scy.myvoicechanger.R;
import com.scy.myvoicechanger.service.FloatWindowService;
import com.scy.myvoicechanger.service.MyWindowManager;

import java.lang.reflect.Field;

/**
 * description ：
 * author : scy
 * email : 1797484636@qq.com
 * date : 2020/3/30 15:41
 */
public class FloatWindowBigView extends LinearLayout {

    public static int viewWidth;
    public static int viewHeight;

    public FloatWindowBigView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_detail, this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        Button close = (Button) findViewById(R.id.close);
        Button back = (Button) findViewById(R.id.back);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
                MyWindowManager.removeBigWindow(context);
                MyWindowManager.removeSmallWindow(context);
                Intent intent = new Intent(getContext(), FloatWindowService.class);
                context.stopService(intent);
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击返回的时候，移除大悬浮窗，创建小悬浮窗
                MyWindowManager.removeBigWindow(context);
                MyWindowManager.createSmallWindow(context);
            }
        });

    }


}
