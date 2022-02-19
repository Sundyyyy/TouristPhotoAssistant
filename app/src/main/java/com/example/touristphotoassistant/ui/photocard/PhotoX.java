package com.example.touristphotoassistant.ui.photocard;

import android.graphics.Bitmap;

public class PhotoX {

    private String photoDesc;
    private Bitmap photo;

    public PhotoX(String photoDesc, Bitmap photo) {

        this.photoDesc = photoDesc;
        this.photo = photo;

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
