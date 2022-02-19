package com.example.touristphotoassistant.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.UUID;

public class UserViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public UserViewModel() {
        mText = new MutableLiveData<>();
        String uniqueId = UUID.randomUUID().toString();

        mText.setValue("User Id: " + uniqueId);
    }

    public LiveData<String> getText() {
        return mText;
    }
}