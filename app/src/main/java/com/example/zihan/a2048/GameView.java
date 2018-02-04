package com.example.zihan.a2048;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

/**
 * Created by Zihan on 2018/1/29.
 */

public class GameView extends GridLayout {
    private Card[][] cardMap = new Card[4][4];
    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initGameView();
    }
    public GameView(Context context) {
        super(context);
        initGameView();
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }
    // To dynamically change the card size on different size of screens
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    private void initGameView(){
        setColumnCount(4);
        setBackgroundGrid();
        getGesture();
        addCards(GetCardWidth(), GetCardWidth());
    }
    private void setBackgroundGrid() {
        setBackgroundColor(0xffbbadc0);
    }
    private int GetCardWidth() {
        DisplayMetrics displayMetrics;
        displayMetrics = getResources().getDisplayMetrics();
        int cardWidth;
        cardWidth = displayMetrics.widthPixels;
        return ( cardWidth - 10 ) / 4;

    }
    private void getGesture(){
        setOnTouchListener(new OnTouchListener() {
            private float startX, startY, offsetX, offsetY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if(Math.abs(offsetX)>Math.abs(offsetY)){//horizontal
                            if (offsetX<-5){//left
                                swipeLeft();
                            }else if(offsetX>5){//rught
                                swipeRight();
                            }
                        }else{//vertical
                            if (offsetY<-5){//up
                                swipeUp();
                            }else if(offsetY>5){//down
                                swipeDown();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }
    private void addCards(int CardWidth, int CardHeight){
        Card c;
        for(int y=0; y<4; y++){
            for (int x=0; x<4; x++){
                c = new Card(getContext());
                c.setNum(2);
                addView(c, CardWidth, CardHeight);
                cardMap[x][y] = c;
            }
        }
    }
    private void swipeLeft() {
    }

    private void swipeRight() {
    }

    private void swipeUp() {
    }

    private void swipeDown() {
    }
}
