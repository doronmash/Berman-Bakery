package com.benkatzav.berman;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragOrderManagment extends GeneralFragment {
    private String userID;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private AppCompatButton updateOrder;
    private TextInputEditText[] items;
    private TextView[] qtys;
    private TextView[] products;
    private String customerName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_order, container, false);


        qtys = new TextView[6];
        products = new TextView[6];
        items = new TextInputEditText[6];

        customerName = getArguments().getString("customer_name");
        findViews();

        firebaseDatabase = FirebaseDatabase.getInstance();

        myRef = firebaseDatabase.getReference();
        currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                startSound(R.raw.button_sound, 50);

                Log.d("Pttt", "Inside dataChange");
                for(int i = 0; i < 6; i++){
                    if(snapshot.child(userID).child("customers").child(customerName).child("order").child(products[i].getText().toString()).getValue() != null)
                        qtys[i].setText(snapshot.child(userID).child("customers").child(customerName).child("order").child(products[i].getText().toString()).getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Pttt", "Failed to read value.", error.toException());
            }
        });

        updateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSound(R.raw.button_sound, 50);

                for(int i = 0; i < 6; i++) {
                    if (!items[i].getText().toString().equals("")) {
                        myRef.child(userID).child("customers").child(customerName).child("order").child(products[i].getText().toString()).setValue(items[i].getText().toString());
                        items[i].setText("");
                    }
                }
                Toast.makeText(getContext(),"Order updated successfuly",Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }

    private void findViews() {

        items[0] = rootView.findViewById(R.id.order_EDT_item1);
        items[1] = rootView.findViewById(R.id.order_EDT_item2);
        items[2] = rootView.findViewById(R.id.order_EDT_item3);
        items[3] = rootView.findViewById(R.id.order_EDT_item4);
        items[4] = rootView.findViewById(R.id.order_EDT_item5);
        items[5] = rootView.findViewById(R.id.order_EDT_item6);
        qtys[0] = rootView.findViewById(R.id.order_LBL_qty1);
        qtys[1] = rootView.findViewById(R.id.order_LBL_qty2);
        qtys[2] = rootView.findViewById(R.id.order_LBL_qty3);
        qtys[3] = rootView.findViewById(R.id.order_LBL_qty4);
        qtys[4] = rootView.findViewById(R.id.order_LBL_qty5);
        qtys[5] = rootView.findViewById(R.id.order_LBL_qty6);
        products[0] = rootView.findViewById(R.id.order_LBL_product1);
        products[1] = rootView.findViewById(R.id.order_LBL_product2);
        products[2] = rootView.findViewById(R.id.order_LBL_product3);
        products[3] = rootView.findViewById(R.id.order_LBL_product4);
        products[4] = rootView.findViewById(R.id.order_LBL_product5);
        products[5] = rootView.findViewById(R.id.order_LBL_product6);
        updateOrder = rootView.findViewById(R.id.order_BTN_updateOrder);

    }
}