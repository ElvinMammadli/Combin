package com.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import java.util.List;

public class DeleteClothes extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView myView;
    private FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_delete_clothes);
        mAuth = FirebaseAuth.getInstance();
        myView=findViewById(R.id.firestore);
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();


        CollectionReference subjectsRef = rootRef.collection("Users").document(currentUser.getEmail()).collection("Cekmece");
        List<String> subjects = new ArrayList<>();
        Spinner spinner = (Spinner) findViewById(R.id.spinner5);

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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               Query query=rootRef.collection("Users").document(currentUser.getEmail()).collection("Cekmece").document(spinner.getSelectedItem().toString()).collection("Clothes").whereEqualTo("selected","true");
                FirestoreRecyclerOptions<Clothe> options=new FirestoreRecyclerOptions.Builder<Clothe>().setQuery(query,Clothe.class).build();
                adapter= new FirestoreRecyclerAdapter<Clothe, DeleteClothes.ClotheView_Holder>(options) {


                    @NonNull
                    @NotNull
                    @Override
                    public ClotheView_Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item2, parent, false);
                        return new DeleteClothes.ClotheView_Holder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull @NotNull DeleteClothes.ClotheView_Holder holder, int position, @NonNull @NotNull Clothe model) {
                        holder.renk.setText(model.getRenk());
                        holder.desen.setText(model.getDesen());
                        holder.fiyat.setText(model.getFiyat());
                        holder.tarih.setText(model.getTarih());
                        holder.kiyafet.setText(model.getKiyafet_turu());






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

                        LinearLayoutManager(getApplicationContext()));
                myView.setAdapter(adapter);
                adapter.startListening();






            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



     class ClotheView_Holder  extends  RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        private TextView desen, tarih, renk, fiyat, kiyafet;
        private ImageView photo;
        private ImageButton imageButton;

        public ClotheView_Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            renk = itemView.findViewById(R.id.renk5);
            desen = itemView.findViewById(R.id.desen5);
            photo = itemView.findViewById(R.id.photo);
            tarih = itemView.findViewById(R.id.tarih5);
            fiyat = itemView.findViewById(R.id.fiyat5);
            kiyafet = itemView.findViewById(R.id.kiyafet);
            imageButton = itemView.findViewById(R.id.menu);
            imageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            showPopupMenu(v);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.edit:
                    Intent myIntent = new Intent(itemView.getContext(), UpdateClothes.class);
                    int a=getAdapterPosition();
                    Clothe ne= (Clothe) adapter.getItem(a);
                    myIntent.putExtra("drawer_id", ne.getDrawer().toString());
                    myIntent.putExtra("clothe_id", ne.getId().toString());
                    myIntent.putExtra("desen",ne.getDesen().toString());
                    myIntent.putExtra("renk",ne.getRenk().toString());
                    myIntent.putExtra("fiyat",ne.getFiyat().toString());
                    myIntent.putExtra("tarih",ne.getTarih().toString());
                    Toast.makeText(DeleteClothes.this, ne.getTarih(), Toast.LENGTH_SHORT).show();

                    getApplicationContext().startActivity(myIntent);


                    return true;
                case R.id.delete:
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure?");

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            int a=getAdapterPosition();
                            Clothe ne= (Clothe) adapter.getItem(a);
                            Toast.makeText(getApplicationContext(),ne.getId(), Toast.LENGTH_LONG).show();

                            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                            mAuth = FirebaseAuth.getInstance();
                            final FirebaseUser currentUser = mAuth.getCurrentUser();
                            rootRef.collection("Users").document(currentUser.getEmail()).collection("Cekmece").document(ne.getDrawer()).collection("Clothes").document(ne.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                                    rootRef.collection("Users").document(currentUser.getEmail()).collection("Kiyafet").document(ne.getId()).update("selected","null");


                                }
                            });


                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"Canceled",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                default:
                    return false;
            }


        }


    }

}