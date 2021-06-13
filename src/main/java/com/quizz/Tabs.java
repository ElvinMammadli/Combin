package com.quizz;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.quizz.listeners.BindHomeNTabs;
import com.quizz.model.User;
import com.quizz.viewmodel.MainActivityViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class Tabs extends AppCompatActivity implements BindHomeNTabs {
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mainActivityViewModel=
                ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_game, R.id.navigation_rank, R.id.navigation_send_question, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }


    @Override
    public MainActivityViewModel getViewModel() {
        return mainActivityViewModel;
    }

    @Override
    public void sendUser(MutableLiveData<User> user) {
        mainActivityViewModel.setUser(user);
    }
}