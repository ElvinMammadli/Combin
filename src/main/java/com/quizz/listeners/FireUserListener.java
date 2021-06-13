package com.quizz.listeners;

import androidx.lifecycle.MutableLiveData;

import com.quizz.model.User;

public interface FireUserListener {
    void onComplete(MutableLiveData<User> data);
}
