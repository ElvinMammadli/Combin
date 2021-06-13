package com.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    private Button button;
    private EditText name,type,date;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        name=findViewById(R.id.name);
        type=findViewById(R.id.type);
        date=findViewById(R.id.date);
        button=findViewById(R.id.add_activity);
        mAuth = FirebaseAuth.getInstance();

        List<String> subjects = new ArrayList<>();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        CollectionReference subjectsRef = rootRef.collection("Users").document(currentUser.getEmail()).collection("Combin");
        Spinner spinner = (Spinner) findViewById(R.id.combin);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long time= System.currentTimeMillis();

                Map<String, Object> aktivite = new HashMap<>();
                aktivite.put("id",String.valueOf(time));
                aktivite.put("isim",name.getText().toString());
                aktivite.put("tur", type.getText().toString());
                aktivite.put("tarih", date.getText().toString());
                aktivite.put("combin", spinner.getSelectedItem().toString());
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Users").document(currentUser.getEmail().toString()).collection("Etkinlik").document(name.getText().toString()).set(aktivite).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });



    }
}