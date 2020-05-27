package com.scy.myvoicechanger;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scy.myvoicechanger.adapter.DetailRvAdapter;
import com.scy.myvoicechanger.utils.RSBlur;
import com.scy.myvoicechanger.utils.SpUtils;
import com.scy.myvoicechanger.utils.sql.MyOpenHelper;

import java.io.IOException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.detail_sc_iv)
    ImageView detailScIv;
    @BindView(R.id.detail_sc_tv)
    TextView detailScTv;
    private String name;
    private int imageId;
    private String[] nameArr;
    private DetailRvAdapter detailRvAdapter;
    private MediaPlayer player;
    private AssetFileDescriptor fileDescriptor;
    private SQLiteDatabase database;

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
        detailIv1.setImageResource(imageId);
        detailTv1.setText(name);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);
        bitmap = RSBlur.rsBlur(this, bitmap, 20);
        detailRl.setBackground(new BitmapDrawable(bitmap));

        if (name.equals("李云龙")) {
            nameArr = getResources().getStringArray(R.array.lyl);
        } else if (name.equals("呆妹儿")) {
            nameArr = getResources().getStringArray(R.array.girl);
        } else if (name.equals("卢本伟")) {
            nameArr = getResources().getStringArray(R.array.lbw);
        } else if (name.equals("pdd")) {
            nameArr = getResources().getStringArray(R.array.pdd);
        }else if (name.equals("窃格瓦拉")) {
            nameArr = getResources().getStringArray(R.array.qgwl);
        }else if (name.equals("茄子")) {
            nameArr = getResources().getStringArray(R.array.qz);
        }else if (name.equals("源氏")) {
            nameArr = getResources().getStringArray(R.array.ys);
        }
        initDetailRv();
        database = MyOpenHelper.getHelper(getApplicationContext()).getWritableDatabase();
        if (SpUtils.isCollect(this, name)) {
            detailScIv.setImageResource(R.drawable.yishoucang);
            detailScTv.setText("已收藏");
        }
    }

    @TargetApi(26)
    private void initDetailRv() {
        detailRv.setLayoutManager(new LinearLayoutManager(this));
        detailRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        detailRvAdapter = new DetailRvAdapter(Arrays.asList(nameArr), this);
        detailRvAdapter.setOnItemClickListener(new DetailRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    if (name.equals("李云龙")) {
                        fileDescriptor = getAssets().openFd("lyl/" + nameArr[position] + ".mp3");
                    } else if (name.equals("呆妹儿")) {
                        fileDescriptor = getAssets().openFd("girl/" + nameArr[position] + ".mp3");
                    } else if (name.equals("卢本伟")) {
                        fileDescriptor = getAssets().openFd("lbw/" + nameArr[position] + ".mp3");
                    } else if (name.equals("pdd")) {
                        fileDescriptor = getAssets().openFd("pdd/" + nameArr[position] + ".mp3");
                    } else if (name.equals("窃格瓦拉")) {
                        fileDescriptor = getAssets().openFd("qgwl/" + nameArr[position] + ".mp3");
                    } else if (name.equals("茄子")) {
                        fileDescriptor = getAssets().openFd("qz/" + nameArr[position] + ".mp3");
                    } else if (name.equals("源氏")) {
                        fileDescriptor = getAssets().openFd("ys/" + nameArr[position] + ".mp3");
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
        detailRv.setAdapter(detailRvAdapter);
    }

    @Override
    protected void onDestroy() {
        nameArr = null;
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        super.onDestroy();
    }

    @OnClick({R.id.detail_sc_iv, R.id.detail_sc_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detail_sc_iv:
                if (!SpUtils.isCollect(this, name)) {
                    detailScIv.setImageResource(R.drawable.yishoucang);
                    detailScTv.setText("已收藏");
                    SpUtils.Collect(this, name);
                    ContentValues values = new ContentValues();
                    for (int i = 0; i < nameArr.length; i++) {
                        values.put("name", nameArr[i]);
                        values.put("author", name);
                        database.insert("Voice", null, values);
                        values.clear();
                    }
                } else {
                    detailScIv.setImageResource(R.drawable.meishoucang);
                    detailScTv.setText("收藏");
                    SpUtils.notCollect(this, name);
                    database.delete("Voice", "author=?", new String[]{name});
                }
                break;
            case R.id.detail_sc_tv:
                break;
        }
    }
}
