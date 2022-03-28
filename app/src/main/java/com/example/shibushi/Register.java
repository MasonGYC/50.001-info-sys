package com.example.shibushi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity implements View.OnClickListener{
    Button bRegister;
    EditText etEmailAddress, etPassword, etUsername;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bRegister = findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);

        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        progressBar = findViewById(R.id.progress_circular);

        //Firebase authentication object
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            //Switch statement used in case more buttons are required on the Register page.
            case R.id.bRegister:
                registerUser();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    // TODO: 3/15/2022 refactor into a new java file
    private void registerUser() {
        String email = etEmailAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String username = etUsername.getText().toString().trim();

        //Email validation
        if(email.isEmpty()){
            etEmailAddress.setError("Email address is required!");
            etEmailAddress.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmailAddress.setError("Please enter a valid email address!");
            etEmailAddress.requestFocus();
            return;
        }
        //Username validation
        if (username.isEmpty()){
            etUsername.setError("Username is required!");
            etUsername.requestFocus();
            return;
        }
        //Password validation
        if (password.isEmpty()){
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            etPassword.setError("Min password length is 6 characters!");
        }
        //Todo check Password strength

        progressBar.setVisibility(View.VISIBLE);
        //Todo Add username to user profile during registration.

        // Add the user to Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });

                            //Redirect to Login activity
                            startActivity(new Intent(Register.this, Login.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        progressBar.setVisibility(View.GONE);
    }

}
/*This is the Register page where new users can sign up.
* If they provide invalid details, they will stay on this page.
* If details are valid they will be registered and sent back to the Login page instantly.
* */

//Todo May change toasts to snackbar
