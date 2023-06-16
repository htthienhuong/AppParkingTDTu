package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterAcitivy extends AppCompatActivity {

    TextInputEditText nameEdit, emailEdit,birthdayTextEdit ;

    CircleImageView ava;
    Button btnlogout, btnnext;

    RadioButton radioButton;
    RadioGroup radioGroup;

    final Calendar birthdayCalendar = Calendar.getInstance();

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;


//    private DatabaseReference mDatabase;
//    private FirebaseDatabase myRef



    String[] items = {"Công Nghệ Thông Tin", "Dược", "Điện - Điện Tử","Giáo Dục Quốc Tế"
            , "Kế Toán" ,"Khoa Học Thể Thao","Khoa Học Ứng Dụng","Khoa Học Xã Hội và Nhân Văn",
            "Kỹ Thuật Công Trình", "Lao Động và Công Đoàn", "Luật", "Môi Trường và Bảo Hộ Lao Động"
            , "Ngoại Ngữ", "Quản Trị Kinh Doanh", "Tài Chính - Ngân Hàng", "Toán - Thông Kê"
    };


    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitivy);

        nameEdit = findViewById(R.id.txtname);
        emailEdit = findViewById(R.id.txtemail);
        birthdayTextEdit = findViewById(R.id.txtbirthday);

        ava = findViewById(R.id.profile_image);

        btnlogout = findViewById(R.id.btnlogout);
        btnnext = findViewById(R.id.btnnext);

        radioGroup = findViewById(R.id.radiogroup);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        if(account != null){
            String Name = account.getDisplayName();
            String Email = account.getEmail();
            Uri personPhoto = account.getPhotoUrl();

            Pattern pattern = Pattern.compile("tdtu.edu.vn", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(Email);
            boolean matchFound = matcher.find();
            if(matchFound) {
                nameEdit.setText(Name);
                emailEdit.setText(Email);
                Glide.with(this).load(personPhoto).into(ava);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterAcitivy.this);
                builder.setTitle("Warning")
                        .setMessage("Must Using TDTU Email")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SignOut();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        }

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
            }
        });

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
                                RegisterAcitivy.this,
                                birthdayPicker,
                                birthdayCalendar.get(Calendar.YEAR),
                                birthdayCalendar.get(Calendar.MONTH),
                                birthdayCalendar.get(Calendar.DAY_OF_MONTH)
                        ).show();
                    }
                }
        );


        autoCompleteTextView = findViewById(R.id.txtmajor);

        adapterItems = new ArrayAdapter<String>(this,R.layout.dropdown_item,items);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(),"Item "+item, Toast.LENGTH_SHORT).show();
            }
        });


        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");


                String email, pass, name, day, month, year, birthday,gender,major;
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

                Intent i =new Intent(RegisterAcitivy.this,MapActivityLoggin.class);
                startActivity(i);
            }
        });

    }

    private void SignOut() {
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);
    }


}