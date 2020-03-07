package com.example.fitnessapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private CardView btn_addfood;
    private CardView item_food;
    ArrayList<Item> itemList = new ArrayList();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView food;
    private TextView text_cremaining;
    private TextView text_cconsumed;
    int calories_remaining;
    int calories_consumed;
    String user_email;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    HomeAdapter adapter;
    String sd;
    private static final String TAG = HomeFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String user = currentUser.toString();

        final CollapsibleCalendar viewCalendar = view.findViewById(R.id.calendarView);
        viewCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = viewCalendar.getSelectedDay();
                /*sd = "" + day.getYear() + (day.getMonth()+1) + day.getDay();*/
                String a = String.valueOf(day.getYear());
                String b = String.valueOf(day.getMonth()+1);
                String c = String.valueOf(day.getDay());
                sd = a+b+c;
                datechangeRecyclerView(view, sd, user);
                Log.i(getClass().getName(), "Selected Day: " + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());
                Toast.makeText(viewCalendar.getContext(), sd, Toast.LENGTH_LONG).show();
                Toast.makeText(viewCalendar.getContext(), "Selected Day: " + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemClick(@NotNull  View view) {

            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {

            }

            @Override
            public void onClickListener() {

            }

            @Override
            public void onDayChanged() {

            }
        });

        setUpRecyclerView(view);

       /*recyclerView = view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(itemList);
        recyclerView.setAdapter(mAdapter);*/

        btn_addfood = view.findViewById(R.id.btn_addfood);

        //user data
        calories_remaining = 2400;
        calories_consumed = 0;

        btn_addfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                startActivity(intent);
                /*LayoutInflater inflater = getLayoutInflater();
                View v1 = inflater.inflate(R.layout.item, null);
                food = v1.findViewById(R.id.item_food);
                final EditText editText = new EditText(getContext());
                editText.setText(food.getText());
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                AlertDialog.Builder editDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                editDialog.setTitle("Enter Your Item");

                editDialog.setView(editText);
                editDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        int pos = 0;
                        food.setText(editText.getText().toString());
                        insertItem(pos,food);
                        // check if calories remaining < 0
                        String check = text_cremaining.getText().toString();
                        int c = Integer.parseInt(check);
                        if(c <= 0){
                            calories_remaining = 0;
                            text_cremaining.setText(Integer.toString(calories_remaining));
                        }
                }
                });

                editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        //...
                    }
                });

                editDialog.show();*/

            }
        });

        text_cremaining = view.findViewById(R.id.diary_cr);
        text_cconsumed = view.findViewById(R.id.diary_cc);

        return view;

    }

    /*public void insertItem(int position,TextView f){
        String a = f.getText().toString();
        int b = (int)(Math.random()*999);
        calories_remaining -=b;
        calories_consumed +=b;
        text_cremaining.setText(Integer.toString(calories_remaining));
        text_cconsumed.setText(Integer.toString(calories_consumed));
        itemList.add(position,new Item(a,b));
        mAdapter.notifyItemChanged(position);
        mAdapter.notifyItemInserted(position);
        mAdapter.notifyItemRangeChanged(position,itemList.size());
        recyclerView.scrollToPosition(position);
    }*/

    private void datechangeRecyclerView(View v, String selected_date, String user) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMdd", Locale.getDefault());
        String today = sdf.format(new Date());
        Query query = db.collection("records").whereEqualTo("Date", selected_date).whereEqualTo("User", user);

        FirestoreRecyclerOptions<Record> options = new FirestoreRecyclerOptions.Builder<Record>()
                .setQuery(query, Record.class)
                .build();

        adapter = new HomeAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setUpRecyclerView(View v) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMdd", Locale.getDefault());
        String today = sdf.format(new Date());
        Query query = db.collection("records").whereEqualTo("Date", today);

        FirestoreRecyclerOptions<Record> options = new FirestoreRecyclerOptions.Builder<Record>()
                .setQuery(query, Record.class)
                .build();

        adapter = new HomeAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
                Toast.makeText(HomeFragment.this.getContext(), "Deleted.", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}