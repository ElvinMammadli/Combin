package com.quizz.ui.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActicityViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ActicityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send question fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}