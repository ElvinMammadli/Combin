package com.quizz.listeners;

import androidx.lifecycle.MutableLiveData;

import com.quizz.model.User;
import com.quizz.viewmodel.MainActivityViewModel;

public interface BindHomeNTabs {
     MainActivityViewModel getViewModel();
     void sendUser(MutableLiveData<User> user);
}
