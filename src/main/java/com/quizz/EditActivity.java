package com.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView myView;
    private EditText type,name,date;
    private Button buton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mAuth = FirebaseAuth.getInstance();
        myView=findViewById(R.id.firebase);
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference subjectsRef = rootRef.collection("Users").document(currentUser.getEmail()).collection("Combin");
        List<String> subjects = new ArrayList<>();
        Spinner spinner = (Spinner) findViewById(R.id.spinner_combin);
        buton=findViewById(R.id.guncel);
        Intent intent=getIntent();
        String combin=intent.getStringExtra("combin");
        String isim=intent.getStringExtra("isim");
        String tarih=intent.getStringExtra("tarih");
        String tur=intent.getStringExtra("tur");

        type=findViewById(R.id.type2);
        name=findViewById(R.id.isim2);
        date=findViewById(R.id.date2);
        type.setText(tur);
        name.setText(isim);
        date.setText(tarih);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, subjects);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter2);
        subjectsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("isim");
                        subjects.add(subject);
                    }
                    adapter2.notifyDataSetChanged();
                }
            }
        });
        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootRef.collection("Users").document(currentUser.getEmail()).collection("Etkinlik").document(isim).update("tarih",date.getText().toString(),"tur",type.getText().toString(),"isim",name.getText().toString(),"combin",spinner.getSelectedItem().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}