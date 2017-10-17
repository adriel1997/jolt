package com.example.anantmehra.jolt;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class step3 extends AppCompatActivity {

    ImageView upload_image;
    Integer REQUEST_CAMERA = 1, select_file = 0;

    String jill,jack;


    private StorageReference storageReference;

    private FirebaseAuth firebaseAuth;

    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);

        Button step3 = (Button) findViewById(R.id.button5);
        upload_image = (ImageView) findViewById(R.id.imagecollege);
        Button upload = (Button) findViewById(R.id.upload);

        storageReference = FirebaseStorage.getInstance().getReference();

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        jill=user.getUid();
        jack =user.getEmail();



        step3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFile();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_image();
            }
        });

    }

    // to upload image and create builders in an app

    private void select_image() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(step3.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface DialogInterface, int which) {
                if (items[which].equals("Camera")) {

                    Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    if (ActivityCompat.checkSelfPermission(step3.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Please grant permission to access Camera", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(step3.this, new String[]{Manifest.permission.CAMERA}, 1);
                        startActivityForResult(camera,REQUEST_CAMERA);

                    } else {
                        startActivityForResult(camera,REQUEST_CAMERA);

                    }


                   // if(camera.resolveActivity(getPackageManager())!=null) {



                 }



                 else if (items[which].equals("Gallery")) {

                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    gallery.setType("image/*");
                    startActivityForResult(gallery, select_file);


                } else if (items[which].equals("Cancel")) {

                    DialogInterface.dismiss();


                }
            }


        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                // Bundle bundle = data.getExtras();
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                upload_image.setImageBitmap(bmp);

            }


             else if (requestCode == select_file) {

                selectedImageUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    upload_image.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                upload_image.setImageURI(selectedImageUri);


            }


        }


    }
    private void uploadFile() {
        //if there is a file to upload
        if (selectedImageUri != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = storageReference.child("Images/CollegeID"+jill+"/"+jack);
            riversRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            Intent step3 = new Intent(step3.this, step4.class);
                            startActivity(step3);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            @SuppressWarnings("VisibleForTests") double re=taskSnapshot.getBytesTransferred();
                            @SuppressWarnings("VisibleForTests") double rem=taskSnapshot.getBytesTransferred();
                            double progress = (100.0 * re) / rem;

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

}



