package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;


/*@SuppressLint("SdCardPath")*/
public class MainActivity extends Activity {
    /**
     * Called when the activity is first cr eated.
     */
    private Button button;
    private ImageView view;
    ImageView homeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button photo = (Button) findViewById(R.id.photo);
        final Button about = (Button) findViewById(R.id.about);
        final Button setting = (Button) findViewById(R.id.setting);
        final Button test = (Button) findViewById(R.id.test);

        view = (ImageView) findViewById(R.id.imageView1);
        homeImage = (ImageView) findViewById(R.id.start);


        photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
                startActivity(intent);
            }
        });
        setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, BackActivity.class);
                startActivity(intent1);
            }
        });
        about.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, ViewPageActivity.class);
                startActivity(intent2);
            }
        });
        test.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this, RevertActivity.class);
                startActivity(intent3);
            }
        });
        AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);
        alphaAnimation.setDuration(3000);//设定动画时间
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                homeImage.setVisibility(View.GONE);
            }
        });

        homeImage.setAnimation(alphaAnimation);
        homeImage.setVisibility(View.VISIBLE);
    }


}

