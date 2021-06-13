package com.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddClothes extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView myView;
    private FirestoreRecyclerAdapter adapter;
    private Button ekle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_clothes);
        mAuth = FirebaseAuth.getInstance();
        myView=findViewById(R.id.firestore);
        ekle=findViewById(R.id.ekle);
        List<String> ids = new ArrayList<>();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query=rootRef.collection("Users").document(currentUser.getEmail()).collection("Kiyafet").whereEqualTo("selected","null");
        FirestoreRecyclerOptions<Clothe> options=new FirestoreRecyclerOptions.Builder<Clothe>().setQuery(query,Clothe.class).build();
        Map<String, Object> clothes = new HashMap<>();
        CollectionReference subjectsRef = rootRef.collection("Users").document(currentUser.getEmail()).collection("Cekmece");
        List<String> subjects = new ArrayList<>();
        Spinner spinner = (Spinner) findViewById(R.id.spinner3);

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
        adapter= new FirestoreRecyclerAdapter<Clothe, AddClothes.ClotheView_Holder>(options) {

            @NonNull
            @NotNull
            @Override
            public ClotheView_Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                return new AddClothes.ClotheView_Holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull AddClothes.ClotheView_Holder holder, int position, @NonNull @NotNull Clothe model) {
                holder.renk.setText(model.getRenk());
                holder.desen.setText(model.getDesen());
                holder.fiyat.setText(model.getFiyat());
                holder.tarih.setText(model.getTarih());
                holder.kiyafet.setText(model.getKiyafet_turu());
                holder.check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(holder.check.isChecked()){
                            clothes.put("desen", model.getDesen());
                            clothes.put("fiyat",model.getFiyat());
                            clothes.put("id",model.getId());
                            clothes.put("kiyafet_turu", model.getKiyafet_turu());
                            clothes.put("renk", model.getRenk());
                            clothes.put("selected", "true");
                            clothes.put("tarih", model.getTarih());
                            clothes.put("url",model.getUrl());
                            clothes.put("drawer",spinner.getSelectedItem());
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("Users").document(currentUser.getEmail()).collection("Cekmece").document(spinner.getSelectedItem().toString()).collection("Clothes").document(model.getId())
                                    .set(clothes).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    db.collection("Users").document(currentUser.getEmail()).collection("Kiyafet").document(model.getId()).update("selected","true");
                                    Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });

                Glide
                        .with(getApplicationContext())
                        .load(model.getUrl())
                        .centerCrop()
                        .thumbnail(0.01f)
                        .into(holder.photo);

            }

        }

        ;
        myView.setHasFixedSize(true);
        myView.setLayoutManager(new

                LinearLayoutManager(this));
        myView.setAdapter(adapter);


    }

    private class ClotheView_Holder  extends  RecyclerView.ViewHolder {

        private TextView desen, tarih, renk, fiyat, kiyafet;
        private CheckBox check;
        private ImageView photo;

        public ClotheView_Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            renk = itemView.findViewById(R.id.renk5);
            desen = itemView.findViewById(R.id.desen5);
            photo = itemView.findViewById(R.id.photo);
            tarih = itemView.findViewById(R.id.tarih5);
            fiyat = itemView.findViewById(R.id.fiyat5);
            kiyafet = itemView.findViewById(R.id.kiyafet);
            check=itemView.findViewById(R.id.check);

        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

}