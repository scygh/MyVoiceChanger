package com.scy.myvoicechanger.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scy.myvoicechanger.R;
import com.scy.myvoicechanger.adapter.MainRvAdapter;
import com.scy.myvoicechanger.adapter.MainVpAdapter;
import com.scy.myvoicechanger.entity.MainRvBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.main_rv)
    RecyclerView mainRv;
    @BindView(R.id.main_vp)
    ViewPager mainVp;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.main_collapsing_toolbar)
    CollapsingToolbarLayout mainCollapsingToolbar;
    private MainRvAdapter mainRvAdapter;
    private List<MainRvBean> mainRvBeans = new ArrayList<>();
    private int currentPosiotion = 1;


    private static final int HANDLER_WHAT_ONE = 1;
    private static final int REQUEST_CODE_ONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mainToolbar);
        mainCollapsingToolbar.setTitle("使用方法");
        initMainRv();
        initMainVp();
    }

    float startX = 0;
    float startY = 0;
    float endX = 0;
    float endY = 0;

    private void initMainVp() {
        List<View> views = new ArrayList<>();
        LayoutInflater inflater = getLayoutInflater();
        views.add(inflater.inflate(R.layout.mainvp_item3, null, false));
        views.add(inflater.inflate(R.layout.mainvp_item1, null, false));
        views.add(inflater.inflate(R.layout.mainvp_item2, null, false));
        views.add(inflater.inflate(R.layout.mainvp_item3, null, false));
        views.add(inflater.inflate(R.layout.mainvp_item1, null, false));
        mainVp.setPageMargin(20);
        mainVp.setAdapter(new MainVpAdapter(views));
        mainVp.setCurrentItem(1, false);
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosiotion = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state != ViewPager.SCROLL_STATE_IDLE) return;
                if (currentPosiotion == 0) {
                    mainVp.setCurrentItem(3, false);
                } else if (currentPosiotion == 4) {
                    mainVp.setCurrentItem(1, false);
                }
            }
        });
        mainVp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getX();
                        startY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        float offsetX = motionEvent.getX() - startX;
                        float offsetY = motionEvent.getY() - startY;
                        if (Math.abs(offsetX) <= 5) {
                            if (currentPosiotion == 1) {
                                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), REQUEST_CODE_ONE);
                                Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                            } else if (currentPosiotion == 2) {
                                Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
                            } else if (currentPosiotion == 3) {
                                Toast.makeText(MainActivity.this, "3", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
                return false;
            }
        });
        handler.sendEmptyMessageDelayed(HANDLER_WHAT_ONE, 2000);
    }

    private void initMainRv() {
        mainRv.setLayoutManager(new GridLayoutManager(this, 3));
        mainRvBeans.add(new MainRvBean("一只网友酥", R.drawable.mainrv_item_wys));
        mainRvBeans.add(new MainRvBean("甜甜女生", R.drawable.mainrv_item_wys2));
        mainRvBeans.add(new MainRvBean("王者荣耀梦泪", R.drawable.mainrv_item_ml));
        mainRvBeans.add(new MainRvBean("一只网友酥", R.drawable.mainrv_item_wys));
        mainRvBeans.add(new MainRvBean("王者荣耀梦泪", R.drawable.mainrv_item_ml));
        mainRvBeans.add(new MainRvBean("一只网友酥", R.drawable.mainrv_item_wys));
        mainRvBeans.add(new MainRvBean("一只网友酥", R.drawable.mainrv_item_wys));
        mainRvBeans.add(new MainRvBean("一只网友酥", R.drawable.mainrv_item_wys));
        mainRvBeans.add(new MainRvBean("一只网友酥", R.drawable.mainrv_item_wys));
        mainRvBeans.add(new MainRvBean("甜甜女生", R.drawable.mainrv_item_wys2));
        mainRvBeans.add(new MainRvBean("王者荣耀梦泪", R.drawable.mainrv_item_ml));
        mainRvBeans.add(new MainRvBean("一只网友酥", R.drawable.mainrv_item_wys));
        mainRvBeans.add(new MainRvBean("王者荣耀梦泪", R.drawable.mainrv_item_ml));
        mainRvBeans.add(new MainRvBean("一只网友酥", R.drawable.mainrv_item_wys));
        mainRvBeans.add(new MainRvBean("一只网友酥", R.drawable.mainrv_item_wys));
        mainRvBeans.add(new MainRvBean("甜甜女生", R.drawable.mainrv_item_wys2));
        mainRvBeans.add(new MainRvBean("王者荣耀梦泪", R.drawable.mainrv_item_ml));
        mainRvBeans.add(new MainRvBean("一只网友酥", R.drawable.mainrv_item_wys));
        mainRvBeans.add(new MainRvBean("一只网友酥", R.drawable.mainrv_item_wys));
        mainRvBeans.add(new MainRvBean("一只网友酥", R.drawable.mainrv_item_wys));
        mainRvBeans.add(new MainRvBean("王者荣耀梦泪", R.drawable.mainrv_item_ml));
        mainRvBeans.add(new MainRvBean("王者荣耀梦泪", R.drawable.mainrv_item_ml));
        mainRvAdapter = new MainRvAdapter(mainRvBeans, this);
        mainRv.setAdapter(mainRvAdapter);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case HANDLER_WHAT_ONE:
                    mainVp.setCurrentItem(currentPosiotion++);
                    handler.sendEmptyMessageDelayed(HANDLER_WHAT_ONE, 2000);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                Toast.makeText(this, "打开成功", Toast.LENGTH_SHORT).show();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
