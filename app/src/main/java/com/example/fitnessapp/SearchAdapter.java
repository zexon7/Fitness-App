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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class SearchAdapter extends FirestoreRecyclerAdapter<Item, SearchAdapter.SearchHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public SearchAdapter(@NonNull FirestoreRecyclerOptions<Item> options) {
        super(options);
    }

    ArrayList<Item> itemList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Context context;

    @Override
    protected void onBindViewHolder(@NonNull SearchHolder holder, int position, @NonNull final Item model) {
        /*Item currentItem = itemList.get(position);*/
        holder.food.setText(model.getFName());
        holder.calories.setText(String.valueOf(model.getFCal()));
        holder.btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder editDialog = new AlertDialog.Builder(view.getContext(), R.style.AlertDialogTheme);
                editDialog.setTitle("Add");
                editDialog.setMessage("Add this food to your list?");

                editDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        final FirebaseFirestore db = FirebaseFirestore.getInstance();
                        final FirebaseUser currentUser = mAuth.getCurrentUser();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMdd", Locale.getDefault());
                        String today = sdf.format(new Date());

                        Map<String, Object> eaten = new HashMap<>();
                        eaten.put("User", currentUser.getEmail());
                        eaten.put("Date", today);
                        eaten.put("FName", model.getFName());
                        eaten.put("FCal", model.getFCal());
                        db.collection("records").document().set(eaten);
                        /*eaten.put(model.getFName(), model.getFCal());*/
                        /*db.collection("records").document(currentUser.getEmail() + today).set(eaten, SetOptions.merge());*/
                        view.getContext().startActivity(new Intent(view.getContext(), MainActivity.class));
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
        });
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new SearchHolder(v);
    }

    class SearchHolder extends RecyclerView.ViewHolder{
        TextView food, calories;
        CardView btn_card;

        public SearchHolder(@NonNull View itemView) {
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
