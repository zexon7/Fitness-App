package com.example.fitnessapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView name;
    TextView email;
    TextView gender;
    TextView age;
    TextView height;
    TextView weight;
    private CardView btn_name;
    Button btn_logout;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    private static final String TAG = ProfileFragment.class.getName();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        db.collection("users").document(currentUser.getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }

                if (snapshot.exists()){
                    String user_name=snapshot.getString("Name");
                    String user_email=snapshot.getString("Email");
                    String user_gender=snapshot.getString("Gender");
                    int user_age= snapshot.getLong("Age").intValue();
                    int user_height=snapshot.getLong("Height").intValue();
                    int user_weight=snapshot.getLong("Weight").intValue();

                    name.setText(user_name);
                    email.setText(user_email);
                    gender.setText(user_gender);
                    age.setText(""+user_age);
                    height.setText(""+user_height);
                    weight.setText(""+user_weight);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            Intent intent = new Intent(view.getContext(), WelcomeActivity.class);
            startActivity(intent);
        }

        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        gender = view.findViewById(R.id.profile_gender);
        age = view.findViewById(R.id.profile_age);
        height = view.findViewById(R.id.profile_height);
        weight = view.findViewById(R.id.profile_weight);
        btn_name = view.findViewById(R.id.name);
        btn_logout = view.findViewById(R.id.p_logout);

        db.collection("users").document(currentUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        String user_name=documentSnapshot.getString("Name");
                        String user_email=documentSnapshot.getString("Email");
                        String user_gender=documentSnapshot.getString("Gender");
                        int user_age= documentSnapshot.getLong("Age").intValue();
                        int user_height=documentSnapshot.getLong("Height").intValue();
                        int user_weight=documentSnapshot.getLong("Weight").intValue();

                        name.setText(user_name);
                        email.setText(user_email);
                        gender.setText(user_gender);
                        age.setText(""+user_age);
                        height.setText(""+user_height);
                        weight.setText(""+user_weight);
                    }else{
                        Log.d(TAG, "No such document");
                    }
                }else{Log.d(TAG, "get failed with ", task.getException());}
            }

        });

        btn_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                AlertDialog.Builder editDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                editDialog.setTitle("Enter Your Name");

                final EditText editText = new EditText(getContext());
                editText.setText(name.getText());
                editDialog.setView(editText);

                editDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        String chg_name = editText.getText().toString();
                        db.collection("users").document(currentUser.getEmail()).update("Name",chg_name);
                    }
                });

                editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        //...
                    }
                });

                editDialog.show();

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(view.getContext(), "Logged Out.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), WelcomeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
