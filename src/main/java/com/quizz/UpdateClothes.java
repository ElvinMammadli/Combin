package com.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class UpdateClothes extends AppCompatActivity {
   private Spinner spinner;
   private Button button;
    private FirebaseAuth mAuth;
    private EditText desen,tarih,renk,fiyat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_clothes);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        Intent intent=getIntent();
        String renk3=intent.getStringExtra("renk");
        String desen3=intent.getStringExtra("desen");
        String fiyat3=intent.getStringExtra("fiyat");
        String tarih3=intent.getStringExtra("tarih");

        String drawer_id=intent.getStringExtra("drawer_id");
        String clothe_id=intent.getStringExtra("clothe_id");
        desen=findViewById(R.id.cizgi);
        tarih=findViewById(R.id.buy_date);
        renk=findViewById(R.id.colour);
        fiyat=findViewById(R.id.money);
        Toast.makeText(this, tarih3, Toast.LENGTH_SHORT).show();
        tarih.setText(tarih3);
        renk.setText(renk3);
        fiyat.setText(fiyat3);
        desen.setText(desen3);




       spinner=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getApplicationContext(),R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        button=findViewById(R.id.ekle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootRef.collection("Users").document(currentUser.getEmail()).collection("Cekmece").document(drawer_id).collection("Clothes").document(clothe_id).update("desen",desen.getText().toString(),"fiyat",fiyat.getText().toString(),"kiyafet_turu",spinner.getSelectedItem().toString(),"renk",renk.getText().toString(),"tarih",tarih.getText().toString());

                rootRef.collection("Users").document(currentUser.getEmail()).collection("Kiyafet").document(clothe_id).update("desen",desen.getText().toString(),"fiyat",fiyat.getText().toString(),"kiyafet_turu",spinner.getSelectedItem().toString(),"renk",renk.getText().toString(),"tarih",tarih.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                Toast.makeText(UpdateClothes.this, "Updated", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}