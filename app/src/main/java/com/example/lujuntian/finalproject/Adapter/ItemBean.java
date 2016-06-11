package com.example.lujuntian.finalproject.Adapter;

/**
 * Created by sky on 16/6/6.
 */

public class ItemBean {
    private int image;
    private String title;
    private String text;
    private String sold;



    public ItemBean(int image, String title, String text, String sold) {
        this.image = image;
        this.title = title;
        this.text = text;
        this.sold = sold;

    }

    public String getSold() {
        return sold;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
