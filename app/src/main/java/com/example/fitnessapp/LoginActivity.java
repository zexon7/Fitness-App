package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button btn_login;
    FirebaseAuth mAuth;
    private static final String TAG = LoginActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        btn_login = findViewById(R.id.l_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_email = email.getText().toString().trim();
                String login_password = password.getText().toString().trim();

                if(login_email.isEmpty()){
                    email.setError("Email is Required.");
                    email.requestFocus();
                    return;
                }

                if(login_password.isEmpty()){
                    password.setError("Password is Required.");
                    password.requestFocus();
                    return;
                }

                if(login_password.length()<6){
                    password.setError("Password is Required.");
                    password.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(login_email, login_password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(LoginActivity.this, "Login Successful.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                        Toast.makeText(LoginActivity.this, "Invalid Password.",
                                                Toast.LENGTH_SHORT).show();
                                    }else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                                        Toast.makeText(LoginActivity.this, "Invalid User.",
                                                Toast.LENGTH_SHORT).show();

                                        AlertDialog.Builder editDialog = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogTheme);
                                        editDialog.setTitle("Account invalid");
                                        editDialog.setMessage("Do you want to create a new account?");

                                        editDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            // do something when the button is clicked
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        });

                                        editDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            // do something when the button is clicked
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                //...
                                            }
                                        });

                                        editDialog.show();
                                    }

                                }

                                // ...
                            }
                        });
            }
        });

    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}