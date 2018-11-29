package com.agilya.syc.moodtracker;

import android.app.Activity;
//----
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvSwipDescription) TextView tvSwipDescription;

    private ImageView mImageView;
    private int tableauChaine[] = {R.drawable.smiley_sad, R.drawable.smiley_disappointed, R.drawable.smiley_normal, R.drawable.smiley_happy, R.drawable.smiley_super_happy};
    private int index = 2 ;

    //---
    SharedPreferences sharedPreferences;
    private static final String PREFS = "PREFS";
    private static final String PREFS_AGE = "PREFS_AGE";
    private static final String PREFS_NAME = "PREFS_NAME";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        //bind imageview with your xml's id
        mImageView = (ImageView)findViewById(R.id.imageHumeur);
        //set resource for imageview
        mImageView.setImageResource(tableauChaine[index]);



        sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);
        //pour cela, on commence par regarder si on a déjà des éléments sauvegardés
        if (sharedPreferences.contains(PREFS_AGE) && sharedPreferences.contains(PREFS_NAME)) {

            int age = sharedPreferences.getInt(PREFS_AGE, 0);
            String name = sharedPreferences.getString(PREFS_NAME, null);

            Toast.makeText(this, "Age: " + age + " name: " + name, Toast.LENGTH_SHORT).show();

        } else {
            //si aucun utilisateur n'est sauvegardé, on ajouter [24,florent]
            sharedPreferences
                    .edit()
                    .putInt(PREFS_AGE, 24)
                    .putString(PREFS_NAME, "florent")
                    .apply();

            Toast.makeText(this, "Sauvegardé, relancez l'application pour voir le résultat", Toast.LENGTH_SHORT).show();
        }







        tvSwipDescription.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                //Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastTop), Toast.LENGTH_SHORT).show();
                if (index + 1 <= tableauChaine.length)
                {
                    index++ ;
                    mImageView.setImageResource(tableauChaine[index]);
                }


            }
            public void onSwipeRight(MotionEvent e1) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastRight), Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                //Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastLeft), Toast.LENGTH_SHORT).show();
                if (index + 1 <= tableauChaine.length)
                {
                    index-- ;
                    mImageView.setImageResource(tableauChaine[index]);
                }
            }
            public void onSwipeBottom() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastBottom), Toast.LENGTH_SHORT).show();
            }


            public void onSwipe(String p_s_mvt_e1, String p_s_mvt_e2, float Xe1, float Ye1, float Xe2, float Ye2){
                tvSwipDescription.setText(
                        tvSwipDescription.getText() + "\n"
                                + p_s_mvt_e1 + " - X = " + Xe1 + " - Y = " + Ye1  + "\n"
                                + p_s_mvt_e2 + " - X = " + Xe2 + " - Y = " + Ye2  + "\n"
                );
            }
        });

}

    /*
    private void initializeView() {
        tvSwipDescription=(TextView) findViewById(R.id.tvSwipDescription);
    }
    */
    @OnClick(R.id.button)
    public void clickmoi (){
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastRight), Toast.LENGTH_SHORT).show();
    }



}
