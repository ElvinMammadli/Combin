package com.quizz.ui.Clothes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quizz.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ClothesFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private static final int PICK_IMAGE = 1;
    private ImageView photo;
    private ClothesViewModel ClothesViewModel;
    Activity activity;
    private EditText desen, tarih , fiyat ,renk;
    private Button add;
    boolean flag;
    private FirebaseAuth mAuth;
    private File f;
    StorageReference storageReference;
    Uri imageUri;
    private Bitmap bitmap;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ClothesViewModel =
                ViewModelProviders.of(this).get(ClothesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_clothes, container, false);
        activity=getActivity();
        mAuth = FirebaseAuth.getInstance();
        photo = root.findViewById(R.id.photo);
        Spinner spinner=root.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getContext(),R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        renk=root.findViewById(R.id.color);
        desen=root.findViewById(R.id.desn);
        fiyat=root.findViewById(R.id.fiy);
        tarih=root.findViewById(R.id.dat);
        spinner.setOnItemSelectedListener(this);
        add= root.findViewById(R.id.ekle);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long time= System.currentTimeMillis();

                if(imageUri!=null) {
                    String s = getRealPathFromURI(imageUri);
                     f=new File(s);



                final StorageReference ref = storageReference.child(time+".jpg");
                ref.putFile(Uri.fromFile(f)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<UploadTask.TaskSnapshot> task) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Map<String, Object> kiyafet = new HashMap<>();
                                kiyafet.put("id",String.valueOf(time));
                                kiyafet.put("selected","null");
                                kiyafet.put("desen", desen.getText().toString());
                                kiyafet.put("tarih", tarih.getText().toString());
                                kiyafet.put("fiyat", fiyat.getText().toString());
                                kiyafet.put("renk", renk.getText().toString());
                                kiyafet.put("kiyafet_turu", spinner.getSelectedItem().toString());
                                kiyafet.put("url",uri.toString());
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                db.collection("Users").document(currentUser.getEmail()).collection("Kiyafet").document(String.valueOf(time)).set(kiyafet).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        Toast.makeText(getContext(),"Added",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                    }
                });
                }
                else{
                    Toast.makeText(getContext(),"You must add photo",Toast.LENGTH_LONG).show();
                }
            }

        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},PICK_IMAGE);
                } else {
                    Intent gallery = new Intent(Intent.ACTION_PICK );
                    gallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                    startActivityForResult(Intent.createChooser(gallery,"SELECT PICTURE"),PICK_IMAGE);
                }
            }
        });
        return root;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imageUri);
                photo.setImageBitmap(bitmap);

            }catch (IOException e){
                e.printStackTrace();
            }
        }
}

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private String getRealPathFromURI(Uri contentUri){
//        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor= getContext().getContentResolver().query(contentUri,null,null,null,null);
        if (cursor==null){
            return contentUri.getPath();
        }else{
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
}