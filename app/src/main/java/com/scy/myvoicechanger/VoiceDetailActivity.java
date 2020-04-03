package com.scy.myvoicechanger;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scy.myvoicechanger.adapter.DetailRvAdapter;
import com.scy.myvoicechanger.adapter.MainRvAdapter;
import com.scy.myvoicechanger.entity.MainRvBean;
import com.scy.myvoicechanger.utils.GridSpacingItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;

public class VoiceDetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_rl)
    RelativeLayout detailRl;
    @BindView(R.id.detail_toolbar)
    Toolbar detailToolbar;
    @BindView(R.id.detail_collapsing_toolbar)
    CollapsingToolbarLayout detailCollapsingToolbar;
    @BindView(R.id.detail_appBar)
    AppBarLayout detailAppBar;
    @BindView(R.id.detail_rv)
    RecyclerView detailRv;
    @BindView(R.id.detail_iv1)
    ImageView detailIv1;
    @BindView(R.id.detail_tv1)
    TextView detailTv1;
    private String name;
    private int imageId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_detail);
        ButterKnife.bind(this);
        setSupportActionBar(detailToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Intent intent = getIntent();
        imageId = intent.getIntExtra("imageid", 0);
        name = intent.getStringExtra("name");
        detailRl.setBackgroundResource(imageId);
        detailIv1.setImageResource(imageId);
        detailTv1.setText(name);
        Blurry.with(this)
                .radius(50)
                .sampling(2)
                .async()
                .animate(0)
                .onto(detailRl);
        initDetailRv();
    }

    private void initDetailRv() {
        //detailRv.setAdapter(new DetailRvAdapter());
    }

}
