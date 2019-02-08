package com.agilya.syc.moodtracker;

public class Item implements Comparable<Item>{
    private String itemDay;
    private String itemComment;
    private int itemColor;

    public Item(String itemDay, String itemComment, int itemColor) {
        this.itemDay = itemDay;
        this.itemComment = itemComment;
        this.itemColor = itemColor;
    }

    public String getItemDay() {
        return itemDay;
    }
    public String getItemComment() { return itemComment; }
    public int getItemColor() { return itemColor; }

    @Override
    public int compareTo(Item itemCurrent) {
        return itemDay.compareTo(itemCurrent.getItemDay());
    }

    @Override
    public String toString() {
        return itemDay + "  -  " + itemComment;
    }
}
