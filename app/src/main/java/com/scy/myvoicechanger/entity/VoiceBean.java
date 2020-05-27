package com.scy.myvoicechanger.entity;

/**
 * description ：
 * author : scy
 * email : 1797484636@qq.com
 * date : 2020/4/8 08:57
 */
public class VoiceBean {

    private String name;
    private String author;

    public VoiceBean(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
