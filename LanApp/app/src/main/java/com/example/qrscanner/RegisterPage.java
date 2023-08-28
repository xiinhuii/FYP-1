package com.example.qrscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.qrscanner.databinding.ActivityMainBinding;
import com.example.qrscanner.databinding.ActivityRegisterPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {


    ActivityRegisterPageBinding binding;
    String emailDb, usernameDb, passwordDb;

    float balanceDb;
    FirebaseDatabase fdb;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    FirebaseFirestore firebaseFirestore;
    ProgressBar progressBar;


    EditText email, username, password;
    Button loginButton, registerButton, viewButton;

    DatabaseAccount db;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();



        email = (EditText) findViewById(R.id.emailTextR);
        username = (EditText) findViewById(R.id.userTextR);
        password = (EditText) findViewById(R.id.passwordTextR);

        //Search through resources based on ID
        loginButton = (Button) findViewById(R.id.LoginBtnR);
        registerButton = (Button) findViewById(R.id.registerBtnR);

        progressBar = (ProgressBar)findViewById(R.id.progressBarR);


        /*
        emailDb = String.valueOf(binding.emailTextR.getText());
        usernameDb = String.valueOf(binding.userTextR.getText());
        passwordDb = String.valueOf(binding.passwordTextR.getText());
        */
        db = new DatabaseAccount(this);

        //Go back to login page
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        RegisterPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        //AUTHENTICATION DATABASE
        binding.registerBtnR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view)
            {
                progressBar.setVisibility(View.VISIBLE);
                emailDb = String.valueOf(email.getText());
                usernameDb = String.valueOf(username.getText());
                passwordDb = String.valueOf(password.getText());

                System.out.println("Email: " + emailDb);
                if (TextUtils.isEmpty(emailDb)){
                    Toast.makeText(RegisterPage.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(usernameDb)){
                    Toast.makeText(RegisterPage.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passwordDb)){
                    Toast.makeText(RegisterPage.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }




                mAuth.createUserWithEmailAndPassword(emailDb, passwordDb)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    userid = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = firebaseFirestore.collection("user").document(userid);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("email", emailDb);
                                    user.put("balance", 0);
                                    user.put("points", 0);
                                    documentReference.set(user);


                                    email.setText("");
                                    username.setText("");
                                    password.setText("");

                                    Toast.makeText(RegisterPage.this, "Account created.", Toast.LENGTH_SHORT).show();
                                }
                                    else
                                {
                                    Toast.makeText(RegisterPage.this, task.getException().getLocalizedMessage()
                                                                            , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                /*
                //FIRESTORE
                firebaseFirestore.collection("user")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                email.setText("");
                                username.setText("");
                                password.setText("");
                                Toast.makeText(RegisterPage.this, "Successful", Toast.LENGTH_SHORT).show();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterPage.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                */

                /*
                if (!emailDb.isEmpty() && !usernameDb.isEmpty())
                {
                    Users users = new Users(emailDb, usernameDb, 50);
                    fdb = FirebaseDatabase.getInstance();
                    reference = fdb.getReference("Users");
                    reference.child(emailDb).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            binding.emailTextR.setText("");
                            binding.userTextR.setText("");

                            Toast.makeText(RegisterPage.this, "Success!!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }*/
            }
        });
        /* //NORMAL DATABASE
        //Register account
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(register.this, "Account registered successfully",Toast.LENGTH_LONG).show();
                String emailAddress = email.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(emailAddress.equals("") || user.equals("") || pass.equals(""))
                {
                    Toast.makeText(RegisterPage.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Boolean checkUser = db.checkUsername(user);
                    if (checkUser == false)
                    {
                        Boolean insert = db.insertData(emailAddress, user, pass);
                        if(insert == true)
                        {


                            Toast.makeText(RegisterPage.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(
                                    RegisterPage.this, LoginPage.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(RegisterPage.this, "Registration failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(RegisterPage.this, "User already exists! Please sign in.",  Toast.LENGTH_SHORT).show();

                    }
                }



            }
        });*/

    }


}