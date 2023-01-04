package com.example.sliderdemo.model;

import android.graphics.drawable.Drawable;

import java.util.UUID;

public class Image {
    public String id = UUID.randomUUID().toString();
    public int image;
    public Drawable imageDrw;
    public String title;
    public String body;
    public Integer counter = null;
}
