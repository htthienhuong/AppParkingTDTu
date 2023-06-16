package com.example.myapplication;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QrCodeParking extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 112;
    ImageView qrcode;
    private static final int REQUEST_CODE =1;
    androidx.appcompat.widget.AppCompatButton download;
    public static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_parking);

        ActivityCompat.requestPermissions(QrCodeParking.this, new String[]{WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(QrCodeParking.this, new String[]{READ_EXTERNAL_STORAGE},1);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String mssv = acct.getEmail();
            mssv = mssv.split("@")[0];
            String encodeMssv = decryptData(mssv,4);
            qrcode = findViewById(R.id.qr_code);
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

            try{
                BitMatrix bitMatrix = multiFormatWriter.encode(encodeMssv,BarcodeFormat.QR_CODE,600,700);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                qrcode.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        download = findViewById(R.id.btndownload);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToGallery();

            }
        });

        }

    private void saveToGallery(){
        BitmapDrawable bitmapDrawable=(BitmapDrawable) qrcode.getDrawable();
        Bitmap bitmap=bitmapDrawable.getBitmap();

        FileOutputStream fileOutputStream=null;

        File sdCard = Environment.getExternalStorageDirectory();
        File Directory=new File(sdCard.getAbsolutePath()+ "/Download");
        Directory.mkdir();

        String filename=String.format("%d.jpg",System.currentTimeMillis());
        File outfile=new File(Directory,filename);

        Toast.makeText(QrCodeParking.this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();

        try {
            fileOutputStream=new FileOutputStream(outfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outfile));
            sendBroadcast(intent);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==1 && resultCode == RESULT_OK && null !=data){
            Uri selectedImage = data.getData();
            String[] filepath={MediaStore.Images.Media.DATA};

            Cursor cursor=getContentResolver().query(selectedImage,filepath,null,null,null);
            cursor.moveToFirst();
            int columneIndex=cursor.getColumnIndex(filepath[0]);
            String picturepath =cursor.getString(columneIndex);
            cursor.close();

            qrcode.setImageBitmap(BitmapFactory.decodeFile(picturepath));
            String filename=picturepath.substring(picturepath.lastIndexOf("/")+1);
//            textView.setText(filename);

        }
    }
    public static String decryptData(String inputStr, int shiftKey)
    {
        // convert inputStr into lower case
        inputStr = inputStr.toLowerCase();

        // decryptStr to store decrypted data
        String decryptStr = "";

        // use for loop for traversing each character of the input string
        for (int i = 0; i < inputStr.length(); i++)
        {
            // get position of each character of inputStr in ALPHABET
            int pos = ALPHABET.indexOf(inputStr.charAt(i));

            // get decrypted char for each char of inputStr
            int decryptPos = (pos - shiftKey) % 26;

            // if decryptPos is negative
            if (decryptPos < 0){
                decryptPos = ALPHABET.length() + decryptPos;
            }
            char decryptChar = ALPHABET.charAt(decryptPos);

            // add decrypted char to decrypted string
            decryptStr += decryptChar;
        }
        // return decrypted string
        return decryptStr;
    }



}