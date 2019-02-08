package com.agilya.syc.moodtracker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

class ViewHolder extends RecyclerView.ViewHolder {
    TextView itemDay;
    ImageButton itemComment;

    ViewHolder(View itemView) {
        super(itemView);
        itemDay = itemView.findViewById(R.id.item_day_rv);
        itemComment = itemView.findViewById(R.id.item_comment_rv);
    }

    void displayItem(Item pItem){
        itemDay.setText(pItem.getItemDay());
    }

}
