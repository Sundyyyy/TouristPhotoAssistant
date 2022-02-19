package com.example.touristphotoassistant.ui.about;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AboutViewModel() {
        mText = new MutableLiveData<>();

        String about = "Приложение поможет вам быстро определить, что на фото." +
                "\r\n" +
                "Сейчас это только первый шаг в создании моего первого приложения и многое еще предстоит сделать, но уже сейчас вы можете испытать взаимодействие человека с ИИ.";

        mText.setValue(about);
    }

    public LiveData<String> getText() {
        return mText;
    }
}