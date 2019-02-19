package com.agilya.syc.moodtracker;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Item> itemList;
    public Context context;

    public RecyclerViewAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        try {
            final Item monItem = itemList.get(position);

            //TODO : mettre les textes dans les values !!
            SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
            Date maDate = formater.parse( monItem.getItemDay());
            holder.itemDay.setText(Utils.formatDate(maDate));
            holder.moodLayout.setBackgroundResource( monItem.getItemColor());
            holder.itemComment.setVisibility( TextUtils.isEmpty(monItem.getItemComment()) ? View.INVISIBLE:View.VISIBLE );
            holder.moodLayout.getLayoutParams().width = getMoodWidth(monItem);

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

    private int getMoodWidth(Item item) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int mWidth = size.x;
        int moodUnit = mWidth / 5;
        return moodUnit * (item.getItemMood() + 1);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

