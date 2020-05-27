package com.scy.myvoicechanger;

/**
 * description ：
 * author : scy
 * email : 1797484636@qq.com
 * date : 2020/4/2 13:40
 */
public class VoiceTools {

    public static native void changeVoice(String path, int mode);

    static {
        System.loadLibrary("changeVoice");
        System.loadLibrary("fmod");
        System.loadLibrary("fmodL");
    }
}
