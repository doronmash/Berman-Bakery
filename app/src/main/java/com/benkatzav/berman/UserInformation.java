package com.benkatzav.berman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserInformation extends AppCompatActivity {
    private String userID;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private Button update;
    private TextView currentPhone, currentName,permission;
    private TextInputEditText name,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uinformation);

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
                if(snapshot.child(userID).child("name").getValue(String.class) != null)
                currentName.setText(snapshot.child(userID).child("name").getValue(String.class));
                if(snapshot.child(userID).child("phone").getValue(String.class) != null)
                currentPhone.setText(snapshot.child(userID).child("phone").getValue(String.class));
                if(snapshot.child(userID).child("permission").getValue(String.class) != null)
                permission.setText(snapshot.child(userID).child("permission").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Pttt", "Failed to read value.", error.toException());
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(userID).child("name").setValue(name.getText().toString());
                myRef.child(userID).child("phone").setValue(phone.getText().toString());
                name.setText("");
                phone.setText("");
            }
        });

    }


    private void findViews() {
        phone = findViewById(R.id.uInfo_EDT_phone);
        name = findViewById(R.id.uInfo_EDT_name);
        currentName = findViewById(R.id.uInfo_LBL_insertedName);
        currentPhone = findViewById(R.id.uInfo_LBL_insertedPhone);
        update = findViewById(R.id.uInfo_BTN_update);
        permission = findViewById(R.id.uInfo_LBL_permission);
    }
}