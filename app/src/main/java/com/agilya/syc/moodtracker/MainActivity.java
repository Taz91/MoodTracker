package com.agilya.syc.moodtracker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.agilya.syc.moodtracker.history.HistoryActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.WRITE_CALENDAR;


/**************************************************************************************************************
 * @author SYC
 * @since 01/10/2018
 * MoodTracker application to store mood day by day
 * Possibility :
 * store mood and one comment for every day
 * show history with 2 option : only the last 7 mood or all history
 * show history : color of mood, comment showing when click on icone comment
 * delete all hsitory
 * send the current day to the calendar
 * ************************************************************************************************************
 */
public class MainActivity extends AppCompatActivity implements CustomPopupComment.OnPopupActionCommentListener {
    // for swipe and background
    @BindView(R.id.firstPageId)
    RelativeLayout firstPageId;
    // add or modify a comment
    @BindView(R.id.btnComment)
    ImageButton btnComment;
    //show history
    @BindView(R.id.btnHistory)
    ImageButton btnHistory;
    // imageview variable to vary screen
    @BindView(R.id.imgMood)
    //smiley Mood
    ImageView imgMood;
    //delete history
    @BindView(R.id.btnRemoveHistory)
    Button btnRemoveHistory;
    //send current Mood to calendar
    @BindView(R.id.btnEventInCalendar)
    Button btnEventInCalendar;
    // Smiley : use tbl to show picture in swipe context
    private int tbliSmiley[] = {R.drawable.smiley_sad, R.drawable.smiley_disappointed, R.drawable.smiley_normal, R.drawable.smiley_happy, R.drawable.smiley_super_happy};
    // index of current Mood, here for Mood default
    private int index = 3;
    private String curentComment = "";
    private String curentShared;
    // Color : use color.xlm to define background screen
    private int tbliColor[] = {R.color.faded_red, R.color.warm_grey, R.color.cornflower_blue_65, R.color.light_sage, R.color.banana_yellow};
    // History mood use SharedPreferences
    private SharedPreferences sharedPreferencesHistory;
    //File
    private static final String PREFS_HISTORY = "History";
    //key
    private static final String PREFS_DAY = "DayKey";
    private static final String PREFS_COMMENT = "DayComment";
    // modify a comment
    private CustomPopupComment modifyCommentPopup;
    //use in alerteRemove;
    Cursor cursor;

    /**************************************************************************************************************
     * @author SYC
     * @since 01/10/2018
     *
     * ************************************************************************************************************
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
         // load sharedPreferences and use for history
        sharedPreferencesHistory = getBaseContext().getSharedPreferences(PREFS_HISTORY, MODE_PRIVATE);

        // ****************************  use when necessary to test some different situation
        //DoHistory();
        // ***************************************************************************************************
        //set resource for imageview if SharedPreferences(key) if null then by default else img already stored
        applyMoodDay(true);

        /**
         * swipe action, use to select moodDay
         */
        firstPageId.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            // show next up smiley
            public void onSwipeTop() {
                if (index + 1 <= tbliSmiley.length - 1) {
                    index++;
                    // apply mood selected
                    applyMoodDay(false);
                }
            }
            // show next down smiley
            public void onSwipeBottom() {
                if (index - 1 >= 0) {
                    index--;
                    // apply mood selected
                    applyMoodDay(false);
                }
            }
        });
    }

    /**
     * On Click, build key, get current comment, build and show a custom popu to modify the comment
     */
    @OnClick(R.id.btnComment)
    void loadModifyComment() {
        // build the key and get comment, to compare with history
        final String keyDay = buildKey();
        curentComment = getComment(keyDay);
        // setting custom popup, before show.
        modifyCommentPopup = new CustomPopupComment(this);
        modifyCommentPopup.setsTitle("Votre commentaire du : " + keyDay.substring(6, 8) + "/" + keyDay.substring(4, 6) + "/" + keyDay.substring(0, 4));
        modifyCommentPopup.setsComment(curentComment);
        // wait and see user action on button yes/no
        modifyCommentPopup.setListener(this);
        modifyCommentPopup.buildPopup();
    }

    /**
     * on click, show history, based on RecyclerView.
     */
    @OnClick(R.id.btnHistory)
    void showHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    /**
     * on click, history is deleting.
     * info: in test you can uncomment 'doHistory'
     */
    @OnClick(R.id.btnRemoveHistory)
    void removeHistory() {
        AlertDialog.Builder alerteRemove = new AlertDialog.Builder(this);
        alerteRemove.setMessage("Attention, vous allez tout supprimer !!");
        alerteRemove.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor editor = sharedPreferencesHistory.edit();
                editor.clear().commit();
                //get the current mood to store the first history
                applyMoodDay(true);
            }
        });
        alerteRemove.setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alerteRemove.show();
    }

    /**
     * on click, insert mood of day in calendar
     *
     *
     */
    @OnClick(R.id.btnEventInCalendar)
    void eventInCalendar() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_CALENDAR )
            .withListener(new PermissionListener() {

                long calID; // = 3
                long startMillis = 0;
                long endMillis = 0;
                String displayName;

                @Override
                public void onPermissionGranted(PermissionGrantedResponse response) {
                    // permission is granted, continu the traitement
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED){
                        String KeyDayCalendar = buildKey();
                        String KeyDayComment = getComment(KeyDayCalendar);

                        if ( !"".equals(KeyDayComment)){
                            // build request - return data
                            String[] projection = new String[]{
                                    CalendarContract.Calendars._ID,
                                    CalendarContract.Calendars.NAME,
                                    CalendarContract.Calendars.ACCOUNT_NAME,
                                    CalendarContract.Calendars.ACCOUNT_TYPE
                            };
                            // query
                            cursor = getContentResolver().query(
                                    CalendarContract.Calendars.CONTENT_URI,
                                    projection,
                                    CalendarContract.Calendars.VISIBLE + " = 1",
                                    null,
                                    CalendarContract.Calendars._ID + " ASC"
                            );
                            // get id of local calendar
                            if (cursor.moveToFirst()) {
                                do {
                                    calID = cursor.getLong(0);
                                    displayName = cursor.getString(1);
                                } while (cursor.moveToNext());
                            }
                            // build startTime and endTime of insert event
                            Date timeNow = new Date();
                            Date hour = new Date(3600*1000);
                            Date timeEnd = new Date(timeNow.getTime() + hour.getTime());

                            Calendar beginTime = Calendar.getInstance();
                            beginTime.setTime(timeNow);
                            Calendar endTime = Calendar.getInstance();
                            endTime.setTime(timeEnd);

                            // build Event
                            ContentResolver cr = getContentResolver();
                            ContentValues values = new ContentValues();
                            values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis()); // startMillis
                            values.put(CalendarContract.Events.DTEND, String.valueOf(endTime.getTimeInMillis())); // endMillis
                            values.put(CalendarContract.Events.TITLE, KeyDayComment );
                            values.put(CalendarContract.Events.DESCRIPTION, "MoodDay");
                            values.put(CalendarContract.Events.CALENDAR_ID, calID);
                            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Paris"); // America/Los_Angeles
                            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Aucun commentaire saisi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse response) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied()) {
                        // navigate user to app settings
                    }
                }
                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).check();
    }

    /**
     * fct to test the MoodTraker : insert some entry in sharedPreferences
     * uncomment the call in onCreat
     */
    private void DoHistory(){
        sharedPreferencesHistory
                .edit()

                .remove("DayComment")
                .remove("DayKey")
                .remove("fullday")

                .putString("20181226", "0_1er commentaire humeur 0")
                .putString("20181227", "1_humeur 1")
                .putString("20181228", "2_humeur 2")
                .putString("20181229", "3_humeur 3")
                .putString("20181230", "4_humeur 4")
                .putString("20181231", "3_humeur défaut")
                .putString("20190101", "4_nouvelle année")
                .putString("20190102", "4_c bien !!")
                .putString("20190103", "4_tjrs bien !!")
                .putString("20190104", "3_tjrs presque bien !!")
                .putString("20190110", "2_moins bien !!")
                .putString("20190113", "1_moins moins bien !!")
                .putString("20190114", "1_idem bof !!")
                .putString("20190115", "1_3 jours de bofs")
                .putString("20190117", "0_au tas !!")
                .putString("20190118", "1_ça remonte ?")
                .putString("20190119", "0_et ça descend")
                .putString("20190120", "1_et up !!")
                .putString("20190122", "0_et dowmn...")
                .putString("20190123", "2_et re up !!")
                .putString("20190124", "3_plus haut !!")
                .putString("20190125", "2_dowmn !!")
                .putString("20190126", "1_bof ")
                .putString("20190127", "3_presque en haut ")
                .putString("20190128", "4_tout en haut !!")
                .putString("20190130", "0_on fini en bas !!")
                .putString("20190201", "0_en février c idem on commence en bas !!")
                .putString("20190202", "1_on remontent doucement")
                .putString("20190203", "1_j'ai mangé un kebab")
                .putString("20190204", "2_une salade et ça repart")
                .putString("20190206", "3_pas simple mais cela avance")
                .putString("20190207", "4_mettre un genou à terre n'est jamais grave, ne pas tenter de se relever l'est !")
                .putString("20190208", "0_il fait froid qd mm")
                .putString("20190209", "2_semaine chargée")
                .putString("20190212", "2_tjrs aussi chargée")
                .putString("20190213", "2_data de test 01")
                .putString("20190218", "0_data de test 02")
                .putString("20190217", "4_data de test 04")
                .putString("20190216", "1_data de test 05")
                .putString("20190215", "2_data de test 06")
                .putString("20190214", "3_data de test 07")
                .commit();
    }

     /**
     * load or load and modify curent mood
     * @param bInit :
     */
    private void applyMoodDay(boolean bInit){
        //build the key to store or/and compare with history
        String keynowFormated = buildKey();
        //key exist, load for init view else value must going by modified, else by only put.
        if ( sharedPreferencesHistory.contains(keynowFormated)) {
            //get curentkey to get index and comment
            curentShared = sharedPreferencesHistory.getString(keynowFormated,"");
            if (curentShared.length()>2){
                curentComment = curentShared.substring(2,curentShared.length());
            }
            // onCreate, load index else modify value of key
            if (bInit){
                index = Integer.parseInt(curentShared.substring(0,1));
            }
            //   !!!!!!!!!!!!!!!!!!!!!   else ci dessous ne doit pas servir car si création alors passe pas dans ce if au dessus
            else{
                sharedPreferencesHistory.edit().putString(keynowFormated, index + "_" + curentComment ).commit();
            }
        }
        //key not exist, store the mood default
        else {
            sharedPreferencesHistory.edit().putString(keynowFormated, index + "_" + curentComment ).commit();
        }
        // after swipe mvt, apply in view the mood's choice, image and background color
        imgMood.setImageResource(tbliSmiley[index]);
        firstPageId.setBackgroundColor(getResources().getColor( tbliColor[index]));
    }

    /**
     * load or load and modify curent mood in sharedPreferences
     * example : when the comment is created or modify
     */
    public void applyComment(String pkeyDay, String pComment){
        //get curentkey to get index and comment
        curentShared = sharedPreferencesHistory.getString(pkeyDay,"");
        sharedPreferencesHistory.edit().putString( pkeyDay, curentShared.substring(0,1) + "_" + pComment ).commit();
    }

    /**
     * Get the comment with the param keyDay
     * @param pkeyDay : date reverse like format "yyyymmdd"
     * @return the comment
     */
    private String getComment(String pkeyDay){
        curentComment = "";
        //key exist, load for init view else value must going by modified, else by only put.
        if ( sharedPreferencesHistory.contains(pkeyDay)) {
            //with keyDay, get index and comment
            curentShared = sharedPreferencesHistory.getString(pkeyDay,"");
            index = Integer.parseInt(curentShared.substring(0,1));
            if (curentShared.length()>2){
                curentComment = curentShared.substring(2,curentShared.length());
            }
        }
        return curentComment;
    }

    /**
     * build the curent key for store in sharedPreferences
     * @return the keyDay
     */
    private String buildKey(){
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(today);
    }

    // PopupModifyComment - Yes / No
    @Override
    public void onYes() {
        applyComment(buildKey(),modifyCommentPopup.modifyComment.getText().toString());
    }
    @Override
    public void onNo() {

    }
}