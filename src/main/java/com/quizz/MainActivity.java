package com.quizz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.quizz.listeners.RegisterTabs;
import com.quizz.model.User;
import com.quizz.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements RegisterTabs {
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityViewModel=
                ViewModelProviders.of(this).get(MainActivityViewModel.class);
//        mainActivityViewModel.init();
    }

    @Override
    public void sendUser(MutableLiveData<User> user) {
        mainActivityViewModel.setUser(user);
    }
}