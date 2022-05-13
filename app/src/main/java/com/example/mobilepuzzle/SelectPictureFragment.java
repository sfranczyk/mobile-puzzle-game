package com.example.mobilepuzzle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilepuzzle.adapter.SelectImageAdapter;

import java.util.ArrayList;

public class SelectPictureFragment extends Fragment implements GestureDetector.OnGestureListener {

    Button btnSelect;
    Button btnBack;
    Button btnLeft;
    Button btnRight;
    TextView textViewImageNumber;
    ImageView imgViewSelectImage;
    SelectImageAdapter adapter;
    private int currentImgIndex = 0;

    private GestureDetector myGesture;
    private final ArrayList<Float> scroll = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_picture, container, false);

        setElements(view);
        setImageViewSelect();
        setButtonsOnClick();

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setImageViewSelect() {
        adapter = new SelectImageAdapter(getContext());
        myGesture = new GestureDetector(getContext(), this);
        imgViewSelectImage.setOnTouchListener((v, event) -> !myGesture.onTouchEvent(event));
        imgViewSelectImage.setScaleType((ImageView.ScaleType.FIT_XY));
        changeViewImg(currentImgIndex);
    }

    private void setElements(View view) {
        btnSelect = view.findViewById(R.id.btnSelect);
        btnBack = view.findViewById(R.id.btnBack);
        btnLeft = view.findViewById(R.id.btnLeftImage);
        btnRight = view.findViewById(R.id.btnRightImage);
        imgViewSelectImage = view.findViewById(R.id.imageViewSelectImage);
        textViewImageNumber = view.findViewById(R.id.textViewImageNumber);
    }

    private void setButtonsOnClick() {
        btnSelect.setOnClickListener(this::onClickBtnSelect);
        btnBack.setOnClickListener(this::onClickBtnBack);
        btnLeft.setOnClickListener(l -> previousImg());
        btnRight.setOnClickListener(l -> nextImg());
    }

    private void onClickBtnSelect(View l) {
        Intent i = new Intent(getContext(), PuzzleActivity.class);
        i.putExtra("id", currentImgIndex);
        startActivity(i);
    }

    private void onClickBtnBack(View l) {
        Navigation.findNavController(l).navigate(R.id.navigate_to_menuFragment);
    }

    @SuppressLint("DefaultLocale")
    private void updateTextNumber(int position) {
        textViewImageNumber.setText(String.format("%d/%d", position + 1, adapter.getCount()));
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        scroll.clear();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        scroll.add(v);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if (scroll.stream().allMatch(n -> n >= 0)) {
            nextImg();
        } else if (scroll.stream().allMatch(n -> n <= 0)) {
            previousImg();
        }
        return false;
    }

    private void previousImg() {
        if (0 < currentImgIndex) {
            changeViewImg(--currentImgIndex);
        }
    }

    private void nextImg() {
        if (currentImgIndex < adapter.getCount() - 1) {
            changeViewImg(++currentImgIndex);
        }
    }

    private void changeViewImg(int position) {
        updateTextNumber(position);
        imgViewSelectImage.setImageResource((Integer) adapter.getItem(position));
    }

}
