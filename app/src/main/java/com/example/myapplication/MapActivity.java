package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MapActivity extends AppCompatActivity {
    private RelativeLayout container;
    private int currentX;
    private int currentY;
    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    GestureDetector gestureDetector;
    private Button nhathidau;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        container =findViewById(R.id.Container);
        nhathidau = findViewById(R.id.button1);

        gestureDetector = new GestureDetector(this, new GestureListener());

// animation for scalling
        mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener()
        {
            @Override
            public boolean onScale(ScaleGestureDetector detector)
            {
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
                ScrollView layout =(ScrollView) findViewById(R.id.scrollViewZoom);
                layout.startAnimation(scaleAnimation);
                return true;
            }
        });

        //F_Hall
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
                }else if(dens.equals("Loading")){
                    nhathidau.setBackgroundColor(Color.parseColor("#7a786f"));
                }
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
                }else if(dens.equals("Loading")){
                    nhathidau.setBackgroundColor(Color.parseColor("#7a786f"));
                }
                else{
                    nhathidau.setBackgroundColor(Color.parseColor("#ff352b"));
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

        Button button1 = findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
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
                else if(dens.equals("Loading")){
                    fhall.setBackgroundColor(Color.parseColor("#7a786f"));
                }
                else{
                    fhall.setBackgroundColor(Color.parseColor("#ff352b"));
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
                else if(dens.equals("Loading")){
                    fhall.setBackgroundColor(Color.parseColor("#7a786f"));
                }
                else{
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
                alertDialogBuilder.setTitle("Tòa F")
                        .setMessage("Hiện còn 200 chỗ");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });


        ImageView help = findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.helpguid, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(MapActivity.this);
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

//step 4: add private class GestureListener

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


}
