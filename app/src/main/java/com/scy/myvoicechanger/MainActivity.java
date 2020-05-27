package com.scy.myvoicechanger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scy.myvoicechanger.adapter.MainRvAdapter;
import com.scy.myvoicechanger.adapter.MainVpAdapter;
import com.scy.myvoicechanger.entity.MainRvBean;
import com.scy.myvoicechanger.service.FloatWindowService;
import com.scy.myvoicechanger.service.MyWindowManager;
import com.scy.myvoicechanger.utils.AppBarStateChangeListener;
import com.scy.myvoicechanger.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.main_rv)
    RecyclerView mainRv;
    @BindView(R.id.main_vp)
    ViewPager mainVp;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.main_collapsing_toolbar)
    CollapsingToolbarLayout mainCollapsingToolbar;
    @BindView(R.id.main_fab)
    FloatingActionButton mainFab;
    @BindView(R.id.main_appBar)
    AppBarLayout mainAppBar;
    private MainRvAdapter mainRvAdapter;
    private List<MainRvBean> mainRvBeans = new ArrayList<>();
    private int currentPosiotion = 1;


    private static final int HANDLER_WHAT_ONE = 1;
    private static final int REQUEST_CODE_ONE = 1;

    @Override
    public int layoutView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mainAppBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {

                } else if (state == State.COLLAPSED) {

                } else {

                }
            }
        });
    }

    @Override
    public void initData() {
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
        mainVp.setPageMargin(40);
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
                            } else if (currentPosiotion == 2) {
                                startActivity(new Intent(MainActivity.this, CvActivity.class));
                            } else if (currentPosiotion == 3) {
                                startActivity(new Intent(MainActivity.this, UsewaysActivity.class));
                            }
                        }
                        break;
                }
                return false;
            }
        });
        handler.sendEmptyMessageDelayed(HANDLER_WHAT_ONE, 5000);
    }

    private void initMainRv() {
        mainRv.setLayoutManager(new GridLayoutManager(this, 3));
        mainRvBeans.add(new MainRvBean("卢本伟", R.drawable.lbw));
        mainRvBeans.add(new MainRvBean("李云龙", R.drawable.lyl));
        mainRvBeans.add(new MainRvBean("呆妹儿", R.drawable.dm));
        mainRvBeans.add(new MainRvBean("pdd", R.drawable.pdd));
        mainRvBeans.add(new MainRvBean("窃格瓦拉", R.drawable.qgwl));
        mainRvBeans.add(new MainRvBean("茄子", R.drawable.qz));
        mainRvBeans.add(new MainRvBean("源氏", R.drawable.ys));
        mainRvAdapter = new MainRvAdapter(mainRvBeans, this);
        mainRv.addItemDecoration(new GridSpacingItemDecoration(3, 50, false));
        mainRvAdapter.setOnItemClickListener(new MainRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, VoiceDetailActivity.class);
                intent.putExtra("name", mainRvBeans.get(position).getName());
                intent.putExtra("imageid", mainRvBeans.get(position).getImageId());
                startActivity(intent);
            }
        });
        mainRv.setAdapter(mainRvAdapter);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case HANDLER_WHAT_ONE:
                    mainVp.setCurrentItem(currentPosiotion++);
                    handler.sendEmptyMessageDelayed(HANDLER_WHAT_ONE, 5000);
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

    @OnClick(R.id.main_fab)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_fab:
                Intent intent = new Intent(this, FloatWindowService.class);
                startService(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        MyWindowManager.isWindowShowing();
        MyWindowManager.removeBigWindow(this);
        MyWindowManager.removeSmallWindow(this);
        Intent intent = new Intent(this, FloatWindowService.class);
        stopService(intent);
        super.onStart();
    }
}
