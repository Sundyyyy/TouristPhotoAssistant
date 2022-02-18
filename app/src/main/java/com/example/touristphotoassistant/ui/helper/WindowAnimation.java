package com.example.touristphotoassistant.ui.helper;

import android.transition.Slide;
import android.view.Window;

public class WindowAnimation {
    public static void setupWindowAnimations(Window window){
        //minSdkVersion = 21
        //добавить вызов WindowAnimation.setupWindowAnimations(getWindow()); в onCreate
        //Option 2
        Slide slideAnimation = new Slide();
        slideAnimation.setDuration(1000);
        window.setExitTransition(slideAnimation);

    }
}