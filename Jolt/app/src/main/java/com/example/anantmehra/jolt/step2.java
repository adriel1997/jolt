package com.example.anantmehra.jolt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class step2 extends AppCompatActivity {

    String email,pass,confirmp;
    EditText email_id,password,confirm_password;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step2);

        Button step2 = (Button) findViewById(R.id.button4);
        email_id = (EditText) findViewById(R.id.edit7);
        password = (EditText) findViewById(R.id.edit8);
        confirm_password = (EditText) findViewById(R.id.edit9);


        step2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();

                // Intent step2=new Intent(v.getContext(),step3.class);
                //startActivity(step2);
            }
        });
    }

    public void register() {
        initialize();
        if(!validate()) {
            Toast.makeText(this,"Please fill the required fields",Toast.LENGTH_SHORT).show();

        }
        else{


            registerUser();
            //Intent step1=new Intent(step2.this,step1.class);
            //startActivity(step1);

        }

    }

    public void initialize() {
        email=email_id.getText().toString().trim();
        pass=password.getText().toString().trim();
        confirmp=confirm_password.getText().toString().trim();

    }

    public boolean validate() {
        boolean valid=true;
        if(email.isEmpty() ||!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_id.setError("Enter a valid email id");
            valid=false;

        }

        if(pass.isEmpty() || pass.length()!=8) {

            password.setError("Min. Length should be 8 characters");
            valid = false;


        }

        if(confirmp.isEmpty() || (!pass.equals(confirmp))) {

            confirm_password.setError("Passwords does not match");
            valid = false;


        }


        return valid;

    }

    private void registerUser(){

        //getting email and password from edit texts
        String email = email_id.getText().toString().trim();
        String passwords  = password.getText().toString().trim();



        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, passwords)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), step1.class));
                        }
                        else{
                            //display some message here
                            Toast.makeText(step2.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

}
