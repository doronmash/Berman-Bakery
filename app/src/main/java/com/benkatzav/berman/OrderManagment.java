package com.benkatzav.berman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderManagment extends AppCompatActivity {
    private String userID;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private Button updateOrder;
    private TextInputEditText[] items;
    private TextView[] qtys;
    private TextView[] products;
    private String customerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        qtys = new TextView[6];
        products = new TextView[6];
        items = new TextInputEditText[6];

        customerName = getIntent().getStringExtra("customer_name");
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
                for(int i = 0; i < 6; i++) {
                    if (!items[i].getText().toString().equals("")) {
                        myRef.child(userID).child("customers").child(customerName).child("order").child(products[i].getText().toString()).setValue(items[i].getText().toString());
                        items[i].setText("");
                    }
                }
                Toast.makeText(OrderManagment.this,"Order updated successfuly",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void findViews() {

        items[0] = findViewById(R.id.order_EDT_item1);
        items[1] = findViewById(R.id.order_EDT_item2);
        items[2] = findViewById(R.id.order_EDT_item3);
        items[3] = findViewById(R.id.order_EDT_item4);
        items[4] = findViewById(R.id.order_EDT_item5);
        items[5] = findViewById(R.id.order_EDT_item6);
        qtys[0] = findViewById(R.id.order_LBL_qty1);
        qtys[1] = findViewById(R.id.order_LBL_qty2);
        qtys[2] = findViewById(R.id.order_LBL_qty3);
        qtys[3] = findViewById(R.id.order_LBL_qty4);
        qtys[4] = findViewById(R.id.order_LBL_qty5);
        qtys[5] = findViewById(R.id.order_LBL_qty6);
        products[0] = findViewById(R.id.order_LBL_product1);
        products[1] = findViewById(R.id.order_LBL_product2);
        products[2] = findViewById(R.id.order_LBL_product3);
        products[3] = findViewById(R.id.order_LBL_product4);
        products[4] = findViewById(R.id.order_LBL_product5);
        products[5] = findViewById(R.id.order_LBL_product6);
        updateOrder = findViewById(R.id.order_BTN_updateOrder);

    }
}