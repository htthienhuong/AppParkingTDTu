package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class history extends AppCompatActivity {
    EditText input_minimal,
            input_maximal;
    Button btn_minimal,
            btn_maximal,
            cari;
    ArrayList<dataUser> list = new ArrayList<>();
    AdapterItem adapterItem;
    RecyclerView recyclerView;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
    FloatingActionButton fab_add;
    AlertDialog builderAlert;
    Context context;
    LayoutInflater layoutInflater;
    View showInput;
    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-YYYY", id);
    Date date_minimal;
    Date date_maximal;
    String Email;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        context = this;
        cari = findViewById(R.id.cari);
        input_minimal = findViewById(R.id.input_minimal);
        input_maximal = findViewById(R.id.input_maximal);
        btn_minimal = findViewById(R.id.btn_minimal);
        btn_maximal = findViewById(R.id.btn_maximal);
        recyclerView = findViewById(R.id.recyclerView);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            Email = acct.getEmail();
        }

        btn_minimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                        input_minimal.setText(dateFormat.format(calendar.getTime()));
                        date_minimal = calendar.getTime();

                        String input1 = input_minimal.getText().toString();
                        String input2 = input_maximal.getText().toString();
                        if (input1.isEmpty() && input2.isEmpty()){
                            cari.setEnabled(false);
                        }else {
                            cari.setEnabled(true);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btn_maximal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                        input_maximal.setText(dateFormat.format(calendar.getTime()));
                        date_maximal = calendar.getTime();

                        String input1 = input_maximal.getText().toString();
                        String input2 = input_minimal.getText().toString();
                        if (input1.isEmpty() && input2.isEmpty()){
                            cari.setEnabled(false);
                        }else {
                            cari.setEnabled(true);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });



        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                Query query = database.child(Email.split("@")[0]).child("history").orderByChild("check_in").startAt( dateFormat.format(date_minimal.getTime())).endAt( dateFormat.format(date_maximal.getTime())+ ",23:59:59");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        showLisener(snapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        showData();
    }



    private void showData() {
        database.child(Email.split("@")[0]).child("history").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showLisener(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showLisener(DataSnapshot snapshot) {
        list.clear();
        for (DataSnapshot item : snapshot.getChildren()) {
            dataUser user = item.getValue(dataUser.class);
            list.add(user);
        }
        adapterItem = new AdapterItem(context, list);
        recyclerView.setAdapter(adapterItem);
    }
}