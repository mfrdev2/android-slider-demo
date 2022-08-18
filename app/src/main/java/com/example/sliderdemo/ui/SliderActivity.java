package com.example.sliderdemo.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sliderdemo.R;
import com.example.sliderdemo.model.Image;
import com.example.sliderdemo.ui.adapter.AdapterImageSlider;

import java.util.ArrayList;
import java.util.List;

public class SliderActivity extends AppCompatActivity {
    Runnable runnable;
    Handler handler;
    long pressTime = 0L;
    long limit = 500L;
    boolean onClick;
    private int[] array_image_place = {
            R.drawable.sample1,
            R.drawable.sample2,
            R.drawable.sample3,
            R.drawable.sample4,
            R.drawable.sample5
    };

    private LinearLayout layout_dots;
    private ViewPager viewPager;
    private AdapterImageSlider adapterImageSlider;

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        startBannerSlider();
    }


    private void startBannerSlider() {
        handler = new Handler();
        String[] titleArr = this.getResources().getStringArray(R.array.titles);
        String[] bodyArr = this.getResources().getStringArray(R.array.bodies);

        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapterImageSlider = new AdapterImageSlider(this, new ArrayList());

        final List<Image> items = new ArrayList<>();
        for (int i = 0; i < array_image_place.length; i++) {
            Image obj = new Image();
            obj.image = array_image_place[i];
            obj.imageDrw = getResources().getDrawable(obj.image);
            obj.title = titleArr[i];
            obj.body = bodyArr[i];
            items.add(obj);
        }

        adapterImageSlider.setItems(items);
        viewPager.setAdapter(adapterImageSlider);
        // displaying selected image first
        viewPager.setCurrentItem(0);

        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);

        ((TextView) findViewById(R.id.title)).setText(items.get(0).title);
        ((TextView) findViewById(R.id.brief)).setText(items.get(0).body);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                ((TextView) findViewById(R.id.title)).setText(items.get(pos).title);
                ((TextView) findViewById(R.id.brief)).setText(items.get(pos).body);
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        adapterImageSlider.setOnTouchListener(onTouchListener);

        adapterImageSlider.setOnItemClickListener(new AdapterImageSlider.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Image obj) {
                if(onClick){
                    onClick = false;
                    Toast.makeText(SliderActivity.this,"Click"+obj.body,Toast.LENGTH_LONG).show();
                }
            }
        });

        startAutoSlider(adapterImageSlider.getCount(),false);

    }

    private void startAutoSlider(final int count,boolean isUp) {
        if(isUp){
            handler.postDelayed(runnable, 100);
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 3000);
            }
        };
        if(!isUp){
            handler.postDelayed(runnable, 3000);
        }
    }

    private void stopAutoSlider() {
        handler.removeCallbacks(runnable);
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 0, 10, 0);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.circle_outline);
            dots[i].setColorFilter(ContextCompat.getColor(this, R.color.grey40), PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.circle_shape);
            dots[current].setColorFilter(ContextCompat.getColor(this, R.color.grey40), PorterDuff.Mode.SRC_ATOP);
        }
    }


    AdapterImageSlider.OnTouchListener onTouchListener = (v, event) -> {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressTime = System.currentTimeMillis();
                stopAutoSlider();
                return false;
            case MotionEvent.ACTION_UP:
                long now = System.currentTimeMillis();
                startAutoSlider(adapterImageSlider.getCount(),true);
                boolean aBol = limit < now - pressTime;
                if(!aBol){
                    //click perform
                    onClick = true;
                }
                return aBol;

        }
        return false;
    };

}