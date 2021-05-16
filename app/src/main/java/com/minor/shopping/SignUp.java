package com.minor.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    EditText editTextMail, editTextPass, editTextCnfrm;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextMail = findViewById(R.id.editTextEmailSignUp);
        editTextPass = findViewById(R.id.editTextPassSignUp);
        editTextCnfrm = findViewById(R.id.editTextCmPassSignUp);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBarSignUp);


        //to goto login screen
        findViewById(R.id.loginTop).setOnClickListener(v -> gotoLogin());
        findViewById(R.id.loginDown).setOnClickListener(v -> gotoLogin());

        //signup
        findViewById(R.id.sign_up_button).setOnClickListener(v -> signUp());
    }

    private void signUp() {
        Validate validate = Validate.getInstance();
        String email = editTextMail.getText().toString().trim();
        String pass = editTextPass.getText().toString().trim();
        String confm = editTextCnfrm.getText().toString().trim();

        if(!validate.email(email)){
            editTextMail.setError("Please enter valid email id");
            editTextMail.requestFocus();
            return;
        }
        if(!validate.password(pass)){
            editTextPass.setError("Enter valid password");
            editTextPass.requestFocus();
            return;
        }
        if(!pass.equals(confm)){
            editTextCnfrm.setError("Password do not match");
            editTextCnfrm.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Registration successful!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Chooser.class);
                    finish();
                    startActivity(intent);
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        editTextMail.setError("Email already in use");
                        return;
                    }else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void gotoLogin() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        finish();
        startActivity(intent);
    }

}