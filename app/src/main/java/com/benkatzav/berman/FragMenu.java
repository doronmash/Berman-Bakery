package com.benkatzav.berman;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragMenu extends GeneralFragment {

    private ProgressBar progressBar;
    private String userID;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private AppCompatButton logout, userInfo, cusInfo;
    private String permission = null;

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

    private void findViews() {
        progressBar = rootView.findViewById(R.id.prog);
        userInfo = rootView.findViewById(R.id.menu_BTN_userInfo);
        cusInfo = rootView.findViewById(R.id.menu_BTN_cusInfo);
        logout = rootView.findViewById(R.id.menu_BTN_logout);
    }
}