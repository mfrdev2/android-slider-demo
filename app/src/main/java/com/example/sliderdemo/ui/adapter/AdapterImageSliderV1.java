package com.example.sliderdemo.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.sliderdemo.R;
import com.example.sliderdemo.model.Image;

import java.util.List;

public class AdapterImageSliderV1 extends PagerAdapter {
    public static long pressTime = 0L;
    public static long limit = 500L;
    private Activity act;
    private List<Image> items;

    private AdapterImageSliderV1.OnItemClickListener onItemClickListener;
    private AdapterImageSliderV1.OnTouchListener onTouchListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Image obj);
    }


    public interface OnTouchListener{
        boolean onTouch(View v, MotionEvent event);
    }

    public void setOnItemClickListener(AdapterImageSliderV1.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnTouchListener(AdapterImageSliderV1.OnTouchListener onTouchListener){
        this.onTouchListener = onTouchListener;
    }

    // constructor
    public AdapterImageSliderV1(Activity activity, List<Image> items) {
        this.act = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

  /*  public Image getItem(int pos) {
        return items.get(pos);
    }*/

    public void setItems(List<Image> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Image o = items.get(position);
        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_slider_layout, container, false);

        ImageView image = (ImageView) v.findViewById(R.id.image);
        CardView lyt_parent = (CardView) v.findViewById(R.id.lyt_parent);
        image.setImageResource(o.image);
        lyt_parent.setOnClickListener(v1 -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v1, o);
            }
        });

        lyt_parent.setOnTouchListener((view, motionEvent) -> {
            if (onTouchListener != null) {
                onTouchListener.onTouch(view,motionEvent);
            }
            return false;
        });

        ((ViewPager) container).addView(v);

        return v;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
