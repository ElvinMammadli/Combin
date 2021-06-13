package com.quizz.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quizz.listeners.FireUserListener;
import com.quizz.model.User;
import com.quizz.repository.UserRepository;

public class MainActivityViewModel extends ViewModel implements FireUserListener {
    private static  MutableLiveData<com.quizz.model.User> User;
    private static  MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private UserRepository UserRep;

    public void init(){
        if(User!=null){
            return ;
        }
        UserRep=UserRepository.getInstance();
        User=UserRep.getUser(this);
        isUpdating.setValue(false);
    }

    public void setUser(MutableLiveData<com.quizz.model.User> User){
        this.User=User;
    }
    public LiveData<com.quizz.model.User> getUser(){
        return User;
    }

    @Override
    public void onComplete(MutableLiveData<com.quizz.model.User> data) {
        isUpdating.setValue(true);
        User = data;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }
}

