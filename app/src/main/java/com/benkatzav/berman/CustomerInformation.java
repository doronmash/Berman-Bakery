package com.benkatzav.berman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.ArrayList;

public class CustomerInformation extends AppCompatActivity {
    private String userID;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private Button phoneNumber, changeOrder,findLocation;
    private Spinner spinner;
    private ArrayList<String> customers;
    private ArrayAdapter<String> adapter;
    private String currentCustomer;
    Dialog myDialog;
    double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinformation);

        myDialog = new Dialog(this);

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
                String per = snapshot.child(userID).child("permission").getValue(String.class);
                initCustomersArray(per);
                initSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Pttt", "Failed to read value.", error.toException());
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                findLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Log.d("Pttt", "Inside dataChange");
                                if(spinner.getSelectedItem().toString() != null || !spinner.getSelectedItem().toString().equals(""))
                                    currentCustomer = spinner.getSelectedItem().toString();
                                lat = snapshot.child("locations").child(currentCustomer).child("latitude").getValue(Double.class);
                                lng = snapshot.child("locations").child(currentCustomer).child("longitude").getValue(Double.class);
                                openMapsActivity(lat,lng,currentCustomer);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w("Pttt", "Failed to read value.", error.toException());
                            }
                        });
                    }
                });

                changeOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(spinner.getSelectedItem().toString() != null || !spinner.getSelectedItem().toString().equals(""))
                            currentCustomer = spinner.getSelectedItem().toString();
                        Intent oIntent = new Intent(CustomerInformation.this,OrderManagment.class);
                        oIntent.putExtra("customer_name",currentCustomer);
                        startActivity(oIntent);
                    }
                });

                phoneNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(spinner.getSelectedItem().toString() != null || !spinner.getSelectedItem().toString().equals(""))
                            currentCustomer = spinner.getSelectedItem().toString();
                        showPopUp();
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CustomerInformation.this, "No customer selected",Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void openMapsActivity(double lat, double lng, String currentCustomer) {
        Intent mIntent = new Intent(CustomerInformation.this,MapsActivity.class);
        mIntent.putExtra("LAT",lat);
        mIntent.putExtra("LNG",lng);
        mIntent.putExtra("LOCATION",currentCustomer);
        startActivity(mIntent);
    }

    private void showPopUp() {
        TextView pNumber;
        TextInputEditText newNumber;
        Button update;

        myDialog.setContentView(R.layout.popup_number);

        pNumber = (TextView) myDialog.findViewById(R.id.pop_phonenumber);
        newNumber = myDialog.findViewById(R.id.pop_EDT_newNumber);
        update = myDialog.findViewById(R.id.pop_BTN_update);

        myDialog.show();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Pttt", "Inside dataChange");
                if(snapshot.child(userID).child("customers").child(currentCustomer).child("phone").getValue() != null)
                pNumber.setText(snapshot.child(userID).child("customers").child(currentCustomer).child("phone").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Pttt", "Failed to read value.", error.toException());
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(userID).child("customers").child(currentCustomer).child("phone").setValue(newNumber.getText().toString());
            }
        });
    }

    private void initSpinner() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, customers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initCustomersArray(String per) {
        customers = new ArrayList<>();
        if(per.equals("Hod_Hasharon_Permission")) {
            customers.add("חצי חינם הוד השרון");
            customers.add("סטופ מרקט הוד השרון");
            customers.add("טיב טעם הוד השרון");
        }

        if(per.equals("Petah_Tikva_Permission")) {
            customers.add("חצי חינם פתח תקווה");
            customers.add("אושר עד פתח תקווה");
            customers.add("שופרסל דיל פתח תקווה");
        }

        if(per.equals("Rishon_Lezion_Permission")) {
            customers.add("אושר עד ראשון לציון");
            customers.add("שופרסל דיל ראשון לציון");
            customers.add("יינות ביתן ראשון לציון");
        }

        if(per.equals("Rosh_Haayin_Permission")) {
            customers.add("יינות ביתן ראש העין");
            customers.add("ויקטורי ראש העין");
            customers.add("שופרסל שלי ראש העין");
        }
    }

    private void findViews() {
        phoneNumber = findViewById(R.id.cInfo_BTN_number);
        changeOrder = findViewById(R.id.cInfo_BTN_order);
        findLocation = findViewById(R.id.cInfo_BTN_map);
        spinner = findViewById(R.id.cInfo_spinner);
    }
}