package com.quizz.ui.Combin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.quizz.AddCombin;
import com.quizz.DeleteCombin;
import com.quizz.R;

public class CombinFragment extends Fragment {
    private Button create,show;

    private CombinViewModel CombinViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CombinViewModel =
                ViewModelProviders.of(this).get(CombinViewModel.class);
        View root = inflater.inflate(R.layout.fragment_combin, container, false);
        create=root.findViewById(R.id.create);
        show=root.findViewById(R.id.show);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCombin.class);
                startActivity(intent);
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteCombin.class);
                startActivity(intent);
            }
        });



        return root;
    }
}