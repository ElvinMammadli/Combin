package com.quizz.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.quizz.AddActivity;
import com.quizz.AddDrawer;
import com.quizz.DeleteActivity;
import com.quizz.R;

public class ActivityFragment extends Fragment {

    private ActicityViewModel ActivityViewModel;
    private EditText wrong3;
    private Button add,delete;
    private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ActivityViewModel =
                ViewModelProviders.of(this).get(ActicityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_activity, container, false);
        add=root.findViewById(R.id.create2);
        delete=root.findViewById(R.id.delete2);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}