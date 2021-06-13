package com.quizz.listeners;

import androidx.lifecycle.MutableLiveData;

import com.quizz.model.User;

public interface RegisterTabs {
    void sendUser(MutableLiveData<User> user);

}
