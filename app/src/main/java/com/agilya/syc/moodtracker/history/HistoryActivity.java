package com.agilya.syc.moodtracker.history;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;

import com.agilya.syc.moodtracker.Item;
import com.agilya.syc.moodtracker.R;
import com.agilya.syc.moodtracker.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryActivity extends AppCompatActivity {
    // History mood use SharedPreferences
    private SharedPreferences sharedPreferencesHistory;
    //File
    private static final String PREFS_HISTORY = "History";
    //index of curent mood
    private int iMood;
    // Color : use color.xlm to define background screen
    private int tbliColor[] = {R.color.faded_red,R.color.warm_grey,R.color.cornflower_blue_65,R.color.light_sage,R.color.banana_yellow };
    //show history (full or not)
    @BindView(R.id.cbFullHistory) CheckBox cbFullHistory ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        //screen history by default (show 7 item)
        showHistory(false);
    }

    private void showHistory(boolean bFull){
        //Build RecyclerView
        RecyclerView rvList;
        RecyclerViewAdapter rvAdapter;
        List<Item> listDayTemp ;
        listDayTemp = new ArrayList<>();
        List<Item> listDay ;
        listDay = new ArrayList<>();
        int countItem = 0;

        //Get all mood
        sharedPreferencesHistory = getBaseContext().getSharedPreferences(PREFS_HISTORY, MODE_PRIVATE);
        Map<String, ?> prefsMap = sharedPreferencesHistory.getAll();
        for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
            listDayTemp.add(new Item(entry.getKey(), getComment( entry.getKey() ), tbliColor[iMood] ));
        }
        //Collections.sort(listDayTemp, (Item p1, Item p2) -> p1.itemColor > p2.itemColor );

        //listDayTemp full mood list, listDay contruct with full oy max 7 item
        Collections.sort(listDayTemp, Collections.reverseOrder());
        if (bFull){
            listDay = listDayTemp;
        }
        else {
            //if list is less then 7 item, error ibound
            countItem = (listDayTemp.size()<7) ? listDayTemp.size(): 7 ;
            for(int i = 0; i <countItem; i++){
                listDay.add(listDayTemp.get(i));
            }
        }

        //RecyclerView :
        rvList = findViewById(R.id.myRecyclerView);
        rvAdapter = new RecyclerViewAdapter(listDay);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvList.setLayoutManager(mLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setAdapter(rvAdapter);
    }

    //get comment with keyDay
    private String getComment(String pkeyDay){
        String Comment = "";
        String curentShared;
        //key exist, load for init view else value must going by modified, else by only put.
        if ( sharedPreferencesHistory.contains(pkeyDay)) {
            //with keyDay, get index (Mood) and Day Comment
            curentShared = sharedPreferencesHistory.getString(pkeyDay,"");
            iMood = Integer.parseInt(curentShared.substring(0,1));
            if (curentShared.length()>2){
                Comment = curentShared.substring(2,curentShared.length());
            }
        }
        return Comment;
    }

    //Checkbox to show in full mode (checked) or not (unchecked)
    @OnClick(R.id.cbFullHistory)
    void f_showHistory(){ showHistory( cbFullHistory.isChecked() ); }

}
