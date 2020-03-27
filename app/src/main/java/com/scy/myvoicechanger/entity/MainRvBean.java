package com.scy.myvoicechanger.entity;

/**
 * description ：
 * author : scy
 * email : 1797484636@qq.com
 * date : 2020/3/27 10:26
 */
public class MainRvBean {

    private String name;
    private int imageId;

    public MainRvBean(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
