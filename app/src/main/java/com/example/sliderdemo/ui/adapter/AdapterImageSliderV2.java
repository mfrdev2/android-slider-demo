package com.example.sliderdemo.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sliderdemo.R;
import com.example.sliderdemo.databinding.ItemSliderLayoutBinding;
import com.example.sliderdemo.model.Image;

import java.util.List;
import java.util.Objects;

public class AdapterImageSliderV2 extends PagingDataAdapter<Image, AdapterImageSliderV2.SliderViewHolder> {
    public static long pressTime = 0L;
    public static long limit = 500L;
    private Activity act;
    private List<Image> items;

    private AdapterImageSliderV2.OnItemClickListener onItemClickListener;
    private AdapterImageSliderV2.OnTouchListener onTouchListener;

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemSliderLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_slider_layout, parent,
                false);
        binding.getRoot().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new SliderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        final ItemSliderLayoutBinding binding = holder.binding;
        final Image o = items.get(position);

        binding.image.setImageResource(o.image);
        CardView lytParent = binding.lytParent;
        lytParent.setOnClickListener(v1 -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v1, o);
            }
        });

        lytParent.setOnTouchListener((view, motionEvent) -> {
            if (onTouchListener != null) {
                onTouchListener.onTouch(view, motionEvent);
            }
            return false;
        });

    }

    public interface OnItemClickListener {
        void onItemClick(View view, Image obj);
    }


    public interface OnTouchListener {
        boolean onTouch(View v, MotionEvent event);
    }


    public void setOnItemClickListener(AdapterImageSliderV2.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnTouchListener(AdapterImageSliderV2.OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    // constructor
    public AdapterImageSliderV2(Activity activity, List<Image> items) {
        super(DIFF_CALLBACK);
        this.act = activity;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

   /*  public Image getItem(int pos) {
        return items.get(pos);
    }*/

    public void setItems(List<Image> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        public final ItemSliderLayoutBinding binding;

        public SliderViewHolder(@NonNull final ItemSliderLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static DiffUtil.ItemCallback<Image> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Image>() {


                @Override
                public boolean areItemsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
                    return Objects.equals(oldItem.id, newItem.id);
                }

                @Override
                public boolean areContentsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
                    boolean isSame = false;
                    if (oldItem.body.equals(newItem.body)) {
                        isSame = true;
                    }

                    if (oldItem.image == newItem.image) {
                        isSame = true;
                    }

                    return isSame;
                }
            };


}
