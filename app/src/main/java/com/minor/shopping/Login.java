package com.minor.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText editTextMail, editTextPassword;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.loginButton).setOnClickListener(v -> userLogin());

        //initialize
        editTextMail = findViewById(R.id.editTextLoginEmail);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);

        //Goto Signup
        findViewById(R.id.signUpTop).setOnClickListener(v -> gotoSignup());
        findViewById(R.id.signUpDown).setOnClickListener(v -> gotoSignup());
        findViewById(R.id.textViewForgotPassLogin).setOnClickListener(v -> gotoForgotPass(v));
    }

    private void gotoForgotPass(View v) {
        EditText resetMail = new EditText(v.getContext());
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
        passwordResetDialog.setTitle("Reset Password");
        passwordResetDialog.setMessage("Enter Email to Receive Reset Link");
        passwordResetDialog.setView(resetMail);

        passwordResetDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //extract email and send
                String email = resetMail.getText().toString().trim();
                mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Login.this, "Check Email to reset password", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Error!, "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        passwordResetDialog.create().show();
    }


    private void gotoSignup() {
        Intent intent = new Intent(getApplicationContext(),SignUp.class);
        finish();
        startActivity(intent);
    }

    private void userLogin() {
        String email = editTextMail.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();
        Validate validate = Validate.getInstance();
        if(!validate.email(email)){
            editTextMail.setError("Enter Valid email");
            editTextMail.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    String name = user.getDisplayName();
                    Intent intent;
                    if(name == null){
                        intent = new Intent(getApplicationContext(), Chooser.class);
                    }else{
                        intent = new Intent(getApplicationContext(),MainActivity.class);
                    }
                    finish();
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}