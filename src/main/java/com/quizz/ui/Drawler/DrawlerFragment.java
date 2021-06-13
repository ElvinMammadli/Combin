package com.quizz.ui.Drawler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.quizz.AddClothes;
import com.quizz.DeleteClothes;
import com.quizz.DeleteDrawer;
import com.quizz.AddDrawer;
import com.quizz.R;
import com.quizz.model.User;
import com.quizz.viewmodel.MainActivityViewModel;

public class DrawlerFragment extends Fragment {

    private MainActivityViewModel mainActivityViewModel;
    private TextView name;
    private TextView mail;
    private Activity activity;
    private ProgressBar progressBar;
    private ImageView ProfileImage;
    private Button perin,perin1,ekle,sil;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mainActivityViewModel=
                ViewModelProviders.of(this).get(MainActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_drawer, container, false);
        activity=getActivity();
        ProfileImage=root.findViewById(R.id.imageView);
        mainActivityViewModel.init();
        perin=root.findViewById(R.id.perin);
        perin1=root.findViewById(R.id.perin2);
        ekle=root.findViewById(R.id.ekle);
        sil=root.findViewById(R.id.sil);
        perin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddDrawer.class);
                startActivity(intent);
            }
        });

        perin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteDrawer.class);
                startActivity(intent);
            }
        });

        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddClothes.class);
                startActivity(intent);
            }
        });
        sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteClothes.class);
                startActivity(intent);
            }
        });
        mainActivityViewModel.getIsUpdating().observe(this.getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean){

                    if (mainActivityViewModel.getUser().getValue().getUrl()!=null && !mainActivityViewModel.getUser().getValue().getUrl().equals(" ")) {

                        Glide
                                .with(getContext())
                                .load(mainActivityViewModel.getUser().getValue().getUrl())
                                .centerCrop()
                                .thumbnail(0.01f)
                                .into(ProfileImage);

                    }

                }

            }
        });

        mainActivityViewModel.getUser().observe(this.getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {

                if ( mainActivityViewModel.getUser().getValue().getUrl()!=null && !mainActivityViewModel.getUser().getValue().getUrl().equals(" ")) {

                    Glide
                            .with(getContext())
                            .load(mainActivityViewModel.getUser().getValue().getUrl())
                            .centerCrop()
                            .thumbnail(0.01f)
                            .into(ProfileImage);
                }
            }
        });


        return root;
    }


}