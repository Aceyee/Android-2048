package com.example.zihan.a2048;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvScore;
    private GameView gameView;
    private static MainActivity mainActivity;
    private int score = 0;
    public MainActivity(){
        mainActivity=this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvScore=(TextView) findViewById(R.id.tvScore);
        gameView=(GameView) findViewById(R.id.gameView);
    }
    @Override
    protected void onStart() {
        super.onStart();
        gameView.getGameView().startGame();
    }

    public void clearScore(){
        score=0;
        showScore();
    }
    public void addScore(int s){
        score+=s;
        showScore();
    }
    public void showScore(){
        tvScore.setText(score+"");
    }
    public static MainActivity getMainActivity(){
        return mainActivity;
    }
}
