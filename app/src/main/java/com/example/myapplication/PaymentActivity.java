package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

//import com.vnpay.authentication.VNP_AuthenticationActivity;
//import com.vnpay.authentication.VNP_SdkCompletedCallback;


public class PaymentActivity extends AppCompatActivity {
    ArrayList<Payment> list = new ArrayList<>();
    PaymentAdapter adapterItem;
    RecyclerView recyclerView;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
    FloatingActionButton fab_add;
    AlertDialog builderAlert;
    Context context;
    LayoutInflater layoutInflater;
    View showInput;
    Locale id = new Locale("in", "ID");
    TextView amount;
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        amount = findViewById(R.id.amount);

        context = this;
        recyclerView = findViewById(R.id.recyclerView);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            Email = acct.getEmail();
        }
        showData();

    }
    private void showData() {
        database.child(Email.split("@")[0]).child("payment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showLisener(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.child(Email.split("@")[0]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                int num = user.getAmount();
                Locale locale = new Locale("en", "US"); // Set the locale to US English
                NumberFormat formatter = NumberFormat.getInstance(locale); // Get the number formatter
                String formattedValue = formatter.format(num);
                amount.setText(formattedValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void showLisener(DataSnapshot snapshot) {
        list.clear();
        for (DataSnapshot item : snapshot.getChildren()) {
            Payment user = item.getValue(Payment.class);
            list.add(user);
        }
        adapterItem = new PaymentAdapter(context, list);
        recyclerView.setAdapter(adapterItem);
    }

}