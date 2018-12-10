package com.agilya.syc.moodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    //for swipe and background
    @BindView(R.id.accueilid) RelativeLayout accueilid;
    @BindView(R.id.btnComment) ImageButton btnComment;
    @BindView(R.id.btnHistory) ImageButton btnHistory;
    // imageview vairable to vary screen
    @BindView(R.id.imageHumeur) ImageView imgImageHumeur;


    private ImageView mImageView;
    // Smiley : use tbl to show picture in swipe context
    private int tbliSmiley[] = {R.drawable.smiley_sad, R.drawable.smiley_disappointed, R.drawable.smiley_normal, R.drawable.smiley_happy, R.drawable.smiley_super_happy};
    private int index = 3 ;

    // Color : use color.xlm to define background screen
    private int tbliColor[] = {R.color.faded_red,R.color.warm_grey,R.color.cornflower_blue_65,R.color.light_sage,R.color.banana_yellow };

    // History mood use SharedPreferences
    private SharedPreferences sharedPreferencesHistory;
    //File
    private static final String PREFS_HISTORY = "History";
    //key
    private static final String PREFS_DAY = "DayMood";
    private static final String PREFS_COMMENT = "DayComment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //set resource for imageview by default
        mImageView.setImageResource(tbliSmiley[index]);

        //-------------- load sharedPreferences and use for history
        sharedPreferencesHistory = getBaseContext().getSharedPreferences(PREFS_HISTORY, MODE_PRIVATE);

        accueilid.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            // show next up smiley
            public void onSwipeTop() {
                if (index + 1 <= tbliSmiley.length - 1 )
                {
                    index++ ;
                    // apply mood selected
                    f_applyMoodDay();
                }
            }
            // show next down smiley
            public void onSwipeBottom() {
                if ( index - 1 >= 0 )
                {
                    index-- ;
                    // apply mood selected
                    f_applyMoodDay();
                }
            }
        });

}

    @OnClick(R.id.btnComment)
    void f_history(){
        // enregistrement du jour + etat et décaler les jours présents
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String keynowFormated = format.format(now);


        // pourquoi ces 2 façon de faire ??

        if (sharedPreferencesHistory.contains(keynowFormated)) {
            //key exist, value must be modified
            SharedPreferences.Editor editor = sharedPreferencesHistory.edit();
            editor.putString(keynowFormated, index + "_" + "mon nouveau commentaire").commit();
        }
        else {
            sharedPreferencesHistory.edit().putString(keynowFormated, index + "_" + "mon commentaire qui tue la mort " ).commit();
        }
    }

    @OnClick(R.id.btnHistory)
    void f_showhistory(){
        /*
        les clés doivent être soit néttoyé soit filtrées sur la date.
        vue à faire :
        hauteurEcran = ( hauteurEcran - hauteurBtn )/7
        */
        // afficher si existe
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String keynowFormated = format.format(now);
        if (sharedPreferencesHistory.contains(keynowFormated)){
            Context context = getApplicationContext();
            CharSequence text = sharedPreferencesHistory.getString(keynowFormated, "pas trouvé, mais c'est pas possible normalement !!!");
            Toast.makeText(context, text, Toast.LENGTH_SHORT ).setGravity(Gravity.TOP | Gravity.LEFT,0 ,0);
            Toast.makeText(context, text, Toast.LENGTH_SHORT ).show();
        }
    }

    private void f_nextDay(){
    // produire le décalage de jour, surement que jour 0 n'est pas utile, car on écrase jour 1, puis quand on décale jour 1 devient vide.

        Map<String,?> keys = sharedPreferencesHistory.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            //Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());

            if(true){
                //sequenece de suppressoin:
                SharedPreferences.Editor editor = sharedPreferencesHistory.edit();
                editor.remove(entry.getKey()).commit();
            }
        }

        /*

        la key actu =>
        J1 y a rien, je stocke avec la key
        J1 y a qq chose, je vérifie si key existe, oui => modif si non => put

        J1 à l'ouverture de l'appli
            => faire un nettoyage des key périmées càd dire key > key +7 ,
            => si key la plus petite < key actu => on déplace

        */
    }


    private void f_applyMoodDay(){
        // Apply the mood's choice, image and background color
        mImageView.setImageResource(tbliSmiley[index]);
        accueilid.setBackgroundColor(getResources().getColor( tbliColor[index]));

        //build the key
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String keynowFormated = format.format(today);

        try {
            Date maDate = format.parse("20181204");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // pourquoi ces 2 façon de faire ??

        //key exist, value must going by modified, else by only put.
        if (sharedPreferencesHistory.contains(keynowFormated)) {
            SharedPreferences.Editor editor = sharedPreferencesHistory.edit();
            editor.putString(keynowFormated, index + "_" + "mon nouveau commentaire").commit();
        }
        else {
            sharedPreferencesHistory.edit().putString(keynowFormated, index + "_" + "mon commentaire qui tue la mort " ).commit();
        }
    }
    private void f_DoHistory(){
        sharedPreferencesHistory
                .edit()
                .putString(PREFS_DAY, "20181207_4")
                .putString(PREFS_COMMENT, "20181207_tjrs c super content que je suis")
                .putString(PREFS_DAY, "20181206_4")
                .putString(PREFS_COMMENT, "20181206_c super content que je suis")
                .putString(PREFS_DAY, "20181205_2")
                .putString(PREFS_COMMENT, "20181205_cv mieux")
                .putString(PREFS_DAY, "20181204_1")
                .putString(PREFS_COMMENT, "20181204_c bof")
                .putString(PREFS_DAY, "20181203_0")
                .putString(PREFS_COMMENT, "20181203_c a chié")
                .putString(PREFS_DAY, "20181202_0")
                .putString(PREFS_COMMENT, "20181202_c super à chier")
                .putString(PREFS_DAY, "20181202_2")
                .putString(PREFS_COMMENT, "20181202_cv")
                .putString(PREFS_DAY, "20181201_2")
                .putString(PREFS_COMMENT, "20181201_c pas mal")
                .commit();
    }
}

