package com.example.sliderdemo.ui;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sliderdemo.R;
import com.example.sliderdemo.databinding.ActivitySliderV2Binding;
import com.example.sliderdemo.model.Image;
import com.example.sliderdemo.ui.adapter.AdapterImageSliderV1;
import com.example.sliderdemo.ui.adapter.AdapterImageSliderV2;

import java.util.ArrayList;
import java.util.List;

public class SliderActivityV2 extends AppCompatActivity {
    private ActivitySliderV2Binding binding;
    Runnable runnable;
    Handler handler;
    long pressTime = 0L;
    long limit = 500L;
    boolean onClick;
    boolean isStopSlider;
    private int[] array_image_place = {
            R.drawable.sample1,
            R.drawable.sample2,
            R.drawable.sample3,
            R.drawable.sample4,
            R.drawable.sample5
    };

    private LinearLayout layout_dots;
    private ViewPager2 viewPager2;
    private AdapterImageSliderV2 adapterImageSlider;

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_slider_v2);
        startBannerSlider();
    }


    private void startBannerSlider() {
        handler = new Handler();
        String[] titleArr = this.getResources().getStringArray(R.array.titles);
        String[] bodyArr = this.getResources().getStringArray(R.array.bodies);

        layout_dots = binding.layoutDots;
        viewPager2 = binding.pager;
        adapterImageSlider = new AdapterImageSliderV2(this, new ArrayList());

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
        viewPager2.setAdapter(adapterImageSlider);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        // displaying selected image first
        viewPager2.setCurrentItem(0);

        addBottomDots(layout_dots, adapterImageSlider.getItemCount(), 0);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (isStopSlider) {
                    isStopSlider = false;
                    startAutoSlider(adapterImageSlider.getItemCount(), false);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                addBottomDots(layout_dots, adapterImageSlider.getItemCount(), position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });



        adapterImageSlider.setOnTouchListener(onTouchListener);

        adapterImageSlider.setOnItemClickListener((view, obj) -> {
            if (onClick) {
                onClick = false;
                Toast.makeText(SliderActivityV2.this, "Click" + obj.body, Toast.LENGTH_LONG).show();
            }
        });

        startAutoSlider(adapterImageSlider.getItemCount(), false);

    }

    private void startAutoSlider(final int count, boolean isUp) {
        if (isUp) {
            handler.postDelayed(runnable, 1000);
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                isStopSlider = false;
                int pos = viewPager2.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager2.setCurrentItem(pos);
                handler.postDelayed(runnable, 3000);
            }
        };
        if (!isUp) {
            handler.postDelayed(runnable, 3000);
        }
    }

    private void stopAutoSlider() {
        isStopSlider = true;
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


    AdapterImageSliderV2.OnTouchListener onTouchListener = (v, event) -> {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressTime = System.currentTimeMillis();
                stopAutoSlider();
                return false;
            case MotionEvent.ACTION_UP:
                long now = System.currentTimeMillis();
                startAutoSlider(adapterImageSlider.getItemCount(), true);
                boolean aBol = limit < now - pressTime;
                if (!aBol) {
                    //click perform
                    onClick = true;
                }
                return aBol;

        }
        return false;
    };

}