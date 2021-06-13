package com.quizz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quizz.listeners.RegisterTabs;
import com.quizz.model.User;
import com.quizz.viewmodel.MainActivityViewModel;

import static android.content.ContentValues.TAG;


public class Login extends Fragment {

    private TextView register;
    private NavController nav;
    private FirebaseAuth mAuth;
    private Button signin;
    private  EditText demail;
    private EditText password;
    View View;
    private  User User;

    public Login() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signin=view.findViewById(R.id.signin);
        nav = Navigation.findNavController(view);
        register=view.findViewById(R.id.register);
        demail=view.findViewById(R.id.mail);
        password=view.findViewById(R.id.password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             nav.navigate(R.id.action_login_to_register2);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(demail.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            mAuth=FirebaseAuth.getInstance();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();


                            DocumentReference docRef = db.collection("Users").document(currentUser.getEmail());
                            User= new User();
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            User.setEmail((String) document.getData().get("Mail"));
                                            User.setName((String) document.getData().get("Name"));
                                            User.setGender((String) document.getData().get("Gender"));
                                            User.setUser_name((String)document.getData().get("Username"));
                                            User.setRank((String) document.getData().get("Rank"));
                                            User.setUrl((String)document.getData().get("Url"));
                                            User.setCountry((String)document.getData().get("Country"));
                                            MutableLiveData<User> ser= new MutableLiveData<>();
                                            ser.setValue(User);
                                            ((RegisterTabs)getActivity()).sendUser(ser);
                                            startActivity(new Intent(getContext(),Tabs.class));



                                        }
                                    }
                                }
                            });

                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }
                    }
                });


            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View= inflater.inflate(R.layout.fragment_login, container, false);
        return View;
    }
}