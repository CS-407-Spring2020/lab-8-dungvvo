package com.uw.lab8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(null);
    }

    public void upload(View view) {

        try {
            // Create reference //
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference badgerRef = storageRef.child("images/badger.jpg");

            // Convert image to byte stream //
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.badger);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] badgerByteStream = baos.toByteArray();


            UploadTask uploadTask = badgerRef.putBytes(badgerByteStream);
            uploadTask.addOnFailureListener((exception) -> {
                //Handle unsuccessful uploads
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("ImageUpload", "Image successfully uploaded to Firebase");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Error", "Image upload failed");
        }
    }

    public void download(View view) {
        // Create reference //
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference badgerRef = storageReference.child("images/badger.jpg");

        // Get Imageview object //
        final ImageView imageView = findViewById(R.id.imageView);
        final long ONE_MEGABYTE = 1024 * 1024;

        // Download the image into a byte stream //
        badgerRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for the badger.jpg is returned //
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                // Set the image in imageView //
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Log.i("Error", "Image Download failed");
            }
        });

    }
}
