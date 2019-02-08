package com.agilya.syc.moodtracker;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Item> itemList;
    public RecyclerViewAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        try {
            final Item monItem = itemList.get(position);

            SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
            Date maDate = formater.parse( monItem.getItemDay());
            holder.itemDay.setText(DateFormat.getDateInstance( DateFormat.MEDIUM, Locale.FRANCE ).format(maDate));
            holder.itemView.setBackgroundResource( monItem.getItemColor());
            holder.itemComment.setVisibility( TextUtils.isEmpty(monItem.getItemComment()) ? View.INVISIBLE:View.VISIBLE );
            holder.itemComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),monItem.getItemComment(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
