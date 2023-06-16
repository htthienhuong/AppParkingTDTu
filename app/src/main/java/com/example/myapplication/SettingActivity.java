package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
    CircleImageView ava;

    TextView logout, nameuser, edit_profile, qr_code, language;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    private Switch darkModeSwitch;

    private Switch notificationSwitch;
    Context context;
    Resources resource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        ava = findViewById(R.id.profileCircleImageView);
        nameuser = findViewById(R.id.usernameTextView);
        if (acct != null) {
            Uri personPhoto = acct.getPhotoUrl();
            String name = acct.getDisplayName();
            Glide.with(this).load(personPhoto).into(ava);

            nameuser.setText(name);
        }

        logout = findViewById(R.id.logout);
        edit_profile = findViewById(R.id.edit_profile);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(SettingActivity.this,EditProfileActivity.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
            }
        });

        if(new DarkModePrefManager(this).isNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        setDarkModeSwitch();

        qr_code = findViewById(R.id.qr_code);

        qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(SettingActivity.this,QrCodeParking.class);
                startActivity(i);
            }
        });

        language = findViewById(R.id.language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLanguageDialog();

            }
        });

        TextView payment = findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this,PaymentActivity.class);
                startActivity(i);
            }
        });

       TextView history = findViewById(R.id.history);
       history.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(SettingActivity.this,history.class);
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

    private void setDarkModeSwitch(){
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        darkModeSwitch.setChecked(new DarkModePrefManager(this).isNightMode());
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DarkModePrefManager darkModePrefManager = new DarkModePrefManager(SettingActivity.this);
                darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
            }
        });
    }

    private void setLanguageDialog(){

        int defaultChecked =-1;


        final String[] listItems = new String[]{"English","Việt Nam"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingActivity.this);
        mBuilder.setTitle(R.string.language)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recreate();
                        dialog.dismiss();
                    }
                })
                .setSingleChoiceItems(listItems, defaultChecked, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which == 0){
                           setLanguage("en");
                            recreate();

                        }
                        else if(which == 1){
                            setLanguage("vi");
                            recreate();
                        }

                    }
                });


        mBuilder.create().show();
    }

    private void setLanguage(String language){
        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

}