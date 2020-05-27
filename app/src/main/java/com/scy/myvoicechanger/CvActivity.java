package com.scy.myvoicechanger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.scy.myvoicechanger.view.VolumeWaveView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CvActivity extends BaseActivity {

    private static final int REQUEST_PERMISSIONS = 1000;

    List<String> permissionList = new ArrayList<>();
    @BindView(R.id.voice_input)
    ImageView voiceInput;
    @BindView(R.id.vwv)
    VolumeWaveView vwv;

    @Override
    public int layoutView() {
        return R.layout.activity_cv;
    }

    @Override
    public void initView() {
        if (ContextCompat.checkSelfPermission(CvActivity.this, Manifest.
                permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else if (ContextCompat.checkSelfPermission(CvActivity.this, Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else if (ContextCompat.checkSelfPermission(CvActivity.this, Manifest.
                permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(CvActivity.this, permissions, REQUEST_PERMISSIONS);
        }
        voiceInput.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                vwv.startAnimation();
                startRecord();
                return false;
            }
        });
        voiceInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        vwv.removeAnimation();
                        stopRecord();
                }
                return false;
            }
        });
    }

    @Override
    public void initData() {

    }

    /*private void toSaveWav() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = getResources().openRawResource(R.raw.jaguar);
                    File file = new File(Environment.getExternalStorageDirectory().getPath(), "jaguar.wav");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[10];
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                    byte[] bs = outputStream.toByteArray();
                    fileOutputStream.write(bs);
                    outputStream.close();
                    inputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("getPath", e.getMessage());
                }
            }
        }).start();
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "你拒绝了权限，功能不可用", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private MediaRecorder mMediaRecorder;
    private String fileName;
    private String filePath;

    public void startRecord() {
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            fileName = DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".m4a";
            File destDir = new File(Environment.getExternalStorageDirectory()+"/test/");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            filePath =  Environment.getExternalStorageDirectory()+"/test/"+ fileName;

            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            Log.i("failed!", e.getMessage());
        } catch (IOException e) {
            Log.i("failed!", e.getMessage());
        }
    }


    public void stopRecord() {
        if (mMediaRecorder != null) {
            try {
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mMediaRecorder = null;
            } catch (RuntimeException e) {
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                File file = new File(filePath);
                if (file.exists())
                    file.delete();
            }
        }
    }

    public void startChange(View view) {
        File file = new File(filePath);
        if (!file.exists()) {
            Log.e("nofile", "没有文件");
            return;
        }
        switch (view.getId()) {
            // 普通
            case R.id.btn_normal:
                VoiceTools.changeVoice(filePath, 0);
                break;
            // 萝莉
            case R.id.btn_luoli:
                VoiceTools.changeVoice(filePath, 1);
                break;// 萝莉
            // 大叔
            case R.id.btn_dashu:
                VoiceTools.changeVoice(filePath, 2);
                break;
            // 惊悚
            case R.id.btn_jingsong:
                VoiceTools.changeVoice(filePath, 3);
                break;
            // 搞怪
            case R.id.btn_gaoguai:
                VoiceTools.changeVoice(filePath, 4);
                break;
            // 空灵
            case R.id.btn_kongling:
                VoiceTools.changeVoice(filePath, 5);
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRecord();
        vwv.removeAnimation();
    }
}
