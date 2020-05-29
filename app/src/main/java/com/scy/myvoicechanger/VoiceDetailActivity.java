package com.scy.myvoicechanger;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scy.myvoicechanger.adapter.DetailRvAdapter;
import com.scy.myvoicechanger.utils.RSBlur;
import com.scy.myvoicechanger.utils.SpUtils;
import com.scy.myvoicechanger.utils.sql.MyOpenHelper;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
        } else if (name.equals("窃格瓦拉")) {
            nameArr = getResources().getStringArray(R.array.qgwl);
        } else if (name.equals("茄子")) {
            nameArr = getResources().getStringArray(R.array.qz);
        } else if (name.equals("源氏")) {
            nameArr = getResources().getStringArray(R.array.ys);
        } else if (name.equals("萌妹子")) {
            nameArr = getResources().getStringArray(R.array.mmz);
        } else if (name.equals("古天乐")) {
            nameArr = getResources().getStringArray(R.array.gtl);
        } else if (name.equals("渣渣辉")) {
            nameArr = getResources().getStringArray(R.array.zzh);
        } else if (name.equals("陈小春")) {
            nameArr = getResources().getStringArray(R.array.cxc);
        } else if (name.equals("王者荣耀")) {
            nameArr = getResources().getStringArray(R.array.wzyy);
        } else if (name.equals("麦克雷")) {
            nameArr = getResources().getStringArray(R.array.maikelei);
        } else if (name.equals("大笑")) {
            nameArr = getResources().getStringArray(R.array.laugh);
        }
        initDetailRv();
        database = MyOpenHelper.getHelper(getApplicationContext()).getWritableDatabase();
        if (SpUtils.isCollect(this, name)) {
            detailScIv.setImageResource(R.drawable.yishoucang);
            detailScTv.setText("已收藏");
        }
    }

    InputStream is;

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
                    } else if (name.equals("萌妹子")) {
                        fileDescriptor = getAssets().openFd("mmz/" + nameArr[position] + ".mp3");
                    } else if (name.equals("古天乐")) {
                        fileDescriptor = getAssets().openFd("gtl/" + nameArr[position] + ".mp3");
                    } else if (name.equals("渣渣辉")) {
                        fileDescriptor = getAssets().openFd("zzh/" + nameArr[position] + ".mp3");
                    } else if (name.equals("陈小春")) {
                        fileDescriptor = getAssets().openFd("cxc/" + nameArr[position] + ".mp3");
                    } else if (name.equals("王者荣耀")) {
                        fileDescriptor = getAssets().openFd("wzyy/" + nameArr[position] + ".mp3");
                    } else if (name.equals("麦克雷")) {
                        fileDescriptor = getAssets().openFd("maikelei/" + nameArr[position] + ".mp3");
                    } else if (name.equals("大笑")) {
                        fileDescriptor = getAssets().openFd("laugh/" + nameArr[position] + ".mp3");
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

            @Override
            public void onItemLongClick(int position) {
                try {
                    if (name.equals("李云龙")) {
                        is = getAssets().open("lyl/" + nameArr[position] + ".mp3");
                    } else if (name.equals("呆妹儿")) {
                        is = getAssets().open("girl/" + nameArr[position] + ".mp3");
                    } else if (name.equals("卢本伟")) {
                        is = getAssets().open("lbw/" + nameArr[position] + ".mp3");
                    } else if (name.equals("pdd")) {
                        is = getAssets().open("pdd/" + nameArr[position] + ".mp3");
                    } else if (name.equals("窃格瓦拉")) {
                        is = getAssets().open("qgwl/" + nameArr[position] + ".mp3");
                    } else if (name.equals("茄子")) {
                        is = getAssets().open("qz/" + nameArr[position] + ".mp3");
                    } else if (name.equals("源氏")) {
                        is = getAssets().open("ys/" + nameArr[position] + ".mp3");
                    } else if (name.equals("萌妹子")) {
                        is = getAssets().open("mmz/" + nameArr[position] + ".mp3");
                    } else if (name.equals("古天乐")) {
                        is = getAssets().open("gtl/" + nameArr[position] + ".mp3");
                    } else if (name.equals("渣渣辉")) {
                        is = getAssets().open("zzh/" + nameArr[position] + ".mp3");
                    } else if (name.equals("陈小春")) {
                        is = getAssets().open("cxc/" + nameArr[position] + ".mp3");
                    } else if (name.equals("王者荣耀")) {
                        is = getAssets().open("wzyy/" + nameArr[position] + ".mp3");
                    } else if (name.equals("麦克雷")) {
                        is = getAssets().open("maikelei/" + nameArr[position] + ".mp3");
                    } else if (name.equals("大笑")) {
                        is = getAssets().open("laugh/" + nameArr[position] + ".mp3");
                    }
                    startShare(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        detailRv.setAdapter(detailRvAdapter);
    }

    private void startShare(InputStream is) {
        String fileName = DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".mp3";
        File destDir = new File(Environment.getExternalStorageDirectory() + "/share/");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        String filePath = Environment.getExternalStorageDirectory() + "/share/" + fileName;
        FileOutputStream fos = null;
        File file = new File(filePath);
        try {
            fos = new FileOutputStream(file);
            byte[] data = new byte[1024];
            int l = 0;
            while ((l = is.read(data)) > -1) {
                fos.write(data, 0, l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Uri uri;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 24) {
            uri = FileProvider.getUriForFile(this.getApplicationContext(), "com.scy.myvoicechanger.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("audio/MP3");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        if (resolveInfos.isEmpty()) {
            Log.d("share", "没有可以分享的应用");
            return;
        }
        List<Intent> targetINtents = new ArrayList<>();
        for (ResolveInfo resolveInfo : resolveInfos) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo.packageName.contains("com.tencent.mm")
                    || activityInfo.packageName.contains("com.tencent.mobileqq")) {
                if (resolveInfo.loadLabel(pm).toString().contains("QQ收藏")) {
                    continue;
                }
                Intent target = new Intent();
                target.setAction(Intent.ACTION_SEND);
                target.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
                target.putExtra(Intent.EXTRA_STREAM, uri);
                target.setType("audio/MP3");
                target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                targetINtents.add(new LabeledIntent(target, activityInfo.packageName, resolveInfo.loadLabel(pm), resolveInfo.icon));
            }
        }
        if (targetINtents.size() <= 0) {
            Log.d("share", "没有可以分享的应用");
            return;
        }
        Intent chooser = Intent.createChooser(targetINtents.remove(targetINtents.size() - 1), "选择分享");
        if (chooser == null) {
            return;
        }
        LabeledIntent[] labeledIntents = targetINtents.toArray(new LabeledIntent[targetINtents.size()]);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, labeledIntents);
        startActivity(chooser);
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
