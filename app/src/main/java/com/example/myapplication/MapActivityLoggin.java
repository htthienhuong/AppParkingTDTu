package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MapActivityLoggin extends AppCompatActivity {
    de.hdodenhof.circleimageview.CircleImageView ava;
    private RelativeLayout container;
    private int currentX;
    private int currentY;
    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    GestureDetector gestureDetector;
    private Button nhathidau;
    final int PERMISSION_REQUEST_CODE =112;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_loggin);

        ava = findViewById(R.id.profile_image_1);
        nhathidau = findViewById(R.id.button1);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);


        if (acct != null) {
            Uri personPhoto = acct.getPhotoUrl();
            Glide.with(this).load(personPhoto).into(ava);
        }

        ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapActivityLoggin.this, SettingActivity.class);
                startActivity(i);
            }
        });

        container = findViewById(R.id.Container);

        gestureDetector = new GestureDetector(this, new MapActivityLoggin.GestureListener());

        mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scale = 1 - detector.getScaleFactor();

                float prevScale = mScale;
                mScale += scale;

                if (mScale < 0.1f) // Minimum scale condition:
                    mScale = 0.1f;

                if (mScale > 10f) // Maximum scale condition:
                    mScale = 10f;
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f / prevScale, 1f / mScale, 1f / prevScale, 1f / mScale, detector.getFocusX(), detector.getFocusY());
                scaleAnimation.setDuration(0);
                scaleAnimation.setFillAfter(true);
                ScrollView layout = (ScrollView) findViewById(R.id.scrollViewZoom);
                layout.startAnimation(scaleAnimation);
                return true;
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Density/NhaThiDau");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String dens = (String) snapshot.getValue();

                if(dens.equals("Vắng")){

                    nhathidau.setBackgroundColor(Color.parseColor("#89a832"));
                } else if (dens.equals("Thưa")) {

                    nhathidau.setBackgroundColor(Color.parseColor("#a6a832"));

                } else if(dens.equals("Khá Đông")){
                    nhathidau.setBackgroundColor(Color.parseColor("#fcf038"));
                }
                else if(dens.equals("Đông")){
                    nhathidau.setBackgroundColor(Color.parseColor("#fcaa38"));
                }
                else if(dens.equals("Rất Đông")){
                    nhathidau.setBackgroundColor(Color.parseColor("#ff352b"));
                }
                else if(dens.equals("Loading")){
                    nhathidau.setBackgroundColor(Color.parseColor("#7a786f"));
                }
                SendNotification(dens,"Nhà Thi Đấu");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildChanged:" + snapshot.getValue());
                String dens = (String) snapshot.getValue();

                if(dens.equals("Vắng")){

                    nhathidau.setBackgroundColor(Color.parseColor("#89a832"));
                } else if (dens.equals("Thưa")) {

                    nhathidau.setBackgroundColor(Color.parseColor("#a6a832"));

                } else if(dens.equals("Khá Đông")){
                    nhathidau.setBackgroundColor(Color.parseColor("#fcf038"));
                }
                else if(dens.equals("Đông")){
                    nhathidau.setBackgroundColor(Color.parseColor("#fcaa38"));
                }
                else if(dens.equals("Rất Đông")){
                    nhathidau.setBackgroundColor(Color.parseColor("#ff352b"));
                }else if(dens.equals("Loading")){
                    nhathidau.setBackgroundColor(Color.parseColor("#7a786f"));
                }
                SendNotification(dens,"Nhà Thi Đấu");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Button button1 = findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivityLoggin.this);
                alertDialogBuilder.setTitle("Nhà Thi đấu")
                        .setMessage("Hiện còn 200 chỗ");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });



        Button fhall = findViewById(R.id.button2);
        DatabaseReference myRef2 = database.getReference("Density/F_Hall");

        myRef2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String dens = (String) snapshot.getValue();

                if(dens.equals("Vắng")){

                    fhall.setBackgroundColor(Color.parseColor("#89a832"));
                } else if (dens.equals("Thưa")) {

                    fhall.setBackgroundColor(Color.parseColor("#a6a832"));

                } else if(dens.equals("Khá Đông")){
                    fhall.setBackgroundColor(Color.parseColor("#fcf038"));
                }
                else if(dens.equals("Đông")){
                    fhall.setBackgroundColor(Color.parseColor("#fcaa38"));
                }
                else if(dens.equals("Rất Đông")){
                    fhall.setBackgroundColor(Color.parseColor("#ff352b"));
                } else if(dens.equals("Loading")){
                    fhall.setBackgroundColor(Color.parseColor("#7a786f"));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildChanged:" + snapshot.getValue());
                String dens = (String) snapshot.getValue();

                if(dens.equals("Vắng")){

                    fhall.setBackgroundColor(Color.parseColor("#89a832"));
                } else if (dens.equals("Thưa")) {

                    fhall.setBackgroundColor(Color.parseColor("#a6a832"));

                } else if(dens.equals("Khá Đông")){
                    fhall.setBackgroundColor(Color.parseColor("#fcf038"));
                }
                else if(dens.equals("Đông")){
                    fhall.setBackgroundColor(Color.parseColor("#fcaa38"));
                }
                else if(dens.equals("Rất Đông")){
                    fhall.setBackgroundColor(Color.parseColor("#ff352b"));
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Button button2 = findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivityLoggin.this);
                alertDialogBuilder.setTitle("Tòa F")
                        .setMessage("Hiện còn 200 chỗ");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });


        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")){
                getNotificationPermission();
            }
        }

        ImageView qr_code = findViewById(R.id.qr_code);

        qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MapActivityLoggin.this,QrCodeParking.class);
                startActivity(i);
            }
        });

        ImageView help = findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.helpguid, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(MapActivityLoggin.this);
                alert.setView(alertLayout);
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                currentX = (int) event.getRawX();
                currentY = (int) event.getRawY();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                int x2 = (int) event.getRawX();
                int y2 = (int) event.getRawY();
                container.scrollBy(currentX - x2, currentY - y2);
                currentX = x2;
                currentY = y2;
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
        }
        super.dispatchTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // double tap fired.
            return true;
        }
    }

    public void SendNotification(String dens, String place) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
                .setSmallIcon(R.mipmap.ic_launcher) //icon
                .setContentTitle(place) //tittle
                .setAutoCancel(true)//swipe for delete
                .setContentText(dens); //content

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, builder.build());
    }
    public void getNotificationPermission(){
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // allow
                }  else {
                    //deny
                }
                return;

        }

    }

}