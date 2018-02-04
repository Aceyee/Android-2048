package com.example.zihan.a2048;

/**
 * Created by Zihan on 2018/1/29.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {
    private int num = 0;
    private TextView label;

    public Card(Context context) {
        super(context);

        label = new TextView(getContext());
        label.setTextSize(32);
        label.setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(-1, -1);
        lp.setMargins(10,10,0,0);
        addView(label, lp);
        setNum(0);
    }
    public int getNum(){
        return num;
    }
    public void setNum(int num){
        this.num = num;
        if(num<=0){
            label.setText("");
        }else {
            label.setText(num + "");
        }
        setColor();
    }
    public void setColor(){
        if(this.getNum()==2){
            label.setBackgroundColor(0xffeee4da);
        }else if(this.getNum()==4){
            label.setBackgroundColor(0xffede0c8);
        }else if(this.getNum()==8){
            label.setBackgroundColor(0xfff2b178);
        }else if(this.getNum()==16){
            label.setBackgroundColor(0xfff59563);
        }else if(this.getNum()==32){
            label.setBackgroundColor(0xfff67c5f);
        }else if(this.getNum()==64){
            label.setBackgroundColor(0xfff65d3b);
        }else if(this.getNum()==128){
            label.setBackgroundColor(0xffedcf72);
        }else if(this.getNum()==256){
            label.setBackgroundColor(0xffedcb60);
        }else if(this.getNum()==512){
            label.setBackgroundColor(0xffedc850);
        }else if(this.getNum()==1024){
            label.setBackgroundColor(0xffedc53e);
        }else if(this.getNum()==2048){
            label.setBackgroundColor(0xffebbb18);
        }else{
            label.setBackgroundColor(0x33ffffff);
        }

    }
    public boolean equals(Card o){
        return this.getNum()==o.getNum();
    }
}
