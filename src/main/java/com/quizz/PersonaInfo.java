package com.quizz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quizz.listeners.BindHomeNTabs;
import com.quizz.model.User;
import com.quizz.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


public class PersonaInfo extends Fragment {
    private EditText user_name;
    private EditText name;
    private Button update;
    private FirebaseAuth mAuth;
    private Spinner gender;
    private CircleImageView ProfileImage;
    private boolean flag;
    private static final int PICK_IMAGE = 1;
    private Spinner country;
    private FragmentActivity activity;
    private String dataBaseUri;
    Uri imageUri;
    Uri compressedImageFile;
    private Bitmap bitmap;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    public PersonaInfo() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PersonaInfo newInstance(String param1, String param2) {
        PersonaInfo fragment = new PersonaInfo();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        flag=false;
        View personInfoView =  inflater.inflate(R.layout.fragment_personal_info, container, false);
        user_name=personInfoView.findViewById(R.id.user_name);
        name=personInfoView.findViewById(R.id.name);
        update=personInfoView.findViewById(R.id.send);
        gender=personInfoView.findViewById(R.id.spinner_gender);
        country=personInfoView.findViewById(R.id.spinner_country);
        ProfileImage = (CircleImageView) personInfoView.findViewById(R.id.profile_image);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        activity=getActivity();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        dataBaseUri=((BindHomeNTabs)activity).getViewModel().getUser().getValue().getUrl();
        if (!dataBaseUri.equals(" ")){
            Glide
                    .with(getContext())
                    .load(dataBaseUri)
                    .centerCrop()
                    .thumbnail(0.01f)
                    .into(ProfileImage);
        }



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (flag){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final DocumentReference Users = db.collection("Users").document(currentUser.getEmail());
                    long time= System.currentTimeMillis();

                        String s = getRealPathFromURI(imageUri);
                        File f=new File(s);
                        Log.d("XXX", "onClick: "+s);

                        try {
                            File n = new Compressor(getContext()).setQuality(40).compressToFile(f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        final StorageReference ref = storageReference.child(time+".jpg");
                    ref.putFile(Uri.fromFile(f)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Users.update(
                                            "Url",uri.toString(),
                                            "Username", user_name.getText().toString(),
                                            "Name", name.getText().toString(),
                                            "Gender", gender.getSelectedItem().toString(),
                                            "Country", country.getSelectedItem().toString(),
                                            "Rank", "0"
                                    ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(activity, "Updated.",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    MutableLiveData<User> ser = new MutableLiveData<>();
                                    User user = new User(name.getText().toString(), user_name.getText().toString(), gender.getSelectedItem().toString(), country.getSelectedItem().toString(), currentUser.getEmail(),uri.toString());
                                    ser.setValue(user);
                                    ((BindHomeNTabs) activity).sendUser(ser);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    });
                    }
                    StorageReference storageRef = storage.getReference();


                        //compress file
                    UserRepository userRepository = new UserRepository();

                    if (!flag) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        final DocumentReference Users = db.collection("Users").document(currentUser.getEmail());
                        Users.update(
                                "Username", user_name.getText().toString(),
                                "Name", name.getText().toString(),
                                "Gender", gender.getSelectedItem().toString(),
                                "Country", country.getSelectedItem().toString(),
                                "Rank", "0"
                        ).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "Updated.",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                        MutableLiveData<User> ser = new MutableLiveData<>();
                        User user = new User(name.getText().toString(), user_name.getText().toString(), gender.getSelectedItem().toString(), country.getSelectedItem().toString(), currentUser.getEmail(),dataBaseUri);
                        ser.setValue(user);
                        ((BindHomeNTabs) activity).sendUser(ser);

                    }
//                }catch (Exception ex){
//                    Toast.makeText(getActivity(), ex.toString(),
//                            Toast.LENGTH_LONG).show();
//                }

            }

        });

        user_name.setText(((BindHomeNTabs)activity).getViewModel().getUser().getValue().getUser_name());
        name.setText(((BindHomeNTabs)activity).getViewModel().getUser().getValue().getName());





        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},PICK_IMAGE);
                } else {
                    Intent gallery = new Intent(Intent.ACTION_PICK );
                    gallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                    startActivityForResult(Intent.createChooser(gallery,"SELECT PICTURE"),PICK_IMAGE);
                }
            }
        });

        Spinner genderSpinner = (Spinner) personInfoView.findViewById(R.id.spinner_gender);
        String[] Gender = {"Male", "Female", "Other"};
        ArrayList<String> GenderList = new ArrayList<>(Arrays.asList(Gender));
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this.getActivity(),R.layout.style_spinner,GenderList);
        genderSpinner.setAdapter(genderAdapter);
        int j = GenderList.indexOf(((BindHomeNTabs)activity).getViewModel().getUser().getValue().getGender());
        gender.setSelection(j);
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Spinner countrySpinner = (Spinner) personInfoView.findViewById(R.id.spinner_country);
        Collections.sort(countries);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this.getActivity(),R.layout.style_spinner,countries);
        countrySpinner.setAdapter(countryAdapter);
        int i = countries.indexOf(((BindHomeNTabs)activity).getViewModel().getUser().getValue().getCountry());
        country.setSelection(i);
        return personInfoView;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            try {
                 bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
                ProfileImage.setImageBitmap(bitmap);

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}