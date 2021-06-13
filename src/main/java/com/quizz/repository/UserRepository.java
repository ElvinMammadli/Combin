package com.quizz.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quizz.listeners.FireUserListener;
import com.quizz.model.User;

public class UserRepository {
    private static UserRepository instance;
    private FirebaseAuth mAuth;
    private static User mUser;
    private FireUserListener listener;

    public static UserRepository getInstance(){
        if(instance==null){
            instance=new UserRepository();
        }
        return instance;
    }

    public MutableLiveData<User> getUser(FireUserListener listener){
        this.listener = listener;
        setUser();
        MutableLiveData<User> data= new MutableLiveData<>();
        data.setValue(mUser);
        return data;
    }

    public void setUser(){
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference docRef = db.collection("Users").document(currentUser.getEmail());
            mUser= new User();
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            mUser.setEmail((String) document.getData().get("Mail"));
                            mUser.setName((String) document.getData().get("Name"));
                            mUser.setGender((String) document.getData().get("Gender"));
                            mUser.setUser_name((String)document.getData().get("Username"));
                            mUser.setRank((String) document.getData().get("Rank"));
                            mUser.setUrl((String)document.getData().get("Url"));
                            mUser.setCountry((String)document.getData().get("Country"));

                            MutableLiveData<User> data= new MutableLiveData<>();
                            data.setValue(mUser);
                            listener.onComplete(data);
                        }
                    }
                }
            });


    }
}
