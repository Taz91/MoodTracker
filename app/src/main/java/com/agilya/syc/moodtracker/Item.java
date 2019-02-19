package com.agilya.syc.moodtracker;

public class Item implements Comparable<Item>{
    private String itemDay;
    private String itemComment;
    private int itemColor;
    private int itemMood;

    public Item(String itemDay, String itemComment, int itemColor, int itemMood ) {
        this.itemDay = itemDay;
        this.itemComment = itemComment;
        this.itemColor = itemColor;
        this.itemMood = itemMood;
    }

    public String getItemDay() {
        return itemDay;
    }
    public String getItemComment() { return itemComment; }
    public int getItemColor() { return itemColor; }
    public int getItemMood(){return itemMood;}

    @Override
    public int compareTo(Item itemCurrent) {
        return itemDay.compareTo(itemCurrent.getItemDay());
    }

    @Override
    public String toString() {
        return itemDay + "  -  " + itemComment;
    }
}
