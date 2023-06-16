package com.example.myapplication;


import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    RadioGroup radioGroup;

    RadioButton radioButton;
    CircleImageView ava;

    TextInputEditText nameEdit, emailEdit, birthdayTextEdit;

    Integer amount;
    final Calendar birthdayCalendar = Calendar.getInstance();

    String[] items = {"Công Nghệ Thông Tin", "Dược", "Điện - Điện Tử", "Giáo Dục Quốc Tế"
            , "Kế Toán", "Khoa Học Thể Thao", "Khoa Học Ứng Dụng", "Khoa Học Xã Hội và Nhân Văn",
            "Kỹ Thuật Công Trình", "Lao Động và Công Đoàn", "Luật", "Môi Trường và Bảo Hộ Lao Động"
            , "Ngoại Ngữ", "Quản Trị Kinh Doanh", "Tài Chính - Ngân Hàng", "Toán - Thông Kê"
    };


    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        ava = findViewById(R.id.unsplash_jm);

        nameEdit = findViewById(R.id.txtname);
        emailEdit = findViewById(R.id.txtemail);
        birthdayTextEdit = findViewById(R.id.txtbirthday);
        radioGroup = findViewById(R.id.radiogroup);
        RadioButton radioBtnMale = findViewById(R.id.radio_male);
        RadioButton radioBtnFemale = findViewById(R.id.radio_female);

        DatePickerDialog.OnDateSetListener birthdayPicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                birthdayCalendar.set(Calendar.YEAR, year);
                birthdayCalendar.set(Calendar.MONTH, month);
                birthdayCalendar.set(Calendar.DAY_OF_MONTH, day);

                // Update birthday edit text
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                birthdayTextEdit.setText(dateFormat.format(birthdayCalendar.getTime()));
            }
        };

        birthdayTextEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(
                                EditProfileActivity.this,
                                birthdayPicker,
                                birthdayCalendar.get(Calendar.YEAR),
                                birthdayCalendar.get(Calendar.MONTH),
                                birthdayCalendar.get(Calendar.DAY_OF_MONTH)
                        ).show();
                    }
                }
        );

        autoCompleteTextView = findViewById(R.id.txtmajor);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if (acct != null) {
            Uri personPhoto = acct.getPhotoUrl();
            String name = acct.getDisplayName();
            String Email = acct.getEmail();
            Glide.with(this).load(personPhoto).into(ava);

            nameEdit.setText(name);
            emailEdit.setText(Email);
            adapterItems = new ArrayAdapter<String>(this,R.layout.dropdown_item,items);

            autoCompleteTextView.setAdapter(adapterItems);

            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(),"Item "+item, Toast.LENGTH_SHORT).show();
                }
            });


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users").child(Email.split("@")[0]);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);

                    birthdayTextEdit.setText(user.getBirthday());
//                    autoCompleteTextView.setText(autoCompleteTextView.getAdapter().getItem(0).toString());
                    autoCompleteTextView.setText(user.getMajor(),false);
                    String gender = user.getGender().toString();
                    if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Nam") ){
                        radioBtnMale.setChecked(true);
                    } else if (gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("Nữ")) {
                        radioBtnFemale.setChecked(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

        Button update = findViewById(R.id.update_button);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");


                String email, name, day, month, year, birthday,gender,major;
                day = month = year = "";

                email = emailEdit.getText().toString().replaceAll("\\s+", "");
                name = nameEdit.getText().toString();
                gender = radioButton.getText().toString();
                major = autoCompleteTextView.getText().toString();


                birthday = birthdayTextEdit.getText().toString();
                String[] dateParts = birthday.split("/");

                boolean validBirthday = false;
                if (dateParts.length == 3) {
                    day = dateParts[0];
                    month = dateParts[1];
                    year = dateParts[2];
                    validBirthday = true;
                }
                User user = new User(name,day,month,year,email,gender,major);
                myRef.child(email.split("@")[0]).setValue(user);
                Toast.makeText(EditProfileActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
            }
        });

        View back = findViewById(R.id.arrow_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }


}