package com.example.qrscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.qrscanner.databinding.ActivityLoginPageBinding;
import com.example.qrscanner.databinding.ActivityRegisterPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPage extends AppCompatActivity {

    ActivityLoginPageBinding binding;

    String emailDb, usernameDb, passwordDb;
    FirebaseAuth mAuth;

    ProgressBar progressBar;



    EditText email, password;
    Button registerBtn, loginBtn;

    DatabaseAccount db;

    FirebaseFirestore firebaseFirestore;
    String userid;

    //Shared preferences (USERID)
    public static final String SHARED_PREFS = "sharedID";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();



        email = (EditText) findViewById(R.id.emailTextL);
        password = (EditText) findViewById(R.id.passwordTextL);
        loginBtn = (Button) findViewById(R.id.LoginBtnL);

        progressBar = (ProgressBar)findViewById(R.id.progressBarL);


        db = new DatabaseAccount(this);

        //Search through resources based on ID
        registerBtn = (Button) findViewById(R.id.RegisterBtnL);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });

        Button loginBtn = (Button) findViewById(R.id.LoginBtnL);

        //AUTHENTICATION
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view)
            {
                progressBar.setVisibility(View.VISIBLE);
                emailDb = String.valueOf(email.getText());
                //usernameDb = String.valueOf(binding.userTextL.getText());
                passwordDb = String.valueOf(password.getText());


                if (TextUtils.isEmpty(emailDb)){
                    Toast.makeText(LoginPage.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passwordDb)){
                    Toast.makeText(LoginPage.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(emailDb, passwordDb)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    userid = mAuth.getCurrentUser().getUid();
                                    Toast.makeText(LoginPage.this, "Login Successful.", Toast.LENGTH_SHORT).show();

                                    SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();

                                    editor.putString("USERID", userid);
                                    editor.apply();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(LoginPage.this, "Authentication failed.",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
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

        /*
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("") || pass.equals(""))
                    Toast.makeText(LoginPage.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else
                {
                    Boolean checkUserPassword = db.checkUsernamePassword(user, pass);

                    if (checkUserPassword==true)
                    {
                        Toast.makeText(LoginPage.this, "Login successsfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(
                                LoginPage.this, MainActivity.class);
                        //intent.putExtra("currentUser", user);

                        //SharedPreferences mypref = getSharedPreferences(SHARED_PREFS_USER, MODE_PRIVATE);
                        //currentUser = mypref.getString("currentUser", "User");

                        //SharedPreferences.Editor editor = mypref.edit();
                        //editor.putString("currentUser", user);
                        //editor.commit();
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(LoginPage.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
           */
    }
}