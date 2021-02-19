package com.benkatzav.berman;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragMenu extends GeneralFragment {

    private ProgressBar progressBar;
    private String userID;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private AppCompatButton logout, userInfo, cusInfo;
    private CircleImageView profilPic;
    private String permission = null;
    private String urlImg;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        initProgressBar();
        findViews();
        firebaseDatabase = FirebaseDatabase.getInstance();

        myRef = firebaseDatabase.getReference();
        currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Pttt", "Inside dataChange");
                permission = snapshot.child(userID).child("permission").getValue(String.class);
                urlImg = snapshot.child(userID).child("photoPath").getValue(String.class);
                readProfilePic(profilPic, urlImg);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Pttt", "Failed to read value.", error.toException());
            }
        });


        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSound(R.raw.button_sound, 50);

                callFragment(new FragUserInformation(), null);
            }
        });

        cusInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSound(R.raw.button_sound, 50);

                if(permission != null)
                    callFragment(new FragCustomerInformation(), FragMenu.this);
                else
                    Toast.makeText(getContext(),"No permissions available",Toast.LENGTH_SHORT).show();
            }
        });


        profilPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralFragment.getImage(FragMenu.this);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSound(R.raw.button_sound, 50);

                FragLogin.signOut();
                callFragment(new FragLogin(), null);
            }
        });


        return rootView;
    }

    private void initProgressBar() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }

        }, 4000);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri fileUri = data.getData();
            uploadImage(fileUri);

            profilPic.setImageURI(fileUri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(),new ImagePicker().Companion.getError(data), Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(getContext(),"Task Cancelled", Toast.LENGTH_SHORT);
        }
    }


    public void uploadImage(Uri filePathUri) {
        if (filePathUri != null) {
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("profilepics/").child(filePathUri.getLastPathSegment());
            ref.putFile(filePathUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        urlImg = uri.getResult().toString();
                        myRef.child(userID).child("photoPath").setValue(urlImg);
                        //Toast.makeText(App.getAppContext(), urlImg, Toast.LENGTH_SHORT);
                    })
                    .addOnFailureListener(e ->Log.d("check","update failed") );

        }
    }

    public void readProfilePic(CircleImageView imageView, String filePath){

        Picasso.with(getContext()).load(filePath).placeholder(R.drawable.ic_defultpic).into(imageView);
    }




    private void findViews() {
        progressBar = rootView.findViewById(R.id.prog);
        userInfo = rootView.findViewById(R.id.menu_BTN_userInfo);
        cusInfo = rootView.findViewById(R.id.menu_BTN_cusInfo);
        logout = rootView.findViewById(R.id.menu_BTN_logout);
        profilPic = rootView.findViewById(R.id.profilePic);
    }
}