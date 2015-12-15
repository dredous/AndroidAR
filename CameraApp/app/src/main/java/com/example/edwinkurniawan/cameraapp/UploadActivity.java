package com.example.edwinkurniawan.cameraapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class UploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1338;
    private EditText inputText;
    private Button galleryButton, sendButton;
    private ImageView imageView;
    private JSONObject imgObj;
    private String encString = "";
    private String ARText = "";
    public Socket socket;
    {
        try {
            socket = IO.socket("http://10.0.1.32:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_upload);
        socket.connect();
        inputText = (EditText) findViewById(R.id.ARText);
        galleryButton = (Button) findViewById(R.id.glrBtn);
        sendButton = (Button) findViewById(R.id.uplBtn);
        imageView = (ImageView) findViewById(R.id.imgView);

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImage(encString);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case PICK_IMAGE:
                if(resultCode == Activity.RESULT_OK && null != data) {
                    Uri selectedImageUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImageUri,filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    if(picturePath!= null) {
                        Bitmap bm = BitmapFactory.decodeFile(picturePath);
                        Bitmap resizedBm = Bitmap.createScaledBitmap(bm, 200, 150, false);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resizedBm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        encString = encodedImage;
                        imageView.setImageBitmap(bm);
//                        sendImage(encodedImage);
                    } else {
                        Log.i("Picture Path", "NULL");
                    }
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    public void sendImage(String encodedImg) {
        ARText = inputText.getText().toString();
        imgObj = new JSONObject();
        try {
            imgObj.put("image", true);
            imgObj.put("buffer", encodedImg);
            imgObj.put("text", ARText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("Image", imgObj);
    }

}
