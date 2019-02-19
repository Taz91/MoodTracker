package com.agilya.syc.moodtracker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

class ViewHolder extends RecyclerView.ViewHolder {
    TextView itemDay;
    ImageButton itemComment;
    LinearLayout linearLayout2;

    ViewHolder(View itemView) {
        super(itemView);
        itemDay = itemView.findViewById(R.id.item_day_rv);
        itemComment = itemView.findViewById(R.id.item_comment_rv);
        linearLayout2 = itemView.findViewById(R.id.linearLayout2);
    }

    void displayItem(Item pItem){
        itemDay.setText(pItem.getItemDay());
    }

}
