package com.example.mobilepuzzle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.mobilepuzzle.R;

public class SelectImageAdapter extends BaseAdapter {
    private final Context context;
    private static Integer[] images = {
        R.drawable.dragon1,
        R.drawable.plane1,
        R.drawable.cat1,
        R.drawable.god1,
        R.drawable.sword1,
        R.drawable.flowers1,
        R.drawable.plane2,
        R.drawable.dragon2,
        R.drawable.flowers2,
        R.drawable.lion1
    };

    public SelectImageAdapter(Context c){
        context = c;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);

        imageView.setImageResource(images[i]);
        imageView.setScaleType((ImageView.ScaleType.CENTER_CROP));
        imageView.setLayoutParams(new GridView.LayoutParams(340, 340));
        return imageView;
    }
}
