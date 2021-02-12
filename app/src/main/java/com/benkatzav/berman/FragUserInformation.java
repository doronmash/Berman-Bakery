package com.benkatzav.berman;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragUserInformation extends GeneralFragment {
    private String userID;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private AppCompatButton update;
    private TextView currentPhone, currentName,permission;
    private TextInputEditText name,phone;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_uinformation, container, false);


        findViews();
        firebaseDatabase = FirebaseDatabase.getInstance();

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
                startSound(R.raw.button_sound, 50);

                myRef.child(userID).child("name").setValue(name.getText().toString());
                myRef.child(userID).child("phone").setValue(phone.getText().toString());
                name.setText("");
                phone.setText("");
            }
        });

        return rootView;
    }


    private void findViews() {
        phone = rootView.findViewById(R.id.uInfo_EDT_phone);
        name = rootView.findViewById(R.id.uInfo_EDT_name);
        currentName = rootView.findViewById(R.id.uInfo_LBL_insertedName);
        currentPhone = rootView.findViewById(R.id.uInfo_LBL_insertedPhone);
        update = rootView.findViewById(R.id.uInfo_BTN_update);
        permission = rootView.findViewById(R.id.uInfo_LBL_permission);
    }
}