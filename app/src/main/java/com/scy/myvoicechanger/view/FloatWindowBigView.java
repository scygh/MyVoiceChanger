package com.scy.myvoicechanger.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scy.myvoicechanger.R;
import com.scy.myvoicechanger.adapter.DetailFloatRvAdapter;
import com.scy.myvoicechanger.entity.VoiceBean;
import com.scy.myvoicechanger.service.FloatWindowService;
import com.scy.myvoicechanger.service.MyWindowManager;
import com.scy.myvoicechanger.utils.sql.MyOpenHelper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * description ：
 * author : scy
 * email : 1797484636@qq.com
 * date : 2020/3/30 15:41
 */
public class FloatWindowBigView extends LinearLayout {

    public static int viewWidth;
    public static int viewHeight;
    private WindowManager windowManager;
    private WindowManager.LayoutParams mParams;
    private float xInScreen;
    private float yInScreen;
    private float xInView;
    private float yInView;
    private AssetFileDescriptor fileDescriptor;
    List<VoiceBean> list = new ArrayList<>();
    private MediaPlayer player;

    public FloatWindowBigView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_detail, this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        ImageView close = findViewById(R.id.close);
        ImageView back = findViewById(R.id.back);
        RecyclerView recyclerView = findViewById(R.id.rv_floatwindow);
        player = new MediaPlayer();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        DetailFloatRvAdapter detailRvAdapter = new DetailFloatRvAdapter(getData(context), context);
        detailRvAdapter.setOnItemClickListener(new DetailFloatRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    String name = list.get(position).getAuthor();
                    if (name.equals("李云龙")) {
                        fileDescriptor = context.getAssets().openFd("lyl/" + list.get(position).getName() + ".mp3");
                    } else if (name.equals("呆妹儿")) {
                        fileDescriptor = context.getAssets().openFd("girl/" + list.get(position).getName() + ".mp3");
                    } else if (name.equals("卢本伟")) {
                        fileDescriptor = context.getAssets().openFd("lbw/" + list.get(position).getName() + ".mp3");
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                player = new MediaPlayer();
                                player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                                player.prepare();
                                player.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        recyclerView.setAdapter(detailRvAdapter);
    }

    private List<VoiceBean> getData(Context context) {
        Cursor cursor = MyOpenHelper.getHelper(context.getApplicationContext()).getWritableDatabase().query("Voice", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                VoiceBean voiceBean = new VoiceBean(cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("author")));
                list.add(voiceBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                // 手指移动的时候更新小悬浮窗的位置
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return false;

    }

    int statusBarHeight;

    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }


}
