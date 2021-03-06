package com.example.zihan.a2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zihan on 2018/1/29.
 */

public class GameView extends GridLayout {
    private static GameView gameView;
    private int count;
    boolean moved = false;
    private Card[][] cardMap = new Card[4][4];
    private Card[][] tempMap = new Card[4][4];
    AnimationSet [][]animationSet = new AnimationSet[4][4];
    private int [][] steps;
    private List<Point> emptyPoints = new ArrayList<Point>();

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gameView=this;
        initGameView();
    }
    public GameView(Context context) {
        super(context);
        gameView=this;
        initGameView();
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameView=this;
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
                cardMap[y][x] = c;
            }
        }
    }
    public void startGame(){
        MainActivity.getMainActivity().clearScore();
        for(int y=0; y<4;y++){
            for(int x=0; x<4; x++){
                cardMap[y][x].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
    }
    private void addRandomNum(){
        emptyPoints.clear();
        for(int y=0; y<4; y++){
            for(int x=0; x<4; x++){
                if(cardMap[x][y].getNum()<=0){
                    emptyPoints.add(new Point(x, y));
                }
            }
        }
        double a = Math.random();
        Point p = emptyPoints.remove((int)(a*emptyPoints.size()));
        cardMap[p.x][p.y].setNum(Math.random()>0.1 ? 2:4);
    }

    private void swipeLeft() {
        moved = false;
        steps = new int[4][4];

        Card c;
        for(int y=0; y<4; y++){
            for (int x=0; x<4; x++){
                c = new Card(getContext());
                c.setNum(cardMap[y][x].getNum());
                tempMap[y][x] = c;
            }
        }
        for(int y=0; y<4; y++){
            for(int x=0;x<4;x++){
                if(tempMap[y][x].getNum()>0) {
                    steps = leftRemoveBlank(x, y, steps);
                    steps = leftMerge(x, y, steps);
                }
            }
            for(int x=0;x<4;x++){
                steps[y][x] = -1* steps[y][x];
                animationSet[y][x] = new AnimationSet(true);
                TranslateAnimation translateAnimation =
                        new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF,0,
                                Animation.RELATIVE_TO_SELF,1f*steps[y][x],
                                Animation.RELATIVE_TO_SELF,0,
                                Animation.RELATIVE_TO_SELF,0);
                translateAnimation.setDuration(250);
                translateAnimation.setFillAfter(true);
                translateAnimation.setAnimationListener(new Animation.AnimationListener(){

                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    public void onAnimationEnd(Animation animation) {
                        // TODO Auto-generated method stub
                        count++;
                        System.out.println(count);
                        if(count % 3==0){
                            update(count/3-1);
                        }
                        if(count==12){
                            count=0;
                            if(moved){
                                addRandomNum();
                                checkComplete();
                            }
                        }
                    }

                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub
                    }
                });
                animationSet[y][x].addAnimation(translateAnimation);
            }
        }
        for(int y=0; y<4; y++){
            for (int x=1; x<4; x++){
                cardMap[y][x].startAnimation(animationSet[y][x]);
            }
        }
    }
    private int[][] leftMerge(int x, int y, int[][] steps) {
        int xcopy = x;
        int xleft = x-1;

        while(xleft>=0) {
            if(tempMap[y][xcopy].getNum()!=0) {
                if (tempMap[y][xleft].getNum() == tempMap[y][xcopy].getNum()) {
                    steps[y][x]++;
                    moved=true;
                    tempMap[y][xleft].setNum(tempMap[y][xcopy].getNum() * 2);
                    tempMap[y][xcopy].setNum(0);
                    for (int i = x; i < 4; i++) {
                        if (tempMap[y][i].getNum() > 0) {
                            steps = leftRemoveBlank(i, y, steps);
                        }
                    }
                    break;
                }else{
                    break;
                }
            }else{
                xcopy--;
                xleft=xcopy;
            }
            xleft--;
        }
        return steps;
    }
    private int[][] leftRemoveBlank(int x, int y, int [][]steps) {
        int xcopy = x;
        int xleft = x-1;
        while (xleft>=0) {
            if (tempMap[y][xcopy].getNum()!=0 && tempMap[y][xleft].getNum() <= 0) {
                swap(xleft, xcopy, y);
                steps[y][x]++;
                moved=true;
            }
            xcopy--;
            xleft--;
        }
        return steps;
    }

    private void swipeRight() {
        moved = false;
        steps = new int[4][4];

        Card c;
        for(int y=0; y<4; y++){
            for (int x=0; x<4; x++){
                c = new Card(getContext());
                c.setNum(cardMap[y][x].getNum());
                tempMap[y][x] = c;
            }
        }
        for(int y=0; y<4; y++){
            for(int x=3;x>-1;x--){
                if(tempMap[y][x].getNum()>0) {
                    steps = rightRemoveBlank(x, y, steps);
                    steps = rightMerge(x, y, steps);
                }
            }
            for(int x=3;x>-1;x--){
                steps[y][x] = 1* steps[y][x];
                animationSet[y][x] = new AnimationSet(true);
                TranslateAnimation translateAnimation =
                        new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF,0,
                                Animation.RELATIVE_TO_SELF,1f*steps[y][x],
                                Animation.RELATIVE_TO_SELF,0,
                                Animation.RELATIVE_TO_SELF,0);
                translateAnimation.setDuration(250);
                translateAnimation.setFillAfter(true);
                translateAnimation.setAnimationListener(new Animation.AnimationListener(){

                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    public void onAnimationEnd(Animation animation) {
                        // TODO Auto-generated method stub
                        count++;
                        System.out.println(count);
                        if(count % 3==0){
                            update(count/3-1);
                        }
                        if(count==12){
                            count=0;
                            if(moved){
                                addRandomNum();
                                checkComplete();
                            }
                        }
                    }

                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub
                    }
                });
                animationSet[y][x].addAnimation(translateAnimation);
            }
        }
        for(int y=0; y<4; y++){
            for (int x=2; x>-1; x--){
                cardMap[y][x].startAnimation(animationSet[y][x]);
            }
        }
    }
    private int[][] rightMerge(int x, int y, int[][] steps) {
        int xcopy = x;
        int xright = x+1;

        while(xright<=3) {
            if(tempMap[y][xcopy].getNum()!=0) {
                if (tempMap[y][xright].getNum() == tempMap[y][xcopy].getNum()) {
                    steps[y][x]++;
                    moved=true;
                    tempMap[y][xright].setNum(tempMap[y][xcopy].getNum() * 2);
                    tempMap[y][xcopy].setNum(0);
                    for (int i = x; i > -1; i--) {
                        if (tempMap[y][i].getNum() > 0) {
                            steps = rightRemoveBlank(i, y, steps);
                        }
                    }
                    break;
                }else{
                    break;
                }
            }else{
                xcopy++;
                xright=xcopy;
            }
            xright++;
        }
        return steps;
    }
    private int[][] rightRemoveBlank(int x, int y, int [][]steps) {
        int xcopy = x;
        int xright = x+1;
        while (xright<=3) {
            if (tempMap[y][xcopy].getNum()!=0 && tempMap[y][xright].getNum() <= 0) {
                swap(xright, xcopy, y);
                steps[y][x]++;
                moved=true;
            }
            xcopy++;
            xright++;
        }
        return steps;
    }

    private void swap(int xleft, int x, int y){
        int temp = tempMap[y][xleft].getNum();
        tempMap[y][xleft].setNum(tempMap[y][x].getNum());
        tempMap[y][x].setNum(temp);
    }
    private void update(int y) {
        for (int x=0; x<4; x++){
            cardMap[y][x].setNum(tempMap[y][x].getNum());
        }
    }

    private void swipeUp() {
        moved = false;
        steps = new int[4][4];

        Card c;
        for(int y=0; y<4; y++){
            for (int x=0; x<4; x++){
                c = new Card(getContext());
                c.setNum(cardMap[y][x].getNum());
                tempMap[y][x] = c;
            }
        }
        for(int x=0;x<4;x++){
            for(int y=0; y<4; y++){
                if(tempMap[y][x].getNum()>0) {
                    steps = upRemoveBlank(x, y, steps);
                    steps = upMerge(x, y, steps);
                }
            }
            for(int y=0;y<4;y++){
                steps[y][x] = -1* steps[y][x];
                animationSet[y][x] = new AnimationSet(true);
                TranslateAnimation translateAnimation =
                        new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF,0,
                                Animation.RELATIVE_TO_SELF,0,
                                Animation.RELATIVE_TO_SELF,0,
                                Animation.RELATIVE_TO_SELF,1f*steps[y][x]);
                translateAnimation.setDuration(250);
                translateAnimation.setFillAfter(true);
                translateAnimation.setAnimationListener(new Animation.AnimationListener(){

                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    public void onAnimationEnd(Animation animation) {
                        // TODO Auto-generated method stub
                        count++;
                        System.out.println(count);
                        if(count % 3==0){
                            update2(count/3-1);
                        }
                        if(count==12){
                            count=0;
                            if(moved){
                                addRandomNum();
                                checkComplete();
                            }
                        }
                    }

                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub
                    }
                });
                animationSet[y][x].addAnimation(translateAnimation);
            }
        }
        for (int x=0; x<4; x++){
            for(int y=1; y<4; y++){
                cardMap[y][x].startAnimation(animationSet[y][x]);
            }
        }
    }
    private int[][] upMerge(int x, int y, int[][] steps) {
        int ycopy = y;
        int yup = y-1;

        while(yup>=0) {
            if(tempMap[ycopy][x].getNum()!=0) {
                if (tempMap[yup][x].getNum() == tempMap[ycopy][x].getNum()) {
                    steps[y][x]++;
                    moved=true;
                    tempMap[yup][x].setNum(tempMap[yup][x].getNum() * 2);
                    tempMap[ycopy][x].setNum(0);
                    for (int i = y; i < 4; i++) {
                        if (tempMap[i][x].getNum() > 0) {
                            steps = upRemoveBlank(x, i, steps);
                        }
                    }
                    break;
                }else{
                    break;
                }
            }else{
                ycopy--;
                yup=ycopy;
            }
            yup--;
        }
        return steps;
    }
    private int[][] upRemoveBlank(int x, int y, int [][]steps) {
        int ycopy = y;
        int yup = y-1;
        while (yup>=0) {
            if (tempMap[ycopy][x].getNum()!=0 && tempMap[yup][x].getNum() <= 0) {
                swap2(yup, ycopy, x);
                steps[y][x]++;
                moved=true;
            }
            ycopy--;
            yup--;
        }
        return steps;
    }

    private void swipeDown() {
        moved = false;
        steps = new int[4][4];

        Card c;
        for(int y=0; y<4; y++){
            for (int x=0; x<4; x++){
                c = new Card(getContext());
                c.setNum(cardMap[y][x].getNum());
                tempMap[y][x] = c;
            }
        }
        for(int x=0;x<4;x++){
            for(int y=3; y>-1; y--){
                if(tempMap[y][x].getNum()>0) {
                    steps = downRemoveBlank(x, y, steps);
                    steps = downMerge(x, y, steps);
                }
            }
            for(int y=3;y>-1;y--){
                steps[y][x] = 1* steps[y][x];
                animationSet[y][x] = new AnimationSet(true);
                TranslateAnimation translateAnimation =
                        new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF,0,
                                Animation.RELATIVE_TO_SELF,0,
                                Animation.RELATIVE_TO_SELF,0,
                                Animation.RELATIVE_TO_SELF,1f*steps[y][x]);
                translateAnimation.setDuration(250);
                translateAnimation.setFillAfter(true);
                translateAnimation.setAnimationListener(new Animation.AnimationListener(){

                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    public void onAnimationEnd(Animation animation) {
                        // TODO Auto-generated method stub
                        count++;
                        System.out.println(count);
                        if(count % 3==0){
                            update2(count/3-1);
                        }
                        if(count==12){
                            count=0;
                            if(moved){
                                addRandomNum();
                                checkComplete();
                            }
                        }
                    }

                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub
                    }
                });
                animationSet[y][x].addAnimation(translateAnimation);
            }
        }
        for (int x=0; x<4; x++){
            for(int y=2; y>-1; y--){
                cardMap[y][x].startAnimation(animationSet[y][x]);
            }
        }
    }
    private int[][] downMerge(int x, int y, int[][] steps) {
        int ycopy = y;
        int ydown = y+1;

        while(ydown<=3) {
            if(tempMap[ycopy][x].getNum()!=0) {
                if (tempMap[ydown][x].getNum() == tempMap[ycopy][x].getNum()) {
                    steps[y][x]++;
                    moved=true;
                    tempMap[ydown][x].setNum(tempMap[ydown][x].getNum() * 2);
                    tempMap[ycopy][x].setNum(0);
                    for (int i = y; i > -1; i--) {
                        if (tempMap[i][x].getNum() > 0) {
                            steps = downRemoveBlank(x, i, steps);
                        }
                    }
                    break;
                }else{
                    break;
                }
            }else{
                ycopy++;
                ydown=ycopy;
            }
            ydown++;
        }
        return steps;
    }
    private int[][] downRemoveBlank(int x, int y, int [][]steps) {
        int ycopy = y;
        int ydown = y+1;
        while (ydown<=3 ) {
            if (tempMap[ycopy][x].getNum()!=0 && tempMap[ydown][x].getNum() <= 0) {
                swap2(ydown, ycopy, x);
                steps[y][x]++;
                moved=true;
            }
            ycopy++;
            ydown++;
        }
        return steps;
    }
    private void swap2(int yup, int y, int x){
        int temp = tempMap[yup][x].getNum();
        tempMap[yup][x].setNum(tempMap[y][x].getNum());
        tempMap[y][x].setNum(temp);
    }
    private void update2(int x) {
        for (int y=0; y<4; y++){
            cardMap[y][x].setNum(tempMap[y][x].getNum());
        }
    }
    private void checkComplete(){
        boolean complete = true;
        ALL:
        for(int y=0; y<4; y++) {
            for (int x = 0; x < 4; x++) {
                if(cardMap[x][y].getNum()==0||
                        (x>0&&cardMap[x][y].equals(cardMap[x-1][y])||
                                (x<3&&cardMap[x][y].equals(cardMap[x+1][y]))||
                                (y>0&&cardMap[x][y].equals(cardMap[x][y-1]))||
                                (y<3&&cardMap[x][y].equals(cardMap[x][y+1]))
                        )){
                    complete =false;
                    break ALL;
                }
            }
        }
        if (complete){
            new AlertDialog.Builder(getContext()).setTitle("你好").setMessage("游戏结束").setPositiveButton("重来", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).show();
        }
    }
    public static GameView getGameView(){
        return gameView;
    }
}
