package com.example.fitnessapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.shrikanthravi.collapsiblecalendarview.data.Day;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeAdapter extends FirestoreRecyclerAdapter<Record, HomeAdapter.HomeHolder> {

    private static final String TAG = "MainActivity";
    final List<String> keys = new ArrayList<>();
    final List<Long> values = new ArrayList<>();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public HomeAdapter(@NonNull FirestoreRecyclerOptions<Record> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final HomeHolder holder, final int position, @NonNull final Record model) {

        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dbRef = db.collection("records").document("1@mail.com20200112");
        dbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final Map<String, Object> map = task.getResult().getData();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        keys.add(entry.getKey());
                        String stringtoConvert = String.valueOf(entry.getValue());
                        values.add(Long.parseLong(stringtoConvert));
                        holder.food.setText(entry.getKey());
                        holder.calories.setText(String.valueOf(entry.getValue()));
                        Log.d(TAG, " " + task.getResult().getData());
                    }
                    for (String key : keys){
                        holder.food.setText(key);
                        Log.i("Key: ", key);
                    }
                    for (Long value : values){
                        holder.calories.setText(value.toString());
                        Log.i("Value: ", value.toString());
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });*/

        holder.food.setText(model.getFName());
        holder.calories.setText(String.valueOf(model.getFCal()));
        /*holder.btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder editDialog = new AlertDialog.Builder(view.getContext(), R.style.AlertDialogTheme);
                editDialog.setTitle("Remove");
                editDialog.setMessage("Remove this data?");

                editDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        itemList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,itemList.size());
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
        });*/
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new HomeHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class HomeHolder extends RecyclerView.ViewHolder{
        TextView food, calories;
        CardView btn_card;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            food = itemView.findViewById(R.id.item_food);
            calories = itemView.findViewById(R.id.item_calories);
            btn_card = itemView.findViewById(R.id.item_card);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    /*@Override
    public int getItemCount() {
        return itemList.size();
    }*/
}
