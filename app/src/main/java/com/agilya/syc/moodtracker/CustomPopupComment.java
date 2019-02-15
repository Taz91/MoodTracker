package com.agilya.syc.moodtracker;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomPopupComment extends Dialog {
    // references object view
    @BindView(R.id.viewPopup) RelativeLayout viewPopup;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.yesButton) Button yesButton;
    @BindView(R.id.noButton) Button noButton;
    @BindView(R.id.modifyComment) EditText modifyComment;

    OnPopupActionCommentListener listener;

    // Constructor
    public CustomPopupComment(Context context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        //load view
        setContentView(R.layout.popupsaisiecomment);
        ButterKnife.bind(this);
        this.title.setTextSize(12);
    }

    public void setsTitle(String pTitle){ this.title.setText(pTitle); }
    public void setsComment(String pComment){ this.modifyComment.setHint(pComment); }

    //public Button getNoButton() { return noButton; }
    //public Button getYesButton(){ return yesButton; }

    public void setListener(OnPopupActionCommentListener listener) {
        this.listener = listener;
    }
    @OnClick(R.id.yesButton)
    void closeYesComment(){
        listener.onYes();
        this.dismiss();
    }
    @OnClick(R.id.noButton)
    void closeComment(){
        listener.onNo();
        this.dismiss();
    }
    public void buildPopup(){
        show();
    }
    public interface OnPopupActionCommentListener{
        public void onYes();
        public void onNo();
    }
}
