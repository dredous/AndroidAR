package com.example.edwinkurniawan.cameraapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MainActivity extends Activity {

    private static final int PICK_IMAGE = 1338;

    private Button captureImg;
    private Button uploadImg;
    private Button connectBtn;
    private JSONObject imgObj;
//    public Socket socket;
//    {
//        try {
//            socket = IO.socket("http://10.0.1.32:3000");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        socket.connect();
        captureImg = (Button) findViewById(R.id.captureImg);
        uploadImg = (Button) findViewById(R.id.uploadImg);
        connectBtn = (Button) findViewById(R.id.connectBtn);

        captureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(cameraIntent, 0);
            }
        });

//        uploadImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, PICK_IMAGE);
//            }
//        });

//        connectBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                socket.emit("Test", "HELLO WORLD");
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        socket.disconnect();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch(requestCode) {
//            case PICK_IMAGE:
//                if(resultCode == Activity.RESULT_OK && null != data) {
//                    Uri selectedImageUri = data.getData();
//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                    Cursor cursor = getContentResolver().query(selectedImageUri,filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String picturePath = cursor.getString(columnIndex);
//                    cursor.close();
//                    if(picturePath!= null) {
//                        Bitmap bm = BitmapFactory.decodeFile(picturePath);
//                        Bitmap resizedBm = Bitmap.createScaledBitmap(bm, 200, 150, false);
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        resizedBm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                        byte[] b = baos.toByteArray();
//                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//                        sendImage(encodedImage);
//                    } else {
//                        Log.i("Picture Path", "NULL");
//                    }
//                }
//        }
//    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }

//    public void sendImage(String encodedImg) {
//        imgObj = new JSONObject();
//        try {
//            imgObj.put("image", true);
//            imgObj.put("buffer", encodedImg);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        socket.emit("Image", imgObj);
//    }
}
