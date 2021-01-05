package com.benkatzav.berman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {

    private String userID;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private Button logout, userInfo, cusInfo;
    private String permission = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViews();

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
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
                startActivity(new Intent(MenuActivity.this,UserInformation.class));
            }
        });

        cusInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permission != null)
                    startActivity(new Intent(MenuActivity.this,CustomerInformation.class));
                else
                    Toast.makeText(MenuActivity.this,"No permissions available",Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.signOut();
                finish();
            }
        });


    }

    private void findViews() {
        userInfo = findViewById(R.id.menu_BTN_userInfo);
        cusInfo = findViewById(R.id.menu_BTN_cusInfo);
        logout = findViewById(R.id.menu_BTN_logout);
    }
}