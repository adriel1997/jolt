package com.example.anantmehra.jolt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.q42.android.scrollingimageview.ScrollingImageView;

public class LoginActivity extends AppCompatActivity {

    String email;
    EditText email_id;
    EditText passwords;

    ProgressDialog progressDialog = new ProgressDialog(this);

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), book.class));
        }


        ScrollingImageView scrollingBackground = (ScrollingImageView)findViewById(R.id.scrolling_background);

        scrollingBackground.start();


        Button get_started=(Button)findViewById(R.id.button2);
        Button login=(Button)findViewById(R.id.button1);
        email_id=(EditText)findViewById(R.id.editText1);
        passwords=(EditText)findViewById(R.id.editText2);








        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //register();
                userLogin();

                Intent info1=new Intent(v.getContext(),book.class);
                startActivity(info1);
            }
        });


        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent info1=new Intent(v.getContext(),step2.class);
            }
        });




            }

    public void register() {
        initialize();
        if(!validate()) {
            Toast.makeText(this,"Please fill the required fields",Toast.LENGTH_SHORT).show();

        }
        else{
            Intent step1=new Intent(LoginActivity.this,step2.class);
            startActivity(step1);

        }

    }
    public void initialize() {
        email = email_id.getText().toString().trim();
    }


    public boolean validate() {
        boolean valid=true;
        if(email.isEmpty() ||!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_id.setError("Enter a valid email id");
            valid=false;

        }

        return valid;

    }



    private void userLogin(){
        String email = email_id.getText().toString().trim();
        String password  = passwords.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Checking Please Wait...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), book.class));
                        }
                    }
                });

    }


}
