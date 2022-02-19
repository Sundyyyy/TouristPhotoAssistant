package com.example.touristphotoassistant.ui.photocard;

import android.graphics.Bitmap;

public class Task {

    private String photoBase64;
    private String photoDesc;
    private Bitmap photo;

    public Task(String photoBase64, String photoDesc, Bitmap photo) {
        this.photoBase64 = photoBase64;
        this.photoDesc = photoDesc;
        this.photo = photo;

    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public String getPhotoDesc() {
        return photoDesc;
    }

    public void setDesc(String photoDesc) {
        this.photoDesc = photoDesc;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
