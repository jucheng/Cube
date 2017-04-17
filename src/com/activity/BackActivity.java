package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Administrator on 2016/5/3.
 */
public class BackActivity extends Activity  {
    @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        final Button back = (Button) findViewById(R.id.back);
        final  ImageButton history=( ImageButton) findViewById(R.id.history);
        final  ImageButton three=( ImageButton)findViewById(R.id.three);
        final  ImageButton high=( ImageButton)findViewById(R.id.high);
        final  ImageButton game=( ImageButton)findViewById(R.id.game) ;


   back.setOnClickListener(new OnClickListener(){
        @Override
        public void onClick (View v){
        Intent intent = new Intent(BackActivity.this, MainActivity.class);
        startActivity(intent);
        BackActivity.this.finish();
    }
    });
        history.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(BackActivity.this, WebViewActivity.class);
                intent.putExtra("url","http://baike.baidu.com/link?url=8t6sCh6fl6JoBS8WA3KAWaMUNvzHitaTd7shEsM12vM8NoAKjmURjGZcCPqjS4Zrl0pljLO4LGph0yy9yM8Z_AdwOk9dug8Om4R4PKkuVmu");
                startActivity(intent);
                BackActivity.this.finish();
            }
        });
        three.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent1 = new Intent(BackActivity.this, WebViewActivity.class);
                intent1.putExtra("url","http://jingyan.baidu.com/article/a3f121e408820dfc9052bb1c.html");
                startActivity(intent1);
                BackActivity.this.finish();
            }
        });
        high.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent2 = new Intent(BackActivity.this,WebViewActivity.class);
                intent2.putExtra("url","http://jingyan.baidu.com/article/ceb9fb10dc25c38cac2ba048.html");
                startActivity(intent2);
                BackActivity.this.finish();
            }
        });
        game.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent3 = new Intent(BackActivity.this,WebViewActivity.class);
                intent3.putExtra("url","http://www.iqiyi.com/v_19rrl1a2mo.html?vfm=f_191_360y");
                startActivity(intent3);
                BackActivity.this.finish();
            }
        });
    }

}