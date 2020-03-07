package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText name;
    EditText email;
    EditText password;
    EditText cpassword;
    EditText age;
    EditText height;
    EditText weight;
    RadioGroup gender;
    RadioButton btn_gender;
    Button btn_signUp;
    FirebaseFirestore db;
    private static final String TAG = RegisterActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        cpassword = findViewById(R.id.signup_cpassword);
        age = findViewById(R.id.signup_age);
        height = findViewById(R.id.signup_height);
        weight = findViewById(R.id.signup_weight);
        gender = findViewById(R.id.rg_gender);

        btn_signUp = findViewById(R.id.s_signup);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String signup_name = name.getText().toString().trim();
                final String signup_email = email.getText().toString().trim();
                String signup_password = password.getText().toString().trim();
                String signup_cpassword = cpassword.getText().toString().trim();
                final int signup_age = Integer.parseInt(age.getText().toString().trim());
                final int signup_height = Integer.parseInt(height.getText().toString().trim());
                final int signup_weight = Integer.parseInt(weight.getText().toString().trim());
                int radioId = gender.getCheckedRadioButtonId();
                btn_gender = findViewById(radioId);
                final String signup_gender = btn_gender.getText().toString().trim();

                if(signup_email.isEmpty()){
                    email.setError("Email is Required.");
                    email.requestFocus();
                    return;
                }
                if(signup_password.isEmpty() || signup_cpassword.isEmpty()){
                    password.setError("Password is Required.");
                    password.requestFocus();
                    return;
                }
                if(signup_password.length()<6){
                    cpassword.setError("Password should be at least 6 characters.");
                    cpassword.requestFocus();
                    return;
                }
                if(!signup_password.equals(signup_cpassword)){
                    cpassword.setError("Password not match.");
                    cpassword.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(signup_email, signup_password)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG,"signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(RegisterActivity.this, "Succefully Registered.",
                                            Toast.LENGTH_SHORT).show();

                                    Map<String, Object> userdata = new HashMap<>();
                                    userdata.put("Name",signup_name);
                                    userdata.put("Email", signup_email);
                                    userdata.put("Gender", signup_gender);
                                    userdata.put("Age", signup_age);
                                    userdata.put("Weight", signup_weight);
                                    userdata.put("Height", signup_height);
                                    db.collection("users").document(signup_email).set(userdata);

                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    if(task.getException() instanceof FirebaseAuthInvalidUserException){
                                        Toast.makeText(RegisterActivity.this, "Invalid email.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        });


            }
        });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
