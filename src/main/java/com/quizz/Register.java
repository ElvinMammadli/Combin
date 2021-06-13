package com.quizz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quizz.listeners.BindHomeNTabs;
import com.quizz.listeners.RegisterTabs;
import com.quizz.model.User;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class Register extends Fragment {
    private FirebaseAuth mAuth;
    private EditText name;
    private EditText mail;
    private EditText password;
    private Activity activity;
    public Register() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button register = (Button) view.findViewById(R.id.register);
        name=view.findViewById(R.id.name);
        password=view.findViewById(R.id.password);
        mail=view.findViewById(R.id.mail);
        mAuth = FirebaseAuth.getInstance();
        activity=getActivity();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Create a new user with a first and last name
        register.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                mAuth.createUserWithEmailAndPassword(mail.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getActivity(), "Success",Toast.LENGTH_LONG).show();
                                    // Create a new user with a first and last name
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Username", name.getText().toString());
                                    user.put("Mail", mail.getText().toString());
                                    user.put("Password", password.getText().toString());
                                    user.put("Url"," ");
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("Users").document(mail.getText().toString())
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                                            startActivity(new Intent(getContext(), Tabs.class));
                                                            activity.finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });

                                    MutableLiveData<User> ser= new MutableLiveData<>();
                                    User mUser = new User(name.getText().toString()," "," "," ",mail.getText().toString()," ");
                                    ser.setValue(mUser);
                                    ((RegisterTabs)activity).sendUser(ser);




                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                }

                            }
                        });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_register, container, false);
    }
}