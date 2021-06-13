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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class DeleteActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView myView;
    private FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        mAuth = FirebaseAuth.getInstance();
        myView=findViewById(R.id.fire);
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query=rootRef.collection("Users").document(currentUser.getEmail()).collection("Etkinlik");
        FirestoreRecyclerOptions<Activity> options=new FirestoreRecyclerOptions.Builder<Activity>().setQuery(query,Activity.class).build();
        adapter= new FirestoreRecyclerAdapter<Activity, DeleteActivity.ActivityView_Holder>(options) {


            @NonNull
            @NotNull
            @Override
            public DeleteActivity.ActivityView_Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_activity, parent, false);
                return new DeleteActivity.ActivityView_Holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull DeleteActivity.ActivityView_Holder holder, int position, @NonNull @NotNull Activity model) {
                holder.combin.setText(model.getCombin());
                holder.isim.setText(model.getIsim());
                holder.tarih.setText(model.getTarih());
                holder.tur.setText(model.getTur());

            }

        }

        ;
        myView.setHasFixedSize(true);
        myView.setLayoutManager(new

                LinearLayoutManager(getApplicationContext()));
        myView.setAdapter(adapter);
        adapter.startListening();




    }
    class ActivityView_Holder  extends  RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        private TextView isim, tarih, tur, combin;
        private ImageView photo;
        private ImageButton imageButton;

        public ActivityView_Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            isim = itemView.findViewById(R.id.Isim);
            tarih = itemView.findViewById(R.id.tarih);
            tur = itemView.findViewById(R.id.tip);
            combin = itemView.findViewById(R.id.kombin);
            imageButton = itemView.findViewById(R.id.image);
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
                    Intent myIntent = new Intent(itemView.getContext(), EditActivity.class);
                    int a=getAdapterPosition();
                    Activity ne= (Activity) adapter.getItem(a);
                    myIntent.putExtra("combin", ne.getCombin().toString());
                    myIntent.putExtra("isim", ne.getIsim().toString());
                    myIntent.putExtra("tarih",ne.getTarih().toString());
                    myIntent.putExtra("tur",ne.getTur().toString());
                    getApplicationContext().startActivity(myIntent);


                    return true;
                case R.id.delete:
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure?");

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            int a=getAdapterPosition();
                            Activity ne= (Activity) adapter.getItem(a);

                            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                            mAuth = FirebaseAuth.getInstance();
                            final FirebaseUser currentUser = mAuth.getCurrentUser();
                            rootRef.collection("Users").document(currentUser.getEmail()).collection("Etkinlik").document(ne.getIsim()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();

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