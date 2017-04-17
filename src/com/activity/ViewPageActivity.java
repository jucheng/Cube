package com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Created by Administrator on 2016/5/6.
 */
public class ViewPageActivity extends Activity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;

    /**
     * 装点点的ImageView数组
     */
    private ImageView[] tips;

    private ImageView imageView;

    private int[] imgIdArray ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpage);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.viewGroup);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //载入图片资源ID
        imgIdArray = new int[]{R.drawable.item01, R.drawable.item02, R.drawable.item03, R.drawable.item04,
                R.drawable.item05,R.drawable.item06, R.drawable.item07, R.drawable.item08};


        //将点点加入到ViewGroup中
        tips = new ImageView[imgIdArray.length];
        for(int i=0; i<tips.length; i++){
            ImageView imageView = new ImageView(this);

            //控制点间距离
            LinearLayout.LayoutParams ll= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setMargins(5, 0, 5, 0);
            imageView.setLayoutParams(ll);


            tips[i] = imageView;
            if(i == 0){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }

            linearLayout.addView(imageView);
        }

        //设置Adapter
        viewPager.setAdapter(new MyAdapter(imgIdArray));
        //设置监听，主要是设置点点的背景
        viewPager.setOnPageChangeListener(this);
//		//设置当前项，此处是最后一项，故只能右划(默认只能左划)
//		viewPager.setCurrentItem(imgIdArray.length-1);

    }

    public class MyAdapter extends PagerAdapter {

        private int[] ids;
        public MyAdapter(int[] ids) {
            this.ids = ids;
        }
        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //官方推荐
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(imageView);

        }

        /**
         *滑动和初始加载时，会加载2次（不懂）
         */
        @Override
        public Object instantiateItem(View container, int position) {
            ImageView imageView = new ImageView(ViewPageActivity.this);
            imageView.setBackgroundResource(imgIdArray[position]);
            ((ViewPager)container).addView(imageView, 0);
            return imageView;
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0);
    }

    /**
     * 设置选中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

}



