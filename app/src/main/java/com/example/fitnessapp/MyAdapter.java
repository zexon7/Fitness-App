package com.example.fitnessapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Item> itemList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView food, calories;
        public CardView btn_card;
        public MyViewHolder(View v) {
            super(v);
            food = v.findViewById(R.id.item_food);
            calories = v.findViewById(R.id.item_calories);
            btn_card = v.findViewById(R.id.item_card);
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Item> list) {
        itemList = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Item currentItem = itemList.get(position);
        holder.food.setText(currentItem.getFName());
        holder.calories.setText(Long.toString(currentItem.getFCal()));
        holder.btn_card.setOnClickListener(new View.OnClickListener() {
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
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
