package com.benkatzav.berman;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragCustomerInformation extends GeneralFragment {
    private String userID;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private Button phoneNumber, changeOrder,findLocation;
    private Spinner spinner;
    private ArrayList<String> customers;
    private ArrayAdapter<String> adapter;
    private String currentCustomer;
    Dialog myDialog;
    double lat,lng;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_cinformation, container, false);


        myDialog = new Dialog(context);

        findViews();
        firebaseDatabase = FirebaseDatabase.getInstance();

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

                        Bundle bundle = new Bundle();
                        bundle.putString("customer_name",currentCustomer);

                        GeneralFragment fragment = new FragOrderManagment();
                        fragment.setArguments(bundle);
                        callFragment(fragment, FragCustomerInformation.this);
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
                Toast.makeText(context, "No customer selected",Toast.LENGTH_SHORT).show();
            }
        });



        return rootView;
    }


    private void openMapsActivity(double lat, double lng, String currentCustomer) {
        Bundle bundle = new Bundle();
        bundle.putDouble("LAT",lat);
        bundle.putDouble("LNG",lng);
        bundle.putString("LOCATION",currentCustomer);

        GeneralFragment fragment = new FragMaps();
        fragment.setArguments(bundle);
        callFragment(fragment, this);
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
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, customers);
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
        phoneNumber = rootView.findViewById(R.id.cInfo_BTN_number);
        changeOrder = rootView.findViewById(R.id.cInfo_BTN_order);
        findLocation = rootView.findViewById(R.id.cInfo_BTN_map);
        spinner = rootView.findViewById(R.id.cInfo_spinner);
    }
}